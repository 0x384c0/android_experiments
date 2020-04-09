package com.example.experimentskotlin.util

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView

/*
recyclerView.addItemDecoration(
            MarginItemDecoration(
                resources.getDimension(R.dimen.margin_medium).toInt(),
                RecyclerView.VERTICAL
            )
        )
 */

class MarginItemDecoration(
    private val spacing: Int,
    private val direction: Int,
    private val margin: Int = 0
) :
    RecyclerView.ItemDecoration() {
    override fun getItemOffsets(
        outRect: Rect, view: View,
        parent: RecyclerView, state: RecyclerView.State
    ) {
        if (direction == RecyclerView.HORIZONTAL)
            with(outRect) {
                top = margin
                left = if (parent.getChildAdapterPosition(view) == 0) 0 else spacing
                right = 0
                bottom = margin
            }
        else
            with(outRect) {
                top = if (parent.getChildAdapterPosition(view) == 0) margin else spacing / 2
                left = margin
                right = margin
                bottom = spacing / 2
            }
    }
}


class GridMarginItemDecoration(
    private val spacing: Int,
    private val spanCount: Int
) :
    RecyclerView.ItemDecoration() {
    override fun getItemOffsets(
        outRect: Rect, view: View,
        parent: RecyclerView, state: RecyclerView.State
    ) {
        val pos = parent.getChildAdapterPosition(view)
        with(outRect) {
            top = if (pos < spanCount) 0 else spacing
            left = if (pos % spanCount != 0) spacing / 2 else 0
            right = if (pos % spanCount == 0) spacing / 2 else 0
            bottom = 0
        }
    }
}