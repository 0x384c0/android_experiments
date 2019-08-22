package com.example.experimentskotlin.util

import android.view.View
import androidx.databinding.BindingAdapter
import com.google.android.material.textfield.TextInputLayout


@BindingAdapter("visible")
fun setVisible(view: View, visible: Boolean) {
    view.visibility = if (visible) View.VISIBLE else View.GONE
}

@BindingAdapter("errorText")
fun setErrorMessage(view: TextInputLayout, errorMessage: String?) {
    if (view.error == errorMessage) return
    if (errorMessage.isNullOrBlank()) {
        view.error = null
        view.isErrorEnabled = false
    } else {
        view.isErrorEnabled = true
        view.error = errorMessage
    }
}