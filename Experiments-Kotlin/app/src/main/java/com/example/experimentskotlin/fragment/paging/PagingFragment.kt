package com.example.experimentskotlin.fragment.paging


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.paging.*
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.corenetwork.Api
import com.example.corenetwork.extension.disposedBy
import com.example.corenetwork.extension.subscribeOnMain
import com.example.corenetwork.model.articles.ArticleItem
import com.example.experimentskotlin.R
import com.example.experimentskotlin.baseclasses.BaseMVVMFragment
import com.example.experimentskotlin.baseclasses.BaseViewModel
import io.reactivex.disposables.CompositeDisposable
import kotlinx.android.synthetic.main.fragment_paging.*
import kotlinx.android.synthetic.main.item_article.view.*
import java.util.concurrent.Executors


class PagingFragment : BaseMVVMFragment<PagingViewModel>() {

    override val viewModelClass = PagingViewModel::class.java
    override val layoutID = R.layout.fragment_paging

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)


        adapter = ArticleItemPagedListAdapter()

        viewModel.setup(Api.getInstance(), CompositeDisposable())

        viewModel.articleLiveData.observe(this, Observer<PagedList<ArticleItem>> { list -> adapter.submitList(list) })
        recyclerView.layoutManager = LinearLayoutManager(context)
        recyclerView.adapter = adapter
    }
    private lateinit var adapter: ArticleItemPagedListAdapter
}

class PagingViewModel : BaseViewModel() {
    lateinit var articleLiveData: LiveData<PagedList<ArticleItem>>
    fun setup(
            api: Api,
            compositeDisposable: CompositeDisposable) {
        val itemDataSource = ItemDataSourceFactory(api, compositeDisposable)
        val pagedListConfig = PagedList.Config.Builder()
                .setPageSize(25)
                .build()
        val executor = Executors.newFixedThreadPool(5)
        articleLiveData = LivePagedListBuilder(itemDataSource, pagedListConfig)
                .setFetchExecutor(executor)
                .build()
    }
}

class ArticleItemPagedListAdapter() : PagedListAdapter<ArticleItem, ArticleItemPagedListViewHolder>(ArticleItemDiffUtil()) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ArticleItemPagedListViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_article, parent, false)
        return ArticleItemPagedListViewHolder(view)
    }

    override fun onBindViewHolder(holder: ArticleItemPagedListViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

}

class ArticleItemPagedListViewHolder(private val view: View) : RecyclerView.ViewHolder(view) {
    fun bind(item: ArticleItem?) {
        view.titleTextView.text = item?.title ?: "item is null"
        view.urlTextView.text = item?.url ?: "item is null"
    }
}

class ItemDataSource(
        private val api: Api,
        private val compositeDisposable: CompositeDisposable
) : PageKeyedDataSource<String, ArticleItem>() {
    override fun loadInitial(params: LoadInitialParams<String>, callback: LoadInitialCallback<String, ArticleItem>) {
        api.getArticlesList()
                .subscribeOnMain(
                        onNext = {
                            callback.onResult(it.items, null, it.offset)
                        }
                )
                .disposedBy(compositeDisposable)
    }

    override fun loadAfter(params: LoadParams<String>, callback: LoadCallback<String, ArticleItem>) {
        api.getArticlesList(params.key)
                .subscribeOnMain(
                        onNext = {
                            callback.onResult(it.items, it.offset)
                        }
                )
                .disposedBy(compositeDisposable)
    }

    override fun loadBefore(params: LoadParams<String>, callback: LoadCallback<String, ArticleItem>) {}

}

class ItemDataSourceFactory(
        private val api: Api,
        private val compositeDisposable: CompositeDisposable
) : DataSource.Factory<String, ArticleItem>() {
    override fun create(): DataSource<String, ArticleItem> {
        return ItemDataSource(api, compositeDisposable)
    }

}


class ArticleItemDiffUtil : DiffUtil.ItemCallback<ArticleItem>() {
    override fun areItemsTheSame(oldItem: ArticleItem, newItem: ArticleItem): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: ArticleItem, newItem: ArticleItem): Boolean {
        return oldItem.hashCode() == newItem.hashCode()
    }

}