package com.example.experimentskotlin.util.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView

/**
 * адаптер для RecyclerView с item разного типа
 *
 * https://developer.android.com/reference/android/support/v7/widget/RecyclerView.Adapter
 */
class MultiLayoutAdapter(
    context:Context,
    itemViewHolderFactories: List<((View) -> BaseMultiLayoutItemViewHolder<Any>)>
) : RecyclerView.Adapter<MultiLayoutAdapter.BaseMultiLayoutItemViewHolder<Any>>() {

    //region Init
    private val factoryMap:Map<Int,((View) -> BaseMultiLayoutItemViewHolder<Any>)>
    private val typesMap:Map<Class<out Any>,Int>
    private val layoutIdMap:Map<Int,Int>
    init {
        val factoryMap = mutableMapOf<Int,((View) -> BaseMultiLayoutItemViewHolder<Any>)>()
        val typesMap = mutableMapOf<Class<Any>,Int>()
        val layoutIdMap = mutableMapOf<Int,Int>()
        val dummyView = View(context)
        itemViewHolderFactories.forEachIndexed { index, factory ->
            factoryMap[index] = factory
            val dummyAdapter = factory(dummyView)
            typesMap[dummyAdapter.dataClass] = index
            layoutIdMap[index] = dummyAdapter.itemLayoutId
        }
        this.factoryMap = factoryMap.toMap()
        this.typesMap = typesMap.toMap()
        this.layoutIdMap = layoutIdMap.toMap()
    }
    //endregion


    //region Adapter logic
    var data: List<Any>
        set(value) {
            _data = value
            notifyDataSetChanged()
        }
        get() = _data
    private var _data: List<Any> = listOf()

    override fun getItemViewType(position: Int): Int {
        val dataClass = _data[position]::class.java
        return typesMap.getValue(dataClass)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseMultiLayoutItemViewHolder<Any> {
        val view = LayoutInflater.from(parent.context).inflate(layoutIdMap.getValue(viewType), parent, false)
        val factory = factoryMap.getValue(viewType)
        return factory(view)
    }

    override fun getItemCount(): Int {
        return _data.count()
    }

    override fun onBindViewHolder(holder: BaseMultiLayoutItemViewHolder<Any>, position: Int) {
        holder.setup(_data[position])
    }
    //endregion

    abstract class BaseMultiLayoutItemViewHolder<T>(view: View) : BaseItemViewHolder<T>(view) {
        abstract val dataClass:Class<T>
        abstract val itemLayoutId:Int
    }
}