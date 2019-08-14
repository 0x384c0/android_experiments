package com.example.experimentskotlin.view.custom

import android.content.Context
import android.os.Build
import android.util.AttributeSet
import android.widget.ImageView
import androidx.appcompat.widget.SearchView
import androidx.core.content.ContextCompat
import com.example.experimentskotlin.R

/**
 * Кастомный класс SearchView с стилями
 */
class CustomSearchView : SearchView {

    constructor(context: Context) : super(context) {
        setup(context, null)
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        setup(context, attrs)
    }

    private fun setup(context: Context, attrs: AttributeSet?) {

        val mCloseButton = findViewById<ImageView>(androidx.appcompat.R.id.search_close_btn)
        mCloseButton.setColorFilter(
            ContextCompat.getColor(
                context,
                R.color.white
            )
        )
        val mSearchButton = findViewById<ImageView>(androidx.appcompat.R.id.search_button)
        mSearchButton.setColorFilter(
            ContextCompat.getColor(
                context,
                R.color.white
            )
        )
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            setPaddingRelative(-16, 0, 0, 0)
        } else {
            setPadding(-16, 0, 0, 0)
        }
    }
}