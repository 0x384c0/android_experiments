package com.example.experimentskotlin.util.extensions

import android.text.InputFilter
import com.google.android.material.textfield.TextInputEditText

var TextInputEditText.maxLength: Int?
    get() = null
    set(value) {
        if (value != null)
            this.filters = arrayOf(InputFilter.LengthFilter(value))
        else
            this.filters = null
    }