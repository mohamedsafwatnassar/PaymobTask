package com.paymob.moviesapp.ui

import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.fragment.app.viewModels
import com.paymob.moviesapp.BuildConfig
import com.paymob.moviesapp.R
import com.paymob.moviesapp.base.BaseFragment
import com.paymob.moviesapp.bestMovies.viewmodel.MoviesViewModel
import com.paymob.moviesapp.databinding.FragmentMovieDetailsBinding
import com.paymob.moviesapp.model.MovieItem
import com.paymob.moviesapp.utils.extentions.getParcelableCompat
import com.paymob.moviesapp.utils.extentions.loadImage
import com.paymob.moviesapp.utils.extentions.onDebouncedListener
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MovieDetailsFragment : BaseFragment() {

    private var _binding: FragmentMovieDetailsBinding? = null
    private val binding get() = _binding!!

    private val vm: MoviesViewModel by viewModels()

    private var movieItem: MovieItem? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentMovieDetailsBinding.inflate(inflater, container, false)
        return binding.root
    }

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        movieItem = requireArguments().getParcelableCompat("Movie_Item", MovieItem::class.java)

        setBtnListeners()
        initViews()
    }

    private fun setBtnListeners() {
        binding.favorite.onDebouncedListener {
            if (movieItem?.isFavorite == true) {
                vm.removeMovieToFavorite(movieItem?.id!!)
                binding.favorite.setImageResource(R.drawable.ic_not_favorite)
            } else {
                vm.addMovieToFavorite(movieItem?.id!!)
                binding.favorite.setImageResource(R.drawable.ic_favorite)
            }
        }
    }

    private fun initViews() {
        binding.title.text = movieItem?.title.toString()

        binding.favorite.setImageResource(
            if (movieItem?.isFavorite == true) R.drawable.ic_favorite else R.drawable.ic_not_favorite
        )

        binding.language.text = getString(R.string.language, movieItem?.originalLanguage.toString())

        binding.txtVoteAverage.text = movieItem?.voteAverage.toString()
        binding.txtCountAverage.text = movieItem?.voteCount.toString()

        binding.txtDescription.text = movieItem?.overview.toString()

        binding.imgMovie.loadImage(
            BuildConfig.BASE_POSTER_URL + movieItem?.posterPath
        )
    }
}
