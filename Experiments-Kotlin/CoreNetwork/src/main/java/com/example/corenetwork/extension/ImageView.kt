package com.example.corenetwork.extension

import android.widget.ImageView
import com.example.corenetwork.ImageManager

/**
 * Загрузка и отображения картинки из имени
 */
fun ImageView.loadFromImageName(fileName: String?) {
    ImageManager.getInstance().loadFromImageName(fileName, this)
}

/**
 * Загрузка и отображения картинки из url
 */
fun ImageView.loadFromUrl(url:String?){
    ImageManager.getInstance().loadFromUrl(url,this)
}