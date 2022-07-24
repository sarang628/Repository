package com.example.torangrepositorytestapp

import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.example.torang_core.repository.FeedRepository
import com.example.torang_core.repository.LoginRepository
import com.example.torang_core.repository.MyReviewsRepository
import com.example.torang_core.util.Logger
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

//    @Inject
//    lateinit var feedRepository: FeedRepository

//    @Inject
//    lateinit var userRepository: LoginRepository

//    @Inject
//    lateinit var myReviewsRepository: MyReviewsRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        lifecycleScope.launch {
            /*repeatOnLifecycle(Lifecycle.State.RESUMED) {
                userRepository.isLoginFlow().collect {
                    Logger.d("" + it)
                }
            }*/
        }

        runBlocking {
            /*findViewById<TextView>(R.id.tv).text =
                myReviewsRepository.getMyReviews3(4).toString()*/
        }
    }
}