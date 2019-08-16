package com.example.experimentskotlin.util.adapters.paging

import androidx.paging.DataSource
import androidx.paging.LivePagedListBuilder
import androidx.paging.PageKeyedDataSource
import androidx.paging.PagedList
import androidx.recyclerview.widget.DiffUtil
import java.util.concurrent.Executors


typealias PageListLoadedCallback<P, T> = (data: List<T>, nextPage: P?) -> Unit
typealias PageListLoadCallback<P, T> = (page: P?, loaded: PageListLoadedCallback<P, T>) -> Unit

class PagedListSingleLayoutData<P, T>(
        load: PageListLoadCallback<P, T>,
        areItemsTheSame: (oldItem: T, newItem: T) -> Boolean
) {

    private val itemDataSource = PagedListDataSourceFactory(load)
    private val pagedListConfig = PagedList.Config.Builder()
            .setPageSize(25)
            .build()
    private val executor = Executors.newFixedThreadPool(5)
    val pagedLiveData = LivePagedListBuilder(itemDataSource, pagedListConfig)
            .setFetchExecutor(executor)
            .build()
    val diffUtil = PageListItemDiffUtil(areItemsTheSame)

    fun refresh(){
        pagedLiveData.value?.dataSource?.invalidate()
    }

    //region Classes
    private class PagedListDataSourceFactory<P, T>(
            private val load: PageListLoadCallback<P, T>
    ) : DataSource.Factory<P, T>() {
        override fun create(): DataSource<P, T> {
            return PagedListDataSource(load)
        }
    }

    private class PagedListDataSource<P, T>(
            private val load: PageListLoadCallback<P, T>
    ) : PageKeyedDataSource<P, T>() {
        override fun loadInitial(params: LoadInitialParams<P>, callback: LoadInitialCallback<P, T>) {
            load(null) { data, nextPage ->
                callback.onResult(data, null, nextPage)
            }
        }

        override fun loadAfter(params: LoadParams<P>, callback: LoadCallback<P, T>) {
            load(params.key) { data, nextPage ->
                callback.onResult(data, nextPage)
            }
        }

        override fun loadBefore(params: LoadParams<P>, callback: LoadCallback<P, T>) {}
    }

    class PageListItemDiffUtil<T>(private val areItemsTheSameCallback: (oldItem: T, newItem: T) -> Boolean) : DiffUtil.ItemCallback<T>() {
        override fun areItemsTheSame(oldItem: T, newItem: T): Boolean {
            return areItemsTheSameCallback(oldItem, newItem)
        }

        override fun areContentsTheSame(oldItem: T, newItem: T): Boolean {
            return areItemsTheSameCallback(oldItem, newItem)
        }
    }
    //endregion
}