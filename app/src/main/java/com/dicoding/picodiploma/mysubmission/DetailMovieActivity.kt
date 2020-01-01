package com.dicoding.picodiploma.mysubmission

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.activity_detail_movie.*
import kotlinx.android.synthetic.main.activity_detail_movie.iv_movie_poster
import kotlinx.android.synthetic.main.item_row_list.*

class DetailMovieActivity : AppCompatActivity() {

    companion object {
        const val EXTRA_SELECTED_MOVIE = "extra_selected_value"
    }

    private lateinit var rating: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail_movie)

        setSupportActionBar(toolbar)
        supportActionBar?.setTitle("")
        supportActionBar?.setDisplayShowTitleEnabled(true)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        toolbar.setNavigationOnClickListener(View.OnClickListener {
            onBackPressed()
        })


        val movie = intent.getParcelableExtra(EXTRA_SELECTED_MOVIE) as Movie
        Glide
            .with(this)
            .load(movie.poster)
            .into(iv_movie_poster)

        tv_movie_title.text = movie.title
        tv_movie_desc.text = movie.desc
        tv_movie_rating.text = "${movie.rating} / 10"
        tv_movie_release.text = movie.release
        tv_movie_time.text = movie.runtime
        tv_movie_genre.text = movie.genre
        tv_movie_directors.text = movie.directors
        tv_movie_writers.text = movie.writers
        tv_movie_studio.text = movie.studio

    }
}
