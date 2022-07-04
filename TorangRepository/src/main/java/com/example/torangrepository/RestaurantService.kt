package com.example.torangrepository

import com.example.torang_core.data.model.*
import com.example.torangrepository.modules.RetrofitModule
import com.example.torangrepository.modules.TorangOkhttpClient
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.*
import java.util.*
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.collections.HashMap

interface RestaurantService {
    @FormUrlEncoded
    @POST("getAllRestaurant")
    suspend fun getAllRestaurant(@FieldMap params: Map<String, String>): ArrayList<Restaurant>

    @POST("getFilterRestaurant")
    suspend fun getFilterRestaurant(@Body filter: Filter): ArrayList<Restaurant>

    @FormUrlEncoded
    @POST("getRestaurant")
    suspend fun getRestaurant(@FieldMap params: Map<String, String>): Restaurant

    @FormUrlEncoded
    @POST("getOpenHours")
    suspend fun getHoursOfOperation(@FieldMap params: Map<String, String>): ArrayList<HoursOfOperation>

    @FormUrlEncoded
    @POST("getMenus")
    suspend fun getMenus(@FieldMap params: Map<String, String>): ArrayList<Menu>

    @FormUrlEncoded
    @POST("getPictures")
    suspend fun getPictures(@FieldMap params: Map<String, String>): ArrayList<Picture>

    @POST("addReview")
    suspend fun addReview(@Body reviewBody: Review): Call<Review>

    @POST("updateReview")
    suspend fun updateReview(@Body reviewBody: Review): Call<Review>

    @POST("deleteReview")
    suspend fun deleteReview(@Body review: Review): Review

    @FormUrlEncoded
    @POST("getReviews")
    suspend fun getReviews(@FieldMap params: Map<String, String>): ArrayList<Review>

    @FormUrlEncoded
    @POST("getMyReview")
    suspend fun getMyReview(@FieldMap params: Map<String, String>): Call<Review>

    @FormUrlEncoded
    @POST("getMenuReviews")
    suspend fun getMenuReviews(@FieldMap params: Map<String, String>): Call<ArrayList<MenuReview>>

    @FormUrlEncoded
    @POST("facebook_login")
    suspend fun facebook_login(@Field("accessToken") accessToken: String): Response<User>

    @Multipart
    @POST("fileUpload")
    suspend fun fileUpload(
        @PartMap params: HashMap<String, RequestBody>,
        @Part pictures: ArrayList<MultipartBody.Part>
    ): Review

    @Multipart
    @POST("updateReview")
    suspend fun updateReview(
        @PartMap params: HashMap<String, RequestBody>,
        @Part pictures: ArrayList<MultipartBody.Part>
    ): Review

    @FormUrlEncoded
    @POST("deletePicture")
    suspend fun deletePicture(@FieldMap params: Map<String, String>): Call<Void>

    @FormUrlEncoded
    @POST("getTimelines")
    suspend fun getTimelines(@FieldMap params: Map<String, String>): Call<ArrayList<Review>>

    @FormUrlEncoded
    @POST("saveMenu")
    suspend fun saveMenu(@FieldMap params: Map<String, String>): Call<Void>

    @FormUrlEncoded
    @POST("getFeeds")
    suspend fun getFeeds(@FieldMap params: Map<String, String>): ArrayList<FeedResponse>

    @POST("addMenuReview")
    suspend fun addMenuReview(@Body menuReview: MenuReview): Call<MenuReview>

    @FormUrlEncoded
    @POST("getMyReviews")
    suspend fun getMyReviews(@FieldMap params: Map<String, String>): ArrayList<Review>

    @FormUrlEncoded
    @POST("getMyReviewsByUserId")
    suspend fun getMyReviewsByUserId(@FieldMap params: Map<String, String>): Call<ArrayList<Review>>

    @POST("getMyMenuReviews")
    suspend fun getMyMenuReviews(@Body review: Review): Call<ArrayList<MenuReview>>

    @POST("addComment")
    suspend fun addComment(@Body comment: Comment): Comment

    @POST("modifyComment")
    suspend fun modifyComment(@Body comment: Comment): Call<Comment>

    @POST("deleteComment")
    suspend fun deleteComment(@Body comment: Comment): Call<Comment>

    @FormUrlEncoded
    @POST("getComments")
    suspend fun getComments(@Field("review_id") review_id: String): ArrayList<Comment>

    @POST("addLike")
    suspend fun addLike(@Body like: Like): Like

    @POST("deleteLike")
    suspend fun deleteLike(@Body like: Like): Like

    @FormUrlEncoded
    @POST("getAlarms")
    suspend fun getAlarms(@Field("user_id") user_id: Int): ArrayList<Alarm>


    @Multipart
    @POST("updateProfile")
    suspend fun updateProfile(
        @PartMap params: HashMap<String, RequestBody>,
        @Part pictures: ArrayList<MultipartBody.Part>
    ): Response<User>


    @POST("deleteFavorite")
    suspend fun deleteFavorite(@Body favorite: Favorite): Favorite

    @POST("addFavorite")
    suspend fun addFavorite(@Body favorite: Favorite): Favorite
}

/**
 * 토랑 서비스
 */
@Singleton
class ProductRestaurantService @Inject constructor(
    private val torangOkHttpClientImpl: TorangOkhttpClient,
    private val retrofitModule: RetrofitModule
) {
    private var url = "https://www.vrscoo.com:8080/"
    fun create(): RestaurantService {
        return retrofitModule.getRetrofit(torangOkHttpClientImpl.getHttpClient(), url).create(
            RestaurantService::class.java
        )
    }
}

@InstallIn(SingletonComponent::class)
@Module
class RestaurantServiceModule {

    @Singleton
    @Provides
    fun provideRestaurantService(productRestaurantService: ProductRestaurantService): RestaurantService {
        return productRestaurantService.create()
    }
}
