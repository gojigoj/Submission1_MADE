package com.dicoding.picodiploma.mysubmission.view.fragment

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager

import com.dicoding.picodiploma.mysubmission.R
import com.dicoding.picodiploma.mysubmission.model.Movie
import com.dicoding.picodiploma.mysubmission.util.Util
import com.dicoding.picodiploma.mysubmission.view.DetailMovieActivity
import com.dicoding.picodiploma.mysubmission.view.SearchActivity
import com.dicoding.picodiploma.mysubmission.view.adapter.ListItemAdapter
import com.dicoding.picodiploma.mysubmission.viewmodel.ListSearchViewModel
import kotlinx.android.synthetic.main.fragment_tvshow_search.*

class TvShowSearchFragment : Fragment() {

    private lateinit var listTvShowsAdapter: ListItemAdapter
    private lateinit var searchViewModel: ListSearchViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_tvshow_search, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        searchViewModel = ViewModelProvider(
            requireActivity(),
            ViewModelProvider.NewInstanceFactory()
        ).get(ListSearchViewModel::class.java)

        rv_list.layoutManager = LinearLayoutManager(view.context)
        rv_list.setHasFixedSize(true)

        listTvShowsAdapter = ListItemAdapter()
        listTvShowsAdapter.notifyDataSetChanged()
        listTvShowsAdapter.listMovie = ArrayList()
        rv_list.adapter = listTvShowsAdapter

        (activity as SearchActivity).setOnSearchTvShowQueryCallback(object : SearchActivity.OnSearchTvShowQueryCallback {
            override fun onQuerySubmit(query: String?, position: Int) {
                getTvShowFavList(view.context, position, query)
            }
        })

    }

    fun getTvShowFavList(context: Context, index: Int, query: String?) {
        if (query != null) {
            searchViewModel.setSearchTvShows(context, query)
            showLoading(true)

            searchViewModel.getSearchTvShows().observe(viewLifecycleOwner, Observer { tvShowsItems ->
                if (tvShowsItems.size > 0) {
                    listTvShowsAdapter.listMovie = tvShowsItems
                    showNoDataText(false, "Tv Shows")
                } else {
                    showNoDataText(true, "Tv Shows")
                }
                showLoading(false)
            })
        }
        showSelectedMovie(listTvShowsAdapter, index)
    }

    private fun showSelectedMovie(listMovieAdapter: ListItemAdapter, index: Int) {
        listMovieAdapter.setOnItemClickCallback(object : ListItemAdapter.OnItemClickCallback {
            override fun onItemClicked(data: Movie, position: Int) {
                goToDetailMovie(data, index)
            }
        })
    }

    private fun goToDetailMovie(movie: Movie, index: Int) {
        val intent = Intent(context, DetailMovieActivity::class.java)
        intent.putExtra(DetailMovieActivity.EXTRA_TYPE, index)
        intent.putExtra(DetailMovieActivity.EXTRA_SELECTED_VALUE, movie)
        startActivity(intent)
    }

    private fun showNoDataText(state: Boolean, type: String) {
        if (state) {
            tv_no_data_fav.visibility = View.VISIBLE
            tv_no_data_fav.text = resources.getString(R.string.no_data_search, type)
        } else {
            tv_no_data_fav.visibility = View.GONE
        }
    }

    private fun showLoading(state: Boolean) {
        if (state) {
            progressBar.visibility = View.VISIBLE
        } else {
            progressBar.visibility = View.GONE
        }
    }

}
