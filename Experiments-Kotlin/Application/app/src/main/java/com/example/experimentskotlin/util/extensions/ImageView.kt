package com.example.experimentskotlin.util.extensions

import android.annotation.SuppressLint
import android.content.Context
import android.widget.ImageView
import androidx.annotation.DrawableRes
import androidx.core.graphics.drawable.RoundedBitmapDrawableFactory
import androidx.core.graphics.drawable.toBitmap
import com.example.corenetwork.Api
import com.squareup.picasso.Callback
import com.squareup.picasso.OkHttp3Downloader
import com.squareup.picasso.Picasso
import kotlin.math.min


@SuppressLint("StaticFieldLeak")
private lateinit var picasso: Picasso

fun initPicasso(context: Context) {
    picasso = Picasso.Builder(context)
        .downloader(OkHttp3Downloader(Api.getClientBuilderWithAuth().build()))
        .build()
}

/**
 * Загрузка и отображения картинки из url
 */
fun ImageView.loadFromUrl(url: String?) {
    loadFromUrl(url, false, null, null)
}

fun ImageView.loadFromUrl(
    url: String?,
    circle: Boolean,
    @DrawableRes placeholderResId: Int?,
    onError: ((e: Exception?) -> Unit)?
) {
    val imageView = this
    var t = picasso.load(url).fit().centerInside()
    if (placeholderResId != null)
        t = t.placeholder(placeholderResId)
    t.into(this, object : Callback {
        override fun onSuccess() {
            if (circle) {
                try {
                    val d = RoundedBitmapDrawableFactory.create(
                        imageView.resources,
                        imageView.drawable.toBitmap()
                    )
                    d.isCircular = true
                    d.cornerRadius = min(imageView.width, imageView.height) / 2f
                    imageView.setImageDrawable(d)
                } catch (e: Exception) {
                }
            }
        }

        override fun onError(e: Exception?) {
            onError?.invoke(e)
        }
    })
}