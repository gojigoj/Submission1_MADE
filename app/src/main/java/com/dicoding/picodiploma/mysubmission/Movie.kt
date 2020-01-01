package com.dicoding.picodiploma.mysubmission

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Movie (
    var poster: Int,
    var title: String,
    var desc: String,
    var rating: String,
    var release: String,
    var runtime: String,
    var genre: String,
    var directors: String,
    var writers: String,
    var studio: String
) : Parcelable