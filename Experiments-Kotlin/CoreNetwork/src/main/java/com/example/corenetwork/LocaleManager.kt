package com.example.corenetwork

import android.app.Application
import android.content.Context
import android.os.Build
import android.os.Parcelable
import com.example.corenetwork.external.restring.Restring
import com.example.corenetwork.external.restring.RestringConfig
import io.reactivex.Observable
import kotlinx.android.parcel.Parcelize
import java.util.*

/**
 * Класс - обертка над Locale
 * получает словари с переводами, хранит и переключает языки
 *
 * @property api инстанс класса Api, для выполнения запросов
 * @property dataProvider инстанс класса LocaleDataProvider, для хранения
 * @property dictionariesProvider инстанс класса DictionariesProvider, для хранения
 * @property locales список локалей
 * @constructor Создает инстанс класса. Не дожен вызываться напрямую, толко через init() и getInstance()
 */
class LocaleManager(
    private val api: Api,
    private val dataProvider: LocaleDataProvider,
    private val dictionariesProvider: DictionariesProvider,
    private val locales: Array<String>,
    private var defaultLocale: String
) {
    //public
    /**
     *  текущая локаль
     */
    var locale: String
        get() = validateLocale(dataProvider.locale)
        set(value) {
            val validatedLocale = validateLocale(value)
            if (validatedLocale != dataProvider.locale) {
                Locale.setDefault(Locale(validatedLocale))
                dataProvider.locale = validatedLocale
                EventsManager.getInstance().localeChanged.post(validatedLocale)
            }
        }

    /**
     *  текущая локаль для отрисовки в UI
     */
    val localeForUI: Locale
        get() = Locale(locale) //TODO: use everywhere in UI


    //region Restring wrappers
    /**
     *  подменяет строки, доджен вызваться вместо super.wrapActivityContext()
     */
    fun wrapActivityContext(activityContext: Context?): Context {
        return Restring.wrapContext(activityContext)
    }

    /**
     *  подменяет строки, доджен вызваться вместо super.applicationOnCreate()
     */
    fun applicationOnCreate(application: Application) {
        Restring.init(
            application.applicationContext,
            RestringConfig.Builder()
                .persist(false)
                .build()
        )
        setLocaleInApplication(application)
    }
    //endregion
    /**
     * Создать Observable c скачивание словарей
     * @return Observable с запросом, boolean - нужно ли перезапусать activity
     */
    fun loadStrings(): Observable<Boolean> {
        val observables: MutableList<Observable<Boolean>> = mutableListOf()
        if (languagesLoaded) {
            observables.add(Observable.just(false))
        } else {
            languagesLoaded = true
            for (locale in locales) {
                val observableLoadMaybe: Observable<Unit>?
                if (dictionariesProvider.isHasLocaleDictionary(locale)) {
                    observableLoadMaybe = Observable.just(Unit)
                } else {
                    val localeSlug = Constants.LOCALES_MAP.getValue(locale)
                    observableLoadMaybe = api
                        .getDictionaries(localeSlug) //backend requires locales in ru-ru format
                        .map { localization ->
                            val localeDictionary =
                                dictionariesProvider.getLocaleDictionary(locale)!!
                            localeDictionary.resources = localization.mapKeys {
                                it.key.replace(
                                    ".",
                                    "_"
                                )
                            }//android cant handle resource id with dots
                            dictionariesProvider.saveLocaleDictionary(localeDictionary)
                        }
                }
                val observable = observableLoadMaybe!!
                    .map {
                        val localeDictionary = dictionariesProvider.getLocaleDictionary(locale)!!
                        Restring.setStrings(localeDictionary.locale, localeDictionary.resources)
                        true
                    }
                observables.add(observable)
            }
        }
        return Observable
            .zip(observables.toList()) { results ->
                var isNeedRecreate = false
                for (loadLangResult in results) {
                    isNeedRecreate = isNeedRecreate || (loadLangResult as Boolean)
                }
                isNeedRecreate
            }
            .map {
                setTestStrings()
                it
            }
    }

    //private
    private var languagesLoaded = false

    private fun validateLocale(localeMaybe: String?): String {
        val locale = localeMaybe ?: ""
        return if (locales.contains(locale)) {
            locale
        } else {
            defaultLocale
        }
    }

    /**
     * фикс для установки локали
     */
    @Suppress("DEPRECATION")
    private fun setLocaleInApplication(application: Application) {
        val locale = Locale(locale)
        val configuration = application.resources.configuration
        val displayMetrics = application.resources.displayMetrics
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            configuration.setLocale(locale)
        } else {
            configuration.locale = locale
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            application.applicationContext.createConfigurationContext(configuration)
        } else {
            application.resources.updateConfiguration(configuration, displayMetrics)
        }
    }


    private fun setTestStrings() {//TODO: remove, when api will be able to return languages
        if (BuildConfig.DEBUG) {
            Restring.setString(Constants.LOCALES[0], "close", "close 0")
            Restring.setString(Constants.LOCALES[1], "close", "close 1")
            Restring.setString(Constants.LOCALES[2], "close", "close 2")
        }
    }


    companion object {
        //singleton
        private lateinit var localeManager: LocaleManager

        fun init(
            api: Api,
            dataProvider: LocaleDataProvider,
            dictionariesProvider: DictionariesProvider,
            locales: Array<String>,
            defaultLocale: String

        ) {
            localeManager = LocaleManager(
                api,
                dataProvider,
                dictionariesProvider,
                locales,
                defaultLocale
            )
        }

        fun getInstance(): LocaleManager {
            return localeManager
        }
    }

    interface LocaleDataProvider {
        var locale: String?
    }

    interface DictionariesProvider {
        fun isHasLocaleDictionary(locale: String): Boolean
        fun getLocaleDictionary(locale: String): LocaleDictionary?
        fun saveLocaleDictionary(localeDictionary: LocaleDictionary)
    }


    @Parcelize
    data class LocaleDictionary(
        var locale: String? = null,
        var resources: ResourceDict? = null
    ) : Parcelable {
        constructor(locale: String) : this(locale, mapOf())
    }
}
typealias ResourceDict = Map<String, String>