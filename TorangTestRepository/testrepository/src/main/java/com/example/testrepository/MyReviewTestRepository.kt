package com.example.testrepository

import android.content.Context
import androidx.lifecycle.LiveData
import com.example.torang_core.data.AppDatabase
import com.example.torang_core.data.data.ReviewAndImage
import com.example.torang_core.data.model.FeedData
import com.example.torang_core.data.model.Review
import com.example.torang_core.data.model.ReviewImage
import com.example.torang_core.repository.MyReviewsRepository

class MyReviewTestRepository(val context: Context) : MyReviewsRepository {

    protected val appDatabase = AppDatabase.getInstance(context)
    val userDao = appDatabase.userDao()

    override suspend fun getMyReviews(restaurantId: Int): List<ReviewAndImage> {

        val list = java.util.ArrayList<Review>()

        list.add(TorangDummy.getReview(context = context))

        val list1 = ArrayList<ReviewAndImage>()
        for (review in list) {
            list1.add(ReviewAndImage.parse(review))
        }

        //피드 추가하기
        val feeds = ArrayList<FeedData>()
        val images = ArrayList<ReviewImage>()
        for (reviewAndInage in list1) {
            val rv = reviewAndInage
            FeedData.parse(rv)?.let {
                feeds.add(it)
            }
            reviewAndInage.images?.let {
                images.addAll(it)
            }

        }
        userDao.insertFeed(feedData = feeds)
        userDao.insertPictures(images)

        return ArrayList()
    }

    override fun getMyReviews1(restaurantId: Int): LiveData<List<ReviewAndImage>> {
        return appDatabase.myReviewDao().getMyReviews(4, 1)
    }
}