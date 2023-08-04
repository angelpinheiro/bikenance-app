package com.anxops.bkn.data.repository

import com.anxops.bkn.data.network.ApiResponse
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import java.util.concurrent.CancellationException

/**
 * Sealed interface representing a result of an operation that can be either a Success with data
 * or an Error with an optional exception.
 */
sealed interface Result<out T> {
    /**
     * Represents a successful result with associated data.
     */
    data class Success<T>(val data: T) : Result<T>

    /**
     * Represents an error result with an optional exception.
     */
    data class Error(val exception: Throwable? = null) : Result<Nothing>
}

/**
 * Extension function that either applies the provided block function to the data if the result is Success,
 * or throws an exception with the stored error if the result is Error.
 */
inline fun <T, R> Result<T>.successOrException(block: (T) -> R): R {
    try {
        return when (this) {
            is Result.Success -> block(this.data)
            is Result.Error -> throw Exception("Expected success but got error", this.exception)
        }
    } catch (e: Throwable) {
        throw e
    }
}

/**
 * Extension function that executes the provided block function if the result is Success.
 */
inline fun <T> Result<T>.onSuccess(block: (T) -> Unit): Result<T> {
    when (this) {
        is Result.Success -> block(this.data)
        else -> {}
    }
    return this
}

/**
 * Extension function that executes the provided block function if the result is Success and data is not null.
 */
inline fun <T> Result<T>.onSuccessNotNull(block: (T & Any) -> Unit): Result<T> {
    when (this) {
        is Result.Success -> if (this.data != null) block(this.data)
        else -> {}
    }
    return this
}

/**
 * Extension function that executes the provided block function if the result is Success but data is null
 */
inline fun <T> Result<T>.onSuccessWithNull(block: () -> Unit): Result<T> {
    when (this) {
        is Result.Success -> if (this.data == null) block()
        else -> {}
    }
    return this
}

/**
 * Extension function that executes the provided block function if the result is Error, passing the stored exception.
 */
inline fun <T> Result<T>.onError(block: (Throwable?) -> Unit): Result<T> {
    when (this) {
        is Result.Error -> block(this.exception)
        else -> {}
    }
    return this
}

/**
 * Function that runs the provided block within a try-catch block. Returns a Result.Success if the
 * block executes successfully, or a Result.Error with the caught exception.
 */
suspend inline fun <T> runCatchingResult(block: suspend () -> T): Result<T> {
    return try {
        val result = block()
        Result.Success(result)
    } catch (e: Throwable) {
        Result.Error(e)
    }
}

/**
 * Extension function that converts an ApiResponse to a Result.
 * Success responses are converted to Result.Success, and Error responses to Result.Error.
 */
inline fun <T> ApiResponse<T>.asResult(): Result<T> {
    return try {
        when (this) {
            is ApiResponse.Success -> Result.Success(this.data)
            is ApiResponse.Error -> Result.Error(this.exception)
        }
    } catch (e: CancellationException) {
        throw e
    } catch (e: Throwable) {
        Result.Error(e)
    }
}

fun <T> Flow<T>.asResult(): Flow<Result<T>> {
    return this
        .filter { it != null }
        .map<T, Result<T>> {
            Result.Success(it)
        }
//        .onStart { emit(Result.Loading) }
        .catch { emit(Result.Error(it)) }
}

/**
 * Base repository class that provides a result function that executes a block within a specified coroutine dispatcher,
 * and returns the block result wrapped in a Result
 */
open class BaseRepository(private val dispatcher: CoroutineDispatcher = Dispatchers.IO) {

    /**
     * Executes the provided suspend block within the specified coroutine dispatcher,
     * and returns a Result.Success if successful, or a Result.Error with the caught exception.
     */
    suspend fun <T> result(block: suspend () -> T): Result<T> {
        return withContext(dispatcher) {
            runCatchingResult(block)
        }
    }
}
