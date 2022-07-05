package com.example.testrepository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.torang_core.data.dao.MyReviewDao
import com.example.torang_core.data.data.ReviewAndImage
import com.example.torang_core.data.uistate.MyReviewItemUiState
import com.example.torang_core.repository.MyReviewsRepository
import com.example.torang_core.util.Logger
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TestMyReviewsRepository @Inject constructor(
    val myReviewDao: MyReviewDao
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
        return MutableStateFlow(ArrayList<MyReviewItemUiState>().apply {
            add(MyReviewItemUiState(1, "2", 3.0f, "4", "5"))
            add(MyReviewItemUiState(1, "2", 3.0f, "4", "5"))
            add(MyReviewItemUiState(1, "2", 3.0f, "4", "5"))
            add(MyReviewItemUiState(1, "2", 3.0f, "4", "5"))
        })
    }
}