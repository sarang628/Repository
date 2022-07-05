package com.example.testrepository

import android.content.Context
import android.util.Log
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.workDataOf
import com.example.testrepository.SeedDatabaseWorker.Companion.KEY_FILENAME
import com.example.torang_core.data.AppDatabase
import com.example.torang_core.utilities.DATABASE_NAME

object TestAppDatabase {
    // For Singleton instantiation
    @Volatile
    private var instance: AppDatabase? = null

    fun getInstance(context: Context): AppDatabase {
        return instance ?: synchronized(this) {
            instance ?: buildDatabase(context).also { instance = it }
        }
    }

    private fun buildDatabase(context: Context): AppDatabase {
        Log.d("__torang", "buildDatabase")
        return Room.databaseBuilder(context, AppDatabase::class.java, DATABASE_NAME)
            .addCallback(
                object : RoomDatabase.Callback() {
                    override fun onCreate(db: SupportSQLiteDatabase) {
                        Log.d("__torang", "db created")
                        super.onCreate(db)
                        val request = OneTimeWorkRequestBuilder<SeedDatabaseWorker>()
                            .setInputData(workDataOf(KEY_FILENAME to PLANT_DATA_FILENAME))
                            .build()
                        WorkManager.getInstance(context).enqueue(request)
                    }
                }
            )
            .build()
    }
}