package com.example.experimentskotlin.util.localize

import android.content.Context
import android.view.Menu
import androidx.core.view.iterator

/**
 *
 * Класс workaround, переводит меню из resources, тк андроид этого не делает
 */
class MenuItemLocalizer:BaseLocalizer() {
    companion object {
        /**
         * перевод меню с помощью context.getString
         * должен вызываться после MenuInflater.inflate
         */
        fun localizeMenu(
            context: Context,
            menu: Menu
        ) {
            for (item in menu.iterator()){
                val t = item.title?.toString()
                if (!t.isNullOrBlank())
                    item.title = translateString(context,t)
            }
        }
    }
}