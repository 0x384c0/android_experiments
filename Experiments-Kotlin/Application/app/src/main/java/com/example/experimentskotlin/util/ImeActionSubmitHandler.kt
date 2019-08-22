package com.example.experimentskotlin.util

import android.view.inputmethod.EditorInfo
import android.widget.Button
import android.widget.EditText
import com.example.experimentskotlin.baseclasses.BaseFragment


class ImeActionSubmitHandler(
    editTexts: List<EditText>,
    submitButton: Button? = null,
    fragment: BaseFragment
) {
    constructor(
        editText: EditText,
        submitButton: Button,
        fragment: BaseFragment
    )
            : this(
        listOf(editText),
        submitButton,
        fragment
    )

    init {
        for (editText in editTexts) {
            editText.maxLines = 1
            editText.imeOptions = EditorInfo.IME_ACTION_DONE
            editText.setOnEditorActionListener { _, _, _ ->
                fragment.hideKeyboard()
                true
            }
        }
    }
}