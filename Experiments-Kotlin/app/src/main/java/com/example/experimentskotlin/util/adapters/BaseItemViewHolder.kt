package com.example.experimentskotlin.util.adapters

import android.view.View
import androidx.recyclerview.widget.RecyclerView

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