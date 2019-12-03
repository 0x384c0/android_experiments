package com.example.experimentskotlin.fragment.paging


import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.corenetwork.Constants
import com.example.corenetwork.model.ErrorResponse
import com.example.corenetwork.model.articles.ArticleItem
import com.example.experimentskotlin.R
import com.example.experimentskotlin.baseclasses.BaseMVVMFragment
import com.example.experimentskotlin.util.adapters.SingleLayoutSectionedAdapter
import com.example.experimentskotlin.util.adapters.SingleLayoutSectionedDiffUtilAdapter
import com.example.experimentskotlin.util.extensions.observe
import com.example.experimentskotlin.util.infinity_scroll.InfinityScrollManager
import kotlinx.android.synthetic.main.item_article.view.*
import kotlinx.android.synthetic.main.item_operation_section_date.view.*
import kotlinx.android.synthetic.main.view_recycler_view_swipe_refresh.*


/**
 * A simple [Fragment] subclass.
 *
 */
class SectionedDiffUtilFragment : BaseMVVMFragment<SectionedDiffUtilViewModel>() {
    override val viewModelClass = SectionedDiffUtilViewModel::class.java
    override val layoutID = R.layout.fragment_paging_sectioned

    //region LifeCycle
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRecyclerView()
        setupSwipeRefreshLayout()
    }
    //endregion


    //region RecyclerView
    private lateinit var adapter: SingleLayoutSectionedDiffUtilAdapter<ArticleItem, String>

    private fun setupRecyclerView() {
        if (!::adapter.isInitialized)
            adapter = SingleLayoutSectionedDiffUtilAdapter(
                    R.layout.item_article,
                    R.layout.item_operation_section_date,
                    {
                        ItemVH(
                                view = it,
                                refresh = { data ->
                                    viewModel.paginationManager.reloadPageForItemAtNextOnResume(data)
                                    findNavController().navigate(SectionedDiffUtilFragmentDirections.next(data.title, data))
                                }
                        )
                    },
                    { SectionVH(it) }
            )
        recyclerView.layoutManager = LinearLayoutManager(context)
        recyclerView.adapter = adapter
    }
    //endregion

    //region Data Binding
    override fun bindData() {
        super.bindData()
        viewModel.recyclerViewDataBinding.observe(this) {
            adapter.calculateAndSetDataInBackgroundThread(
                    items = it,
                    itemToSectionData = { (it.id / 10).toString() })
        }
        infinityScrollManager = InfinityScrollManager(this, viewModel.paginationManager, Constants.PAGE_SIZE)
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

    //region View Holders
    class ItemVH(view: View, private val refresh: (ArticleItem) -> Unit) : SingleLayoutSectionedAdapter.BaseItemViewHolder<ArticleItem>(view) {
        override fun setup(data: ArticleItem) {
            view.titleTextView.text = data.title
            view.urlTextView.text = data.url
            view.setOnClickListener {
                refresh(data)
            }
        }
    }

    class SectionVH(view: View) : SingleLayoutSectionedAdapter.BaseItemViewHolder<String>(view) {
        override fun setup(data: String) {
            view.sectionTitleTextView.text = data
        }
    }
    //endregion


    //region Managers
    private lateinit var infinityScrollManager: InfinityScrollManager
    //endregion
}