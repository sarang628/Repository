package com.example.torangrepositorytestapp.di

import com.example.torang_core.repository.LoginRepository
import com.example.torang_core.repository.MyReviewsRepository
import com.example.torangrepository.LoginRepositoryImpl
import com.example.torangrepository.MyReviewsRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {
    @Binds
    abstract fun provideMyReviewsRepository(
//        testMyReviewsRepository: MyReviewsRepositoryWithData
        myReviewsRepositoryImpl: MyReviewsRepositoryImpl
    ): MyReviewsRepository

    @Binds
    abstract fun provideLoginRepository(loginRepositoryImpl: LoginRepositoryImpl): LoginRepository
}