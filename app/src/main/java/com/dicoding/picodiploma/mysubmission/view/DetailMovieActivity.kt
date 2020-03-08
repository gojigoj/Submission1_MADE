package com.dicoding.picodiploma.mysubmission.view

import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.dicoding.picodiploma.mysubmission.R
import com.dicoding.picodiploma.mysubmission.model.Movie
import com.dicoding.picodiploma.mysubmission.util.EspressoIdlingResource
import com.dicoding.picodiploma.mysubmission.viewmodel.itemViewModel
import kotlinx.android.synthetic.main.activity_detail_movie.*
import kotlinx.android.synthetic.main.layout_detail_mov.*

class DetailMovieActivity : AppCompatActivity() {

    private lateinit var itemViewModel: itemViewModel

    companion object {
        const val EXTRA_SELECTED_VALUE = "extra_selected_value"
        const val EXTRA_TYPE = "extra_type"
        const val MOVIE_INDEX = 0
        const val TVSHOW_INDEX = 1
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail_movie)

        setSupportActionBar(toolbar)
        supportActionBar?.title = ""
        supportActionBar?.setDisplayShowTitleEnabled(true)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        toolbar.setNavigationOnClickListener {
            onBackPressed()
        }

        itemViewModel = ViewModelProvider(
            this,
            ViewModelProvider.NewInstanceFactory()
        ).get(itemViewModel::class.java)

        val movie = intent.getParcelableExtra(EXTRA_SELECTED_VALUE) as Movie

        var index = intent.getIntExtra(EXTRA_TYPE, 3)

        if (index == 1) {
            runtime_title.text = resources.getString(R.string.episode_runtime)
            directors_title.text = resources.getString(R.string.creators)
            writers_title.text = resources.getString(R.string.starring)
            studio_title.text = resources.getString(R.string.tv_network)
            getTvSeriesItem(this, movie)
        } else {
            getMovieItem(this, movie)
        }


    }

    private fun getMovieItem(context: Context, movie: Movie) {
        EspressoIdlingResource.increment()
        itemViewModel.setMovieItem(context, movie)
        showLoading(true)

        itemViewModel.getMovieItem().observe(this, Observer { movieItem ->
            if (movieItem != null) {
                initView(movieItem)
                showLoading(false)
                EspressoIdlingResource.decrement()
            }
        })
    }

    private fun getTvSeriesItem(context: Context, movie: Movie) {
        EspressoIdlingResource.increment()
        itemViewModel.setTvShowsCredit(context, movie)
        showLoading(true)

        itemViewModel.getTvShowsItem().observe(this, Observer { tvSeriesItem ->
            if (tvSeriesItem != null) {
                initView(tvSeriesItem)
                showLoading(false)
                EspressoIdlingResource.decrement()
            }
        })
    }

    private fun initView(movie: Movie) {
        Glide
            .with(this)
            .load(movie.poster)
            .into(iv_movie_poster)

        Glide
            .with(this)
            .load(movie.backdrop)
            .into(iv_movie_backdrop)

        tv_movie_title.text = movie.title
        tv_movie_desc.text = movie.desc
        tv_movie_rating.text = resources.getString(R.string.movie_rating, movie.rating)
        tv_movie_release.text = movie.release
        tv_movie_time.text = resources.getString(R.string.movie_time, movie.runtime)
        tv_movie_genre.text = movie.genre
        tv_movie_directors.text = movie.directors
        tv_movie_writers.text = movie.writers
        tv_movie_studio.text = movie.studio

    }

    private fun showLoading(state: Boolean) {
        if (state) {
            progressBar.visibility = View.VISIBLE
        } else {
            progressBar.visibility = View.GONE
        }
    }
}
