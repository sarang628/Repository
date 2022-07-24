package com.example.torangrepository.test

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.torang_core.data.dao.LoggedInUserDao
import com.example.torang_core.data.model.Alarm
import com.example.torang_core.data.model.LoggedInUserData
import com.example.torang_core.repository.AlarmRepository
import com.example.torang_core.util.Logger
import com.example.torangrepository.RestaurantService
import javax.inject.Inject
import javax.inject.Singleton


@Singleton
class TestAlarmRepositoryImpl @Inject constructor(
    private val loggedInUserDao: LoggedInUserDao,
    private val restaurantService: RestaurantService
) : AlarmRepository {

    override suspend fun loadAlarm(): ArrayList<Alarm> {
        var userId = 0
        loggedInUserDao.getLoggedInUserData1()?.userId?.let {
            userId = it
        }
        Logger.d("getService().getAlarms(user_id = userId)")
        return restaurantService.getAlarms(user_id = 4)
    }

    override suspend fun deleteAlarm() {

    }

    override fun user(): LiveData<LoggedInUserData?> {
        return MutableLiveData(LoggedInUserData(userId = 4))
    }

    override val isLogin: LiveData<Boolean>
        get() = MutableLiveData(true)
}