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
import com.dicoding.picodiploma.mysubmission.model.db.MovieHelper
import com.dicoding.picodiploma.mysubmission.util.EspressoIdlingResource
import com.dicoding.picodiploma.mysubmission.util.MappingHelper
import com.dicoding.picodiploma.mysubmission.view.DetailMovieActivity
import com.dicoding.picodiploma.mysubmission.view.adapter.ListItemAdapter
import com.dicoding.picodiploma.mysubmission.viewmodel.ListFavoriteViewModel
import com.dicoding.picodiploma.mysubmission.viewmodel.ListMovieViewModel
import com.dicoding.picodiploma.mysubmission.viewmodel.ListTvShowViewModel
import kotlinx.android.synthetic.main.fragment_list_movies.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

class ItemMoviesFragment : Fragment() {
    private lateinit var movieHelper: MovieHelper

    private lateinit var listMoviesAdapter: ListItemAdapter
    private lateinit var listTvShowAdapter: ListItemAdapter

    companion object {
        private val ARG_SECTION_NUMBER = "section_number"

        fun newInstance(index: Int): ItemMoviesFragment {
            val fragment =
                ItemMoviesFragment()
            val bundle = Bundle()
            bundle.putInt(ARG_SECTION_NUMBER, index)
            fragment.arguments = bundle
            return fragment
        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        movieHelper = MovieHelper.getInstance(context)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_list_movies, container, false) as View

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        rv_list.layoutManager = LinearLayoutManager(view.context)
        rv_list.setHasFixedSize(true)

        var index = 0
        if (arguments != null) {
            index = arguments?.getInt(ARG_SECTION_NUMBER, 0) as Int
        }

        if (index == 0) {
            getMovieFavList(index)
        } else {
            getTvShowFavList(index)
        }

    }

    private fun getMovieFavList(index: Int) {
        listMoviesAdapter = ListItemAdapter()
        listMoviesAdapter.notifyDataSetChanged()

        GlobalScope.launch(Dispatchers.Main) {
            showLoading(true)
            val defferedMovies = async(Dispatchers.IO) {
                val cursor = movieHelper.queryByType(index.toString())
                MappingHelper.mapCursorToArrayList(cursor)
            }
            showLoading(false)
            val movies = defferedMovies.await()
            if (movies.size > 0) {
                listMoviesAdapter.listMovie = movies
            } else {
                listMoviesAdapter.listMovie = ArrayList()
                showNoDataText(true, "Movies")
            }
        }
        rv_list.adapter = listMoviesAdapter
        showSelectedMovie(listMoviesAdapter, index)
    }

    private fun getTvShowFavList(index: Int) {
        listTvShowAdapter = ListItemAdapter()
        listTvShowAdapter.notifyDataSetChanged()

        GlobalScope.launch(Dispatchers.Main) {
            showLoading(true)
            val defferedTvShows = async(Dispatchers.IO) {
                val cursor = movieHelper.queryByType(index.toString())
                MappingHelper.mapCursorToArrayList(cursor)
            }
            showLoading(false)
            val tvShows = defferedTvShows.await()
            if (tvShows.size > 0) {
                listTvShowAdapter.listMovie = tvShows
            } else {
                listTvShowAdapter.listMovie = ArrayList()
                showNoDataText(true, "Tv Shows")
            }
        }
        rv_list.adapter = listTvShowAdapter
        showSelectedMovie(listTvShowAdapter, index)
    }
    private fun showSelectedMovie(listMovieAdapter: ListItemAdapter, index: Int) {
        listMovieAdapter.setOnItemClickCallback(object : ListItemAdapter.OnItemClickCallback {
            override fun onItemClicked(data: Movie, position: Int) {
                goToDetailMovie(data, index, position)
            }
        })
    }

    private fun goToDetailMovie(movie: Movie, index: Int, position: Int) {
        val intent = Intent(context, DetailMovieActivity::class.java)
        intent.putExtra(DetailMovieActivity.EXTRA_TYPE, index)
        intent.putExtra(DetailMovieActivity.EXTRA_SELECTED_VALUE, movie)
        intent.putExtra(DetailMovieActivity.EXTRA_POSITION, position)
        startActivityForResult(intent, DetailMovieActivity.REQUEST_ADD)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (data != null) {
            if (requestCode == DetailMovieActivity.REQUEST_ADD) {
                when (resultCode) {
                    DetailMovieActivity.RESULT_ADD -> {
                        val item = data.getParcelableExtra<Movie>(DetailMovieActivity.EXTRA_SELECTED_VALUE)
                        val index = data.getIntExtra(DetailMovieActivity.EXTRA_TYPE, 0)
                        if (index == 0) {
                            listMoviesAdapter.addItem(item)
                        } else {
                            listTvShowAdapter.addItem(item)
                        }
                    }
                    DetailMovieActivity.RESULT_DELETE -> {
                        val position = data.getIntExtra(DetailMovieActivity.EXTRA_POSITION,0)
                        val index = data.getIntExtra(DetailMovieActivity.EXTRA_TYPE, 0)

                        if (index == 0) {
                            listMoviesAdapter.removeItem(position)
                            if (listMoviesAdapter.listMovie.size == 0) {
                                listMoviesAdapter.listMovie = ArrayList()
                                showNoDataText(true, "Movies")
                            }
                        } else {
                            listTvShowAdapter.removeItem(position)
                            if (listTvShowAdapter.listMovie.size == 0) {
                                listTvShowAdapter.listMovie = ArrayList()
                                showNoDataText(true, "Tv Shows")
                            }
                        }
                    }
                }
            }
        }
    }

    private fun showNoDataText(state: Boolean, type: String) {
        if (state) {
            tv_no_data_fav.visibility = View.VISIBLE
            tv_no_data_fav.text = resources.getString(R.string.no_data_favorite, type)
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
