package com.anxops.bkn.network

import coil.network.HttpException
import java.io.IOException

sealed class ApiResponse<T>(
    val message: String? = null
) {
    class Success<T>(val data: T) : ApiResponse<T>()
    class Error<T>(errorMessage: String) : ApiResponse<T>(message = errorMessage)
}

suspend fun <T> safeApiCall(apiToBeCalled: suspend () -> T): ApiResponse<T> {
    return try {
        val response: T = apiToBeCalled()
        ApiResponse.Success(response)
    } catch (e: HttpException) {
        // Returning HttpException's message
        // wrapped in Resource.Error
        ApiResponse.Error<T>(errorMessage = e.message ?: "Something went wrong")
    } catch (e: IOException) {
        // Returning no internet message
        // wrapped in Resource.Error
        ApiResponse.Error<T>(e.message ?: "Please check your network connection")
    } catch (e: Exception) {
        // Returning 'Something went wrong' in case
        // of unknown error wrapped in Resource.Error
        ApiResponse.Error<T>(errorMessage = e.message ?: "Something went wrong")
    }
}