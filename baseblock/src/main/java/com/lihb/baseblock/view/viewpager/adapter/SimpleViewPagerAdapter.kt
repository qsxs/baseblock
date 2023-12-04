package com.lihb.baseblock.view.viewpager.adapter

import android.view.View
import android.view.ViewGroup
import androidx.viewpager.widget.PagerAdapter

/**
 * SimpleViewPagerAdapter
 * Created by lihuabin on 2016/11/7.
 */
class SimpleViewPagerAdapter
@JvmOverloads constructor(
    private var itemViewList: List<View>,
    titles: List<String>? = null
) : PagerAdapter() {
    var titles: List<CharSequence>? = titles

    override fun getCount(): Int {
        return itemViewList.size
    }

    override fun isViewFromObject(view: View, `object`: Any): Boolean {
        return view === `object`
    }

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        container.addView(itemViewList[position])
        return itemViewList[position]
    }

    override fun destroyItem(
        container: ViewGroup,
        position: Int,
        `object`: Any
    ) { //        super.destroyItem(container, position, object);
        container.removeView(itemViewList[position])
    }

    override fun getPageTitle(position: Int): CharSequence? {
        return titles?.get(position) ?: ""
    }
}