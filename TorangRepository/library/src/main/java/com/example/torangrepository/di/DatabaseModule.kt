package com.example.torangrepository.di

import com.example.torang_core.data.AppDatabase
import com.example.torang_core.data.dao.*
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@InstallIn(SingletonComponent::class)
@Module
class DatabaseModule {

    /** 로컬 데이터베이스 제공 */
    /*@Singleton
    @Provides
    fun provideAppDatabase(@ApplicationContext context: Context): AppDatabase {
        return AppDatabase.getInstance(context)
    }*/

    /** 로컬 데이터베이스의 사용자 관리 DAO 제공 */
    @Provides
    fun provideUserDao(appDatabase: AppDatabase): UserDao {
        return appDatabase.userDao()
    }

    /** 로컬 데이터베이스의 사용자 로그인 관리 DAO 제공 */
    @Provides
    fun provideLoggedInUserDao(appDatabase: AppDatabase): LoggedInUserDao {
        return appDatabase.LoggedInUserDao()
    }

    /** 로컬 데이터베이스의 사용자 관리 DAO 제공 */
    @Provides
    fun providefeedDao(appDatabase: AppDatabase): FeedDao {
        return appDatabase.feedDao()
    }

    /** 로컬 데이터베이스의 사용자 관리 DAO 제공 */
    @Provides
    fun provideSearchDao(appDatabase: AppDatabase): SearchDao {
        return appDatabase.searchDao()
    }

    /** 로컬 데이터베이스의 사용자 관리 DAO 제공 */
    @Provides
    fun provideCommentDao(appDatabase: AppDatabase): CommentDao {
        return appDatabase.commentDao()
    }

    /** 로컬 데이터베이스의 사용자 관리 DAO 제공 */
    @Provides
    fun provideRestaurantDao(appDatabase: AppDatabase): RestaurantDao {
        return appDatabase.restaurantDao()
    }

    /** 로컬 데이터베이스의 사용자 관리 DAO 제공 */
    @Provides
    fun provideReviewDao(appDatabase: AppDatabase): ReviewDao {
        return appDatabase.reviewDao()
    }

    /** 로컬 데이터베이스의 사용자 관리 DAO 제공 */
    @Provides
    fun provideMyReviewDao(appDatabase: AppDatabase): MyReviewDao {
        return appDatabase.myReviewDao()
    }

    /** 로컬 데이터베이스의 사용자 관리 DAO 제공 */
    @Provides
    fun providePictureDao(appDatabase: AppDatabase): PictureDao {
        return appDatabase.pictureDao()
    }


}