package com.example.experimentskotlin.util.localize

import android.content.Context
import com.example.corenetwork.extension.getLocalizedString
import com.example.experimentskotlin.R

/**
 * Класс, генерирующий словать со всеми строками и их ID
 *
 * нужен для получения id строки и перевода ее с помощью context.getString
 */
open class BaseLocalizer {
    companion object {
        internal fun translateString(context: Context, string: String): String {
            try {
                val id = stringsMap?.get(string)
                if (id != null) {
                    return context.resources.getLocalizedString(id)
                }
            } catch (e: Exception) {
            }
            return string
        }


        fun init(applicationContext: Context) {
            val fields = R.string::class.java.fields
            val mutableStringsMap = mutableMapOf<String, Int>()
            for (filed in fields) {
                try {
                    val id = filed.getInt(R.string::class.java)
                    mutableStringsMap[applicationContext.getLocalizedString(id)] = id
                } catch (e: Exception) {
                }
            }
            stringsMap = mutableStringsMap.toMap()
        }

        private var stringsMap: Map<String, Int>? = null
    }
}
