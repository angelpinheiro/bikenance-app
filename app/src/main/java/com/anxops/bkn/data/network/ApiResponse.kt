package com.anxops.bkn.data.network

import io.ktor.client.features.ClientRequestException
import io.ktor.client.features.HttpRequestTimeoutException
import io.ktor.client.features.ServerResponseException
import io.ktor.http.HttpStatusCode
import java.io.IOException
import java.util.concurrent.CancellationException

sealed class ApiResponse<T> {
    class Success<T>(val data: T) : ApiResponse<T>()
    class Error<T>(val exception: ApiException? = null) : ApiResponse<T>()
}

sealed class ApiException(override val message: String? = null, override val cause: Throwable?) : Throwable(message, cause) {

    data class Unknown(
        override val message: String? = "Unknown api exception",
        override val cause: Throwable? = null
    ) : ApiException(message, cause)

    class NotFound(
        override val message: String? = null,
        override val cause: Throwable? = null
    ) : ApiException(message, cause)

    class Authorization(
        override val message: String? = null,
        override val cause: Throwable? = null
    ) : ApiException(message, cause)

    class Connection(
        override val message: String? = null,
        override val cause: Throwable? = null
    ) : ApiException(message, cause)

    class Server(
        override val message: String? = null,
        override val cause: Throwable? = null
    ) : ApiException(message, cause)
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
            is ApiResponse.Error -> throw this.exception ?: ApiException.Unknown()
        }
    } catch (e: Throwable) {
        throw e
    }
}

suspend fun <T> apiResponse(apiToBeCalled: suspend () -> T): ApiResponse<T> {
    return try {
        val response: T = apiToBeCalled()
        ApiResponse.Success(response)
    } catch (e: ClientRequestException) {
        // error http response
        when (e.response.status) {
            HttpStatusCode.NotFound -> ApiResponse.Error(ApiException.NotFound(cause = e))
            HttpStatusCode.Unauthorized -> ApiResponse.Error(ApiException.Authorization(cause = e))
            else -> ApiResponse.Error(ApiException.Unknown(cause = e))
        }
    } catch (e: ServerResponseException) {
        ApiResponse.Error(ApiException.Server(cause = e))
    } catch (e: HttpRequestTimeoutException) {
        // Request timeout
        ApiResponse.Error(ApiException.Connection(cause = e))
    } catch (e: IOException) {
        // No internet, cant reach server,...
        ApiResponse.Error(ApiException.Connection(cause = e))
    } catch (e: Exception) {
        if (e is CancellationException) {
            throw e
        }
        // Unknown error
        ApiResponse.Error(ApiException.Unknown(cause = e))
    }
}
