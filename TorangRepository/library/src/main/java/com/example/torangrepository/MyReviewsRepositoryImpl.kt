package com.example.torangrepository

import android.content.Context
import androidx.lifecycle.LiveData

import com.example.torang_core.data.dao.LoggedInUserDao
import com.example.torang_core.data.dao.MyReviewDao
import com.example.torang_core.data.dao.UserDao
import com.example.torang_core.data.data.MyReview
import com.example.torang_core.data.data.ReviewAndImage
import com.example.torang_core.data.model.FeedData
import com.example.torang_core.data.model.ReviewImage
import com.example.torang_core.datasource.local.MyReviewsLocalDataSource
import com.example.torang_core.datasource.local.MyReviewsRemoteDataSource
import com.example.torang_core.repository.MyReviewsRepository
import com.example.torang_core.util.Logger
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MyReviewsRepositoryImpl @Inject constructor(
    @ApplicationContext private val context: Context,
    @Deprecated("MyReviewsRemoteDataSource로 이동") private val restaurantService: RestaurantService,
    @Deprecated("MyReviewsLocalDataSource 이동") private val userDao: UserDao,
    @Deprecated("MyReviewsLocalDataSource 이동") private val myReviewDao: MyReviewDao,
    @Deprecated("MyReviewsLocalDataSource 이동") private val loggedInUserDao: LoggedInUserDao,
    private val myReviewsLocalDataSource: MyReviewsLocalDataSource,
    private val myReviewsRemoteDataSource: MyReviewsRemoteDataSource
) :
    MyReviewsRepository {

    override suspend fun getMyReviews(restaurantId: Int): List<ReviewAndImage> {
        val list = restaurantService.getMyReviews(HashMap<String, String>().apply {
            put("restaurant_id", restaurantId.toString())
            put("user_id", TorangPreference().getUserId(context).toString())
        })

        val list1 = ArrayList<ReviewAndImage>()
        for (review in list) {
            list1.add(ReviewAndImage.parse(review))
        }

        //피드 추가하기
        val feeds = ArrayList<FeedData>()
        val images = ArrayList<ReviewImage>()
        for (reviewAndInage in list1) {
            FeedData.parse(reviewAndInage)?.let {
                feeds.add(it)
            }

            reviewAndInage.images?.let {
                images.addAll(it)
            }

        }
        userDao.insertFeed(feedData = feeds)
        userDao.insertPictures(images)
        return list1
    }

    fun userId(): Int {
        return TorangPreference().getUserId(context)
    }

    suspend fun userId1() : Int?{
        return loggedInUserDao.getLoggedInUserData1()?.userId
    }

    override fun getMyReviews1(restaurantId: Int): LiveData<List<ReviewAndImage>> {
        Logger.d("${userId()}, $restaurantId")
        return myReviewDao.getMyReviews(userId(), restaurantId)
    }

    override suspend fun getMyReviews3(restaurantId: Int): List<MyReview> {
        val list = restaurantService.getMyReviews(HashMap<String, String>().apply {
            put("user_id", "" + userId1())
            put("restaurant_id", "" + restaurantId)
        })
        val list1 = ArrayList<MyReview>()
        for (review in list) {
            list1.add(review.toMyReview())
        }
        return list1
    }
}