package com.example.experimentskotlin.view.custom

import android.annotation.SuppressLint
import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.FrameLayout
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.example.corenetwork.extension.getLocalizedString
import com.example.experimentskotlin.R
import kotlinx.android.synthetic.main.view_recycler_placeholder.view.*

/**
 * RecyclerView с плейсхолдером
 *
 * parent должен бытьFrameLayout
 */
class RecyclerViewWithEmptyView : RecyclerView {
    //region Init
    constructor(context: Context) : super(context)

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs)

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr)
    //endregion

    //region private
    private var emptyView: View? = null
    private var placeholderTextId: Int = R.string.close
        set(value) {
            if (field != value) {
                field = value
                emptyView?.textView?.text = context.getLocalizedString(placeholderTextId)
            }
        }
    private var topPadding: Int = 0
        set(value) {
            if (field != value) {
                field = value
                emptyView?.setPadding(0, topPadding, 0, 0)
            }
        }

    @SuppressLint("DrawAllocation")
    override fun onLayout(changed: Boolean, l: Int, t: Int, r: Int, b: Int) {
        super.onLayout(changed, l, t, r, b)
        if (emptyView == null) {
            val inflater =
                context?.applicationContext?.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as? LayoutInflater

            if (inflater != null) {
                emptyView = inflater.inflate(R.layout.view_recycler_placeholder, null)
                emptyView?.textView?.text = context.getLocalizedString(placeholderTextId)
                emptyView?.setPadding(0, topPadding, 0, 0)
            }

            var parentView: FrameLayout? = null
            if (parent is FrameLayout)
                parentView = parent as? FrameLayout
            else if (parent?.parent is FrameLayout)
                parentView = parent.parent as? FrameLayout
            parentView?.addView(emptyView,0)
        }
    }

    private val emptyObserver = AdapterDataObserver(
        { adapter },
        { emptyView }
    )

    override fun setAdapter(adapter: RecyclerView.Adapter<*>?) {
        super.setAdapter(adapter)
        try {
            adapter?.registerAdapterDataObserver(emptyObserver)
        } catch (it: IllegalStateException) {
        }
        emptyObserver.onChanged()
    }
    //endregion

    //region public
    fun setup(
        placeholderTextId: Int,
        topPadding: Int = 0,
        emptyViewIsVisible: ((Adapter<*>) -> Boolean)? = null
    ) {
        this.placeholderTextId = placeholderTextId
        this.topPadding = topPadding
        emptyObserver.emptyViewIsVisible = emptyViewIsVisible
    }
    //endregion

    /**
     * Класс, наблюдающий за изменениями в Adapter и обновляющий фон для RecyclerView
     */
    private class AdapterDataObserver(
            private val adapter: () -> Adapter<*>?,
            private val emptyView: () -> View?
    ) :
            RecyclerView.AdapterDataObserver() {
        override fun onChanged() {
            resetBgState()
        }

        override fun onItemRangeChanged(positionStart: Int, itemCount: Int) {
            resetBgState()
        }

        override fun onItemRangeInserted(positionStart: Int, itemCount: Int) {
            resetBgState()
        }

        override fun onItemRangeChanged(positionStart: Int, itemCount: Int, payload: Any?) {
            resetBgState()
        }

        override fun onItemRangeMoved(fromPosition: Int, toPosition: Int, itemCount: Int) {
            resetBgState()
        }

        override fun onItemRangeRemoved(positionStart: Int, itemCount: Int) {
            resetBgState()
        }

        var emptyViewIsVisible: ((Adapter<*>) -> Boolean)? = null
        private fun resetBgState(){
            val adapter = adapter()
            val emptyView = emptyView()
            val emptyViewIsVisible = emptyViewIsVisible
            if (adapter != null && emptyView != null) {
                val isVisible = if (emptyViewIsVisible != null)
                    emptyViewIsVisible(adapter)
                else
                    adapter.itemCount == 0
                emptyView.isVisible = isVisible
            }
        }
    }
}