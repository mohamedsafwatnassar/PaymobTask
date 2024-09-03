package com.paymob.moviesapp.bestMovies.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.paymob.moviesapp.BuildConfig
import com.paymob.moviesapp.R
import com.paymob.moviesapp.databinding.ItemMovieBinding
import com.paymob.moviesapp.model.MovieItem
import com.paymob.moviesapp.utils.extentions.onDebouncedListener
import java.util.Locale

class MoviesAdapter(
    private val onMovieCallback: (movie: MovieItem) -> Unit,
    private val onFavouriteCallback: (movieId: Int?, isFavorite: Boolean, position: Int) -> Unit,
) : RecyclerView.Adapter<MoviesAdapter.MoviesVieHolder>(), Filterable {

    private var moviesList: List<MovieItem> = ArrayList()
    private var filterMoviesList: List<MovieItem> = ArrayList()

    inner class MoviesVieHolder(private val binding: ItemMovieBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bindData(movie: MovieItem) {

            binding.txtTitle.text = movie.title
            binding.txtVoteAverage.text = movie.voteAverage.toString()
            binding.txtReleaseDate.text = movie.releaseDate.toString()

            val moviePosterUrl = BuildConfig.BASE_POSTER_URL + movie.posterPath

            Glide.with(binding.root.context)
                .load(moviePosterUrl)
                .into(binding.imgMovie)

            binding.btnFavorite.setImageResource(
                if (movie.isFavorite) R.drawable.ic_favorite else R.drawable.ic_not_favorite
            )

            binding.root.onDebouncedListener {
                onMovieCallback.invoke(movie)
            }

            binding.btnFavorite.onDebouncedListener {
                onFavouriteCallback.invoke(movie.id, movie.isFavorite, adapterPosition)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MoviesVieHolder {
        val binding = ItemMovieBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MoviesVieHolder(binding)
    }

    override fun onBindViewHolder(holder: MoviesVieHolder, position: Int) {
        val item = moviesList[position]

        // bind view
        holder.bindData(item)
    }

    override fun getItemCount(): Int {
        return moviesList.size
    }

    fun submitData(list: List<MovieItem>) {
        moviesList = list
        notifyDataSetChanged()
    }

    fun updateFavoriteStatusFromMovie(position: Int) {
        moviesList[position].isFavorite = !moviesList[position].isFavorite
        notifyItemChanged(position)
    }

    override fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(constraint: CharSequence?): FilterResults {
                val charString =
                    constraint?.toString()?.lowercase(Locale.getDefault()).toString().trim()
                filterMoviesList = if (charString.isEmpty()) moviesList else {
                    moviesList.filter { it.title!!.lowercase().contains(charString) }
                }
                return FilterResults().apply { values = filterMoviesList }
            }

            override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
                filterMoviesList = if (results?.values == null)
                    ArrayList()
                else
                    results.values as ArrayList<MovieItem>
                notifyDataSetChanged()
            }
        }
    }
}