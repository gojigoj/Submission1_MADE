package com.dicoding.picodiploma.mysubmission.model.db

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.dicoding.picodiploma.mysubmission.model.db.DatabaseContract.MovieFavColumns.Companion.TABLE_NAME
import com.dicoding.picodiploma.mysubmission.model.db.DatabaseContract.MovieFavColumns

class DatabaseHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        private const val DATABASE_NAME = "dbmymov"
        private const val DATABASE_VERSION = 1

        private val SQL_CREATE_TABLE_NOTE ="CREATE TABLE $TABLE_NAME" +
                " (${MovieFavColumns._ID} INTEGER PRIMARY KEY," +
                " ${MovieFavColumns.TITLE} TEXT NOT NULL," +
                " ${MovieFavColumns.POSTER} TEXT NOT NULL," +
                " ${MovieFavColumns.RATING} TEXT NOT NULL," +
                " ${MovieFavColumns.RELEASE} TEXT NOT NULL," +
                " ${MovieFavColumns.TYPE} INTEGER NOT NULL)"
    }

    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL(SQL_CREATE_TABLE_NOTE)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS $TABLE_NAME")
        onCreate(db)
    }
}