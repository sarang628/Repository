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
import com.example.torangrepository.test.TestAlarmRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AlarmRepositoryImpl @Inject constructor(
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

    /** 로그인 여부 */
    override val isLogin: LiveData<Boolean> = loggedInUserDao.getLoggedInUserData().switchMap {
        if (it != null) {
            MutableLiveData(it.userId != 0)
        } else {
            MutableLiveData(false)
        }
    }
}

/**
 * 로그인 테스트용 TestAlarmRepositoryImpl
 * 실제 사용 AlarmRepositoryImpl
 */
@Module
@InstallIn(SingletonComponent::class)
abstract class AlarmRepositoryModule {

    @Binds
    //실제 사용
    abstract fun provideAlarmRepository(alarmRepository: AlarmRepositoryImpl): AlarmRepository
    //로그인 테스트
//    abstract fun provideAlarmRepository(alarmRepository: TestAlarmRepositoryImpl): AlarmRepository
}