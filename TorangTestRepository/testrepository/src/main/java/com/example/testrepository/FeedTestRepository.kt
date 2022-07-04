package com.example.testrepository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.torang_core.data.model.*
import com.example.torang_core.repository.FeedRepository
import javax.inject.Inject

class FeedTestRepository @Inject constructor() : FeedRepository {
    override suspend fun deleteFeed(reviewId: Int) {

    }

    override suspend fun user1(): LoggedInUserData? {
        TODO("Not yet implemented")
    }

    override val isLogin: LiveData<Boolean>
        get() = TODO("Not yet implemented")

    override suspend fun isLogin(): Boolean {
        return true
    }

    override fun getReviewImages(reviewId: Int): LiveData<List<ReviewImage>> {
        return MutableLiveData()
    }

    override fun getFeed(): LiveData<List<Feed>> {
        return MutableLiveData()
    }

    override suspend fun loadFeed() {

    }

    override suspend fun like(reviewId: Int) {

    }

    override suspend fun favorite(reviewId: Int) {

    }

    override fun getLike(reviewId: Int): LiveData<Like> {
        return MutableLiveData()
    }

    override fun getFavorite(reviewId: Int): LiveData<Favorite> {
        return MutableLiveData()
    }
}