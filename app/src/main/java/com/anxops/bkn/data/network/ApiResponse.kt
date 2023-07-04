/*
 * Copyright 2023 Angel Piñeiro
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.anxops.bkn.data.network

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