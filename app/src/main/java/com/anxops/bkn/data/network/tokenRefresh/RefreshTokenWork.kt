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

package com.anxops.bkn.data.network.tokenRefresh

import android.content.Context
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.anxops.bkn.data.network.Api
import com.anxops.bkn.data.preferences.BknDataStore
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject


@HiltWorker
class RefreshTokenWork @AssistedInject constructor(
    @Assisted appContext: Context,
    @Assisted workerParams: WorkerParameters,
    val dataStore: BknDataStore,
    val api: Api
) : CoroutineWorker(appContext, workerParams) {
    override suspend fun doWork(): Result {
        return if (RefreshTokenHelper.performRefresh(dataStore, api))
            Result.success()
        else
            Result.failure()
    }
}