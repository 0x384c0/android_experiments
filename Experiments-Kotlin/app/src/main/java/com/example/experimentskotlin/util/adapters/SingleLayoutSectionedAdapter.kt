package com.example.experimentskotlin.util.adapters

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import io.github.luizgrp.sectionedrecyclerviewadapter.SectionParameters
import io.github.luizgrp.sectionedrecyclerviewadapter.SectionedRecyclerViewAdapter
import io.github.luizgrp.sectionedrecyclerviewadapter.StatelessSection

open class SingleLayoutSectionedAdapter<T, S>(
    private val itemId: Int,
    private val headerId: Int,
    private val itemViewHolderFactory: (View) -> BaseItemViewHolder<T>,
    private val sectionViewHolderFactory: (View) -> BaseItemViewHolder<S>
) : SectionedRecyclerViewAdapter() {

    //region Adapter logic
    private var _data: List<SectionData<T, S>> = listOf()
    var data: List<SectionData<T, S>>
        set(value) {
            _data = value
            reloadAllSections()
        }
        get() {
            return _data
        }

    private fun reloadAllSections() {
        removeAllSections()
        for (d in data) {
            addSection(
                SingleLayoutSection(
                    itemsData = d.itemsData,
                    data = d.data,
                    itemViewHolderFactory = itemViewHolderFactory,
                    sectionViewHolderFactory = sectionViewHolderFactory,
                    itemLayoutId = itemId,
                    headerLayoutId = headerId
                )
            )
        }
        notifyDataSetChanged()
    }

    class SingleLayoutSection<T, S>(
        private val itemsData: List<T>,
        private val data: S,
        private val itemViewHolderFactory: (View) -> BaseItemViewHolder<T>,
        private val sectionViewHolderFactory: (View) -> BaseItemViewHolder<S>,
        itemLayoutId: Int,
        headerLayoutId: Int
    ) : StatelessSection(
        SectionParameters.builder()
            .itemResourceId(itemLayoutId)
            .headerResourceId(headerLayoutId)
            .build()
    ) {
        override fun getContentItemsTotal(): Int {
            return itemsData.count()
        }

        override fun onBindItemViewHolder(holder: RecyclerView.ViewHolder?, position: Int) {
            @Suppress("UNCHECKED_CAST") val itemHolder = holder as BaseItemViewHolder<T>
            itemHolder.setup(itemsData[position])
        }

        override fun getItemViewHolder(view: View?): RecyclerView.ViewHolder {
            return itemViewHolderFactory(view!!)
        }

        override fun getHeaderViewHolder(view: View?): RecyclerView.ViewHolder {
            return sectionViewHolderFactory(view!!)
        }

        override fun onBindHeaderViewHolder(holder: RecyclerView.ViewHolder?) {
            @Suppress("UNCHECKED_CAST") val sectionViewHolder = holder as BaseItemViewHolder<S>
            sectionViewHolder.setup(data)
        }

    }

    abstract class BaseItemViewHolder<T>(var view: View) : RecyclerView.ViewHolder(view) {
        abstract fun setup(data: T)
    }
    //endregion


    //region Implementation with handlers
    constructor(
        itemId: Int,
        headerId: Int,
        bindViewHandler: (view: View, data: T) -> Unit,
        bindHeaderViewHandler: (view: View, data: S) -> Unit,
        onClickHandler: ((position: Int, data: T) -> Unit)
    ) : this(
        itemId,
        headerId,
        {
            SectionItemViewHolderWithHandler(
                view = it,
                bindViewHandler = bindViewHandler,
                onClickHandler = onClickHandler
            )
        },
        {
            SectionHeaderViewHolderWithHandler(
                view = it,
                bindViewHandler = bindHeaderViewHandler
            )
        }
    )

    class SectionItemViewHolderWithHandler<T>(
        view: View,
        private val bindViewHandler: (view: View, data: T) -> Unit,
        private val onClickHandler: (position: Int, data: T) -> Unit
    ) : BaseItemViewHolder<T>(view), View.OnClickListener {
        init {
            view.setOnClickListener(this)
        }

        var data: T? = null
        override fun setup(data: T) {
            this.data = data
            bindViewHandler.invoke(view, data!!)
        }

        override fun onClick(p0: View?) {
            onClickHandler.invoke(adapterPosition, data!!)
        }

    }

    class SectionHeaderViewHolderWithHandler<T>(
        view: View,
        private val bindViewHandler: (view: View, data: T) -> Unit
    ) : BaseItemViewHolder<T>(view) {

        var data: T? = null
        override fun setup(data: T) {
            this.data = data
            bindViewHandler.invoke(view, data!!)
        }
    }
    //endregion
}


@Suppress("EqualsOrHashCode")
data class SectionData<T, S>(
        val itemsData: List<T>,
        val data: S
) {
    override fun equals(other: Any?): Boolean {
        try {
            @Suppress("UNCHECKED_CAST")
            val otherSection = other as SectionData<T, S>
            val sameItemsCount = itemsData.count() == otherSection.itemsData.count()
            if (sameItemsCount) {
                var itemsEquals = true
                itemsData.forEachIndexed { i, item ->
                    if (item != otherSection.itemsData[i]) {
                        itemsEquals = false
                        return@forEachIndexed
                    }
                }
                return itemsEquals
            }
        } catch (e: Exception) {
        }
        return false
    }
}
