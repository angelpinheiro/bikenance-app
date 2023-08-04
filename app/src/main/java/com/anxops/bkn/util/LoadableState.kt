package com.anxops.bkn.util

import androidx.compose.runtime.Immutable
import com.anxops.bkn.data.repository.Result

@Immutable
sealed interface LoadableState<out T> {
    data class Success<T>(val value: T) : LoadableState<T>
    object Error : LoadableState<Nothing>
    object Loading : LoadableState<Nothing>
}

inline fun <T> Result<T>.toLoadableState(): LoadableState<T> {
    return when (this) {
        is Result.Success -> LoadableState.Success(this.data)
        is Result.Error -> LoadableState.Error
    }
}
