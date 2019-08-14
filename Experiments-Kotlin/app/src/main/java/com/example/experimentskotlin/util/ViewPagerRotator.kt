package com.example.experimentskotlin.util

import android.os.CountDownTimer
import android.os.Handler
import androidx.viewpager.widget.ViewPager
import com.example.experimentskotlin.UIConstants

/**
 *  Утилита для автоматического преключения страниц ViewPager
 *
 *  @param viewPager ViewPager, в котором нужно переключать страницы
 */
class ViewPagerRotator(private val viewPager: ViewPager) {

    //region Public
    /**
     *  запуск ротации
     */
    fun start() {
        startHandler.postDelayed(startHandlerRunnable, UIConstants.BANNER_ROTATION_INTERVAL_MILLISECONDS)
    }
    /**
     *  остановка ротации
     */
    fun stop() {
        startHandler.removeCallbacks(startHandlerRunnable)
        timer.cancel()
    }
    //endregion

    //region Private
    private var timer: CountDownTimer

    init {
        timer = object : CountDownTimer(Long.MAX_VALUE, UIConstants.BANNER_ROTATION_INTERVAL_MILLISECONDS) {
            override fun onFinish() {
                if (viewPager.adapter!!.count != 0) {
                    viewPager.setCurrentItem(0, false)
                }
            }

            override fun onTick(millisUntilFinished: Long) {
                if (viewPager.adapter!!.count != 0) {
                    var nextItem = viewPager.currentItem + 1
                    if (nextItem > viewPager.adapter!!.count - 1)
                        nextItem = 0
                    viewPager.setCurrentItem(nextItem, true)
                }
            }
        }
    }

    private val startHandler = Handler()
    private val startHandlerRunnable = Runnable { timer.start() }
    //endregion
}