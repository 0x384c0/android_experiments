package com.example.corenetwork.helpers

/**
 * класс для валидации пароля
 */
class PasswordValidator(
    private val minLength: Int,
    private val passwordType: PasswordType
) {

    //region Public
    /**
     * @return правила валидации
     */
    fun getValidateRules(): List<PasswordValidationRule> {
        return validateRulesTypeMap.getValue(passwordType)
    }

    /**
     * Валидация пароля
     *
     * @param password пароль
     * @return найденные ошибки валидации
     */
    fun validatePassword(password: String): List<PasswordValidationRule> {
        return getValidateRules().filter { !validateRulesMap.getValue(it)(password) }
    }
    //endregion

    //region Private
    private val validateRulesTypeMap = mapOf(
        PasswordType.Lowest to listOf(),
        PasswordType.Weak to listOf(
            PasswordValidationRule.AboveMinLength,
            PasswordValidationRule.ContainsAllCases
        ),
        PasswordType.Medium to listOf(
            PasswordValidationRule.AboveMinLength,
            PasswordValidationRule.ContainsAllCases,
            PasswordValidationRule.ContainsNumbers
        ),
        PasswordType.High to listOf(
            PasswordValidationRule.AboveMinLength,
            PasswordValidationRule.ContainsAllCases,
            PasswordValidationRule.ContainsNumbers,
            PasswordValidationRule.ContainsSpecialChars
        )
    )
    private val validateRulesMap = mapOf(
        PasswordValidationRule.AboveMinLength to this::validateAboveMinLength,
        PasswordValidationRule.ContainsAllCases to this::validateContainsAllCases,
        PasswordValidationRule.ContainsNumbers to this::validateContainsNumbers,
        PasswordValidationRule.ContainsSpecialChars to this::validateContainsSpecialChars
    )

    private fun validateAboveMinLength(password: String): Boolean {
        return password.length >= minLength
    }

    private fun validateContainsAllCases(password: String): Boolean {
        return Regex(".*[A-ZА-Я].*").containsMatchIn(password) && Regex(".*[a-zа-я].*").containsMatchIn(password)
    }

    private fun validateContainsNumbers(password: String): Boolean {
        return Regex(".*\\d.*").containsMatchIn(password)
    }

    private fun validateContainsSpecialChars(password: String): Boolean {
        return Regex(".*[$&+,:;=\\\\?@#|/'<>.^*()%!-].*").containsMatchIn(password)
    }
    //endregion

    /**
     * правила валидации
     */
    enum class PasswordValidationRule {
        AboveMinLength,
        ContainsAllCases,
        ContainsNumbers,
        ContainsSpecialChars
    }

    enum class PasswordType(val value: String) {
        Lowest("Lowest"),
        Weak("Weak"),
        Medium("Medium"),
        High("High")
    }
}