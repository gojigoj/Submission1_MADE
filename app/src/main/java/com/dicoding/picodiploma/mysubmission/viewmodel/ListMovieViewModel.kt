package com.dicoding.picodiploma.mysubmission.viewmodel

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.dicoding.picodiploma.mysubmission.BuildConfig
import com.dicoding.picodiploma.mysubmission.model.Movie
import com.dicoding.picodiploma.mysubmission.util.util
import com.loopj.android.http.AsyncHttpClient
import com.loopj.android.http.AsyncHttpResponseHandler
import cz.msebera.android.httpclient.Header
import org.json.JSONObject

class ListMovieViewModel : ViewModel() {
    companion object {
        private const val API_KEY = BuildConfig.TMDB_API_KEY
    }

    val listMovies = MutableLiveData<ArrayList<Movie>>()

    fun setMovies(context: Context) {
        val client = AsyncHttpClient()
        val listMovieItems = ArrayList<Movie>()
        val url = "https://api.themoviedb.org/3/discover/movie?api_key=$API_KEY&language=en-US"
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
                        val linkPoster = movie.getString("poster_path")
                        moviesItem.poster = "https://image.tmdb.org/t/p/w185$linkPoster"
                        moviesItem.title = movie.getString("title")
                        moviesItem.rating = movie.getDouble("vote_average").toString()
                        moviesItem.release = util.changeDateFormat(movie.getString("release_date"))
                        listMovieItems.add(moviesItem)
                    }
                    listMovies.postValue(listMovieItems)
                } catch (e: Exception) {
                    util.showToast(context, e.message.toString())
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
                util.showToast(context, errorMessage)
            }

        })
    }

    internal fun getMovies(): LiveData<ArrayList<Movie>> {
        return listMovies
    }
}