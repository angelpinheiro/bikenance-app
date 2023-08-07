package com.anxops.bkn.data.repository

import com.anxops.bkn.data.network.ApiException
import com.anxops.bkn.data.network.ApiResponse
import com.anxops.bkn.util.LoadableState
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.withContext
import timber.log.Timber
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
    data class Error(val exception: Throwable? = null) : Result<Nothing> {
        fun toAppError(msg: String? = null) = exception.toAppError(msg)
    }
}

fun <T> Result<T>.isNullOrError(): Boolean {
    return this is Error || this is Result.Success && data == null
}

/**
 * Extension function that either applies the provided block function to the data if the result is Success,
 * or throws an exception with the stored error if the result is Error.
 */
inline fun <T, R> Result<T>.successOrException(block: (T) -> R): R {
    try {
        return when (this) {
            is Result.Success -> block(this.data)
            is Result.Error -> throw this.exception ?: Exception("Expected success but got error")
        }
    } catch (e: Throwable) {
        throw e
    }
}

/**
 * Extension function that returns the result data if the result is Success,
 * or throws an exception with the stored error if the result is Error.
 */
inline fun <T> Result<T>.data(): T {
    try {
        return when (this) {
            is Result.Success -> this.data
            is Result.Error -> throw this.exception ?: Exception("Expected data but got error")
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
        is Result.Error -> {
            Timber.e(this.exception ?: RuntimeException("Unexpected error"))
        }
    }
    return this
}

/**
 * Extension function that executes the provided block function if the result is Success and data is not null.
 */
inline fun <T> Result<T>.onSuccessNotNull(block: (T & Any) -> Unit): Result<T> {
    when (this) {
        is Result.Success -> if (this.data != null) block(this.data)
        is Result.Error -> {
            Timber.e(this.exception ?: RuntimeException("Unexpected error"))
        }
    }
    return this
}

/**
 * Extension function that executes the provided block function if the result is Success but data is null
 */
inline fun <T> Result<T>.onSuccessWithNull(block: () -> Unit): Result<T> {
    when (this) {
        is Result.Success -> if (this.data == null) block()
        is Result.Error -> {
            Timber.e(this.exception ?: RuntimeException("Unexpected error"))
        }
    }
    return this
}

/**
 * Extension function that executes the provided block function if the result is Error, passing the stored exception.
 */
inline fun <T> Result<T>.onError(block: (AppError) -> Unit): Result<T> {
    when (this) {
        is Result.Error -> block(this.exception.toAppError())
        else -> {}
    }
    return this
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
    return this.map<T, Result<T>> {
        Result.Success(it)
    }
//        .onStart { emit(Result.Loading) }
        .catch {
            Timber.e(it)
            emit(Result.Error(it))
        }
}


fun <T> Flow<T>.asLoadableState(): Flow<LoadableState<T>> {
    return this.map<T, LoadableState<T>> {
        LoadableState.Success(it)
    }.onStart { emit(LoadableState.Loading) }.catch {
        Timber.e(it)
        emit(LoadableState.Error)
    }
}

/**
 * Function that runs the provided block within a try-catch block. Returns a Result.Success if the
 * block executes successfully, or a Result.Error with the caught exception.
 */
suspend inline fun <T> runCatchingResult(block: suspend () -> T): Result<T> {
    return try {
        val result = block()
        Result.Success(result)
    }
    catch (e: Throwable) {
        Timber.e(e)
        Result.Error(e)
    }
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
            try {
                val result = block()
                Result.Success(result)
            }
            catch (e: Throwable) {
                Timber.e(e)
                Result.Error(e)
            }
        }
    }
}
