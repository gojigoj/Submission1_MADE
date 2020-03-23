package com.dicoding.picodiploma.mysubmission.view.adapter

import android.content.Context
import androidx.annotation.StringRes
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.dicoding.picodiploma.mysubmission.R
import com.dicoding.picodiploma.mysubmission.view.fragment.MovieSearchFragment
import com.dicoding.picodiploma.mysubmission.view.fragment.TvShowSearchFragment

class SearchPagerAdapter(private val mContext: Context, fm: FragmentManager) :
    FragmentPagerAdapter(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {

    @StringRes
    private val TAB_TITLES = intArrayOf(
        R.string.tab_title_1,
        R.string.tab_title_2
    )

    override fun getItem(position: Int): Fragment {
        var fragment: Fragment?
        when (position) {
            0 -> fragment = MovieSearchFragment()
            1 -> fragment = TvShowSearchFragment()
            else -> fragment = MovieSearchFragment()
        }
        return fragment
    }

    override fun getPageTitle(position: Int): CharSequence? {
        return mContext.resources.getString(TAB_TITLES[position])
    }

    override fun getCount(): Int = 2
}