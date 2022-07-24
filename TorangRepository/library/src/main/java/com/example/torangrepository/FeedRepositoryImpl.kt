package com.example.torangrepository

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.switchMap
import com.example.torang_core.data.dao.LoggedInUserDao
import com.example.torang_core.data.dao.UserDao
import com.example.torang_core.data.model.*
import com.example.torang_core.data.model.ReviewImage.Companion.toReviewImage
import com.example.torang_core.data.remote.RemoteFeed
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

    override suspend fun loadFeed(): ArrayList<RemoteFeed> {
        TODO("Not yet implemented")
    }


}

@Module
@InstallIn(SingletonComponent::class)
abstract class FeedRepositoryModule {
    @Binds
    abstract fun provideFeedRepository(feedRepositoryImpl: FeedRepositoryImpl): FeedRepository
}
