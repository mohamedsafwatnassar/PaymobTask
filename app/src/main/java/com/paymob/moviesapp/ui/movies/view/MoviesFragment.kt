package com.paymob.moviesapp.ui.movies.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.paymob.moviesapp.R
import com.paymob.moviesapp.base.BaseFragment
import com.paymob.moviesapp.ui.movies.adapter.MoviesAdapter
import com.paymob.moviesapp.ui.movies.viewmodel.MoviesViewModel
import com.paymob.moviesapp.databinding.FragmentMoviesBinding
import com.paymob.moviesapp.network.ApiHandler
import com.paymob.moviesapp.utils.NetworkConnectivity
import com.paymob.moviesapp.utils.extentions.customNavigate
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MoviesFragment : BaseFragment(), NetworkConnectivity {

    private var _binding: FragmentMoviesBinding? = null
    private val binding get() = _binding!!

    private val vm: MoviesViewModel by viewModels()
    private lateinit var moviesAdapter: MoviesAdapter

    private var selectedPosition: Int = -1

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentMoviesBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // call api to fetch movies list()
        vm.getMoviesList()

        initRecyclerView()
        subscribeData()
    }

    private fun initRecyclerView() {
        binding.rvBestMovies.apply {
            moviesAdapter = MoviesAdapter(
                onMovieCallback = { movie ->
                    val bundle = Bundle()
                    bundle.putParcelable("Movie_Item", movie)
                    findNavController().customNavigate(R.id.movieDetailsFragment, data = bundle)
                },
                onFavouriteCallback = { movieId, isFavorite, position ->
                    selectedPosition = position
                    if (isFavorite) {
                        vm.removeMovieToFavorite(movieId!!)
                    } else {
                        vm.addMovieToFavorite(movieId!!)
                    }
                },
            )
            adapter = moviesAdapter
        }
    }

    private fun subscribeData() {
        vm.allMovies.observe(viewLifecycleOwner) {
            when (it) {
                is ApiHandler.Success -> {
                    moviesAdapter.submitData(it.data)
                }

                is ApiHandler.Error -> {
                    showToast(it.message)
                }

                is ApiHandler.HideLoading -> hideLoading()
                is ApiHandler.ShowLoading -> showLoading()
                is ApiHandler.ServerError -> showToast(it.message)
            }
        }

        vm.favoriteStatus.observe(viewLifecycleOwner) {
            when (it) {
                is ApiHandler.Success -> {
                    moviesAdapter.updateFavoriteStatusFromMovie(selectedPosition)
                }

                is ApiHandler.Error -> {
                    showToast(it.message)
                }

                is ApiHandler.HideLoading -> hideLoading()
                is ApiHandler.ShowLoading -> showLoading()
                is ApiHandler.ServerError -> showToast(it.message)
            }
        }
    }

    override fun retryNetworkConnectionCallBack() {
        vm.getMoviesList()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}