package com.example.torangrepository.di.myreviews

import com.example.torang_core.data.model.ReviewImage
import com.example.torang_core.datasource.local.MyReviewsLocalDataSource
import com.example.torang_core.datasource.local.MyReviewsRemoteDataSource
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MyReviewsLocalDataSourceImpl @Inject constructor() : MyReviewsLocalDataSource {
    override suspend fun getMyReviews(): List<ReviewImage> {
        TODO("Not yet implemented")
    }
}

@Singleton
class MyReviewsRemoteDataSourceImpl @Inject constructor() : MyReviewsRemoteDataSource {
    override suspend fun getMyReviews(): List<ReviewImage> {
        TODO("Not yet implemented")
    }
}