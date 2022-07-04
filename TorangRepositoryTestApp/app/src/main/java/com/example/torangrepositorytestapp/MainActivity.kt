package com.example.torangrepositorytestapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.torang_core.repository.FeedRepository
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.runBlocking
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    @Inject
    lateinit var feedRepository: FeedRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        runBlocking {
            feedRepository.loadFeed()
        }
    }
}