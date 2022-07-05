package com.example.torangrepository

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.torang_core.data.dao.LoggedInUserDao
import com.example.torang_core.data.dao.RestaurantDao
import com.example.torang_core.data.dao.ReviewDao
import com.example.torang_core.data.dao.UserDao
import com.example.torang_core.data.data.ReviewAndImage
import com.example.torang_core.data.model.FeedData
import com.example.torang_core.data.model.ModifyFeedData
import com.example.torang_core.data.model.RestaurantData
import com.example.torang_core.data.model.ReviewImage
import com.example.torang_core.repository.MyReviewRepository
import com.example.torang_core.util.Logger
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.MultipartBody
import java.io.File
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MyReviewRepositoryImpl @Inject constructor(
    @ApplicationContext private val context: Context,
    private val reviewDao: ReviewDao,
    private val restaurantService: RestaurantService,
    private val userDao: UserDao,
    private val restaurantDao: RestaurantDao,
    private val loggedInUserDao: LoggedInUserDao
) :
    MyReviewRepository {
    override fun getMyReview(reviewId: Int): LiveData<ReviewAndImage?> {
        return reviewDao.getFeedbyReviewId(reviewId)
    }

    override fun getUploadedPicture(reviewId: Int): LiveData<List<ReviewImage>> {
        return MutableLiveData<List<ReviewImage>>()
    }


    override suspend fun uploadReview(review: ReviewAndImage) {

        if (review.review == null)
            throw IllegalArgumentException("리뷰 데이터가 없습니다.")

        if (review.user == null || review.user?.userId == -1)
            throw IllegalArgumentException("사용자 정보가 없습니다.")

        val fileList = ArrayList<File>()

        review.images?.let {
            for (image in it) {
                fileList.add(File(image.picture_url))
            }
        }

        val pictureList = ArrayList<MultipartBody.Part>()

        for (i in fileList.indices) {
            val file: File = fileList.get(i)
            val requestFile = CountingFileRequestBody(file, "image/*", object :
                CountingFileRequestBody.ProgressListener {
                override fun transferred(num: Long) {

                }
            })

            pictureList.add(MultipartBody.Part.createFormData("file", file.name, requestFile))
        }

        try {
            val result = restaurantService.fileUpload(review.toMap(), pictureList)

            //피드 추가하기
            val list1 = ArrayList<ReviewAndImage>()
            list1.add(ReviewAndImage.parse(result))

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
        } catch (e: Exception) {
            Logger.e(e.toString())
        }
    }

    override suspend fun modifyReview(review: ReviewAndImage) {

        if (review.review == null)
            throw IllegalArgumentException("리뷰 데이터가 없습니다.")

        if (review.user == null || review.user?.userId == -1)
            throw IllegalArgumentException("사용자 정보가 없습니다.")

        val fileList = ArrayList<File>()

        review.images?.let {
            for (image in it) {
                fileList.add(File(image.picture_url))
            }
        }

        val pictureList = ArrayList<MultipartBody.Part>()

        for (i in fileList.indices) {
            val file: File = fileList.get(i)
            val requestFile = CountingFileRequestBody(file, "image/*", object :
                CountingFileRequestBody.ProgressListener {
                override fun transferred(num: Long) {

                }
            })

            pictureList.add(MultipartBody.Part.createFormData("file", file.name, requestFile))
        }

        try {
            val result = restaurantService.updateReview(review.toMap(), pictureList)

            //피드 추가기
            val list1 = ArrayList<ReviewAndImage>()
            list1.add(ReviewAndImage.parse(result))

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
        } catch (e: Exception) {
            Logger.e(e.toString())
        }
    }

    override suspend fun modifyReview(review: ModifyFeedData) {

        if (review.reviewAndImage.review == null)
            throw IllegalArgumentException("리뷰 데이터가 없습니다.")

        if (review.reviewAndImage.user == null || review.reviewAndImage.user?.userId == -1)
            throw IllegalArgumentException("사용자 정보가 없습니다.")

        val fileList = ArrayList<File>()

        review.reviewAndImage.images?.let {
            for (image in it) {
                fileList.add(File(image.picture_url))
            }
        }

        val pictureList = ArrayList<MultipartBody.Part>()

        for (i in fileList.indices) {
            val file: File = fileList.get(i)
            val requestFile = CountingFileRequestBody(file, "image/*", object :
                CountingFileRequestBody.ProgressListener {
                override fun transferred(num: Long) {

                }
            })

            pictureList.add(MultipartBody.Part.createFormData("file", file.name, requestFile))
        }

        try {
            val result = restaurantService.updateReview(review.toMap(), pictureList)

            //피드 추가기
            val list1 = ArrayList<ReviewAndImage>()
            list1.add(ReviewAndImage.parse(result))

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
            userDao.deletePicturesByReviewId(review.reviewAndImage.review!!.review_id)
            userDao.insertPictures(images)
        } catch (e: Exception) {
            Logger.e(e.toString())
        }
    }

    override suspend fun getRestaurant(restaurantId: Int): RestaurantData? {
        return restaurantDao.getRestaurantByRestaurantId(restaurantId)
    }

    override fun userId(): Int {
        return TorangPreference().getUserId(context)
    }

    override suspend fun userId1(): Int {
        var userId = -1

        loggedInUserDao.getLoggedInUserData1()?.let {
            var data = it
            userId = it.userId!!
            Logger.d(data)
        }

        loggedInUserDao.getLoggedInUserData1()?.userId?.let {
            userId = it
        }
        return userId
    }
}
