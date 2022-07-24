package com.example.torangrepository

import android.content.Context
import androidx.lifecycle.LiveData
import com.example.torang_core.data.dao.LoggedInUserDao
import com.example.torang_core.data.model.LoggedInUserData
import com.example.torang_core.data.model.User
import com.example.torang_core.repository.LoginRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class LoginRepositoryImpl @Inject constructor(
    @ApplicationContext private val context: Context,
    private val restaurantService: RestaurantService,
    private val loggedInUserDao: LoggedInUserDao
) : LoginRepository {

//    @Inject
//    lateinit var facebookLoginProvider: FaceBookLoginProviderForRepository

    override suspend fun isLogin(): Boolean {
        return TorangPreference().getUserId(context) != -1
    }

    override suspend fun facebookLogin(token: String): User? {

        restaurantService.facebook_login(token).data?.let {

            LoggedInUserData.parse(it)?.let {
                loggedInUserDao.insert(it)
            }

            return it
        }
        return null
    }

    override fun isLoginFlow(): Flow<Int> {
        return loggedInUserDao.isLpogin()
    }

    override fun getLoginUser(): LiveData<LoggedInUserData?> {
        return loggedInUserDao.getLoggedInUserData()
    }

    override suspend fun setLoggedInUser(loggedInUserData: LoggedInUserData) {
        loggedInUserData.access_token?.let {
            TorangPreference().saveAccessToken(context, it)
        }
        TorangPreference().saveUserId(context, loggedInUserData.userId!!)
        loggedInUserDao.insert(loggedInUserData)
    }
}