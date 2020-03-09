package com.dicoding.picodiploma.mysubmission.viewmodel

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.dicoding.picodiploma.mysubmission.model.Movie
import com.dicoding.picodiploma.mysubmission.model.db.MovieHelper
import com.dicoding.picodiploma.mysubmission.util.MappingHelper
import com.dicoding.picodiploma.mysubmission.util.util
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import java.lang.Exception

class ListFavoriteViewModel : ViewModel() {

    private lateinit var movieHelper: MovieHelper

    val listFavMovies = MutableLiveData<ArrayList<Movie>>()
    val listFavTvShows = MutableLiveData<ArrayList<Movie>>()


    fun initHelper(context: Context) {
        movieHelper = MovieHelper.getInstance(context)
    }

    fun setFavMovies(context: Context, index: Int) {
        try {
            val listFavMovieItems = ArrayList<Movie>()

            GlobalScope.launch(Dispatchers.Main) {
                val defferedTvShows = async(Dispatchers.IO) {
                    val cursor = movieHelper.queryByType(index.toString())
                    MappingHelper.mapCursorToArrayList(cursor)
                }
                listFavMovieItems.addAll(defferedTvShows.await())
            }
            listFavMovies.postValue(listFavMovieItems)
        } catch (e: Exception) {
            util.showToast(context, e.message.toString())
            e.printStackTrace()
        }
    }

    fun setFavTvShows(context: Context, index: Int) {
        try {
            val listFavTvShowsItems = ArrayList<Movie>()

            GlobalScope.launch(Dispatchers.Main) {
                val defferedTvShows = async(Dispatchers.IO) {
                    val cursor = movieHelper.queryByType(index.toString())
                    MappingHelper.mapCursorToArrayList(cursor)
                }
                listFavTvShowsItems.addAll(defferedTvShows.await())
            }
            listFavTvShows.postValue(listFavTvShowsItems)
        } catch (e: Exception) {
            util.showToast(context, e.message.toString())
            e.printStackTrace()
        }
    }

    internal fun getFavMovies(): LiveData<ArrayList<Movie>> {
        return listFavMovies
    }

    internal fun getFavTvShows(): LiveData<ArrayList<Movie>> {
        return listFavTvShows
    }
}