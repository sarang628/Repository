/*
 * Copyright 2018 Google LLC
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.example.testrepository

import android.content.Context
import android.util.Log
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.example.torang_core.data.AppDatabase
import com.example.torang_core.data.model.FeedData
import com.example.torang_core.data.model.ReviewImage
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.google.gson.stream.JsonReader
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class SeedDatabaseWorker(
    context: Context,
    workerParams: WorkerParameters
) : CoroutineWorker(context, workerParams) {
    override suspend fun doWork(): Result = withContext(Dispatchers.IO) {
        try {

            Log.d(TAG, "doWork")
            val filename = inputData.getString(KEY_FILENAME)
            val filename1 = inputData.getString(KEY_FILENAME1)
            if (filename1 != null) {
                applicationContext.assets.open(filename1).use { inputStream ->
                    JsonReader(inputStream.reader()).use { jsonReader ->
                        val imageType = object : TypeToken<List<ReviewImage>>() {}.type
                        val imageList: List<ReviewImage> = Gson().fromJson(jsonReader, imageType)

                        val database = AppDatabase.getInstance(applicationContext)
                        database.pictureDao().insertAll(imageList)
                        Result.success()
                    }
                }
            }

            if (filename != null) {
                applicationContext.assets.open(filename).use { inputStream ->
                    JsonReader(inputStream.reader()).use { jsonReader ->
                        val plantType = object : TypeToken<List<FeedData>>() {}.type
                        val plantList: List<FeedData> = Gson().fromJson(jsonReader, plantType)

                        val database = AppDatabase.getInstance(applicationContext)
                        database.feedDao().insertAll(plantList)
                        Result.success()
                    }
                }
            } else {
                Log.e(TAG, "Error seeding database - no valid filename")
                Result.failure()
            }
        } catch (ex: Exception) {
            Log.e(TAG, "Error seeding database", ex)
            Result.failure()
        }
    }

    companion object {
        private const val TAG = "__torang"
        const val KEY_FILENAME = "PLANT_DATA_FILENAME"
        const val KEY_FILENAME1 = "REVIEW_IMAGE_DATA_FILENAME"
    }
}
