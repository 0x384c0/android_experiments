package com.example.corenetwork.extension

import android.content.Context
import android.os.Handler
import android.view.inputmethod.InputMethodManager
import android.widget.EditText


fun EditText.requestFocusWithKeyboard() {
    Handler().postDelayed({
        if (requestFocus()) {
            val imm = context?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager?
            imm?.showSoftInput(this, InputMethodManager.SHOW_IMPLICIT)
        }
    }, 500)
}