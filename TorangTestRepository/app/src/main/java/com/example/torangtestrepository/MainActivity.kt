package com.example.torangtestrepository

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.testrepository.TestRepository

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        TestRepository().apply {
            context = this@MainActivity
        }.getAlarm()
    }
}