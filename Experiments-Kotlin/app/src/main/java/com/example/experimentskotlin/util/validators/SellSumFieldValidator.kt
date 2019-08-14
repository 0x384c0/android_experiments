package com.example.experimentskotlin.util.validators

import androidx.databinding.ObservableField
import com.example.corenetwork.helpers.NumberFormatter

open class SellSumFieldValidator(
    field: ObservableField<String?>,
    errorField: ObservableField<String?>,
    emptyErrorMessage: String,
    private val insufficientBalanceMessage: String,
    private val getSellAccountBalance: () -> (Float?)
) : EmptyStringObservableFieldValidator(
    field = field,
    errorField = errorField,
    errorMessage = emptyErrorMessage
) {
    override fun isValid(): Boolean {
        val isNotNull = super.isValid()
        return if (isNotNull)
            getInvalidSumErrorMessage(field.get()!!) == null
        else
            isNotNull
    }

    override fun validate(): Boolean {
        val isNotNull = super.isValid()
        return if (isNotNull)
            validateSum(field.get()!!)
        else
            super.validate()
    }


    internal open fun getInvalidSumErrorMessage(sum: String): String? {
        try {
            val floatSum = sum.toFloat()
            val sellAccountBalance = getSellAccountBalance()
            if (sellAccountBalance != null &&
                floatSum > sellAccountBalance
            ) {
                return insufficientBalanceMessage
            }
        } catch (it: Throwable) {
        }
        return null
    }

    internal open fun validateSum(sum: String): Boolean {
        val invalidErrorMessage = getInvalidSumErrorMessage(sum)
        return if (invalidErrorMessage == null) {
            errorField?.set(null)
            true
        } else {
            errorField?.set(invalidErrorMessage)
            false
        }
    }
}

class MinSellSumFieldValidator(
    field: ObservableField<String?>,
    errorField: ObservableField<String?>,
    emptyErrorMessage: String,
    insufficientBalanceMessage: String,
    getSellAccountBalance: () -> (Float?),
    private val mustBeGreaterThenString: String,
    private val getCurrencySymbol:() -> (String?),
    private val getMinSum: () -> (Double?)
) : SellSumFieldValidator(
    field = field,
    errorField = errorField,
    emptyErrorMessage = emptyErrorMessage,
    insufficientBalanceMessage = insufficientBalanceMessage,
    getSellAccountBalance = getSellAccountBalance
){
    override fun getInvalidSumErrorMessage(sum: String): String? {
        val result =  super.getInvalidSumErrorMessage(sum)
        if (result != null)
            return result
        else {
            val minSum = getMinSum()?.toBigDecimal()
            if (minSum != null) {
                if (sum.toBigDecimal() < minSum){
                    return "$mustBeGreaterThenString ${NumberFormatter.formatToStringTwoZeros(getMinSum())} ${getCurrencySymbol()}"
                }
            }
        }
        return null
    }
}
