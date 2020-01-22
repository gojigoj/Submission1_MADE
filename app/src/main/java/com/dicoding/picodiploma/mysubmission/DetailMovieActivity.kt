package com.dicoding.picodiploma.mysubmission

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.activity_detail_movie.*
import kotlinx.android.synthetic.main.layout_detail_mov.*

class DetailMovieActivity : AppCompatActivity() {

    companion object {
        const val EXTRA_SELECTED_VALUE = "extra_selected_value"
        const val EXTRA_TYPE = "extra_type"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail_movie)

        setSupportActionBar(toolbar)
        supportActionBar?.title = ""
        supportActionBar?.setDisplayShowTitleEnabled(true)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        toolbar.setNavigationOnClickListener(View.OnClickListener {
            onBackPressed()
        })


        var index = intent.getIntExtra(EXTRA_TYPE, 3)
        if (index == 1) {
            directors_title.text = resources.getString(R.string.creators)
            writers_title.text = resources.getString(R.string.starring)
            studio_title.text = resources.getString(R.string.tv_network)
        }

        val movie = intent.getParcelableExtra(EXTRA_SELECTED_VALUE) as Movie
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
