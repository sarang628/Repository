package com.example.testrepository

import androidx.lifecycle.LiveData
import com.example.torang_core.data.dao.MyReviewDao
import com.example.torang_core.data.data.ReviewAndImage
import com.example.torang_core.data.uistate.MyReviewItemUiState
import com.example.torang_core.repository.MyReviewsRepository
import com.example.torang_core.util.Logger
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TestMyReviewsRepository @Inject constructor(
    private val myReviewDao: MyReviewDao
) : MyReviewsRepository {
    override suspend fun getMyReviews(restaurantId: Int): List<ReviewAndImage> {
        Logger.d("!!!!!")
        return ArrayList<ReviewAndImage>().apply {
            add(ReviewAndImage())
            add(ReviewAndImage())
            add(ReviewAndImage())
            add(ReviewAndImage())
            add(ReviewAndImage())
        }
    }

    override fun getMyReviews1(restaurantId: Int): LiveData<List<ReviewAndImage>> {
        return myReviewDao.getMyReviews(0, 470)
    }

    override fun getMyReviews2(restaurantId: Int): Flow<List<MyReviewItemUiState>> {
        return myReviewDao.getMyReviews2(0,0)
    }
}