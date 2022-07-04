package com.example.torangrepository

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.switchMap
import com.example.torang_core.data.dao.LoggedInUserDao
import com.example.torang_core.data.dao.UserDao
import com.example.torang_core.data.model.*
import com.example.torang_core.data.model.ReviewImage.Companion.toReviewImage
import com.example.torang_core.repository.FeedRepository
import com.example.torang_core.util.Logger
import com.example.torangrepository.services.FeedServices
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class FeedRepositoryImpl @Inject constructor(
    @ApplicationContext private val context: Context,
    private val feedServices: FeedServices, // 원격 저장소 주입
    private val userDao: UserDao, // 로컬 저장소 주입
    private val user: LoggedInUserDao // 로컬 저장소 주입
) : FeedRepository {

    /** 로그인 여부 */
    override val isLogin: LiveData<Boolean> = user.getLoggedInUserData().switchMap {
        Logger.v("isLogin observed $it")
        MutableLiveData(it != null)
    }

    override suspend fun isLogin(): Boolean {
        return user.getLoggedInUserData1() != null
    }

    fun userId(): Int {
        return TorangPreference().getUserId(context)
    }

    override suspend fun deleteFeed(reviewId: Int) {
        try {
            //원격 저장소 요청
            feedServices.deleteReview(Review().apply { this.review_id = reviewId })
        } catch (e: java.lang.Exception) {
            Logger.e(e.toString())
        }
        //로컬 저장소에서 삭제
        userDao.deleteFeed(reviewId)
    }

    override suspend fun user1(): LoggedInUserData? {
        return user.getLoggedInUserData1()
    }

    override fun getReviewImages(reviewId: Int): LiveData<List<ReviewImage>> {
        return userDao.getReviewImages(reviewId)
    }

    override fun getLike(reviewId: Int): LiveData<Like> {
        return userDao.getLike(reviewId)
    }

    override fun getFavorite(reviewId: Int): LiveData<Favorite> {
        return userDao.getFavorite(reviewId)
    }

    override fun getFeed(): LiveData<List<Feed>> {
        return userDao.getAllFeed()
    }

    /**
     * 서버에 피드를 요청합니다.
     */
    override suspend fun loadFeed() {
        // 서버요청
        val userId = userId()
        val map = HashMap<String, String>().apply { put("user_id", "" + userId) }
        //API 호출
        val list: ArrayList<FeedResponse> = feedServices.getFeeds(map)
        val reviewImages = ArrayList<ReviewImage>()
        val users = ArrayList<UserData>()
        val feeds = ArrayList<FeedData>()
        val likes = ArrayList<Like>()
        val favorites = ArrayList<Favorite>()
        val deleteFavorites = ArrayList<Favorite>()
        val deleteLikes = ArrayList<Like>()
        val restaurants = ArrayList<RestaurantData>()
        //서버에서 받은 값을 리스트로 만듬 하나하나 삽입 할 경우 observe 이벤트가 과도하게 발생하여 아래와 같이 처리
        for (feed in list) {
            try {
                feed.pictures?.also {
                    for (picture in it) {
                        reviewImages.add(picture.toReviewImage())
                    }
                }
                feeds.add(feed.toFeedData())

                feed.user?.also { users.add(UserData.parse(it)) }
                if (feed.like != null) {
                    feed.like!!.reviewId = feed.review_id
                    feed.like!!.user_id = userId
                    likes.add(feed.like!!)
                } else {
                    deleteLikes.add(Like(reviewId = feed.review_id, user_id = userId()))
                }

                if (feed.favorite != null) {
                    Logger.d("즐겨찾기 추가 ${feed.favorite}")
                    feed.favorite!!.reviewId = feed.review_id
                    feed.favorite!!.user_id = userId
                    favorites.add(feed.favorite!!)
                } else {
                    deleteFavorites.add(Favorite(reviewId = feed.review_id, user_id = userId()))
                }

                feed.restaurant?.also {
                    val data = RestaurantData.parse(it)
                    restaurants.add(data)
                }
            } catch (e: Exception) {
                Logger.e(e.toString())
            }
        }
        userDao.deleteLikes(deleteLikes)
        userDao.deleteAll()
        userDao.insertUserAndPictureAndLikeAndRestaurantAndFeed(
            users,
            reviewImages,
            likes,
            restaurants,
            feeds,
            favorites
        )
    }

    //좋아요 처리
    override suspend fun like(reviewId: Int) {

        if (userDao.hasLike(reviewId) > 0) {
            //좋아요가 있다면 제거
            //api 호출
            feedServices.deleteLike(
                Like(
                    reviewId = reviewId,
                    user_id = userId(),
                    like_id = userDao.getLike1(reviewId).like_id
                )
            )
            //local db 처리
            userDao.deleteLike(Like(reviewId = reviewId))
        } else {
            //좋아요가 없다면 추가
            val like = Like(reviewId = reviewId, user_id = userId())
            //api 호출
            val resultLike = feedServices.addLike(like)
            //local db 처리
            userDao.insertLike(resultLike)
        }
    }

    override suspend fun favorite(reviewId: Int) {
        if (userDao.hasFavorite(reviewId) > 0) {
            //좋아요가 있다면 제거
            //api 호출
            feedServices.deleteFavorite(
                Favorite(
                    reviewId = reviewId,
                    user_id = userId(),
                    favorite_id = userDao.getFavorite1(reviewId).favorite_id
                )
            )
            //local db 처리
            userDao.deleteFavorite(Favorite(reviewId = reviewId))
        } else {
            //좋아요가 없다면 추가
            val favorite = Favorite(reviewId = reviewId, user_id = userId())
            //api 호출
            val resultLike = feedServices.addFavorite(favorite)
            //local db 처리
            userDao.insertFavorite(resultLike)
        }
    }
}

@Module
@InstallIn(SingletonComponent::class)
abstract class FeedRepositoryModule {
    @Binds
    abstract fun provideFeedRepository(feedRepositoryImpl: FeedRepositoryImpl): FeedRepository
}
