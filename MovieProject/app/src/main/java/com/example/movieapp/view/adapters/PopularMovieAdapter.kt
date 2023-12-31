package com.example.movieapp.view.adapters

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.movieapp.R
import com.example.movieapp.databinding.MovieItemPopularBinding
import com.example.movieapp.databinding.MovieItemPopularGridViewBinding
import com.example.movieapp.model.movie.MovieResult
import com.example.movieapp.room.FavoriteMovie

class PopularMovieAdapter(
    private val listener: OnItemClickListener,
    private val favStatusChangeListener: OnFavoriteStatusChangeListener,
) :
    RecyclerView.Adapter<PopularMovieAdapter.MovieViewHolder>() {
    private var movieList = ArrayList<MovieResult>()
    private var viewType = ViewType.LIST

    enum class ViewType {
        LIST, GRID
    }

    @SuppressLint("NotifyDataSetChanged")
    fun setViewType(viewType: ViewType) {
        this.viewType = viewType
        notifyDataSetChanged()
    }

    inner class MovieViewHolder(bindingItem: ViewBinding) :
        RecyclerView.ViewHolder(bindingItem.root) {
        init {
            val btnAddFav = when (bindingItem) {
                is MovieItemPopularBinding -> bindingItem.btnAddFav
                is MovieItemPopularGridViewBinding -> bindingItem.btnAddFav
                else -> null
            }

            btnAddFav?.setOnClickListener {
                val position = bindingAdapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    val movie = movieList[position]
                    movie.isFavorite = !movie.isFavorite
                    favStatusChangeListener.onFavoriteStatusChanged(movie)
                    notifyItemChanged(position)
                }
            }
        }

        fun bind(popularMovie: MovieResult) {
            if (viewType == ViewType.LIST) {
                val bindingItem = MovieItemPopularBinding.bind(itemView)
                bindingItem.apply {
                    movieTitle.text = popularMovie.title
                    voteMovie?.text = popularMovie.voteAverage.toString()
                    if (popularMovie.isFavorite) {
                        btnAddFav.setImageResource(R.drawable.add_fav_filled_icon)
                    } else {
                        btnAddFav.setImageResource(R.drawable.add_fav_empty_icon)
                    }

                    Glide.with(itemView.context)
                        .load("https://image.tmdb.org/t/p/w500${popularMovie.posterPath}")
                        .centerCrop().transform(CenterCrop(), RoundedCorners(20))
                        .into(movieImage as ImageView)
                }
            } else {
                val bindingItem = MovieItemPopularGridViewBinding.bind(itemView)
                bindingItem.apply {
                    if (popularMovie.isFavorite) {
                        btnAddFav.setImageResource(R.drawable.add_fav_filled_icon_top_rated)
                    } else {
                        btnAddFav.setImageResource(R.drawable.add_fav_empty_icon_top_rated)
                    }

                    Glide.with(itemView.context)
                        .load("https://image.tmdb.org/t/p/w500${popularMovie.posterPath}")
                        .centerCrop()
                        .into(movieImage)
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieViewHolder {
        return if (viewType == ViewType.LIST.ordinal) {
            val itemView =
                MovieItemPopularBinding.inflate(LayoutInflater.from(parent.context), parent, false)
            MovieViewHolder(itemView)
        } else {
            val itemView =
                MovieItemPopularGridViewBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false,
                )
            MovieViewHolder(itemView)
        }
    }

    override fun getItemViewType(position: Int): Int {
        return viewType.ordinal
    }

    override fun getItemCount() = movieList.size

    override fun onBindViewHolder(holder: MovieViewHolder, position: Int) {
        holder.bind(movieList[position])
        holder.itemView.setOnClickListener {
            listener.onItemClick(movieList[position])
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    fun updateList(list: List<MovieResult>?) {
        movieList.addAll(list ?: emptyList())
        notifyDataSetChanged()
    }

    interface OnItemClickListener {
        fun onItemClick(movie: MovieResult)
    }

    fun updateVoteStyle(vote: Double): String {
        return if (vote >= 7.0) {
            "green"
        } else if (vote >= 5.0) {
            "yellow"
        } else {
            "red"
        }
    }

    interface OnFavoriteStatusChangeListener {
        fun onFavoriteStatusChanged(movie: MovieResult)
    }

    fun updateFavoriteStatus(favoriteMovies: List<FavoriteMovie>) {
        movieList.forEach { movie ->
            val isFav = favoriteMovies.any { it.id == movie.id }
            movie.isFavorite = isFav
        }
        notifyDataSetChanged()
    }
}
