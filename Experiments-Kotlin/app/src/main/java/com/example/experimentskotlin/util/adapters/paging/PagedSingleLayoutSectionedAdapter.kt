package com.example.experimentskotlin.util.adapters.paging

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import androidx.paging.*
import androidx.recyclerview.widget.DiffUtil
import com.example.experimentskotlin.util.adapters.SingleLayoutSectionedAdapter

class PagedSingleLayoutSectionedAdapter<T, S>(
        owner: LifecycleOwner,
        private val itemId: Int,
        private val headerId: Int,
        private val itemViewHolderFactory: (View) -> SingleLayoutSectionedAdapter.BaseItemViewHolder<T>,
        private val sectionViewHolderFactory: (View) -> SingleLayoutSectionedAdapter.BaseItemViewHolder<S>,
        lastPageLoaded: (() -> Unit) -> Unit
) : PagedListAdapter<PagedSingleLayoutSectionedAdapter.ItemOrSectionData<T, S>, SingleLayoutSectionedAdapter.BaseItemViewHolder<*>>(DiffUtil<T, S>()) {
    //region DataSources
    private val pageSize = 10
    private val dataSourceFactory = PagedListDataSourceFactory<ItemOrSectionData<T, S>>(pageSize, lastPageLoaded)
    private val pagedListConfig = PagedList.Config.Builder()
            .setPageSize(pageSize)
            .build()
    private val pagedLiveData = LivePagedListBuilder(dataSourceFactory, pagedListConfig).build()

    init {
        pagedLiveData.observe(owner, Observer<PagedList<ItemOrSectionData<T, S>>> { list -> submitList(list) })
    }
    //endregion


    //region Data logic
    private var _data: List<ItemOrSectionData<T, S>> = listOf()
    var data: List<ItemOrSectionData<T, S>>
        set(value) {
            _data = value
            dataSourceFactory.data = value
        }
        get() {
            return _data
        }

    fun invalidate(){
        pagedLiveData.value?.dataSource?.invalidate()
    }
    //endregion

    //region Overrides
    override fun getItemViewType(position: Int): Int {
        return when {
            getItem(position)?.itemData != null -> ITEM
            getItem(position)?.sectionData != null -> SECTION
            else -> throw Exception("PagedSingleLayoutSectionedAdapter.getItemViewType invalid item at position $position")
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SingleLayoutSectionedAdapter.BaseItemViewHolder<*> {
        val layoutId = if (viewType == ITEM) itemId else headerId
        val view = LayoutInflater.from(parent.context).inflate(layoutId, parent, false)
        val viewHolderFactory = if (viewType == ITEM) itemViewHolderFactory else sectionViewHolderFactory
        return viewHolderFactory(view)
    }

    override fun onBindViewHolder(holder: SingleLayoutSectionedAdapter.BaseItemViewHolder<*>, position: Int) {
        val item = getItem(position)
        @Suppress("UNCHECKED_CAST")
        when {
            item?.itemData != null -> (holder as? SingleLayoutSectionedAdapter.BaseItemViewHolder<T>)?.setup(item.itemData)
            item?.sectionData != null -> (holder as? SingleLayoutSectionedAdapter.BaseItemViewHolder<S>)?.setup(item.sectionData)
            else -> throw Exception("PagedSingleLayoutSectionedAdapter.getItemViewType invalid item at position $position")
        }

    }
    //endregion

    //region Datasources Classes
    private class PagedListDataSourceFactory<T>(
            val pageSize: Int,
            private val lastPageLoaded: (() -> Unit) -> Unit
    ) : DataSource.Factory<Int, T>() {
        var data = listOf<T>()
        override fun create(): DataSource<Int, T> {
            return PagedListDataSource<T>({ data }, pageSize, lastPageLoaded)
        }
    }

    private class PagedListDataSource<T>(
            private val data: () -> List<T>,
            private val pageSize: Int,
            private val lastPageLoaded: (() -> Unit) -> Unit
    ) : PageKeyedDataSource<Int, T>() {
        override fun loadInitial(params: LoadInitialParams<Int>, callback: LoadInitialCallback<Int, T>) {
            val start = 0
            val end = pageSize - 1
            callback.onResult(getSlice(data(), start, end), null, 1)
        }

        override fun loadAfter(params: LoadParams<Int>, callback: LoadCallback<Int, T>) {
            val start = params.key * pageSize
            val end = start + (pageSize - 1)
            var nexPage: Int? = null
            if (end < data().count()) {
                nexPage = params.key + 1
                callback.onResult(getSlice(data(), start, end), nexPage)
            } else {
                lastPageLoaded {
                    var nexPage: Int? = if (end < data().count()) params.key + 1 else null
                    callback.onResult(getSlice(data(), start, end), nexPage)
                }
            }
        }

        override fun loadBefore(params: LoadParams<Int>, callback: LoadCallback<Int, T>) {
            val end = params.key * pageSize
            val start = end - (pageSize - 1)
            val nexPage = if (start > 0) params.key - 1 else null
            callback.onResult(getSlice(data(), start, end), nexPage)
        }

        private fun <T> getSlice(list: List<T>, startParam: Int, endParam: Int): List<T> {
            var start = startParam
            var end = endParam
            if (start > (list.count() - 1))
                start = (list.count() - 1)
            else if (start < 0)
                start = 0
            if (end > (list.count() - 1))
                end = (list.count() - 1)
            else if (end < 0)
                end = 0
            return list.slice(start..end)
        }
    }

    class DiffUtil<T, S> : androidx.recyclerview.widget.DiffUtil.ItemCallback<ItemOrSectionData<T, S>>() {
        override fun areItemsTheSame(oldItem: ItemOrSectionData<T, S>, newItem: ItemOrSectionData<T, S>): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: ItemOrSectionData<T, S>, newItem: ItemOrSectionData<T, S>): Boolean {
            return oldItem == newItem
        }
    }
    //endregion

    //region Classes
    data class ItemOrSectionData<T, S>(
            val itemData: T? = null,
            val sectionData: S? = null
    ) {
        override fun equals(other: Any?): Boolean {
            val otherData = other as ItemOrSectionData<T, S>
            if (otherData.itemData != null && itemData != null)
                return otherData.itemData == itemData
            if (otherData.sectionData != null && sectionData != null)
                return otherData.sectionData == sectionData
            return false
        }
    }
    //endregion

    //region Helpers
    fun toItemOrSectionData(
            items: List<T>,
            itemToSectionData: (T) -> S
    ): List<ItemOrSectionData<T, S>> {

        val sectionsMap = mutableMapOf<S, MutableList<T>>()
        val sortedKeys = mutableListOf<S>()

        for (item in items) {
            val date = itemToSectionData(item)
            if (sectionsMap[date] == null) {
                sectionsMap[date] = mutableListOf()
                sortedKeys.add(date)
            }
            sectionsMap[date]?.add(item)
        }


        val list = mutableListOf<ItemOrSectionData<T, S>>()
        for (key in sortedKeys) {
            list.add(ItemOrSectionData<T, S>(null, key))
            list.addAll(sectionsMap[key]?.map { ItemOrSectionData<T, S>(it, null) } ?: listOf())
        }
        return list
    }
    //endregion

    companion object {
        private const val ITEM = 0
        private const val SECTION = 1
    }
}