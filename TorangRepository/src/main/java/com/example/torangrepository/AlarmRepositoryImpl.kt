package com.example.torangrepository

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.switchMap
import com.example.torang_core.data.dao.LoggedInUserDao
import com.example.torang_core.data.model.Alarm
import com.example.torang_core.data.model.LoggedInUserData
import com.example.torang_core.repository.AlarmRepository
import com.example.torang_core.util.Logger
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AlarmRepositoryImpl @Inject constructor(
    @ApplicationContext private val context: Context,
    private val loggedInUserDao: LoggedInUserDao,
    private val restaurantService: RestaurantService
) : AlarmRepository {


    override suspend fun loadAlarm(): ArrayList<Alarm> {
        Logger.d("")
        var userId = 0
        loggedInUserDao.getLoggedInUserData1()?.userId?.let {
            userId = it
        }
        Logger.d("getService().getAlarms(user_id = userId)")
        return restaurantService.getAlarms(user_id = userId)
    }

    override suspend fun deleteAlarm() {

    }

    override fun user(): LiveData<LoggedInUserData?> {
        return loggedInUserDao.getLoggedInUserData()
    }

    override suspend fun testLogout() {
        loggedInUserDao.clear()
        TorangPreference().logout(context)
    }

    /** 로그인 여부 */
    override val isLogin: LiveData<Boolean> = loggedInUserDao.getLoggedInUserData().switchMap {
        if (it != null) {
            MutableLiveData(it.userId != 0)
        } else {
            MutableLiveData(false)
        }
    }
}

@Module
@InstallIn(SingletonComponent::class)
abstract class AlarmRepositoryModule {
    @Binds
    abstract fun provideAlarmRepository(alarmRepository: AlarmRepositoryImpl): AlarmRepository
}