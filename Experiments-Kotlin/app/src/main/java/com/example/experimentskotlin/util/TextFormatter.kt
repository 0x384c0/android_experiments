package com.example.experimentskotlin.util

import android.text.Spannable
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import android.widget.TextView
import com.example.experimentskotlin.R

class TextFormatter {
    companion object {
        fun colorFloatAmount(textView: TextView,text:String){
            try {
                val r = Regex("\\.\\d\\d")
                changeTextInView(textView,r.find(text)!!.value,text, R.color.colorAccent)
            } catch (it:Throwable){
                textView.text = text
            }
        }

        private fun changeTextInView(tv: TextView, target: String, vString:String, colour: Int) {
            var startSpan = 0
            var endSpan = 0
            val spanRange = SpannableString(vString)

            while (true) {
                startSpan = vString.indexOf(target, endSpan)
                val foreColour = ForegroundColorSpan(colour)
                if (startSpan < 0)
                    break
                endSpan = startSpan + target.length
                spanRange.setSpan(
                    foreColour, startSpan, endSpan,
                    Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
                )
            }
            tv.setText(spanRange, TextView.BufferType.SPANNABLE)
        }
    }
}