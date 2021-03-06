package com.example.experimentskotlin.fragment.articles


import android.os.Bundle
import android.view.View
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.corenetwork.Constants
import com.example.corenetwork.model.ErrorResponse
import com.example.corenetwork.model.articles.ArticleItem
import com.example.experimentskotlin.R
import com.example.experimentskotlin.baseclasses.BaseMVVMFragment
import com.example.experimentskotlin.util.adapters.SingleLayoutAdapter
import com.example.experimentskotlin.util.extensions.observe
import com.example.experimentskotlin.util.infinity_scroll.InfinityScrollManager
import kotlinx.android.synthetic.main.item_article.view.*
import kotlinx.android.synthetic.main.view_recycler_view_infinity_scroll.*
import kotlinx.android.synthetic.main.view_recycler_view_swipe_refresh.*

class ArticlesFragment : BaseMVVMFragment() {
    //region Overrides
    override val layoutID = R.layout.fragment_articles
    //endregion

    //region LifeCycle
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupSwipeRefreshLayout()
        setupRecyclerView()
    }
    //endregion

    //region Data Binding
    private lateinit var viewModel:ArticlesViewModel
    override fun bindData() {
        viewModel = getViewModel(ArticlesViewModel::class.java)
        viewModel.recyclerViewDataBinding.observe(this) {
            adapter.data = it
        }
        infinityScrollManager = InfinityScrollManager(this, viewModel.paginationManager, Constants.PAGE_SIZE)
    }
    //endregion

    //region RecyclerView
    private lateinit var adapter: SingleLayoutAdapter<ArticleItem>

    private fun setupRecyclerView() {
        adapter = SingleLayoutAdapter(
                itemLayoutId = R.layout.item_article,
                bindViewHandler = { view, data ->
                    view.titleTextView.text = data.title
                    view.urlTextView.text = data.url
                },
                onClickHandler = { _, data ->
                    viewModel.paginationManager.reloadPageForItemAtNextOnResume(data)
                    findNavController().navigate(ArticlesFragmentDirections.next(data.title, data))
                }
        )
        recyclerView.layoutManager = LinearLayoutManager(context)
        recyclerView.adapter = adapter
    }
    //endregion


    //region SwipeRefreshLayout
    private fun setupSwipeRefreshLayout() {
        swipeRefreshLayout.setOnRefreshListener { viewModel.refresh() }
    }

    override fun showLoading() {
        swipeRefreshLayout.isRefreshing = true
    }

    override fun hideLoading() {
        swipeRefreshLayout.isRefreshing = false
        infinityScrollManager.hideLoading()
    }

    override fun showAlert(e: Throwable) {
        hideLoading()
        val text = ErrorResponse.create(e)?.getErrorMessage() ?: e.localizedMessage
        if (text != null)
            showAlert(text)
        else
            throw e
    }
    //endregion


    //region Managers
    private lateinit var infinityScrollManager: InfinityScrollManager
    //endregion
}

