package com.example.testrepository.repository.myreviews

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.torang_core.data.dao.MyReviewDao
import com.example.torang_core.data.data.MyReview
import com.example.torang_core.data.data.ReviewAndImage
import com.example.torang_core.repository.MyReviewsRepository
import com.example.torang_core.util.Logger
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MyReviewsRepositoryWithData @Inject constructor(
    private val myReviewDao: MyReviewDao
) : MyReviewsRepository {
    override suspend fun getMyReviews(restaurantId: Int): List<ReviewAndImage> {
        return ArrayList()
    }

    override fun getMyReviews1(restaurantId: Int): LiveData<List<ReviewAndImage>> {
        return MutableLiveData(ArrayList())
    }

    override suspend fun getMyReviews3(restaurantId: Int): List<MyReview> {
        delay(3000)
        return ArrayList()
    }
}