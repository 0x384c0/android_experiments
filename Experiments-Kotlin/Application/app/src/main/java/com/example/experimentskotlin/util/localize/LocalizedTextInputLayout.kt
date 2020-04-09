package com.example.experimentskotlin.util.localize

import android.content.Context
import android.util.AttributeSet
import com.example.experimentskotlin.R
import com.example.experimentskotlin.util.localize.getLocalizedString
import com.google.android.material.textfield.TextInputLayout

/**
 * TextInputLayout с переведенными строками
 */
class LocalizedTextInputLayout : TextInputLayout {
    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        setup(context, attrs)
    }

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ) {
        setup(context, attrs)
    }

    private fun setup(context: Context, attrs: AttributeSet?) {
        val a = context.theme.obtainStyledAttributes(
            attrs,
            R.styleable.TextInputLayout,
            0, 0
        )
        hint = context.getLocalizedString(
            a.getResourceId(
                R.styleable.TextInputLayout_android_hint,
                R.string.blank_string
            )
        )
    }
}