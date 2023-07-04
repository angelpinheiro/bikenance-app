package com.anxops.bkn.util

sealed class RepositoryResult<T>(
    val message: String? = null
) {
    class Success<T>(val data: T) : RepositoryResult<T>()
    class Error<T>(errorMessage: String) : RepositoryResult<T>(message = errorMessage)
}