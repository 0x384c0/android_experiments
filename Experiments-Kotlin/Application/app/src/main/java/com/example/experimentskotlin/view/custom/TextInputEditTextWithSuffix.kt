package com.example.experimentskotlin.view.custom

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.text.Editable
import android.text.TextPaint
import android.util.AttributeSet
import androidx.core.widget.addTextChangedListener
import androidx.databinding.BindingAdapter
import com.google.android.material.textfield.TextInputEditText

class TextInputEditTextWithSuffix : TextInputEditText {
    private var textPaint = TextPaint()
    private var suffix = ""
    private var suffixPadding: Float = 0.toFloat()

    constructor(context: Context) : super(context)

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs)

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

    public override fun onDraw(c: Canvas) {
        super.onDraw(c)
        val suffixXPosition = textPaint.measureText(text!!.toString()).toInt() + paddingLeft
        c.drawText(suffix, suffixXPosition + suffixPadding, baseline.toFloat(), textPaint)
    }

    override fun onFinishInflate() {
        super.onFinishInflate()
        textPaint.color = currentTextColor
        textPaint.textSize = textSize
        textPaint.textAlign = Paint.Align.LEFT
    }

    fun setup(suffix: String, suffixPadding: Float) {
        this.suffix = suffix
        this.suffixPadding = suffixPadding
        showOrHideSuffix(text)
        if (isListenerNotSet) {
            addTextChangedListener { text ->
                showOrHideSuffix(text)
            }
            isListenerNotSet = false
        }
    }

    private var isListenerNotSet = true
    private fun showOrHideSuffix(text: Editable?) {
        textPaint.alpha = if (text.isNullOrEmpty()) 0 else 255
    }

    companion object {
        @JvmStatic
        @BindingAdapter("suffix")
        fun setSuffix(input: TextInputEditTextWithSuffix, suffix: String?) {
            input.setup(suffix ?: "", 20f)
            input.invalidate()
        }
    }
}
