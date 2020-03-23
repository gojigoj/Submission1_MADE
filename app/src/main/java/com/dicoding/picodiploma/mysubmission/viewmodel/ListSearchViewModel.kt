package com.dicoding.picodiploma.mysubmission.viewmodel

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.dicoding.picodiploma.mysubmission.BuildConfig
import com.dicoding.picodiploma.mysubmission.model.Movie
import com.dicoding.picodiploma.mysubmission.model.db.MovieHelper
import com.dicoding.picodiploma.mysubmission.util.MappingHelper
import com.dicoding.picodiploma.mysubmission.util.Util
import com.loopj.android.http.AsyncHttpClient
import com.loopj.android.http.AsyncHttpResponseHandler
import cz.msebera.android.httpclient.Header
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import org.json.JSONObject
import java.lang.Exception

class ListSearchViewModel : ViewModel() {
    companion object {
        private const val API_KEY = BuildConfig.TMDB_API_KEY
    }

    private val listSearchMovies = MutableLiveData<ArrayList<Movie>>()
    private val listSearchTvShows = MutableLiveData<ArrayList<Movie>>()


    fun setSearchMovies(context: Context, query: String) {
        val client = AsyncHttpClient()
        val listMovieItems = ArrayList<Movie>()
        val url = "https://api.themoviedb.org/3/search/movie?api_key=$API_KEY&language=en-US&query=$query"
        client.get(url, object : AsyncHttpResponseHandler() {
            override fun onSuccess(
                statusCode: Int,
                headers: Array<out Header>?,
                responseBody: ByteArray
            ) {
                val result = String(responseBody)
                try {
                    val responseObject = JSONObject(result)
                    val list = responseObject.getJSONArray("results")
                    for (i in 0 until list.length()) {
                        val movie = list.getJSONObject(i)
                        val moviesItem = Movie()
                        moviesItem.id = movie.getInt("id")
                        if (!movie.isNull("poster_path")) {
                            val linkPoster = movie.getString("poster_path")
                            moviesItem.poster = "https://image.tmdb.org/t/p/w185$linkPoster"
                        } else {
                            moviesItem.poster = "no_image"
                        }
                        moviesItem.title = movie.getString("title")
                        moviesItem.rating = movie.getDouble("vote_average").toString()
                        if (!movie.isNull("release_date")) {
                            var release = movie.getString("release_date")
                            if (release.isNotEmpty()) {
                                moviesItem.release = Util.changeDateFormat(release)
                            } else {
                                moviesItem.release = "-"
                            }
                        } else {
                            moviesItem.release = "-"
                        }
                        listMovieItems.add(moviesItem)
                    }
                    listSearchMovies.postValue(listMovieItems)
                } catch (e: Exception) {
                    Util.showToast(context, e.message.toString())
                    e.printStackTrace()
                }
            }

            override fun onFailure(
                statusCode: Int,
                headers: Array<out Header>?,
                responseBody: ByteArray?,
                error: Throwable
            ) {
                val errorMessage = when (statusCode) {
                    401 -> "$statusCode: Invalid API key"
                    404 -> "$statusCode: The resource you requested could not be found."
                    else -> "$statusCode: ${error.message}"
                }
                Util.showToast(context, errorMessage)
            }

        })
    }

    fun setSearchTvShows(context: Context, query: String) {
        val client = AsyncHttpClient()
        val url = "https://api.themoviedb.org/3/search/tv?api_key=$API_KEY&language=en-US&query=$query"
        val listTvSeriesItem = ArrayList<Movie>()
        client.get(url, object : AsyncHttpResponseHandler() {
            override fun onSuccess(
                statusCode: Int,
                headers: Array<out Header>?,
                responseBody: ByteArray
            ) {
                val result = String(responseBody)
                try {
                    val responseObject = JSONObject(result)
                    val list = responseObject.getJSONArray("results")
                    for (i in 0 until list.length()) {
                        val tvSeries = list.getJSONObject(i)
                        val tvSeriesItem = Movie()
                        tvSeriesItem.id = tvSeries.getInt("id")
                        if (!tvSeries.isNull("poster_path")) {
                            val linkPoster = tvSeries.getString("poster_path")
                            tvSeriesItem.poster = "https://image.tmdb.org/t/p/w185$linkPoster"
                        } else {
                            tvSeriesItem.poster = "no_image"
                        }
                        tvSeriesItem.title = tvSeries.getString("name")
                        tvSeriesItem.rating = tvSeries.getDouble("vote_average").toString()
                        if (!tvSeries.isNull("first_air_date")) {
                            val release = tvSeries.getString("first_air_date")
                            if (release.isNotEmpty()){
                                tvSeriesItem.release = Util.changeDateFormat(release)
                            } else {
                                tvSeriesItem.release ="-"
                            }
                        } else {
                            tvSeriesItem.release ="-"
                        }
                        listTvSeriesItem.add(tvSeriesItem)
                    }
                    listSearchTvShows.postValue(listTvSeriesItem)
                } catch (e: Exception) {
                    Util.showToast(context, e.message.toString())
                    e.printStackTrace()
                }
            }

            override fun onFailure(
                statusCode: Int,
                headers: Array<out Header>?,
                responseBody: ByteArray?,
                error: Throwable
            ) {
                val errorMessage = when (statusCode) {
                    401 -> "$statusCode: Invalid API key"
                    404 -> "$statusCode: The resource you requested could not be found."
                    else -> "$statusCode: ${error.message}"
                }
                Util.showToast(context, errorMessage)
            }

        })
    }

    internal fun getSearchMovies(): LiveData<ArrayList<Movie>> {
        return listSearchMovies
    }

    internal fun getSearchTvShows(): LiveData<ArrayList<Movie>> {
        return listSearchTvShows
    }
}