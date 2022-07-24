package com.example.torangrepository

import android.content.Context
import com.example.torang_core.data.dao.LoggedInUserDao
import com.example.torang_core.repository.SettingsRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SettingsRepositoryImpl @Inject constructor(
    private val loggedInUserDao: LoggedInUserDao
) :
    SettingsRepository {
    override suspend fun logout() {
        loggedInUserDao.clear()
    }
}

@Module
@InstallIn(SingletonComponent::class)
abstract class SettingRepositoryModule {
    @Binds
    abstract fun provideSettingRepository(settingsRepositoryImpl: SettingsRepositoryImpl): SettingsRepository
}