package com.dicoding.picodiploma.mysubmission

import android.content.Intent
import android.content.res.TypedArray
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.AdapterView
import androidx.core.view.ViewCompat
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private lateinit var adapter: MovieAdapter

    private lateinit var dataPoster: TypedArray
    private lateinit var dataTitle: Array<String>
    private lateinit var dataDesc: Array<String>
    private lateinit var dataRelease: Array<String>
    private lateinit var dataRating: Array<String>
    private lateinit var dataRuntime: Array<String>
    private lateinit var dataGenre: Array<String>
    private lateinit var dataDirectors: Array<String>
    private lateinit var dataWriters: Array<String>
    private lateinit var dataStudio: Array<String>

    private var Movies = arrayListOf<Movie>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        adapter = MovieAdapter(this)
        lv_list.adapter = adapter
        ViewCompat.setNestedScrollingEnabled(lv_list, true)

        getData()
        addItem()

        lv_list.onItemClickListener = AdapterView.OnItemClickListener { _, _, position, _ ->
            val detailIntent = Intent(this@MainActivity, DetailMovieActivity::class.java)
            detailIntent.putExtra(DetailMovieActivity.EXTRA_SELECTED_MOVIE, Movies[position])
            startActivity(detailIntent)
        }

    }

    private fun addItem() {
        for (i in dataTitle.indices) {
            val movie = Movie(
                dataPoster.getResourceId(i, -1),
                dataTitle[i],
                dataDesc[i],
                dataRating[i],
                dataRelease[i],
                dataRuntime[i],
                dataGenre[i],
                dataDirectors[i],
                dataWriters[i],
                dataStudio[i]
            )

            Movies.add(movie)
        }

        adapter.movies = Movies
    }

    private fun getData() {
        dataPoster = resources.obtainTypedArray(R.array.data_poster)
        dataTitle = resources.getStringArray(R.array.data_title)
        dataDesc = resources.getStringArray(R.array.data_desc)
        dataRelease = resources.getStringArray(R.array.data_release)
        dataRuntime = resources.getStringArray(R.array.data_runtime)
        dataRating = resources.getStringArray(R.array.data_rating)
        dataGenre = resources.getStringArray(R.array.data_genre)
        dataDirectors = resources.getStringArray(R.array.data_directors)
        dataWriters = resources.getStringArray(R.array.data_writers)
        dataStudio = resources.getStringArray(R.array.data_studio)

    }
}
