package com.example.torangrepository

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.switchMap
import com.example.torang_core.data.dao.CommentDao
import com.example.torang_core.data.dao.LoggedInUserDao
import com.example.torang_core.data.dao.RestaurantDao
import com.example.torang_core.data.dao.ReviewDao
import com.example.torang_core.data.data.ReviewAndImage
import com.example.torang_core.data.model.*
import com.example.torang_core.repository.TimeLineDetailRepository
import com.example.torang_core.util.Logger
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TimeLineDetailRepositoryImpl @Inject constructor(
    private val restaurantService: RestaurantService,
    private val commentDao: CommentDao,
    private val restaurantDao: RestaurantDao,
    private val reviewDao: ReviewDao,
    private val loggedInUserDao: LoggedInUserDao
) :
    TimeLineDetailRepository {

    override suspend fun getComments(reviewId: String): ArrayList<Comment> {
        val list = restaurantService.getComments(reviewId)
        commentDao.insertComments(CommentData.parse(list))
        return list
    }

    override fun getComments(reviewId: Int): LiveData<List<CommentData>> {
        Logger.d("getComments $reviewId")
        return commentDao.getComments(reviewId)
    }

    override fun getReview(): LiveData<FeedData> {
        TODO("Not yet implemented")
    }

    override fun getRestaurant(reviewId: Int): LiveData<RestaurantData> {
        return restaurantDao.getRestaurantByReviewId(reviewId)
    }

    override fun getFeed(reviewId: Int): LiveData<ReviewAndImage?> {
        return reviewDao.getFeedbyReviewId(reviewId)
    }

    override suspend fun addComment(reviewId: Int, value: String): Comment {

        val comment = restaurantService.addComment(Comment().apply {
            user = User().apply { userId = loggedInUserDao.getLoggedInUserData1()!!.userId!! }
            comment = value
            review_id = reviewId
        })

        commentDao.insertComment(CommentData.parse(comment))

        return comment
    }

    /** 로그인 여부 */
    override val isLogin: LiveData<Boolean> = loggedInUserDao.getLoggedInUserData().switchMap {
        if (it != null) {
            MutableLiveData(it.userId != 0)
        } else {
            MutableLiveData(false)
        }
    }
}

@Singleton
class TimeLineDetailRepositoryTestImpl @Inject constructor(
    @ApplicationContext private val context: Context,
    private val restaurantDao: RestaurantDao,
    private val restaurantService: RestaurantService,
    private val reviewDao: ReviewDao,
    private val loggedInUserDao: LoggedInUserDao
) :
    TimeLineDetailRepository {

    override suspend fun getComments(reviewId: String): ArrayList<Comment> {
        return restaurantService.getComments(reviewId)
    }

    override fun getComments(reviewId: Int): LiveData<List<CommentData>> {
        TODO("Not yet implemented")
    }

    override fun getReview(): LiveData<FeedData> {
        TODO("Not yet implemented")
    }

    override fun getRestaurant(reviewId: Int): LiveData<RestaurantData> {
        return restaurantDao.getRestaurantByReviewId(reviewId)
    }

    override fun getFeed(reviewId: Int): LiveData<ReviewAndImage?> {
        return reviewDao.getFeedbyReviewId(reviewId)
    }

    fun userId(): Int {
        return if (context != null)
            TorangPreference().getUserId(context!!)
        else -1
    }

    override suspend fun addComment(reviewId: Int, value: String): Comment {
        return restaurantService.addComment(Comment().apply {
            user = User().apply { userId = userId() }
            comment = value
            review_id = reviewId
        })
    }

    /** 로그인 여부 */
    override val isLogin: LiveData<Boolean> = loggedInUserDao.getLoggedInUserData().switchMap {
        if (it != null) {
            MutableLiveData(it.userId != 0)
        } else {
            MutableLiveData(false)
        }
    }
}

@Module
@InstallIn(SingletonComponent::class)
abstract class TimeLineDetailRepositoryModule {
    @Binds
    abstract fun bindTimeLineDetailRepository(
        timeLineDetailRepositoryImpl: TimeLineDetailRepositoryImpl
    ): TimeLineDetailRepository
}
