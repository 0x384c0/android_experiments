package com.example.experimentskotlin.util.infinity_scroll

import android.os.Handler
import android.view.View
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.corenetwork.Constants
import com.example.corenetwork.external.infinitescroll.InfiniteScrollListener
import kotlinx.android.synthetic.main.view_recycler_view_infinity_scroll.*

/**
 * Класс для управления пагинацией
 *
 * Fragment должен содержать в себе view_recycler_view_infinity_scroll.xml
 */
class InfinityScrollManager(private val fragment: Fragment, private val viewModel: InfinityScrollViewModelInterface) {
    /**
     * инициализация бесконечного скролла
     */
    init {
        fragment.recyclerView.addOnScrollListener(object : InfiniteScrollListener(
            Constants.PAGE_SIZE,
            fragment.recyclerView.layoutManager as LinearLayoutManager
        ) {
            override fun onScrolledToEnd(firstVisibleItemPosition: Int) {
                if (isCanLoadMore()) {
                    viewModel.refresh(true)
                    showLoading()
                }
            }
        })
    }

    private fun isCanLoadMore(): Boolean {
        return fragment.loadingMoreIndicator.visibility == View.GONE &&
                !fragment.swipeRefreshLayout.isRefreshing &&
                viewModel.isCanLoadMore()
    }

    private var paddingWasNotSet = true
    private fun showLoading() {
        fragment.loadingMoreIndicator.visibility = View.VISIBLE
        Handler().postDelayed({
            try {
                if (paddingWasNotSet) {
                    paddingWasNotSet = false
                    fragment.recyclerView.clipToPadding = false

                    fragment.recyclerView.setPadding(
                        fragment.recyclerView.paddingLeft,
                        fragment.recyclerView.paddingTop,
                        fragment.recyclerView.paddingRight,
                        fragment.loadingMoreIndicator.height
                    )
                }
            } catch (e: Exception) {
            }
        }
            , 50)
    }

    fun hideLoading() {
        fragment.loadingMoreIndicator.visibility = View.GONE
    }

    interface InfinityScrollViewModelInterface {
        fun refresh(nextPage: Boolean = false)
        fun isCanLoadMore(): Boolean
    }
}