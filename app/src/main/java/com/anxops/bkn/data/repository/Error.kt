package com.anxops.bkn.data.repository

import com.anxops.bkn.data.network.ApiException
import java.io.IOException

data class AppError(
    val type: ErrorType,
    val message: String? = null,
    val throwable: Throwable? = null
)

sealed interface ErrorType {
    object Authorization : ErrorType
    object Backend : ErrorType
    object Network : ErrorType
    object Unexpected : ErrorType
}

fun Throwable?.toAppError(msg: String? = null): AppError {
    val type = when (this) {
        is ApiException.Authorization -> ErrorType.Authorization
        is ApiException.Server -> ErrorType.Backend
        is ApiException -> ErrorType.Network
        is IOException -> ErrorType.Network
        else -> ErrorType.Unexpected
    }
    return AppError(
        type,
        throwable = this,
        message = msg
    )
}
