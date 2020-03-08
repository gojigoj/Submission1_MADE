package com.dicoding.picodiploma.mysubmission.util

import android.content.Context
import android.content.Intent
import android.widget.Toast
import androidx.core.content.ContextCompat.startActivity
import com.dicoding.picodiploma.mysubmission.model.Movie
import com.dicoding.picodiploma.mysubmission.view.DetailMovieActivity
import com.dicoding.picodiploma.mysubmission.view.adapter.ListItemAdapter
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

object util {
    fun changeDateFormat(dateMovie: String): String {
        return try {
            val format = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
            val dateMov = format.parse(dateMovie)
            val newFormat = SimpleDateFormat("dd MMMM yyyy", Locale.getDefault())
            return newFormat.format(dateMov)
        } catch (e: ParseException) {
            e.printStackTrace().toString()
        }
    }

    fun showToast(context: Context, message: String) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
    }
}