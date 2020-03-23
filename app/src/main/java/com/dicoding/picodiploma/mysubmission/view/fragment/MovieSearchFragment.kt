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
import kotlinx.android.synthetic.main.fragment_movie_search.*

/**
 * A simple [Fragment] subclass.
 */
class MovieSearchFragment : Fragment() {

    private lateinit var listMoviesAdapter: ListItemAdapter
    private lateinit var searchViewModel: ListSearchViewModel

    companion object {
        private val ARG_SECTION_NUMBER = "section_number"

        fun newInstance(index: Int): MovieSearchFragment {
            val fragment =
                MovieSearchFragment()
            val bundle = Bundle()
            bundle.putInt(ARG_SECTION_NUMBER, index)
            fragment.arguments = bundle
            return fragment
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_movie_search, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        searchViewModel = ViewModelProvider(
            requireActivity(),
            ViewModelProvider.NewInstanceFactory()
        ).get(ListSearchViewModel::class.java)

        rv_list.layoutManager = LinearLayoutManager(view.context)
        rv_list.setHasFixedSize(true)

        var index = 0
        if (arguments != null) {
            index = arguments?.getInt(ARG_SECTION_NUMBER, 0) as Int
        }

        listMoviesAdapter = ListItemAdapter()
        listMoviesAdapter.notifyDataSetChanged()
        listMoviesAdapter.listMovie = ArrayList()
        rv_list.adapter = listMoviesAdapter

        (activity as SearchActivity).setOnSearchMovieQueryCallback(object : SearchActivity.OnSearchMovieQueryCallback {
            override fun onQuerySubmit(query: String?, position: Int) {
                getItemSearchList(view.context, position, query)
            }
        })
    }

    fun getItemSearchList(context: Context, index: Int, query: String?) {
        if (query != null) {
            searchViewModel.setSearchMovies(context, query)
            showLoading(true)

            searchViewModel.getSearchMovies().observe(viewLifecycleOwner, Observer { moviesItems ->
                if (moviesItems.size > 0) {
                    listMoviesAdapter.listMovie = moviesItems
                    showNoDataText(false, "Movies")
                } else {
                    showNoDataText(true, "Movies")
                }
                showLoading(false)
            })
            showSelectedMovie(listMoviesAdapter, index)
        }
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
            rv_list.visibility = View.INVISIBLE
        } else {
            rv_list.visibility = View.VISIBLE
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
