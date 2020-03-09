package com.dicoding.picodiploma.mysubmission.view.fragment

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.dicoding.picodiploma.mysubmission.R
import com.dicoding.picodiploma.mysubmission.model.Movie
import com.dicoding.picodiploma.mysubmission.view.DetailMovieActivity
import com.dicoding.picodiploma.mysubmission.view.adapter.ListItemAdapter
import com.dicoding.picodiploma.mysubmission.viewmodel.ListTvShowViewModel
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_tv_show.*

class TvShowFragment : Fragment() {

    private lateinit var tvShowViewModel: ListTvShowViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_tv_show, container, false) as View
        requireActivity().tv_toolbar_title.text = resources.getString(R.string.tab_title_2)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        tvShowViewModel = ViewModelProvider(
            requireActivity(),
            ViewModelProvider.NewInstanceFactory()
        ).get(ListTvShowViewModel::class.java)

        rv_list.layoutManager = LinearLayoutManager(view.context)
        rv_list.setHasFixedSize(true)

        getTvShowList(view.context, DetailMovieActivity.TVSHOW_INDEX)
    }

    private fun getTvShowList(context: Context, index: Int) {
        val listMoviesAdapter = ListItemAdapter()
        listMoviesAdapter.notifyDataSetChanged()

//        EspressoIdlingResource.increment()
        tvShowViewModel.setTvShows(context)
        showLoading(true)

        tvShowViewModel.getTvShows().observe(viewLifecycleOwner, Observer { moviesItems ->
            if (moviesItems != null) {
                listMoviesAdapter.listMovie = moviesItems
                showLoading(false)
//                EspressoIdlingResource.increment()
            }
        })
        rv_list.adapter = listMoviesAdapter
        showSelectedMovie(listMoviesAdapter, index)
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

    private fun showLoading(state: Boolean) {
        if (state) {
            progressBar.visibility = View.VISIBLE
        } else {
            progressBar.visibility = View.GONE
        }
    }
}