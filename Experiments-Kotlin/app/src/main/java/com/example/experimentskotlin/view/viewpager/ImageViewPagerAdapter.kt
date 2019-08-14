package com.example.experimentskotlin.view.viewpager

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.viewpager.widget.PagerAdapter
import androidx.viewpager.widget.ViewPager
import com.example.corenetwork.ImageManager
import com.example.corenetwork.extension.loadFromUrl
import com.example.experimentskotlin.R
import com.rd.PageIndicatorView
import kotlinx.android.synthetic.main.item_pager_image.view.*

/**
 * Адаптер для ViewPager с картинками
 *
 * Переопределает методы,  описанные в документации к PagerAdapter
 * [PagerAdapter](https://developer.android.com/reference/android/support/v4/view/PagerAdapter)
 **/
class ImageViewPagerAdapter(
    private val context: Context,
    private val onClickListener:(String)->(Unit)
) : PagerAdapter() {
    var banners: List<String> = listOf()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun getCount(): Int {
        return banners.size
    }

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        val inflater = LayoutInflater.from(context)
        val layout = inflater.inflate(R.layout.item_pager_image, container, false) as ViewGroup
        layout.imageView.loadFromUrl(
            ImageManager.getInstance().getUrlFromName(banners[position])
        )
        container.addView(layout)
        layout.setOnClickListener { onClickListener(banners[position]) }
        return layout
    }

    override fun destroyItem(container: ViewGroup, position: Int, view: Any) {
        container.removeView(view as View)
    }

    override fun isViewFromObject(view: View, `object`: Any): Boolean {
        return view === `object`
    }
}

class ImageViewPagerOnPageChangeListener(private val pageIndicatorView: PageIndicatorView, var getSize: () -> Int) :
    ViewPager.OnPageChangeListener {
    override fun onPageScrollStateChanged(state: Int) {}

    override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {}

    override fun onPageSelected(position: Int) {
        pageIndicatorView.count = getSize()
        pageIndicatorView.setSelected(position)
    }
}