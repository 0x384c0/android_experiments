package com.example.experimentskotlin.util.adapters.paging

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.paging.PagedList
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.DiffUtil
import com.example.experimentskotlin.util.adapters.BaseItemViewHolder
import com.example.experimentskotlin.util.adapters.ItemViewHolderWithHandler

class PagedListLayoutAdapter<T>(
        private val itemLayoutId: Int,
        private val itemViewHolderFactory: (View) -> BaseItemViewHolder<T>,
        owner: LifecycleOwner,
        pagedLiveData: LiveData<PagedList<T>>,
        diffUtil: DiffUtil.ItemCallback<T>
) : PagedListAdapter<T, BaseItemViewHolder<T>>(diffUtil) {
    init {
        pagedLiveData.observe(owner, Observer<PagedList<T>> { list -> submitList(list) })
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseItemViewHolder<T> {
        val view = LayoutInflater.from(parent.context).inflate(itemLayoutId, parent, false)
        return itemViewHolderFactory(view)
    }

    override fun onBindViewHolder(holder: BaseItemViewHolder<T>, position: Int) {
        val item = getItem(position)
        if (item != null)
            holder.setup(item)
    }


    //region Implementation with handlers
    constructor(
            itemLayoutId: Int,
            bindViewHandler: (view: View, data: T) -> Unit,
            onClickHandler: (position: Int, data: T) -> Unit,
            owner: LifecycleOwner,
            pagedLiveData: LiveData<PagedList<T>>,
            diffUtil: DiffUtil.ItemCallback<T>
    ) : this(
            itemLayoutId = itemLayoutId,
            itemViewHolderFactory = {
                ItemViewHolderWithHandler(
                        it,
                        bindViewHandler,
                        onClickHandler
                )
            },
            owner = owner,
            pagedLiveData = pagedLiveData,
            diffUtil = diffUtil)
    //endregion
}