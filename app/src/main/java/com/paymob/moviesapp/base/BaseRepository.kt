package com.paymob.moviesapp.base

import android.app.Application
import android.util.Log
import com.paymob.moviesapp.R
import com.paymob.moviesapp.network.ApiHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import okhttp3.ResponseBody
import org.json.JSONObject
import retrofit2.Response
import javax.inject.Inject

open class BaseRepository @Inject constructor() {

    @Inject
    lateinit var appContext: Application
    inline fun <T> performApiCall(
        crossinline apiCall: suspend () -> Response<T>,
        parseErrorResponse: Boolean = true // pending variable for handle error on all Api has same error response
    ): Flow<ApiHandler<T>> =
        flow {
            emit(ApiHandler.ShowLoading)
            try {
                val response = apiCall()
                if (response.isSuccessful) {
                    emit(ApiHandler.Success(response.body()))
                } else {
                    when (response.code()) {
                        500 -> {
                            emit(ApiHandler.ServerError(appContext.getString(R.string.server_error)))
                        }

                        else -> {
                            if (parseErrorResponse) {
                                emit(
                                    ApiHandler.Error(
                                        parseErrorDefaultResponse(response.errorBody()),
                                        response.code()
                                    )
                                )
                            } else emit(
                                ApiHandler.Error(
                                    parseErrorCustomResponse(
                                        response.errorBody()?.string().toString()
                                    ), response.code()
                                )
                            )
                        }
                    }
                }
                emit(ApiHandler.HideLoading)
            } catch (e: Exception) {
                Log.d("MyDebugData", "BaseRepository : performApiCall :  " + e.localizedMessage);
                emit(
                    ApiHandler.ServerError(
                        e.localizedMessage ?: appContext.getString(R.string.server_error)
                    )
                )
                emit(ApiHandler.HideLoading)
            }
        }.flowOn(Dispatchers.IO) as Flow<ApiHandler<T>>


    fun parseErrorCustomResponse(errorBody: String): String {
        return errorBody
    }

    fun parseErrorDefaultResponse(errorBody: ResponseBody?): String {
        var errorMessage = ""
        try {
            val jsonObject = JSONObject(errorBody!!.string())
            val errorsArray = jsonObject.getJSONArray("Error")
            errorMessage = errorsArray.getString(0)
        } catch (e: Exception) {
            return errorMessage
        }
        return errorMessage
    }
}