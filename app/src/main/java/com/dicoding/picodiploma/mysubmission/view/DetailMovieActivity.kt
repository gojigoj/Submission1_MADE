package com.dicoding.picodiploma.mysubmission.view

import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.dicoding.picodiploma.mysubmission.R
import com.dicoding.picodiploma.mysubmission.model.Movie
import com.dicoding.picodiploma.mysubmission.model.db.DatabaseContract
import com.dicoding.picodiploma.mysubmission.model.db.MovieHelper
import com.dicoding.picodiploma.mysubmission.util.EspressoIdlingResource
import com.dicoding.picodiploma.mysubmission.util.util
import com.dicoding.picodiploma.mysubmission.viewmodel.ItemViewModel
import kotlinx.android.synthetic.main.activity_detail_movie.*
import kotlinx.android.synthetic.main.layout_detail_mov.*
import kotlin.math.log

class DetailMovieActivity : AppCompatActivity(), View.OnClickListener {

    private lateinit var itemViewModel: ItemViewModel
    private lateinit var movieHelper: MovieHelper
    private var TAG = DetailMovieActivity::class.java.simpleName

    private lateinit var movie: Movie
    private var index: Int = 0
    private var position: Int = 0
    private var isFavorite: Boolean = false

    companion object {
        const val EXTRA_SELECTED_VALUE = "extra_selected_value"
        const val EXTRA_TYPE = "extra_type"
        const val EXTRA_POSITION = "extra_position"
        const val REQUEST_ADD = 100
        const val RESULT_ADD = 101
        const val RESULT_DELETE = 301
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

        movieHelper = MovieHelper.getInstance(applicationContext)

        itemViewModel = ViewModelProvider(
            this,
            ViewModelProvider.NewInstanceFactory()
        ).get(ItemViewModel::class.java)

        movie = intent.getParcelableExtra(EXTRA_SELECTED_VALUE) as Movie
        index = intent.getIntExtra(EXTRA_TYPE, 3)
        position = intent.getIntExtra(EXTRA_POSITION, 0)

        isFavorite = checkFav(movie.id)
        btnSetFav(isFavorite)

        if (index == 1) {
            runtime_title.text = resources.getString(R.string.episode_runtime)
            directors_title.text = resources.getString(R.string.creators)
            writers_title.text = resources.getString(R.string.starring)
            studio_title.text = resources.getString(R.string.tv_network)
            getTvSeriesItem(this, movie)
        } else {
            getMovieItem(this, movie)
        }

        btn_fav.setOnClickListener(this)
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
        itemViewModel.setTvShowsItem(context, movie)
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

    private fun checkFav(id: Int?): Boolean {
        val cursor = movieHelper.queryById(id.toString())
        return if (cursor.moveToFirst()) {
            val idDb = cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseContract.MovieFavColumns._ID))
            idDb == id
        } else {
            false
        }
    }

    private fun btnSetFav(fav: Boolean) {
        if (fav) {
            btn_fav.background = resources.getDrawable(R.drawable.shape_btn_active, null)
            Glide
                .with(this)
                .load(R.drawable.ic_favorite_color)
                .into(iv_btn_fav)
            tv_btn_fav.setTextColor(ContextCompat.getColor(this, android.R.color.white))
        } else {
            btn_fav.background = resources.getDrawable(R.drawable.shape_btn_inactive, null)
            Glide
                .with(this)
                .load(R.drawable.ic_favorite_border)
                .into(iv_btn_fav)
            tv_btn_fav.setTextColor(ContextCompat.getColor(this, android.R.color.black))
        }
    }

    override fun onClick(v: View) {
        if (v.id == R.id.btn_fav) {
            if (!isFavorite) {
                val id = movie.id
                val title = movie.title
                val poster = movie.poster
                val rating = movie.rating
                val release = movie.release

                movie = Movie(id = id, poster = poster, title = title, rating = rating, release = release)

                val intent = Intent()
                intent.putExtra(EXTRA_SELECTED_VALUE, movie)
                intent.putExtra(EXTRA_TYPE, index)
                intent.putExtra(EXTRA_POSITION, position)

                val values = ContentValues()
                values.put(DatabaseContract.MovieFavColumns._ID, id)
                values.put(DatabaseContract.MovieFavColumns.TITLE, title)
                values.put(DatabaseContract.MovieFavColumns.POSTER, poster)
                values.put(DatabaseContract.MovieFavColumns.RATING, rating)
                values.put(DatabaseContract.MovieFavColumns.RELEASE, release)
                values.put(DatabaseContract.MovieFavColumns.TYPE, index)

                val result = movieHelper.insert(values)

                if (result > 0) {
                    Log.d(TAG, "Berhasil menambah data")
                    isFavorite = true
                    btnSetFav(isFavorite)
                    setResult(RESULT_ADD, intent)
                    util.showToast(applicationContext, "Satu item berhasil ditambah")
                    finish()
                }  else {
                    util.showToast(this, "Gagal menambah data")
                }
            } else {
                val result = movieHelper.deleteById(movie.id.toString()).toLong()
                if (result > 0) {
                    Log.d(TAG, "Berhasil menghapus data")
                    isFavorite = false
                    btnSetFav(isFavorite)

                    val intent = Intent()
                    intent.putExtra(EXTRA_POSITION, position)
                    intent.putExtra(EXTRA_TYPE, index)
                    setResult(RESULT_DELETE, intent)
                    util.showToast(applicationContext, "Satu item berhasil dihapus")
                    finish()
                } else {
                    util.showToast(this, "Gagal menghapus data")
                }
            }
        }
    }
}
