package com.example.torangrepositorytestapp.di

import android.content.Context
import com.example.testrepository.TestAppDatabase
import com.example.testrepository.TestMyReviewsRepository
import com.example.torang_core.data.AppDatabase
import com.example.torang_core.repository.LoginRepository
import com.example.torang_core.repository.MyReviewsRepository
import com.example.torangrepository.LoginRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {
    @Binds
    abstract fun provideMyReviewsRepository(testMyReviewsRepository: TestMyReviewsRepository): MyReviewsRepository

    @Binds
    abstract fun provideLoginRepository(loginRepositoryImpl: LoginRepositoryImpl): LoginRepository
}


@InstallIn(SingletonComponent::class)
@Module
class DatabaseModule {
    /** 로컬 데이터베이스 제공 */
    @Singleton
    @Provides
    fun provideAppDatabase(@ApplicationContext context: Context): AppDatabase {
        return TestAppDatabase.getInstance(context)
    }
}