package com.example.experimentskotlin.util.extensions

import android.content.Context
import android.os.Handler
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import androidx.core.view.isVisible


fun EditText.requestFocusWithKeyboard() {
    Handler(context.mainLooper).postDelayed({
        if (requestFocus()) {
            if (isEnabled && isVisible && inputType != EditorInfo.TYPE_NULL) {
                val imm =
                    context?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager?
                imm?.showSoftInput(this, InputMethodManager.SHOW_IMPLICIT)
            }
        }
    }, 500)
}