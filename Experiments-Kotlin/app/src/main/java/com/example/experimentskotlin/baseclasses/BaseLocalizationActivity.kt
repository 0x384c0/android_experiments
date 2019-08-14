package com.example.experimentskotlin.baseclasses

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import com.example.corenetwork.EventsManager
import com.example.corenetwork.LocaleManager
import com.example.corenetwork.extension.disposedBy
import io.reactivex.disposables.CompositeDisposable


/**
 * Класс [Activity] с NavController для переключения языка на лету
 *
 * все его методы описаны в документации Android к Activity
 * [Activity](https://developer.android.com/reference/android/app/Activity)
 */
abstract class BaseLocalizationActivity : AppCompatActivity() {

    private var localeManager = LocaleManager.getInstance()
    private var lastLanguage: String? = null
    internal var isTopActivity = false
        private set

    override fun onResume() {
        super.onResume()
        isTopActivity = true
        if (lastLanguage != null) {
            if (lastLanguage != localeManager.locale) {
                recreate()
            }
        } else {
            lastLanguage = localeManager.locale
        }
    }

    override fun onPause() {
        super.onPause()
        isTopActivity = false
    }


    open fun subscribeLocalizationEvents(disposable: CompositeDisposable) {
        EventsManager
            .getInstance()
            .localeChanged
            .subscribe {
                if (isTopActivity)
                    recreate()
            }
            .disposedBy(disposable)
    }

    override fun attachBaseContext(newBase: Context?) {
        super.attachBaseContext(localeManager.wrapActivityContext(newBase))
    }
}

//LocaleManager should be initilaized

//open fun subscribeToEvents(eventsDisposable: CompositeDisposable) {
//    subscribeLocalizationEvents(notificationCompositeDisposable!!)
//}

//
//private fun subscribeToEvents() {
//    notificationCompositeDisposable?.dispose()
//    notificationCompositeDisposable = CompositeDisposable()
//    subscribeToEvents(notificationCompositeDisposable!!)
//}