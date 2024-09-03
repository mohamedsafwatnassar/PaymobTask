package com.paymob.moviesapp.bestMovies.repo

import com.paymob.moviesapp.base.BaseRepository
import com.paymob.moviesapp.localDatabase.FavoriteDao
import com.paymob.moviesapp.model.FavoriteModel
import com.paymob.moviesapp.model.MoviesResponse
import com.paymob.moviesapp.network.ApiHandler
import com.paymob.moviesapp.network.ApiService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class MoviesRepo @Inject constructor(
    private val apiService: ApiService,
    private val favoriteDao: FavoriteDao
) : BaseRepository() {

    suspend fun getBestMoviesList(): Flow<ApiHandler<MoviesResponse>> {
        return performApiCall(apiCall = {
            apiService.getBestMoviesList()
        })
    }

    // Get all favorites with error handling
    fun getAllFavoriteIds(): Flow<ApiHandler<List<FavoriteModel>>> = flow {
        emit(ApiHandler.ShowLoading)
        try {
            val favorites = favoriteDao.getAllFavorites()
            emit(ApiHandler.Success(favorites))
        } catch (e: Exception) {
            emit(ApiHandler.Error(e.localizedMessage ?: "An error occurred", 0))
        } finally {
            emit(ApiHandler.HideLoading)
        }
    }.flowOn(Dispatchers.IO)

    // Add movie to favorites with error handling
    fun addFavoriteMovieId(movieId: Int): Flow<ApiHandler<Int>> = flow {
        val favoriteModel = FavoriteModel(movieId= movieId)
        emit(ApiHandler.ShowLoading)
        try {
            favoriteDao.insertFavorite(favoriteModel)
            emit(ApiHandler.Success(movieId))
        } catch (e: Exception) {
            emit(ApiHandler.Error(e.localizedMessage ?: "An error occurred", 0))
        } finally {
            emit(ApiHandler.HideLoading)
        }
    }.flowOn(Dispatchers.IO)

    // Remove movie from favorites with error handling
    fun removeFavoriteMovieById(movieId: Int): Flow<ApiHandler<Int>> = flow {
        emit(ApiHandler.ShowLoading)
        try {
            favoriteDao.deleteFavorite(movieId)
            emit(ApiHandler.Success(movieId))
        } catch (e: Exception) {
            emit(ApiHandler.Error(e.localizedMessage ?: "An error occurred", 0))
        } finally {
            emit(ApiHandler.HideLoading)
        }
    }.flowOn(Dispatchers.IO)
}