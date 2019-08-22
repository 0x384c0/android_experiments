package com.example.experimentskotlin.util

import android.content.Context
import android.os.Handler
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.Fragment
import com.example.corenetwork.Constants
import kotlinx.android.synthetic.main.view_search_edit_text.*

/**
 * Класс для управления SearchEditText
 *
 * Fragment должен содержать в себе view_search_edit_text.xml
 */
class SearchEditTextManager(
    private val fragment: Fragment,
    private val debounceTime: Long = Constants.DEBOUNCE_INPUT_TIME,
    private val searchHandler: (String) -> (Unit)
) {
    val isEmpty: Boolean
        get() {
            return try {
                fragment.searchEditText.text.isNullOrEmpty()
            } catch (e:Throwable){
                true
            }
        }
    //region Search
    private val handler = Handler()

    init {
        fragment.searchEditText.doAfterTextChanged {
            if (it.isNullOrEmpty()) {
                fragment.searchImage.visibility = View.VISIBLE
                fragment.clearSearchButton.visibility = View.GONE
            } else {
                fragment.searchImage.visibility = View.GONE
                fragment.clearSearchButton.visibility = View.VISIBLE
            }
            if (debounceTime == 0L) {
                searchHandler(it.toString())
            } else {
                handler.removeCallbacksAndMessages(null)
                handler.postDelayed({ searchHandler(it.toString()) }, debounceTime)
            }
        }
        fragment.searchImage.setOnClickListener {
            if (!fragment.searchEditText.isFocused) {
                fragment.searchEditText.requestFocus()
                val imm = fragment.activity?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager?
                imm?.showSoftInput(fragment.searchEditText, InputMethodManager.SHOW_IMPLICIT)
            }
        }
        fragment.clearSearchButton.setOnClickListener {
            fragment.searchEditText.setText("")
        }
    }
    //endregion
}