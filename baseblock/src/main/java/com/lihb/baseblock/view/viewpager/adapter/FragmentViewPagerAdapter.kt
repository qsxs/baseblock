package com.lihb.baseblock.view.viewpager.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter

/**
 * FragmentViewPagerAdapter
 * Created by lihuabin on 2016/10/10.
 */
class FragmentViewPagerAdapter
@JvmOverloads constructor(
    private val fm: FragmentManager,
    var fragments: List<Fragment>?,
    var titles: List<CharSequence>? = null
) : FragmentStatePagerAdapter(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {

    override fun getItem(position: Int): Fragment {
        val fragment = fragments!![position]
        if (fragment.isAdded) {
            fm.beginTransaction().remove(fragment).commit()
        }
        return fragment
    }

    override fun getCount(): Int {
        return fragments?.size ?: 0
    }

    override fun getPageTitle(position: Int): CharSequence? {
        return titles?.get(position) ?: ""
    }
}