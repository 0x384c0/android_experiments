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
                10,
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
        if (::showHideLoadingCallback.isInitialized)
            showHideLoadingHandler.removeCallbacks(showHideLoadingCallback)
        fragment.loadingMoreIndicator.visibility = View.VISIBLE
        showHideLoadingCallback = Runnable {
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
        Handler().postDelayed(showHideLoadingCallback, 50)
    }

    private val showHideLoadingHandler = Handler()
    private lateinit var showHideLoadingCallback: Runnable

    fun hideLoading() {
        if (::showHideLoadingCallback.isInitialized)
            showHideLoadingHandler.removeCallbacks(showHideLoadingCallback)
        showHideLoadingCallback = Runnable {
            try {
                fragment.loadingMoreIndicator.visibility = View.GONE
            } catch (e: Exception) {
            }
        }
        showHideLoadingHandler.postDelayed(showHideLoadingCallback, 50)
    }

    interface InfinityScrollViewModelInterface {
        fun refresh(nextPage: Boolean = false)
        fun isCanLoadMore(): Boolean
    }
}