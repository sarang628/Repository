package com.example.torangrepository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.switchMap
import com.example.torang_core.data.dao.FeedDao
import com.example.torang_core.data.dao.LoggedInUserDao
import com.example.torang_core.data.dao.UserDao
import com.example.torang_core.data.model.*
import com.example.torang_core.repository.ProfileRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ProfileRepositoryImpl @Inject constructor(
    private val loggedUserDao: LoggedInUserDao,
    private val userDao: UserDao,
    private val feedDao: FeedDao
) : ProfileRepository {

    override fun getMyProfile(): LiveData<LoggedInUserData?> {
        return loggedUserDao.getLoggedInUserData()
    }

    override fun loadProfile(userId: Int): LiveData<UserData> {
        return userDao.getUser(userId)
    }

    override fun getMyFeed(userId: Int): LiveData<List<Feed>> {
        return feedDao.getMyFeed(userId)
    }

    override fun getMyFavorite(userId: Int): LiveData<List<Feed>> {
        return feedDao.getMyFavorite(userId)
    }

    override fun getFeed(): LiveData<List<Feed>> {
        return userDao.getAllFeed()
    }

    override suspend fun loadFeed() {

    }

    override suspend fun like(reviewId: Int) {
        //userDao.insertLike(reviewId)
    }

    override suspend fun favorite(reviewId: Int) {
        TODO("Not yet implemented")
    }

    override fun getLike(reviewId: Int): LiveData<Like> {
        return userDao.getLike(reviewId)
    }

    override fun getFavorite(reviewId: Int): LiveData<Favorite> {
        return userDao.getFavorite(reviewId)
    }

    override suspend fun user1(): LoggedInUserData? {
        return loggedUserDao.getLoggedInUserData1()
    }

    override val isLogin: LiveData<Boolean> = loggedUserDao.getLoggedInUserData().switchMap {
        if (it != null) {
            MutableLiveData(it.userId != 0)
        } else {
            MutableLiveData(false)
        }
    }


    override suspend fun isLogin(): Boolean {
        return true
    }

    override suspend fun logout() {
        loggedUserDao.clear()
    }

    override fun getReviewImages(reviewId: Int): LiveData<List<ReviewImage>> {
        return userDao.getReviewImages(reviewId)
    }
}

@Module
@InstallIn(SingletonComponent::class)
abstract class ProfileRepositoryModule {
    @Binds
    abstract fun provideProfileRepository(profileRepositoryImpl: ProfileRepositoryImpl): ProfileRepository
}