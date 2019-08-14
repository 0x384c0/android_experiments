package com.example.experimentskotlin.view.custom

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.view.ViewTreeObserver
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import com.example.experimentskotlin.R


/**
 * Кастомный TextInput с багфиксом положения поля hint
 */
class CustomFilledBoxTextInputEditText : TextInputEditText {
    //region Constructors
    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet) : super(context, attrs)
    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr)
    //endregion

    //region LifeCycle
    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        textInputEditText.setOnFocusChangeListener { _, hasFocus ->
            updateHintPosition(hasFocus, !textInputEditText.text.isNullOrEmpty())
        }
        textInputEditText.viewTreeObserver.addOnPreDrawListener(object : ViewTreeObserver.OnPreDrawListener {
            override fun onPreDraw(): Boolean {
                if ((textInputLayout?.height ?: 0) > 0) {
                    textInputLayout?.viewTreeObserver?.removeOnPreDrawListener(this)
                    updateHintPosition(textInputEditText.hasFocus(), !textInputEditText.text.isNullOrEmpty())
                    return false
                }
                return true
            }
        })
    }
    //endregion

    //region Center hint
    private var paddingBottomBackup:Int? = null
    private var passwordToggleButtonPaddingBottomBackup:Float? = null
    private val textInputEditText: TextInputEditText
        get() {
            return this
        }
    private val textInputLayout:TextInputLayout?
        get(){
            return if (parent is TextInputLayout) (parent as? TextInputLayout) else (parent?.parent as? TextInputLayout)
        }
    private val passwordToggleButton:View?
        get() {
            return (parent as? View)?.findViewById(R.id.text_input_password_toggle)
        }

    private fun updateHintPosition(hasFocus: Boolean, hasText: Boolean) {
        if (paddingBottomBackup == null)
            paddingBottomBackup = paddingBottom

        if (hasFocus || hasText)
            textInputEditText.setPadding(paddingLeft, paddingTop, paddingRight, paddingBottomBackup!!)
        else
            textInputEditText.setPadding(paddingLeft, paddingTop, paddingRight, paddingBottomBackup!! + getTextInputLayoutTopSpace())

        val button = passwordToggleButton
        if (button != null){
            if (passwordToggleButtonPaddingBottomBackup == null)
                passwordToggleButtonPaddingBottomBackup = button.translationY

            if (hasFocus || hasText)
                button.translationY =  - getTextInputLayoutTopSpace().toFloat() * 0.50f
            else
                button.translationY = passwordToggleButtonPaddingBottomBackup!!
        }
    }

    private fun getTextInputLayoutTopSpace(): Int {
        var currentView: View = textInputEditText
        var space = 0
        do {
            space += currentView.top
            currentView = currentView.parent as View
        } while (currentView !is TextInputLayout)
        return space
    }
    //endregion

    //region Internal classes
    data class Padding(val l: Int, val t: Int, val r: Int, val b: Int)
    //endregion
}