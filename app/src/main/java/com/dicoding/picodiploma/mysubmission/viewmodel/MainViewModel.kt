package com.dicoding.picodiploma.mysubmission.viewmodel

import android.content.Context
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.dicoding.picodiploma.mysubmission.BuildConfig
import com.dicoding.picodiploma.mysubmission.model.Movie
import com.loopj.android.http.AsyncHttpClient
import com.loopj.android.http.AsyncHttpResponseHandler
import cz.msebera.android.httpclient.Header
import org.json.JSONObject
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class MainViewModel : ViewModel() {

    companion object {
        private const val API_KEY = BuildConfig.TMDB_API_KEY

        private const val DATE_FORMAT = "yyyy-MM-dd"
        private const val NEW_DATE_FORMAT = "dd MMMM yyyy"
    }

    val listMovies = MutableLiveData<ArrayList<Movie>>()
    val listTvSeries = MutableLiveData<ArrayList<Movie>>()
    val itemMovie = MutableLiveData<Movie>()
    val itemTvSeries = MutableLiveData<Movie>()

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
                        moviesItem.release = changeDateFormat(movie.getString("release_date"))
                        listMovieItems.add(moviesItem)
                    }
                    listMovies.postValue(listMovieItems)
                } catch (e: Exception) {
                    showToast(context, e.message.toString())
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
                showToast(context, errorMessage)
            }

        })
    }

    fun setTvSeries(context: Context) {
        val client = AsyncHttpClient()
        val url = "https://api.themoviedb.org/3/discover/tv?api_key=$API_KEY&language=en-US"
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
                        val linkPoster = tvSeries.getString("poster_path")
                        tvSeriesItem.poster = "https://image.tmdb.org/t/p/w185$linkPoster"
                        tvSeriesItem.title = tvSeries.getString("name")
                        tvSeriesItem.rating = tvSeries.getDouble("vote_average").toString()
                        tvSeriesItem.release =
                            changeDateFormat(tvSeries.getString("first_air_date"))
                        listTvSeriesItem.add(tvSeriesItem)
                    }
                    listTvSeries.postValue(listTvSeriesItem)
                } catch (e: Exception) {
                    showToast(context, e.message.toString())
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
                showToast(context, errorMessage)
            }

        })
    }

    fun setMovieItem(context: Context, data: Movie) {
        val client = AsyncHttpClient()
        val genres = ArrayList<String>()
        val url = "https://api.themoviedb.org/3/movie/${data.id}?api_key=$API_KEY&language=en-US"
        client.get(url, object : AsyncHttpResponseHandler() {
            override fun onSuccess(
                statusCode: Int,
                headers: Array<out Header>?,
                responseBody: ByteArray
            ) {
                val result = String(responseBody)
                try {
                    val responseObject = JSONObject(result)
                    val linkBackdrop = responseObject.getString("backdrop_path")
                    data.backdrop = "https://image.tmdb.org/t/p/w185$linkBackdrop"
                    data.desc = responseObject.getString("overview")
                    data.runtime = responseObject.getInt("runtime").toString()
                    val listStudio = responseObject.getJSONArray("production_companies")
                    if (listStudio.length() == 0) {
                        data.studio = "-"
                    } else {
                        data.studio = listStudio.getJSONObject(0).getString("name")
                    }
                    val listGenre = responseObject.getJSONArray("genres")
                    if (listGenre.length() == 0) {
                        data.genre = "-"
                    } else {
                        if (listGenre.length() > 2) {
                            for (i in 0 until 3) {
                                genres.add(listGenre.getJSONObject(i).getString("name"))
                            }
                        } else {
                            for (i in 0 until listGenre.length()) {
                                genres.add(listGenre.getJSONObject(i).getString("name"))
                            }
                        }
                        data.genre = genres.joinToString(separator = ", ")
                    }
                    setMovieCredit(context, data)
                } catch (e: Exception) {
                    showToast(context, e.message.toString())
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
                showToast(context, errorMessage)
            }

        })
    }

    fun setMovieCredit(context: Context, data: Movie) {
        val client = AsyncHttpClient()
        var item: String
        val listDirector = ArrayList<String>()
        val listWriter = ArrayList<String>()
        val url = "https://api.themoviedb.org/3/movie/${data.id}/credits?api_key=$API_KEY"
        client.get(url, object : AsyncHttpResponseHandler() {
            override fun onSuccess(
                statusCode: Int,
                headers: Array<out Header>?,
                responseBody: ByteArray
            ) {
                val result = String(responseBody)
                try {
                    val responseObject = JSONObject(result)
                    val list = responseObject.getJSONArray("crew")
                    if (list.length() == 0) {
                        data.directors = "-"
                        data.writers = "-"
                    } else {
                        for (i in 0 until list.length()) {
                            item = list.getJSONObject(i).getString("job")
                            when (item) {
                                "Director" -> listDirector.add(list.getJSONObject(i).getString("name"))
                                "Screenplay" -> listWriter.add(list.getJSONObject(i).getString("name"))
                                "Writer" -> listWriter.add(list.getJSONObject(i).getString("name"))
                            }
                        }
                        data.directors = listDirector.joinToString(separator = ", ")
                        data.writers = listWriter.joinToString(separator = ", ")
                    }
                    itemMovie.postValue(data)

                } catch (e: Exception) {
                    showToast(context, e.message.toString())
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
                showToast(context, errorMessage)
            }

        })
    }

    fun setTvSeriesItem(context: Context, data: Movie) {
        val client = AsyncHttpClient()
        val genres = ArrayList<String>()
        val creators = ArrayList<String>()
        val url = "https://api.themoviedb.org/3/tv/${data.id}?api_key=$API_KEY&language=en-US"
        client.get(url, object : AsyncHttpResponseHandler() {
            override fun onSuccess(
                statusCode: Int,
                headers: Array<out Header>?,
                responseBody: ByteArray
            ) {
                val result = String(responseBody)
                try {
                    val responseObject = JSONObject(result)
                    val linkBackdrop = responseObject.getString("backdrop_path")
                    data.backdrop = "https://image.tmdb.org/t/p/w185$linkBackdrop"
                    data.desc = responseObject.getString("overview")
                    val runtimePerEpisode = responseObject.getJSONArray("episode_run_time")
                    data.runtime = runtimePerEpisode[0].toString()
                    val listStudio = responseObject.getJSONArray("networks")
                    if (listStudio.length() == 0) {
                        data.studio = "-"
                    } else {
                        data.studio = listStudio.getJSONObject(0).getString("name")
                    }
                    val listGenre = responseObject.getJSONArray("genres")
                    if (listGenre.length() > 2) {
                        for (i in 0 until 3) {
                            genres.add(listGenre.getJSONObject(i).getString("name"))
                        }
                    } else {
                        for (i in 0 until listGenre.length()) {
                            genres.add(listGenre.getJSONObject(i).getString("name"))
                        }
                    }
                    data.genre = genres.joinToString(separator = ", ")
                    val listCreator = responseObject.getJSONArray("created_by")
                    if (listCreator.length() == 0) {
                        data.directors = "-"
                    } else {
                        if (listCreator.length() > 2) {
                            for (i in 0 until 3) {
                                creators.add(listCreator.getJSONObject(i).getString("name"))
                            }
                        } else {
                            for (i in 0 until listCreator.length()) {
                                creators.add(listCreator.getJSONObject(i).getString("name"))
                            }
                        }
                        data.directors = creators.joinToString(separator = ", ")
                    }
                    setTvSeriesCredit(context, data)

                } catch (e: Exception) {
                    showToast(context, e.message.toString())
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
                showToast(context, errorMessage)
            }

        })
    }

    fun setTvSeriesCredit(context: Context, data: Movie) {
        val client = AsyncHttpClient()
        val listCast = ArrayList<String>()
        val url =
            "https://api.themoviedb.org/3/tv/${data.id}/credits?api_key=$API_KEY&language=en-US"
        client.get(url, object : AsyncHttpResponseHandler() {
            override fun onSuccess(
                statusCode: Int,
                headers: Array<out Header>?,
                responseBody: ByteArray
            ) {
                val result = String(responseBody)
                try {
                    val responseObject = JSONObject(result)
                    val list = responseObject.getJSONArray("cast")
                    if (list.length() == 0) {
                        data.writers = "-"
                    } else {
                        if (list.length() > 2) {
                            for (i in 0 until 3) {
                                listCast.add(list.getJSONObject(i).getString("name"))
                            }
                        } else {
                            for (i in 0 until list.length()) {
                                listCast.add(list.getJSONObject(i).getString("name"))
                            }
                        }
                        data.writers = listCast.joinToString(separator = ", ")
                    }
                    itemTvSeries.postValue(data)
                } catch (e: Exception) {
                    showToast(context, e.message.toString())
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
                showToast(context, errorMessage)
            }

        })
    }

    internal fun getMovies(): LiveData<ArrayList<Movie>> {
        return listMovies
    }

    internal fun getTvSeries(): LiveData<ArrayList<Movie>> {
        return listTvSeries
    }

    internal fun getMovieItem(): LiveData<Movie> {
        return itemMovie
    }

    internal fun getTvSeriesItem(): LiveData<Movie> {
        return itemTvSeries
    }

    private fun changeDateFormat(dateMovie: String): String {
        return try {
            val format = SimpleDateFormat(DATE_FORMAT, Locale.getDefault())
            val dateMov = format.parse(dateMovie)
            val newFormat = SimpleDateFormat(NEW_DATE_FORMAT, Locale.getDefault())
            return newFormat.format(dateMov)
        } catch (e: ParseException) {
            e.printStackTrace().toString()
        }
    }

    private fun showToast(context: Context, message: String) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
    }

}