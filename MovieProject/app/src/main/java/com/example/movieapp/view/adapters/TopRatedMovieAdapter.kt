package com.example.movieapp.view.adapters

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.movieapp.R
import com.example.movieapp.databinding.MovieItemGridBinding
import com.example.movieapp.model.movie.MovieResult
import com.example.movieapp.room.FavoriteMovie

class TopRatedMovieAdapter(
    private val listener: OnItemClickListener,
    private val favStatusChangeListener: OnFavoriteStatusChangeListener,
) :
    RecyclerView.Adapter<TopRatedMovieAdapter.TopRatedMovieViewHolder>() {
    private var tRMovieList = ArrayList<MovieResult>()

    inner class TopRatedMovieViewHolder(bindingItem: MovieItemGridBinding) :
        RecyclerView.ViewHolder(bindingItem.root) {
        init {
            val btnAddFav = bindingItem.favButton

            btnAddFav.setOnClickListener {
                val position = bindingAdapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    val movie = tRMovieList[position]
                    movie.isFavorite = !movie.isFavorite
                    favStatusChangeListener.onFavoriteStatusChanged1(movie)
                    notifyItemChanged(position)
                }
            }
        }

        fun bind(tRMovie: MovieResult) {
            val bindingItem = MovieItemGridBinding.bind(itemView)
            bindingItem.apply {
                if (tRMovie.isFavorite) {
                    favButton.setImageResource(R.drawable.add_fav_filled_icon_top_rated)
                } else {
                    favButton.setImageResource(R.drawable.add_fav_empty_icon_top_rated)
                }
                Glide.with(itemView.context)
                    .load("https://image.tmdb.org/t/p/w500${tRMovie.posterPath}").fitCenter()
                    .transform(CenterCrop(), RoundedCorners(50))
                    .into(movieImage)
            }
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int,
    ): TopRatedMovieViewHolder {
        val itemView =
            MovieItemGridBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        return TopRatedMovieViewHolder(itemView)
    }

    override fun onBindViewHolder(
        holder: TopRatedMovieViewHolder,
        position: Int,
    ) {
        holder.bind(tRMovieList[position])
        holder.itemView.setOnClickListener {
            listener.onItemClick(tRMovieList[position])
        }
    }

    override fun getItemCount() = tRMovieList.size

    @SuppressLint("NotifyDataSetChanged")
    fun updateList(list: List<MovieResult>?) {
        tRMovieList.clear()
        tRMovieList.addAll(list ?: emptyList())
        notifyDataSetChanged()
    }

    interface OnItemClickListener {
        fun onItemClick(movie: MovieResult)
    }

    interface OnFavoriteStatusChangeListener {
        fun onFavoriteStatusChanged1(movie: MovieResult)
    }

    fun updateFavoriteStatus(favoriteMovies: List<FavoriteMovie>) {
        tRMovieList.forEach { movie ->
            val isFav = favoriteMovies.any { it.id == movie.id }
            movie.isFavorite = isFav
        }
        notifyDataSetChanged()
    }
}
