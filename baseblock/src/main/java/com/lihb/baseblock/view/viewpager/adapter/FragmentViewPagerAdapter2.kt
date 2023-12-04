package com.lihb.baseblock.view.viewpager.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter

/**
 * FragmentViewPagerAdapter
 * Created by lihuabin on 2016/10/10.
 */
open class FragmentViewPagerAdapter2 : FragmentStateAdapter {
    private var fragments: List<Fragment>? = null
    private var titles: List<CharSequence>? = null

    @JvmOverloads
    constructor(
        fm: FragmentManager,
        lifecycle: Lifecycle,
        fragments: List<Fragment>?,
        titles: List<CharSequence>? = null
    ) : super(fm, lifecycle) {
        this.fragments = fragments
        this.titles = titles
    }

    @JvmOverloads
    constructor(
        activity: FragmentActivity,
        fragments: List<Fragment>?,
        titles: List<CharSequence>? = null
    ) : super(activity) {
        this.fragments = fragments
        this.titles = titles
    }

    @JvmOverloads
    constructor(
        fragment: Fragment,
        fragments: List<Fragment>?,
        titles: List<CharSequence>? = null
    ) : super(fragment) {
        this.fragments = fragments
        this.titles = titles
    }

    fun getPageTitle(position: Int): CharSequence = titles?.get(position) ?: ""

    override fun getItemCount(): Int = fragments?.size ?: 0

    override fun createFragment(position: Int): Fragment = fragments!![position]

}