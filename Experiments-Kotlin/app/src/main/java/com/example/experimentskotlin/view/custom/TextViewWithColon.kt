package com.example.experimentskotlin.view.custom

import android.content.Context
import android.util.AttributeSet
import android.widget.TextView

class TextViewWithColon : TextView {

    constructor(context: Context) : super(context)

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs)

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr)


    override fun setText(text: CharSequence?, type: BufferType?) {
        var newText = text?.toString()
        if (!newText.isNullOrBlank()) {
            if (newText.last() != ':')
                newText += ':'
            super.setText(newText, type)
        } else {
            super.setText(text, type)
        }
    }
}