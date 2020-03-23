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

class ListTvShowViewModel : ViewModel() {
    companion object {
        private const val API_KEY = BuildConfig.TMDB_API_KEY
    }

    val listTvShows = MutableLiveData<ArrayList<Movie>>()

    fun setTvShows(context: Context) {
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
                        if (!tvSeries.isNull("poster_path")) {
                            val linkPoster = tvSeries.getString("poster_path")
                            tvSeriesItem.poster = "https://image.tmdb.org/t/p/w185$linkPoster"
                        } else {
                            tvSeriesItem.poster = "no_image"
                        }
                        tvSeriesItem.title = tvSeries.getString("name")
                        tvSeriesItem.rating = tvSeries.getDouble("vote_average").toString()
                        tvSeriesItem.release =
                            Util.changeDateFormat(tvSeries.getString("first_air_date"))
                        listTvSeriesItem.add(tvSeriesItem)
                    }
                    listTvShows.postValue(listTvSeriesItem)
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

    internal fun getTvShows(): LiveData<ArrayList<Movie>> {
        return listTvShows
    }
}