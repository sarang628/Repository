package com.example.torangrepository

import android.content.Context
import androidx.lifecycle.LiveData
import com.example.torang_core.data.dao.MyReviewDao
import com.example.torang_core.data.dao.UserDao
import com.example.torang_core.data.data.ReviewAndImage
import com.example.torang_core.data.model.FeedData
import com.example.torang_core.data.model.ReviewImage
import com.example.torang_core.util.Logger
import com.example.torang_core.repository.MyReviewsRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MyReviewsRepositoryImpl @Inject constructor(
    @ApplicationContext private val context: Context,
    private val restaurantService: RestaurantService,
    private val userDao: UserDao,
    private val myReviewDao: MyReviewDao
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

    override fun getMyReviews1(restaurantId: Int): LiveData<List<ReviewAndImage>> {

        Logger.d("${userId()}, $restaurantId")
        return myReviewDao.getMyReviews(userId(), restaurantId)
    }
}

@Module
@InstallIn(SingletonComponent::class)
abstract class MyReviewsRepositoryProvider() {
    @Binds
    abstract fun provideMyReviewsRepository(myReviewsRepositoryImpl: MyReviewsRepositoryImpl): MyReviewsRepository
}