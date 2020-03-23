package com.dicoding.picodiploma.mysubmission.viewmodel

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.dicoding.picodiploma.mysubmission.BuildConfig
import com.dicoding.picodiploma.mysubmission.model.Movie
import com.dicoding.picodiploma.mysubmission.util.Util
import com.loopj.android.http.AsyncHttpClient
import com.loopj.android.http.AsyncHttpResponseHandler
import cz.msebera.android.httpclient.Header
import org.json.JSONObject

class ItemViewModel : ViewModel() {

    companion object {
        private const val API_KEY = BuildConfig.TMDB_API_KEY
    }

    val itemMovie = MutableLiveData<Movie>()
    val itemTvShows = MutableLiveData<Movie>()

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
                    if (!responseObject.isNull("backdrop_path")) {
                        val linkBackdrop = responseObject.getString("backdrop_path")
                        data.backdrop = "https://image.tmdb.org/t/p/w185$linkBackdrop"
                    } else {
                        data.backdrop = "no_image"
                    }
                    data.desc = responseObject.getString("overview")
                    if (!responseObject.isNull("runtime")) {
                        data.runtime = responseObject.getInt("runtime").toString()
                    } else {
                        data.runtime = "0"
                    }

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
                                "Director" -> listDirector.add(
                                    list.getJSONObject(i).getString("name")
                                )
                                "Screenplay" -> listWriter.add(
                                    list.getJSONObject(i).getString("name")
                                )
                                "Writer" -> listWriter.add(list.getJSONObject(i).getString("name"))
                            }
                        }
                        if (listDirector.size == 0) {
                            data.directors = "-"
                        } else {
                            data.directors = listDirector.joinToString(separator = ", ")
                        }
                        if (listWriter.size == 0) {
                            data.writers = "-"
                        } else {
                            data.writers = listWriter.joinToString(separator = ", ")
                        }
                    }
                    itemMovie.postValue(data)

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

    fun setTvShowsItem(context: Context, data: Movie) {
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
                    if (!responseObject.isNull("backdrop_path")) {
                        val linkBackdrop = responseObject.getString("backdrop_path")
                        data.backdrop = "https://image.tmdb.org/t/p/w185$linkBackdrop"
                    } else {
                        data.backdrop = "no_image"
                    }
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
                    setTvShowsCredit(context, data)

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

    fun setTvShowsCredit(context: Context, data: Movie) {
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
                    if (!responseObject.isNull("cast")) {
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
                    } else {
                        data.writers = "-"
                    }
                    itemTvShows.postValue(data)
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

    internal fun getMovieItem(): LiveData<Movie> {
        return itemMovie
    }

    internal fun getTvShowsItem(): LiveData<Movie> {
        return itemTvShows
    }
}