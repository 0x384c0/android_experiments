package com.example.experimentskotlin.fragment.paging


import android.os.Bundle
import androidx.lifecycle.Observer
import androidx.paging.*
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.corenetwork.model.articles.ArticleItem
import com.example.experimentskotlin.R
import com.example.experimentskotlin.baseclasses.BaseMVVMFragment
import com.example.experimentskotlin.util.adapters.paging.PagedListLayoutAdapter
import kotlinx.android.synthetic.main.fragment_paging.*
import kotlinx.android.synthetic.main.item_article.view.*


class PagingFragment : BaseMVVMFragment() {

    //region Overrides
    override val layoutID = R.layout.fragment_paging
    //endregion

    //region LifeCycle
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel.setup()
        setupRecyclerView()
    }
    //endregion
    private lateinit var viewModel:PagingViewModel
    override fun bindData() {
        viewModel = getViewModel(PagingViewModel::class.java)
    }
    //endregion

    //region RecyclerView
    private lateinit var adapter: PagedListLayoutAdapter<ArticleItem>

    private fun setupRecyclerView() {
        adapter = PagedListLayoutAdapter(
                itemLayoutId = R.layout.item_article,
                bindViewHandler = { view, data ->
                    view.titleTextView.text = data?.title ?: "data is null"
                    view.urlTextView.text = data?.url ?: "item is null"
                },
                onClickHandler = { _, _ -> },
                owner = this,
                pagedLiveData = viewModel.pagedRecyclerViewData.pagedLiveData,
                diffUtil = viewModel.pagedRecyclerViewData.diffUtil
        )


        viewModel.pagedRecyclerViewData.pagedLiveData.observe(this, Observer<PagedList<ArticleItem>> { list -> adapter.submitList(list) })
        recyclerView.layoutManager = LinearLayoutManager(context)
        recyclerView.adapter = adapter
    }
    //endregion
}


