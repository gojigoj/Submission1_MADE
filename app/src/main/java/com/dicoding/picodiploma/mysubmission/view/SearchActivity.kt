package com.dicoding.picodiploma.mysubmission.view

import android.app.SearchManager
import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import androidx.appcompat.widget.SearchView
import com.dicoding.picodiploma.mysubmission.R
import com.dicoding.picodiploma.mysubmission.util.Util
import com.dicoding.picodiploma.mysubmission.view.adapter.SearchPagerAdapter
import com.google.android.material.tabs.TabLayout
import kotlinx.android.synthetic.main.activity_search.*

class SearchActivity : AppCompatActivity() {

    private lateinit var searchPagerAdapter: SearchPagerAdapter
    private var onSearchTvShowQueryCallback: OnSearchTvShowQueryCallback? = null
    private var onSearchMovieQueryCallback: OnSearchMovieQueryCallback? = null

    fun setOnSearchTvShowQueryCallback(onSearchTvShowQueryCallback: OnSearchTvShowQueryCallback) {
        this.onSearchTvShowQueryCallback = onSearchTvShowQueryCallback
    }

    fun setOnSearchMovieQueryCallback(onSearchMovieQueryCallback: OnSearchMovieQueryCallback) {
        this.onSearchMovieQueryCallback = onSearchMovieQueryCallback
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)

        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayShowTitleEnabled(false)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)

        toolbar.setNavigationOnClickListener {
            onBackPressed()
        }

        searchPagerAdapter = SearchPagerAdapter(this@SearchActivity, supportFragmentManager)
        view_pager.adapter = searchPagerAdapter
        tabs.setupWithViewPager(view_pager)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.menu_search, menu)

        val searchManager = getSystemService(Context.SEARCH_SERVICE) as SearchManager
        val searchView = menu.findItem(R.id.search).actionView as SearchView

        searchView.setIconifiedByDefault(false)
        searchView.onActionViewExpanded()
        searchView.setSearchableInfo(searchManager.getSearchableInfo(componentName))

        initTab(searchView)

        tabs.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabReselected(tab: TabLayout.Tab) {
                searchView.queryHint = resources.getString(R.string.search_title, searchPagerAdapter.getPageTitle(tab.position))

                searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
                    override fun onQueryTextSubmit(query: String?): Boolean {
                        if (tab.position == 0) {
                            onSearchMovieQueryCallback?.onQuerySubmit(query, tab.position)
                        } else {
                            onSearchTvShowQueryCallback?.onQuerySubmit(query, tab.position)
                        }
                        Util.showToast(baseContext, "1, ${tab.position}: $query")
                        return true
                    }

                    override fun onQueryTextChange(newText: String?): Boolean {
                        return false
                    }
                })
            }

            override fun onTabUnselected(tab: TabLayout.Tab) { }

            override fun onTabSelected(tab: TabLayout.Tab) {
                searchView.queryHint = resources.getString(R.string.search_title, searchPagerAdapter.getPageTitle(tab.position))

                searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
                    override fun onQueryTextSubmit(query: String?): Boolean {
                        if (tab.position == 0) {
                            onSearchMovieQueryCallback?.onQuerySubmit(query, tab.position)
                        } else {
                            onSearchTvShowQueryCallback?.onQuerySubmit(query, tab.position)
                        }
                        Util.showToast(baseContext, "1, ${tab.position}: $query")
                        return true
                    }

                    override fun onQueryTextChange(newText: String?): Boolean {
                        return false
                    }

                })
            }
        })
        return true
    }

    private fun initTab(searchView: SearchView){
        searchView.queryHint = resources.getString(R.string.search_title, searchPagerAdapter.getPageTitle(0))

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                if (tabs != null) {
                    onSearchMovieQueryCallback?.onQuerySubmit(searchView.query.toString(), 0)
                    Util.showToast(baseContext, "0, ${tabs.selectedTabPosition}: $query")
                }
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                return false
            }

        })
    }

    interface OnSearchTvShowQueryCallback {
        fun onQuerySubmit(query: String?, position: Int)
    }

    interface OnSearchMovieQueryCallback {
        fun onQuerySubmit(query: String?, position: Int)
    }
}
