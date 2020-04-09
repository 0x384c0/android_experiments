package com.example.experimentskotlin.util


import android.view.View
import android.widget.CheckBox
import android.widget.EditText
import androidx.databinding.BindingAdapter
import androidx.databinding.InverseBindingAdapter
import androidx.databinding.InverseBindingListener
import com.example.corenetwork.extension.requestFocusWithKeyboard
import com.google.android.material.textfield.TextInputLayout


//region Others
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
//endregion

//region EditText
@BindingAdapter("isFocused")
fun View.setIsFocused(isFocus: Boolean) {
    if (isFocus != getIsFocused())
        if (isFocus)
            if (this is EditText)
                this.requestFocusWithKeyboard()
            else
                this.requestFocus()
        else
            this.clearFocus()
}

@InverseBindingAdapter(attribute = "isFocused")
fun View.getIsFocused() = isFocused

@BindingAdapter("isFocusedAttrChanged")
fun View.setIsFocusedChangedListener(inverseListener: InverseBindingListener) {
    this.setOnFocusChangeListener { _, _ -> inverseListener.onChange() }
}
//endregion

//region CheckBox
@BindingAdapter("isCheckedAttrChanged")
fun CheckBox.setIsCheckedValueListener(listener: InverseBindingListener?) {
    if (listener != null) {
        setOnCheckedChangeListener { _, _ ->
            listener.onChange()
        }
    }
}

@BindingAdapter("isChecked")
fun CheckBox.setIsCheckedValue(value: Boolean?) {
    if (isChecked != value) {
        isChecked = value ?: false
    }
}


@InverseBindingAdapter(attribute = "isChecked")
fun CheckBox.getIsCheckedValue(): Boolean {
    return isChecked
}
//endregion