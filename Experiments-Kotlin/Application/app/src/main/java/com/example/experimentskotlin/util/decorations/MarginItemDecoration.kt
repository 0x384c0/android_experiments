package com.example.experimentskotlin.util.decorations

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView

/**
 * декоратор, добавляющий отсупа для item в RecyclerView
 */
class MarginItemDecoration(
    private val spaceHeight: Int,
    private val hideTopMargin: Boolean = false
) : RecyclerView.ItemDecoration() {
    override fun getItemOffsets(
        outRect: Rect, view: View,
        parent: RecyclerView, state: RecyclerView.State
    ) {
        with(outRect) {
            if (parent.getChildAdapterPosition(view) == 0) {
                top = if (hideTopMargin) 0 else spaceHeight
            }
            left = 0
            right = 0
            bottom = spaceHeight
        }
    }
}