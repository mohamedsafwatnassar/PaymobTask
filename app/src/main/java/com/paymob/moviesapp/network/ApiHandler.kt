package com.paymob.moviesapp.network

sealed class ApiHandler<out T> {
    object ShowLoading : ApiHandler<Nothing>()
    object HideLoading : ApiHandler<Nothing>()
    data class Success<out T>(val data: T) : ApiHandler<T>()
    data class Error(val message: String, val errorCode: Int = 0) : ApiHandler<Nothing>()
    data class ServerError(val message: String) : ApiHandler<Nothing>()
}