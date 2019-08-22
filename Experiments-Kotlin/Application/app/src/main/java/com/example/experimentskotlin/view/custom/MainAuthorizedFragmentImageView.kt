package com.example.experimentskotlin.view.custom

import android.content.Context
import android.util.AttributeSet
import androidx.appcompat.content.res.AppCompatResources
import androidx.appcompat.widget.AppCompatImageView
import com.example.experimentskotlin.R


/**
 *  ImageView с круглым фоном, показывается на главном фрагменте
 *
 *  переопределяет методы, которые описаны в документации Android к AppCompatImageView
 *  [Fragment](https://developer.android.com/reference/android/support/v7/widget/AppCompatImageView)
 */

class MainAuthorizedFragmentImageView : AppCompatImageView {
    constructor(context: Context) : super(context){
        setup()
    }
    constructor(context: Context, attrs: AttributeSet) : super(context, attrs){
        setup()
    }
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr){
        setup()
    }

    private fun setup(){
            background = AppCompatResources.getDrawable(context, R.drawable.ic_launcher_background)
    }
}