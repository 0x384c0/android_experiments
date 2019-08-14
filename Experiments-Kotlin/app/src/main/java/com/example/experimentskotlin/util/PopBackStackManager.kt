package com.example.experimentskotlin.util

import androidx.fragment.app.Fragment
import androidx.navigation.NavDirections
import androidx.navigation.NavOptions
import androidx.navigation.fragment.findNavController
import com.example.experimentskotlin.R
import kotlin.reflect.KClass


/**
 * Класс, убирающий из стека фрагментов ненужные фрагменты для случаев, когда для ноды несколоко actions
 */
class PopBackStackManager {


    /**
     * сброс удаление ненужных фрагментов
     */
    fun reset() {
        navOptions = null
        afterFragments = null
    }

    private var navOptions: NavOptions? = null
    private var afterFragments: List<KClass<out Fragment>>? = null

    /**
     * перейти в другой фрагмент у далением, если возможно
     */
    fun navigate(fragment: Fragment, navDirections: NavDirections) {
        val fragmentClass = fragment::class
        if (this.afterFragments?.contains(fragmentClass) == true) {
            fragment.findNavController().navigate(navDirections, navOptions)
            reset()
        } else {
            fragment.findNavController().navigate(navDirections)
        }
    }


    /**
     * запланировать удаление ненужных фрагментов из стека вплоть до главного фрагмента
     */
    fun popToMain(afterFragments: List<KClass<out Fragment>>) {
        createPopupOption(afterFragments, false, R.id.navHostFragment)
    }

    /**
     * запланировать удаление ненужных фрагментов из стека
     */
    fun popToSelf(
        fragment: Fragment,
        inclusive: Boolean,
        afterFragments: List<KClass<out Fragment>>
    ) {
        createPopupOption(afterFragments, inclusive, fragment.findNavController().currentDestination!!.id)
    }

    private fun createPopupOption(
        afterFragments: List<KClass<out Fragment>>,
        inclusive: Boolean,
        popToId: Int
    ) {
        this.afterFragments = afterFragments
        navOptions = NavOptions.Builder()
            .setPopUpTo(popToId, inclusive)
            .build()
    }


    companion object {
        //singleton
        private lateinit var instance: PopBackStackManager

        /**
         * Инициализация менеджера, должен вызваться после PreferencesManager
         */
        fun getInstance(): PopBackStackManager {
            if (!::instance.isInitialized) {
                init()
            }
            return instance
        }

        /**
         * @return singleton инстанс инстанс инициализированного менеджера в init()
         */
        fun init() {//must be called first
            instance = PopBackStackManager()
        }
    }
}