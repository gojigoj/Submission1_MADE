package com.dicoding.picodiploma.mysubmission.view


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
import com.dicoding.picodiploma.mysubmission.util.EspressoIdlingResource
import com.dicoding.picodiploma.mysubmission.viewmodel.MainViewModel
import kotlinx.android.synthetic.main.fragment_movies.*

/**
 * A simple [Fragment] subclass.
 */
class MoviesFragment : Fragment() {


    private lateinit var mainViewModel: MainViewModel

    companion object {
        private val ARG_SECTION_NUMBER = "section_number"
        private var Movies = ArrayList<Movie>()
        private var TvShow = ArrayList<Movie>()

        fun newInstance(index: Int): MoviesFragment {
            val fragment = MoviesFragment()
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
        val view = inflater.inflate(R.layout.fragment_movies, container, false) as View
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        mainViewModel = ViewModelProvider(
            requireActivity(),
            ViewModelProvider.NewInstanceFactory()
        ).get(MainViewModel::class.java)
        rv_list.layoutManager = LinearLayoutManager(view.context)
        rv_list.setHasFixedSize(true)


        var index = 0
        if (arguments != null) {
            index = arguments?.getInt(ARG_SECTION_NUMBER, 0) as Int
        }

        if (index == 0) {
            getMovieList(view.context, index)
        } else {
            getTvShowList(view.context, index)
        }

    }

    private fun getMovieList(context: Context, index: Int) {
        val listMoviesAdapter = ListMovieAdapter()
        listMoviesAdapter.notifyDataSetChanged()

        EspressoIdlingResource.increment()
        mainViewModel.setMovies(context)
        showLoading(true)

        mainViewModel.getMovies().observe(viewLifecycleOwner, Observer { moviesItems ->
            if (moviesItems != null) {
                listMoviesAdapter.setData(moviesItems)
                showLoading(false)
                EspressoIdlingResource.decrement()
            }
        })
        rv_list.adapter = listMoviesAdapter
        showSelectedMovie(listMoviesAdapter, index)

    }

    private fun getTvShowList(context: Context, index: Int) {
        val listTvShowAdapter = ListMovieAdapter()
        listTvShowAdapter.notifyDataSetChanged()

        EspressoIdlingResource.increment()
        mainViewModel.setTvSeries(context)
        showLoading(true)

        mainViewModel.getTvSeries().observe(viewLifecycleOwner, Observer { tvShowItems ->
            if (tvShowItems != null) {
                listTvShowAdapter.setData(tvShowItems)
                showLoading(false)
                EspressoIdlingResource.decrement()

            }
        })

        rv_list.adapter = listTvShowAdapter
        showSelectedMovie(listTvShowAdapter, index)
    }

    private fun showSelectedMovie(listMovieAdapter: ListMovieAdapter, index: Int) {
        listMovieAdapter.setOnItemClickCallback(object : ListMovieAdapter.OnItemClickCallback {
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
