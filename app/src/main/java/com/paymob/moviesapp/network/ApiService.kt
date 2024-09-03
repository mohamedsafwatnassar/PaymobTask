package com.paymob.moviesapp.network

import com.paymob.moviesapp.BuildConfig
import com.paymob.moviesapp.model.MoviesResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {

    @GET("discover/movie")
    suspend fun getMoviesList(
        @Query("api_key") apiKey: String = BuildConfig.api_key
    ): Response<MoviesResponse>

}