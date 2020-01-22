package com.dicoding.picodiploma.mysubmission

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import kotlinx.android.synthetic.main.item_row_list.view.*

class ListMovieAdapter(private val listMovie: ArrayList<Movie>) :
    RecyclerView.Adapter<ListMovieAdapter.ListViewHolder>() {
    private var onItemClickCallback: OnItemClickCallback? = null

    fun setOnItemClickCallback(onItemClickCallback: OnItemClickCallback) {
        this.onItemClickCallback = onItemClickCallback
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.item_row_list, parent, false)
        return ListViewHolder(view)
    }

    override fun getItemCount(): Int = listMovie.size

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        holder.bind(listMovie[position])
    }

    inner class ListViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(movie: Movie) {
            with(itemView) {
                Glide.with(itemView.context)
                    .load(movie.poster)
                    .apply(RequestOptions().override(80, 120))
                    .into(iv_movie_poster)
                tv_title.text = movie.title
                tv_rating.text = movie.rating
                tv_runtime.text = movie.runtime

                itemView.setOnClickListener { onItemClickCallback?.onItemClicked(movie) }
            }
        }
    }

    interface OnItemClickCallback {
        fun onItemClicked(data: Movie)
    }
}