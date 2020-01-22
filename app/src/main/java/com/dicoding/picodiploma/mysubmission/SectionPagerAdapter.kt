package com.dicoding.picodiploma.mysubmission

import android.content.Context
import androidx.annotation.StringRes
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter

class SectionPagerAdapter(private val mContext: Context, fm: FragmentManager) :
    FragmentPagerAdapter(fm) {

    @StringRes
    private val TAB_TITLES = intArrayOf(R.string.tab_title_1, R.string.tab_title_2)

    override fun getItem(position: Int): Fragment {
        val fragment = MoviesFragment.newInstance(position)
        return fragment
    }

    override fun getPageTitle(position: Int): CharSequence? {
        return mContext.resources.getString(TAB_TITLES[position])
    }

    override fun getCount(): Int = 2
}