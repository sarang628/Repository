package com.example.testrepository

import android.content.Context
import androidx.lifecycle.LiveData
import com.example.testrepository.getRawToString.getRawtoString
import com.example.torang_core.data.AppDatabase
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.example.torang_core.data.data.ReviewAndImage
import com.example.torang_core.data.model.*
import com.example.torang_core.data.model.ReviewImage.Companion.toReviewImage
import com.example.torang_core.repository.*

class TestRepository(private val context: Context) : FeedRepository,
    ReviewRepository, MenuRepository, MyReviewsRepository, MapRepository,
    LoginRepository, PicturesRepository {
    private val appDatabase = AppDatabase.getInstance(context)
    val userDao = appDatabase.userDao()
    val restaurantDao = appDatabase.restaurantDao()
    val reviewDao = appDatabase.reviewDao()
    val menuDao = appDatabase.menuDao()
    val alarmDao = appDatabase.alarmDao()
    val myReviewDao = appDatabase.myReviewDao()
    //val loggedInUserData = appDatabase.LoggedInUserDao()


    suspend fun testLoadMenu() {
        val type = TypeToken.getParameterized(ArrayList::class.java, Menu::class.java).type
        val list: ArrayList<Menu> = Gson().fromJson(context.getRawtoString(R.raw.menu), type)
        menuDao.insertAll(list)
    }
    
    override fun getFeed(): LiveData<List<Feed>> {
        return userDao.getAllFeed()
    }

    override suspend fun loadFeed() {
        val type = TypeToken.getParameterized(ArrayList::class.java, FeedResponse::class.java).type
        val list: ArrayList<FeedResponse> =
            Gson().fromJson(context.getRawtoString(R.raw.feed), type)
        val reviewImages = ArrayList<ReviewImage>()
        val users = ArrayList<UserData>()
        val feeds = ArrayList<FeedData>()
        val likes = ArrayList<Like>()
        val restaurants = ArrayList<RestaurantData>()
        for (feed in list) {
            try {
                feed.pictures?.also {
                    for (picture in it) {
                        reviewImages.add(picture.toReviewImage())
                    }
                }
                feeds.add(feed.toFeedData())
                feed.user?.also {
                    users.add(UserData.parse(it))
                }

                feed.like?.also {
                    likes.add(it)
                }

                feed.restaurant?.also {
                    restaurants.add(RestaurantData.parse(it))
                }
            } catch (e: Exception) {
            }
        }
        userDao.insertPictures(reviewImages)
        userDao.insertAll(users)
        userDao.insertFeed(feeds)
        userDao.insertLikes(likes)
        restaurantDao.insertAllRestaurant(restaurants)

        val type1 =
            TypeToken.getParameterized(ArrayList::class.java, RestaurantData::class.java).type
        val list1: ArrayList<RestaurantData> =
            Gson().fromJson(context.getRawtoString(R.raw.restaurants), type1)


        testAlarmInsert()

        restaurantDao.insertAllRestaurant(list1)
    }

    private suspend fun testAlarmInsert() {
        val type = TypeToken.getParameterized(ArrayList::class.java, Alarm::class.java).type
        val list: ArrayList<Alarm> = Gson().fromJson(context.getRawtoString(R.raw.alarm), type)
        val inserList = ArrayList<AlarmData>()
        for (alarm in list) {
            inserList.add(
                AlarmData(
                    alarm_id = alarm.alarm_id,
                    user_id = alarm.user_id,
                    other_user_id = alarm.other_user_id,
                    contents = alarm.contents,
                    alarm_type = alarm.alarm_type!!.ordinal,
                    review_id = alarm.review_id,
                    create_date = alarm.create_date
                )
            )
        }
        alarmDao.insertAlarms(inserList)
    }

    override fun getReviewImages(reviewId: Int): LiveData<List<ReviewImage>> {
        return userDao.getReviewImages(reviewId)
    }


    override fun getLike(reviewId: Int): LiveData<Like> {
        return userDao.getLike(reviewId)
    }

    override fun getFavorite(reviewId: Int): LiveData<Favorite> {
        TODO("Not yet implemented")
    }

    override suspend fun like(reviewId: Int) {

    }

    override suspend fun favorite(reviewId: Int) {
        TODO("Not yet implemented")
    }

    override suspend fun deleteFeed(reviewId: Int) {
        TODO("Not yet implemented")
    }

    override suspend fun user1(): LoggedInUserData? {
        TODO("Not yet implemented")
    }

    override val isLogin: LiveData<Boolean>
        get() = TODO("Not yet implemented")

    override suspend fun isLogin(): Boolean {
        return true
    }

    override suspend fun getMenus(restaurantId: Int): ArrayList<Menu> {
        TODO("Not yet implemented")
    }

    override fun getRestaurant(): LiveData<List<RestaurantData>> {
        return restaurantDao.getRestaurant()
    }

    override suspend fun loadRestaurant() {
        TODO("Not yet implemented")
    }

    override suspend fun facebookLogin(token: String): User {
        TODO("Not yet implemented")
    }

    override fun getLoginUser(): LiveData<LoggedInUserData?> {
        TODO("Not yet implemented")
        //return loggedInUserData.getLoggedInUserData()
    }

    override suspend fun setLoggedInUser(loggedInUserData: LoggedInUserData) {
        TODO("Not yet implemented")
    }

    override suspend fun getPictures(restaurantId: Int): ArrayList<Picture> {
        return ArrayList()
    }

    override fun getFeedPicture(reviewId: Int): LiveData<List<ReviewImage>> {
        TODO("Not yet implemented")
    }

    override suspend fun getReviews(restaurantId: Int): ArrayList<Review> {
        TODO("Not yet implemented")
    }

    override suspend fun getMyReviews(restaurantId: Int): List<ReviewAndImage> {
        TODO("Not yet implemented")
    }

    override fun getMyReviews1(restaurantId: Int): LiveData<List<ReviewAndImage>> {
        TODO("Not yet implemented")
    }
}