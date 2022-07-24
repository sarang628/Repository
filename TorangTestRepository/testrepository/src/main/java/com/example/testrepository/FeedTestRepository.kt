package com.example.testrepository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.torang_core.data.model.*
import com.example.torang_core.data.remote.RemoteFeed
import com.example.torang_core.repository.FeedRepository
import javax.inject.Inject

class FeedTestRepository @Inject constructor() : FeedRepository {
    override suspend fun deleteFeed(reviewId: Int) {

    }

    override suspend fun loadFeed(): ArrayList<RemoteFeed> {
        TODO("Not yet implemented")
    }
}