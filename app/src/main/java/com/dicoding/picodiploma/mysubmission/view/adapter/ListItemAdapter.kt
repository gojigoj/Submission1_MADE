package com.dicoding.picodiploma.mysubmission.view.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.dicoding.picodiploma.mysubmission.R
import com.dicoding.picodiploma.mysubmission.model.Movie
import kotlinx.android.synthetic.main.item_row_list.view.*

class ListItemAdapter : RecyclerView.Adapter<ListItemAdapter.ListViewHolder>() {

    private var onItemClickCallback: OnItemClickCallback? = null

    var listMovie = ArrayList<Movie>()
        set(listMovie) {
            if (listMovie.size > 0) {
                this.listMovie.clear()
            }
            this.listMovie.addAll(listMovie)
            notifyDataSetChanged()
        }

    fun addItem(movie: Movie) {
        this.listMovie.add(movie)
        notifyItemInserted(this.listMovie.size - 1)
    }

    fun removeItem(position: Int) {
        this.listMovie.removeAt(position)
        notifyItemRemoved(position)
        notifyItemRangeChanged(position, this.listMovie.size)
    }

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
                if (movie.poster.equals("no_image")) {
                    Glide.with(itemView.context)
                        .load(R.drawable.poster_film)
                        .apply(RequestOptions().override(80, 120))
                        .into(iv_movie_poster)
                } else {
                    Glide.with(itemView.context)
                        .load(movie.poster)
                        .apply(RequestOptions().override(80, 120))
                        .into(iv_movie_poster)
                }
                tv_title.text = movie.title
                tv_rating.text = resources.getString(R.string.movie_rating, movie.rating)
                tv_runtime.text = movie.release

                itemView.setOnClickListener {
                    onItemClickCallback?.onItemClicked(
                        movie,
                        adapterPosition
                    )
                }
            }
        }
    }

    interface OnItemClickCallback {
        fun onItemClicked(data: Movie, position: Int)
    }

}