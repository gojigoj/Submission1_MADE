package com.dicoding.picodiploma.mysubmission.view.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.dicoding.picodiploma.mysubmission.R
import com.dicoding.picodiploma.mysubmission.view.adapter.FavoritePagerAdapter
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_favorite.view.*

class FavoriteFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_favorite, container, false) as View
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        requireActivity().tv_toolbar_title.text = resources.getString(R.string.title_3)
        val sectionPagerAdapter =
            FavoritePagerAdapter(
                view.context,
                childFragmentManager
            )
        view.view_pager.adapter = sectionPagerAdapter
        view.tabs.setupWithViewPager(view.view_pager)

    }

}