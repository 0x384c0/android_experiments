package com.example.experimentskotlin.util.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView

class SingleLayoutAdapter<T>(
    private val itemLayoutId: Int,
    private val itemViewHolderFactory: (View) -> BaseItemViewHolder<T>
) : RecyclerView.Adapter<BaseItemViewHolder<T>>() {

    //region Adapter logic
    var data: List<T>
        set(value) {
            _data = value
            notifyDataSetChanged()
        }
        get() = _data
    private var _data: List<T> = listOf()


    override fun getItemCount(): Int {
        return _data.count()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseItemViewHolder<T> {
        val view = LayoutInflater.from(parent.context).inflate(itemLayoutId, parent, false)
        return itemViewHolderFactory(view)
    }

    override fun onBindViewHolder(itemViewHolder: BaseItemViewHolder<T>, position: Int) {
        itemViewHolder.setup(_data[position])
    }
    //endregion


    //region Implementation with handlers
    constructor(
        itemLayoutId: Int,
        bindViewHandler: (view: View, data: T) -> Unit,
        onClickHandler: (position: Int, data: T) -> Unit
    ) : this(
        itemLayoutId = itemLayoutId,
        itemViewHolderFactory = {
            ItemViewHolderWithHandler(
                it,
                bindViewHandler,
                onClickHandler
            )
        })
    //endregion
}

abstract class BaseItemViewHolder<T>(var view: View) : RecyclerView.ViewHolder(view) {
    abstract fun setup(data: T)
}

class ItemViewHolderWithHandler<T>(
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