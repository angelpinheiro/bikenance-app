package com.anxops.bkn.data.network

import java.io.IOException

sealed class ApiResponse<T> {
    class Success<T>(val data: T) : ApiResponse<T>()
    class Error<T>(val message: String, val exception: Throwable? = null) : ApiResponse<T>()
}

/**
 * Inline extension function used to handle the success case of the ApiResponse.
 * It takes a lambda function block that is executed if the ApiResponse is a Success.
 * If the response is not a Success, an exception is thrown with an error message.
 */
inline fun <T, R> ApiResponse<T>.successOrException(block: (T) -> R): R {
    try {
        return when (this) {
            is ApiResponse.Success -> block(this.data)
            is ApiResponse.Error -> throw Exception(
                "Expected success but got error [${this.message}]",
                this.exception
            )
        }
    } catch (e: Throwable) {
        throw e
    }
}

suspend fun <T> apiResponse(apiToBeCalled: suspend () -> T): ApiResponse<T> {
    return try {
        val response: T = apiToBeCalled()
        ApiResponse.Success(response)
    } catch (e: IOException) {
        // Returning no internet message
        // wrapped in Resource.Error
        ApiResponse.Error(e.message ?: "Please check your network connection", e)
    } catch (e: Exception) {
        // Returning 'Something went wrong' in case
        // of unknown error wrapped in Resource.Error
        ApiResponse.Error(message = e.message ?: "Something went wrong", e)
    }
}