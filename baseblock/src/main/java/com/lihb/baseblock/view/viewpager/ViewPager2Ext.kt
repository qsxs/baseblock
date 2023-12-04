package com.lihb.baseblock.view.viewpager

import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator

fun ViewPager2.scrollOnce(isCycle: Boolean = true) {
    val totalCount: Int = adapter?.itemCount ?: 0
//    val nextItem = if (direction == AutoScrollViewPager.LEFT) --currentItem else ++currentItem
    val nextItem = currentItem + 1
    if (nextItem < 0) {
        if (isCycle) {
            setCurrentItem(totalCount - 1, true)
        }
    } else if (nextItem >= totalCount) {
        if (isCycle) {
            setCurrentItem(0, true)
        }
    } else {
        setCurrentItem(nextItem, true)
    }
}

fun ViewPager2.doOnPageSelected(callback: (position: Int) -> Unit) {
    registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
        override fun onPageSelected(position: Int) {
            super.onPageSelected(position)
            callback.invoke(position)
        }
    })
}

fun ViewPager2.setupWithTabLayout(
    tabLayout: TabLayout,
    tabConfigurationStrategy: (TabLayout.Tab, Int) -> Unit
) {
    TabLayoutMediator(tabLayout, this, tabConfigurationStrategy).attach()
}

fun TabLayout.setupWithViewPager2(
    vp: ViewPager2,
    tabConfigurationStrategy: (TabLayout.Tab, Int) -> Unit
) {
    TabLayoutMediator(this, vp, tabConfigurationStrategy).attach()
}