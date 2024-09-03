package com.paymob.moviesapp.ui.movies.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.paymob.moviesapp.model.MovieItem
import com.paymob.moviesapp.network.ApiHandler
import com.paymob.moviesapp.ui.movies.repo.MoviesRepo
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MoviesViewModel @Inject constructor(
    private val repo: MoviesRepo
) : ViewModel() {

    private val _allMovies = MutableLiveData<ApiHandler<List<MovieItem>>>()
    val allMovies: LiveData<ApiHandler<List<MovieItem>>>
        get() = _allMovies

    private val _favoriteStatus = MutableLiveData<ApiHandler<Int>>()
    val favoriteStatus: LiveData<ApiHandler<Int>>
        get() = _favoriteStatus

    fun getMoviesList() {
        viewModelScope.launch {
            repo.getMoviesList().collect {
                when (it) {
                    is ApiHandler.Success -> {
                        val data = it.data
                        getFavoriteIdsFromLocal(data.movies ?: arrayListOf())
                    }

                    is ApiHandler.Error -> {
                        _allMovies.postValue(ApiHandler.Error(""))
                    }

                    is ApiHandler.HideLoading -> {
                        // _allMovies.postValue(ApiHandler.HideLoading)
                    }

                    is ApiHandler.ShowLoading -> {
                        // _allMovies.postValue(ApiHandler.ShowLoading)
                    }

                    is ApiHandler.ServerError -> {
                        _allMovies.postValue(ApiHandler.ServerError(""))
                    }
                }
            }
        }
    }

    private fun getFavoriteIdsFromLocal(allMovies: List<MovieItem>) {
        viewModelScope.launch {
            repo.getAllFavoriteIds().collect {
                when (it) {
                    is ApiHandler.Success -> {
                        val favoriteIds = it.data
                        allMovies.forEach { movie ->
                            if (favoriteIds.any { favorite -> movie.id == favorite.movieId }) {
                                movie.isFavorite = true
                            }
                        }
                        _allMovies.postValue(ApiHandler.Success(allMovies))
                    }

                    is ApiHandler.Error -> {
                        _allMovies.postValue(ApiHandler.Success(allMovies))
                    }

                    is ApiHandler.HideLoading -> {
                        // _allMovies.postValue(ApiHandler.HideLoading)
                    }

                    is ApiHandler.ShowLoading -> {
                        // _allMovies.postValue(ApiHandler.ShowLoading)
                    }

                    is ApiHandler.ServerError -> {
                        _allMovies.postValue(ApiHandler.Success(allMovies))
                    }
                }
            }
        }
    }

    // Add favorite status
    fun addMovieToFavorite(movieId: Int) {
        viewModelScope.launch {
            repo.addFavoriteMovieId(movieId).collect {
                when (it) {
                    is ApiHandler.Success -> {
                        val data = it.data
                        _favoriteStatus.postValue(ApiHandler.Success(data))
                    }

                    is ApiHandler.Error -> {
                        _favoriteStatus.postValue(ApiHandler.Error(""))
                    }

                    is ApiHandler.HideLoading -> {
                        // _favoriteStatus.postValue(ApiHandler.HideLoading)
                    }

                    is ApiHandler.ShowLoading -> {
                        // _favoriteStatus.postValue(ApiHandler.ShowLoading)
                    }

                    is ApiHandler.ServerError -> {
                        _favoriteStatus.postValue(ApiHandler.ServerError(""))
                    }
                }
            }
        }
    }

    fun removeMovieToFavorite(movieId: Int) {
        viewModelScope.launch {
            repo.removeFavoriteMovieById(movieId).collect {
                when (it) {
                    is ApiHandler.Success -> {
                        val data = it.data
                        _favoriteStatus.postValue(ApiHandler.Success(data))
                    }

                    is ApiHandler.Error -> {
                        _favoriteStatus.postValue(ApiHandler.Error(""))
                    }

                    is ApiHandler.HideLoading -> {
                        // _favoriteStatus.postValue(ApiHandler.HideLoading)
                    }

                    is ApiHandler.ShowLoading -> {
                        // _favoriteStatus.postValue(ApiHandler.ShowLoading)
                    }

                    is ApiHandler.ServerError -> {
                        _favoriteStatus.postValue(ApiHandler.ServerError(""))
                    }
                }
            }
        }
    }
}
