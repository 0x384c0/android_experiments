package com.example.corenetwork.utils

import java.text.DecimalFormat
import java.text.DecimalFormatSymbols
import java.util.*

/**
 * Класс для форматирования чисел в строки для отображения их в UI
 * набор статичных фунций для различных целей
 */
@Suppress("LiftReturnOrAssignment")
class NumberFormatter {
    companion object {
        /**
         * сконвертировать число  в строку и добавить 4 нуля справа после точки
         */
        fun formatToStringFourZeros(balance: Float?): String {
            val balance = balance ?: 0f
            return try {
                String.format("%.4f", balance)
            } catch (it: Throwable) {
                balance.toString()
            }
        }

        /**
         * сконвертировать число  в строку и добавить 4 нуля справа после точки
         */
        fun formatToStringFourZeros(balance: Double?): String {
            val balance = balance ?: 0.0
            return try {
                String.format("%.4f", balance)
            } catch (it: Throwable) {
                balance.toString()
            }
        }

        /**
         * в строку добавить 4 нуля справа после точки
         */
        private var formatter4: DecimalFormat? = null

        private fun format4(balance: Float?): String {
            if (formatter4 == null) {
                val formatSymbols = DecimalFormatSymbols(Locale.ENGLISH);
                formatSymbols.decimalSeparator = '.'
                formatSymbols.groupingSeparator = ' '
                formatter4 = DecimalFormat("#,##0.0000", formatSymbols)
            }
            return formatter4!!.format(balance)
        }


        /**
         * сконвертировать число  в строку и добавить 2 нуля справа после точки
         */
        fun formatToStringTwoZeros(balance: Float?): String {
            val balance = balance ?: 0f
            return try {
                format2(balance)
            } catch (it: Throwable) {
                balance.toString()
            }

        }

        /**
         * сконвертировать число  в строку и добавить 2 нуля справа после точки
         */
        fun formatToStringTwoZeros(balance: Int?): String {
            val balance = balance ?: 0
            return try {
                format2(balance.toFloat())
            } catch (it: Throwable) {
                balance.toString()
            }

        }

        /**
         * сконвертировать число  в строку и добавить 2 нуля справа после точки
         */
        fun formatToStringTwoZeros(balance: Long?): String {
            val balance = balance ?: 0
            return try {
                format2(balance.toFloat())
            } catch (it: Throwable) {
                balance.toString()
            }

        }

        /**
         * сконвертировать число  в строку и добавить 2 нуля справа после точки
         */
        fun formatToStringTwoZeros(balance: Double?): String {
            val balance = balance ?: 0.toDouble()
            return try {
                format2(balance.toFloat())
            } catch (it: Throwable) {
                balance.toString()
            }

        }

        /**
         * сконвертировать число  в строку и добавить 2 нуля справа после точки
         */
        fun formatToStringTwoZeros(balance: String?): String {
            val balance = balance ?: "0"
            return try {
                format2(balance.toFloat())
            } catch (it: Throwable) {
                balance
            }

        }

        /**
         * в строку добавить 2 нуля справа после точки
         */
        private var formatter2: DecimalFormat? = null

        private fun format2(balance: Float?): String {
            if (formatter2 == null) {
                val formatSymbols = DecimalFormatSymbols(Locale.ENGLISH);
                formatSymbols.decimalSeparator = '.'
                formatSymbols.groupingSeparator = ' '
                formatter2 = DecimalFormat("#,##0.00", formatSymbols)
            }
            return formatter2!!.format(balance)
        }
    }
}