package com.dicoding.picodiploma.mysubmission.view.fragment

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.dicoding.picodiploma.mysubmission.R
import com.dicoding.picodiploma.mysubmission.model.Movie
import com.dicoding.picodiploma.mysubmission.view.DetailMovieActivity
import com.dicoding.picodiploma.mysubmission.view.adapter.ListItemAdapter
import com.dicoding.picodiploma.mysubmission.viewmodel.ListMovieViewModel
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_main.view.*
import kotlinx.android.synthetic.main.fragment_movies.*

class MoviesFragment : Fragment() {

    private lateinit var movieViewModel: ListMovieViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_movies, container, false) as View
//        (requireActivity() as AppCompatActivity).supportActionBar?.title = resources.getString(R.string.tab_title_1)
        requireActivity().tv_toolbar_title.text = resources.getString(R.string.tab_title_1)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        movieViewModel = ViewModelProvider(
            requireActivity(),
            ViewModelProvider.NewInstanceFactory()
        ).get(ListMovieViewModel::class.java)

        rv_list.layoutManager = LinearLayoutManager(view.context)
        rv_list.setHasFixedSize(true)

        getMovieList(view.context, DetailMovieActivity.MOVIE_INDEX)
    }

    private fun getMovieList(context: Context, index: Int) {
        val listMoviesAdapter = ListItemAdapter()
        listMoviesAdapter.notifyDataSetChanged()

        movieViewModel.setMovies(context)
        showLoading(true)

        movieViewModel.getMovies().observe(viewLifecycleOwner, Observer { moviesItems ->
            if (moviesItems != null) {
                listMoviesAdapter.setData(moviesItems)
                showLoading(false)
            }
        })
        rv_list.adapter = listMoviesAdapter
        showSelectedMovie(listMoviesAdapter, index)
    }

    private fun showSelectedMovie(listMovieAdapter: ListItemAdapter, index: Int) {
        listMovieAdapter.setOnItemClickCallback(object : ListItemAdapter.OnItemClickCallback {
            override fun onItemClicked(data: Movie) {
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