package com.dicoding.picodiploma.mysubmission.model.db

import android.provider.BaseColumns

internal class DatabaseContract {

    internal class MovieFavColumns : BaseColumns {
        companion object {
            const val TABLE_NAME = "movie_favorite"
            const val _ID = "_id"
            const val TITLE = "title"
            const val POSTER = "poster"
            const val RATING = "rating"
            const val RELEASE = "release"
            const val TYPE = "type"
        }
    }
}