package com.example.experimentskotlin.util.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.PublishSubject

class SingleLayoutSectionedDiffUtilAdapter<T, S>(
        private val itemId: Int,
        private val headerId: Int,
        private val itemViewHolderFactory: (View) -> SingleLayoutSectionedAdapter.BaseItemViewHolder<T>,
        private val sectionViewHolderFactory: (View) -> SingleLayoutSectionedAdapter.BaseItemViewHolder<S>
) : RecyclerView.Adapter<SingleLayoutSectionedAdapter.BaseItemViewHolder<*>>() {
    //region Data logic
    private var data = listOf<ItemOrSectionData<T, S>>()
    //endregion

    //region Overrides
    override fun getItemViewType(position: Int): Int {
        return when {
            data[position].itemData != null -> ITEM
            data[position].sectionData != null -> SECTION
            else -> throw Exception("SingleLayoutSectionedDiffUtilAdapter.getItemViewType invalid item at position $position")
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SingleLayoutSectionedAdapter.BaseItemViewHolder<*> {
        val layoutId = if (viewType == ITEM) itemId else headerId
        val view = LayoutInflater.from(parent.context).inflate(layoutId, parent, false)
        val viewHolderFactory = if (viewType == ITEM) itemViewHolderFactory else sectionViewHolderFactory
        return viewHolderFactory(view)
    }

    override fun onBindViewHolder(holder: SingleLayoutSectionedAdapter.BaseItemViewHolder<*>, position: Int) {
        val item = data[position]
        @Suppress("UNCHECKED_CAST")
        when {
            item.itemData != null -> (holder as? SingleLayoutSectionedAdapter.BaseItemViewHolder<T>)?.setup(item.itemData)
            item.sectionData != null -> (holder as? SingleLayoutSectionedAdapter.BaseItemViewHolder<S>)?.setup(item.sectionData)
            else -> throw Exception("SingleLayoutSectionedDiffUtilAdapter.getItemViewType invalid item at position $position")
        }

    }

    override fun getItemCount(): Int {
        return data.count()
    }
    //endregion


    //region Classes
    class DiffUtilCallback<T>(
            private val oldList: List<T>,
            private val newList: List<T>
    ) : androidx.recyclerview.widget.DiffUtil.Callback() {
        override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            return oldList[oldItemPosition] == newList[newItemPosition]
        }

        override fun getOldListSize(): Int {
            return oldList.count()
        }

        override fun getNewListSize(): Int {
            return newList.count()
        }

        override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            return oldList[oldItemPosition].hashCode() == newList[newItemPosition].hashCode()
        }
    }

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

        override fun hashCode(): Int {
            var result = itemData?.hashCode() ?: 0
            result = 31 * result + (sectionData?.hashCode() ?: 0)
            return result
        }
    }
    //endregion

    //region Helpers
    private fun toItemOrSectionData(
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
            list.add(ItemOrSectionData(null, key))
            list.addAll(sectionsMap[key]?.map { ItemOrSectionData<T, S>(it, null) } ?: listOf())
        }
        return list
    }

    private fun calculateData(
            items: List<T>,
            itemToSectionData: (T) -> S): Pair<List<ItemOrSectionData<T, S>>, DiffUtil.DiffResult> {
        val sectionsData = toItemOrSectionData(items, itemToSectionData)
        val callback = DiffUtilCallback(data, sectionsData)
        val diff = DiffUtil.calculateDiff(callback)
        return sectionsData to diff
    }

    private fun setCalculatedData(calculatedData: Pair<List<ItemOrSectionData<T, S>>, DiffUtil.DiffResult>) {
        data = calculatedData.first
        calculatedData.second.dispatchUpdatesTo(this)
    }

    private val dataPublishSubject = PublishSubject.create<List<T>>()
    private var dataDisposable: Disposable? = null
    private lateinit var tmpList: List<T>
    fun calculateAndSetDataInBackgroundThread(
            items: List<T>,
            itemToSectionData: (T) -> S
    ) {
        tmpList = items
        if (dataDisposable == null)
            dataDisposable = dataPublishSubject
                    .observeOn(Schedulers.single())
                    .map { calculateData(tmpList, itemToSectionData) }
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(this::setCalculatedData)
        dataPublishSubject.onNext(items)
    }
    //endregion

    companion object {
        private const val ITEM = 0
        private const val SECTION = 1
    }
}