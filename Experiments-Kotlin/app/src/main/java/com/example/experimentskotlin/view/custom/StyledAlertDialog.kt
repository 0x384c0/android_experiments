package com.example.experimentskotlin.view.custom

import android.content.Context
import androidx.appcompat.app.AlertDialog
import com.example.experimentskotlin.R

/**
 * AlertDialog Builder с примененными стилями из дизайна
 */
class StyledAlertDialogBuilder(context: Context?) : AlertDialog.Builder(context!!, R.style.AlertDialogTheme)