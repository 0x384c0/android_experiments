package com.example.experimentskotlin.view.custom

import android.app.AlertDialog
import android.content.Context
import androidx.annotation.StringRes
import androidx.annotation.StyleRes

/*
private val typesMap by lazy {
        mapOf(
            getString(R.string.type_all) to null,
            getString(R.string.type_1) to Type.Type1,
            getString(R.string.type_2) to Type.Type2,
        )
    }

    SingleChoiceAlertDialogBuilder(
                context!!,
                R.style.AppTheme_AlertDialogTheme,
                R.string.select_type,
                R.string.ok,
                R.string.cancel,
                typesMap,
                selectedType,
                { selectedValue ->
                }
            )
 */
class SingleChoiceAlertDialogBuilder<T>(
    c: Context,
    @StyleRes themeResId: Int,
    @StringRes title: Int,
    @StringRes positiveButtonTitle: Int,
    @StringRes negativeButtonTitle: Int,
    map: Map<String, T>,
    var selectedValue: T?,
    handler: (Pair<String, T>?) -> Unit
) : AlertDialog.Builder(c, themeResId) {
    init {
        val valuesStrings = map.keys.toTypedArray()
        var selectedValueId = map.values.toList().indexOf(selectedValue)
        setTitle(title)
        setPositiveButton(positiveButtonTitle) { _, _ ->
            val selectedValue = selectedValue
            if (selectedValue != null)
                handler(valuesStrings[selectedValueId] to selectedValue)
            else
                handler(null)
        }
        setNegativeButton(negativeButtonTitle) { _, _ -> }
        setSingleChoiceItems(valuesStrings, selectedValueId) { _, item ->
            selectedValueId = item
            selectedValue = map[valuesStrings[selectedValueId]]
        }
    }
}