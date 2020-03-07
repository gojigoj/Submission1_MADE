package com.dicoding.picodiploma.mysubmission.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Movie(
    var id: Int = 0,
    var poster: String? = null,
    var backdrop: String? = null,
    var title: String? = null,
    var desc: String? = null,
    var rating: String? = null,
    var release: String? = null,
    var runtime: String? = null,
    var genre: String? = null,
    var directors: String? = null,
    var writers: String? = null,
    var studio: String? = null
) : Parcelable