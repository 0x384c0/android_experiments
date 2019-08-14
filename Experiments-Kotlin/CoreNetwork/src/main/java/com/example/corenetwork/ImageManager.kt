package com.example.corenetwork

import android.content.Context
import android.widget.ImageView
import com.squareup.picasso.OkHttp3Downloader
import com.squareup.picasso.Picasso

/**
 * Класс - обертка над Picasso
 * показывает картинки из кэши или из апи
 *
 * @param context нужен для Picasso
 * @constructor Создает инстанс класса. Не дожен вызываться напрямую, толко через init() и getInstance()
 */
class ImageManager(context: Context){
    private var picasso = Picasso.Builder(context)
        .downloader(OkHttp3Downloader(Api.getClientBuilderWithAuth().build()))
        .build()

    /**
     * формирует url и показывает изображение из него
     */
    fun loadFromImageName(fileName: String?, image: ImageView) {
        if (fileName != null) {
            val url = getUrlFromName(fileName)
            loadFromUrl(url,image)
        }
    }

    /**
     * показывает изображение из url
     */
    fun loadFromUrl(url:String?, image:ImageView){
        picasso.load(url).into(image)
    }

    /**
     * формирует url из имени файла
     */
    fun getUrlFromName(fileName:String):String{
        return Constants.SERVER_BASE_URL + "api.php/images" + fileName
    }


    companion object {
        //singleton
        private lateinit var ImageManager: ImageManager

        /**
         * Инициализация менеджера, должен вызваться после PreferencesManager
         */
        fun init(context: Context) {
            ImageManager = ImageManager(context)
        }

        /**
         * @return singleton инстанс инстанс инициализированного менеджера в init()
         */
        fun getInstance(): ImageManager {
            return ImageManager
        }
    }
}