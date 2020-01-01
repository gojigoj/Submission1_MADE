package com.dicoding.picodiploma.mysubmission

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import kotlinx.android.synthetic.main.activity_detail_movie.view.*
import kotlinx.android.synthetic.main.item_row_list.view.*
import kotlinx.android.synthetic.main.item_row_list.view.iv_movie_poster

class MovieAdapter internal constructor(private val context: Context): BaseAdapter() {
    internal var movies = arrayListOf<Movie>()

    override fun getView(position: Int, view: View?, parent: ViewGroup?): View {
        var itemView = view
        if (itemView == null) {
            itemView = LayoutInflater.from(context).inflate(R.layout.item_row_list, parent, false)
        }

        val viewHolder = ViewHolder(itemView as View)

        val movie = getItem(position) as Movie
        viewHolder.bind(movie)
        return itemView
    }

    private inner class ViewHolder internal constructor(private  val view: View) {
        internal fun bind(movie: Movie){
            with(view) {
                iv_movie_poster.setImageResource(movie.poster)
                tv_title.text = movie.title
                tv_rating.text = ("${movie.rating} / 10")
                tv_runtime.text = movie.runtime
            }
        }
    }

    override fun getItem(position: Int): Any = movies[position]

    override fun getItemId(position: Int): Long = position.toLong()

    override fun getCount(): Int = movies.size


}