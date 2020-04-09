package com.example.experimentskotlin.util.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.ViewModel
import androidx.recyclerview.widget.RecyclerView

/**
 * адаптер для RecyclerView с item разного типа
 *
 * https://developer.android.com/reference/android/support/v7/widget/RecyclerView.Adapter
 */
class MultiLayoutAdapter(
    context: Context,
    itemViewHolderFactories: List<((View) -> BaseMultiItemViewHolder<*>)>
) : RecyclerView.Adapter<BaseMultiItemViewHolder<Any>>() {

    //region Init
    private val factoryMap: Map<Int, ((View) -> BaseMultiItemViewHolder<*>)>
    private val typesMap: Map<Class<out Any>, Int>
    private val layoutIdMap: Map<Int, Int>

    init {
        val factoryMap = mutableMapOf<Int, ((View) -> BaseMultiItemViewHolder<*>)>()
        val typesMap = mutableMapOf<Class<*>, Int>()
        val layoutIdMap = mutableMapOf<Int, Int>()
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

    @Suppress("UNCHECKED_CAST")
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): BaseMultiItemViewHolder<Any> {
        val factory = factoryMap[viewType]
            ?: throw IllegalStateException("view holder factory for $viewType not found")

        val layoutInflater = LayoutInflater.from(parent.context)
        val view = layoutInflater
            .inflate(layoutIdMap.getValue(viewType), parent, false)
        var vh = factory(view) as BaseMultiItemViewHolder<Any>

        if (vh is BaseMVVMMultiItemViewHolder<*, *>) {
            val binding = DataBindingUtil
                .inflate<ViewDataBinding>(
                    layoutInflater,
                    layoutIdMap.getValue(viewType),
                    parent,
                    false
                );
            val mvvmVh = factory(binding.root) as BaseMVVMMultiItemViewHolder<*, ViewDataBinding>
            mvvmVh.binding = binding
            vh = mvvmVh as BaseMultiItemViewHolder<Any>
        }

        return vh
    }

    override fun getItemCount(): Int {
        return _data.count()
    }

    override fun onBindViewHolder(holder: BaseMultiItemViewHolder<Any>, position: Int) {
        holder.setup(_data[position])
    }
    //endregion
}

abstract class BaseMultiItemViewHolder<T>(view: View) : BaseItemViewHolder<T>(view) {
    abstract val dataClass: Class<T>
    abstract val itemLayoutId: Int
}

abstract class BaseMVVMMultiItemViewHolder<T : ViewModel, B : ViewDataBinding>(
    view: View
) :
    BaseMultiItemViewHolder<T>(view) {
    lateinit var binding: B//binding.viewModel = viewModel
}