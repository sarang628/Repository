package com.example.torangrepository.services

import com.example.torang_core.data.model.Favorite
import com.example.torang_core.data.model.FeedResponse
import com.example.torang_core.data.model.Like
import com.example.torang_core.data.model.Review
import com.example.torangrepository.modules.RetrofitModule
import com.example.torangrepository.modules.TorangOkhttpClient
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.http.Body
import retrofit2.http.FieldMap
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST
import java.util.*
import javax.inject.Inject
import javax.inject.Singleton

interface FeedServices {
    @FormUrlEncoded
    @POST("getFeeds")
    suspend fun getFeeds(@FieldMap params: Map<String, String>): ArrayList<FeedResponse>

    @POST("deleteReview")
    suspend fun deleteReview(@Body review: Review): Review

    @POST("addLike")
    suspend fun addLike(@Body like: Like): Like

    @POST("deleteLike")
    suspend fun deleteLike(@Body like: Like): Like

    @POST("deleteFavorite")
    suspend fun deleteFavorite(@Body favorite: Favorite): Favorite

    @POST("addFavorite")
    suspend fun addFavorite(@Body favorite: Favorite): Favorite
}

/**
 * 실서버 신고 서비스
 */
@Singleton
class ProductFeedService @Inject constructor(
    private val torangOkHttpClientImpl: TorangOkhttpClient,
    private val retrofitModule: RetrofitModule
) {
    private var url = "https://www.vrscoo.com:8080/"
    fun create(): FeedServices {
        return retrofitModule.getRetrofit(torangOkHttpClientImpl.getHttpClient(), url).create(FeedServices::class.java)
    }
}


/**
 * 로컬 서버 신고 서비스
 */
@Singleton
class LocalFeedService @Inject constructor(
    private val torangOkHttpClientImpl: TorangOkhttpClient,
    private val retrofitModule: RetrofitModule
) {
    private var url = "http://10.0.2.2:8080/"
    fun create(): FeedServices {
        return retrofitModule.getRetrofit(torangOkHttpClientImpl.getHttpClient(), url).create(FeedServices::class.java)
    }
}

@InstallIn(SingletonComponent::class)
@Module
class FeedServiceModule {

    @Singleton
    @Provides
    fun provideFeedService(feedProductFeedService: ProductFeedService, localFeedService: LocalFeedService): FeedServices {
        return feedProductFeedService.create()
    }
}