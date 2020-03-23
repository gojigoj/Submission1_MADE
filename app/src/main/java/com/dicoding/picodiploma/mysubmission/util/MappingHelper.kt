package com.dicoding.picodiploma.mysubmission.util

import android.database.Cursor
import com.dicoding.picodiploma.mysubmission.model.Movie
import com.dicoding.picodiploma.mysubmission.model.db.DatabaseContract.MovieFavColumns

object MappingHelper {
    fun mapCursorToArrayList(movieCursor: Cursor?): ArrayList<Movie> {
        val movieList = ArrayList<Movie>()

        movieCursor?.apply {
            while (moveToNext()) {
                val id = getInt(getColumnIndexOrThrow(MovieFavColumns._ID))
                val poster = getString(getColumnIndexOrThrow(MovieFavColumns.POSTER))
                val title = getString(getColumnIndexOrThrow(MovieFavColumns.TITLE))
                val rating = getString(getColumnIndexOrThrow(MovieFavColumns.RATING))
                val release = getString(getColumnIndexOrThrow(MovieFavColumns.RELEASE))
                movieList.add(
                    Movie(
                        id = id,
                        poster = poster,
                        title = title,
                        rating = rating,
                        release = release
                    )
                )
            }
        }
        return movieList
    }
}