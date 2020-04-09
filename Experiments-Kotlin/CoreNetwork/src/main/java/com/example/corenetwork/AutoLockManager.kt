package com.example.corenetwork

import java.util.*

/**
 * Класс - менеджер автоболкировки приложения
 * обрабатывается события от ProcessLifecycleOwner
 *
 * @property api инстанс класса Api, для выполнения запросов
 * @property sessionManager инстанс класса SessionManager, для токена
 * @constructor Создает инстанс класса. Не дожен вызываться напрямую, толко через init() и getInstance()
 */
class AutoLockManager(
    private val sessionManager: SessionManager,
    private val dataProvider: AutoLockDataProvider
) {
    init {
        dataProvider.lastPauseDate = null
    }

    private var disableAutoLockInitialDate: Date? = null
    private var appLocked = false


    var lockHandler: (() -> Boolean)? = null

    var autoLockPeriod: Int
        get() = dataProvider.autoLockPeriod ?: 0
        set(value) {
            dataProvider.autoLockPeriod = value
        }


    //region Disable
    fun disableTemporarilyAutoLock() {
        disableAutoLockInitialDate = Date()
    }

    private fun isAutoLockDisabled(): Boolean {
        val disableTimeoutMs = 500
        val disableNextAutoLockDate = disableAutoLockInitialDate
        if (disableNextAutoLockDate != null) {
            val diff = (Date().time - disableNextAutoLockDate.time)
            if (diff < 0 || diff > disableTimeoutMs) {
                this.disableAutoLockInitialDate = null
            } else {
                return true
            }
        }
        return this.disableAutoLockInitialDate != null
    }
    //endregion

    //region Skip
    private var skipAutoLockInitialDate: Date? = null
    private var shouldSkipAutoLock = false
    fun skipNexAutoLock() {
        shouldSkipAutoLock = true
    }

    private fun isShouldSkipAutoLock(): Boolean {
        val skipTimeoutMs = 60 * 1000
        val skipNextAutoLockDate = skipAutoLockInitialDate
        skipAutoLockInitialDate = null
        if (skipNextAutoLockDate != null) {
            val diff = (Date().time - skipNextAutoLockDate.time)
            if (diff < 0 || diff > skipTimeoutMs) {
                shouldSkipAutoLock = false
            }
        }
        val res = shouldSkipAutoLock
        shouldSkipAutoLock = false
        return res
    }
    //endregion

    //region Events
    fun lockAppIfNeeded() {
        val isSkipAutoLock = isAutoLockDisabled() || isShouldSkipAutoLock()

        if (!isSkipAutoLock && sessionManager.isLoggedIn && !appLocked)
            if (isNeedLockByAutoLockPeriod())
                appLocked = lockHandler?.invoke() ?: false
        this.disableAutoLockInitialDate = null
    }

    fun appWasUnlocked() {
        appLocked = false
    }

    fun onPauseEvent() {
        dataProvider.lastPauseDate = Date().time
    }
    //endregion


    private fun isNeedLockByAutoLockPeriod(): Boolean {
        val pauseDateMs = dataProvider.lastPauseDate
        if (pauseDateMs != null) {
            val diffSec = (Date().time - pauseDateMs) / 1000
            if (diffSec >= 0)
                return diffSec >= autoLockPeriod
        }
        return true
    }


    companion object {
        //singleton
        private lateinit var instance: AutoLockManager

        /**
         * Инициализация менеджера, должен вызваться после PreferencesManager
         */
        fun getInstance(): AutoLockManager {
            return instance
        }

        /**
         * @return singleton инстанс инстанс инициализированного менеджера в init()
         */
        fun init(s: SessionManager, dataProvider: AutoLockDataProvider) {
            instance = AutoLockManager(s, dataProvider)
        }
    }
}

interface AutoLockDataProvider {
    var autoLockPeriod: Int?
    var lastPauseDate: Long?
}

/*

    @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
    fun onResumeEvent() {
        try {
            AutoLockManager.getInstance().lockAppIfNeeded()
        } catch (e: Throwable) {
        }
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_PAUSE)
    fun onPauseEvent() {
        try {
            AutoLockManager.getInstance().onPauseEvent()
        } catch (e: Throwable) {
        }
    }
 */