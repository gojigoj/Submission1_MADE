package com.dicoding.picodiploma.mysubmission


import android.content.Intent
import android.content.res.TypedArray
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.fragment_movies.*

/**
 * A simple [Fragment] subclass.
 */
class MoviesFragment : Fragment() {

    private var Movies = ArrayList<Movie>()
    private var TvShow = ArrayList<Movie>()

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

    companion object {
        private val ARG_SECTION_NUMBER = "section_number"

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
        return inflater.inflate(R.layout.fragment_movies, container, false)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        Movies.addAll(addItemMovies())
        TvShow.addAll(addItemTvShow())

        rv_list.setHasFixedSize(true)
        rv_list.layoutManager = LinearLayoutManager(view.context)
        var index = 0
        if (arguments != null) {
            index = arguments?.getInt(ARG_SECTION_NUMBER, 0) as Int
        }

        if (index == 0) {
            showMovieList(Movies, index)
        } else {
            showMovieList(TvShow, index)
        }

    }

    private fun goToDetailMovie(movie: Movie, index: Int) {
        val intent = Intent(context, DetailMovieActivity::class.java)
        intent.putExtra(DetailMovieActivity.EXTRA_SELECTED_VALUE, movie)
        intent.putExtra(DetailMovieActivity.EXTRA_TYPE, index)
        startActivity(intent)
    }

    private fun showMovieList(data: ArrayList<Movie>, index: Int) {
        val listMovieAdapter = ListMovieAdapter(data)
        rv_list.adapter = listMovieAdapter

        listMovieAdapter.setOnItemClickCallback(object : ListMovieAdapter.OnItemClickCallback {
            override fun onItemClicked(data: Movie) {
                goToDetailMovie(data, index)
            }
        })
    }

    private fun addItemMovies(): ArrayList<Movie> {
        dataPoster = resources.obtainTypedArray(R.array.data_poster_movies)
        dataTitle = resources.getStringArray(R.array.data_title_movies)
        dataDesc = resources.getStringArray(R.array.data_desc_movies)
        dataRelease = resources.getStringArray(R.array.data_release_movies)
        dataRuntime = resources.getStringArray(R.array.data_runtime_movies)
        dataRating = resources.getStringArray(R.array.data_rating_movies)
        dataGenre = resources.getStringArray(R.array.data_genre_movies)
        dataDirectors = resources.getStringArray(R.array.data_directors_movies)
        dataWriters = resources.getStringArray(R.array.data_writers_movies)
        dataStudio = resources.getStringArray(R.array.data_studio_movies)

        val listMovie = ArrayList<Movie>()
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

            listMovie.add(movie)
        }
        return listMovie
    }

    private fun addItemTvShow(): ArrayList<Movie> {
        dataPoster = resources.obtainTypedArray(R.array.data_poster_tvshow)
        dataTitle = resources.getStringArray(R.array.data_title_tvshow)
        dataDesc = resources.getStringArray(R.array.data_desc_tvshow)
        dataRelease = resources.getStringArray(R.array.data_release_tvshow)
        dataRuntime = resources.getStringArray(R.array.data_runtime_tvshow)
        dataRating = resources.getStringArray(R.array.data_rating_tvshow)
        dataGenre = resources.getStringArray(R.array.data_genre_tvshow)
        dataDirectors = resources.getStringArray(R.array.data_creators_tvshow)
        dataWriters = resources.getStringArray(R.array.data_starring_tvshow)
        dataStudio = resources.getStringArray(R.array.data_tvnetwork_tvshow)

        val listTvShow = ArrayList<Movie>()
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

            listTvShow.add(movie)
        }
        return listTvShow
    }


}
