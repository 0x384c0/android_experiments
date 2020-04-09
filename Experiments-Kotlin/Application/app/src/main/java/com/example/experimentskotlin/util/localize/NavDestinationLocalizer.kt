package com.example.experimentskotlin.util.localize

import android.content.Context
import android.os.Bundle
import androidx.appcompat.app.ActionBar
import androidx.navigation.NavController
import androidx.navigation.NavDestination

/**
 *
 * Класс workaround, переводит заголовки фрагментов, тк андроид этого не делает
 */
class NavDestinationLocalizer(private val context: Context, private val actionBar: ActionBar?) :
    BaseLocalizer(),
    NavController.OnDestinationChangedListener {

    override fun onDestinationChanged(
        controller: NavController,
        destination: NavDestination,
        arguments: Bundle?
    ) {
        @Suppress("RegExpRedundantEscape")
        if (destination.label?.matches(Regex("^\\{.*\\}$")) == false) {
            destination.label = translateString(context, destination.label.toString())
            actionBar?.title = destination.label
        }
    }
}