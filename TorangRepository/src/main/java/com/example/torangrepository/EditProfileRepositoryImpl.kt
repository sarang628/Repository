package com.example.torangrepository

import android.content.Context
import androidx.lifecycle.LiveData
import com.example.torang_core.data.dao.LoggedInUserDao
import com.example.torang_core.data.dao.UserDao
import com.example.torang_core.data.model.LoggedInUserData
import com.example.torang_core.repository.EditProfileRepository
import com.example.torang_core.repository.EditProfileResponse
import com.example.torang_core.util.Logger
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.collections.set

@Singleton
class EditProfileRepositoryImpl @Inject constructor(
    @ApplicationContext private val context: Context,
    private val restaurantService: RestaurantService,
    private val loggedInUserDao: LoggedInUserDao,
    private val userDao: UserDao
) : EditProfileRepository {
    override suspend fun editProfile(name: String?, uri: String?): EditProfileResponse {

        var response = EditProfileResponse.NO_USER

        val params: HashMap<String, RequestBody> = HashMap()
        params["user_name"] = ("" + name).toRequestBody("text/plain".toMediaTypeOrNull())
        params["user_id"] = ("" + loggedInUserDao.getLoggedInUserData1()?.userId).toRequestBody("text/plain".toMediaTypeOrNull())

        val pictureList = ArrayList<MultipartBody.Part>()
        if (uri != null) {
            val file = File(uri)
            val requestFile = CountingFileRequestBody(file, "image/*", object :
                CountingFileRequestBody.ProgressListener {
                override fun transferred(num: Long) {

                }
            })
            pictureList.add(MultipartBody.Part.createFormData("file", file.name, requestFile))
        }

        try {
            // 레트로핏으로 사용자 프로필 업데이트 Rest API 처리
            restaurantService.updateProfile(params, pictureList).let {
                it.data?.let { user ->
                    // 통신 결과값을 Room을 이용하여 DB update : 로그인한 사용자 정보
                    loggedInUserDao.update(user.userName!!, user.profile_pic_url!!)
                    // 통신 결과값을 Room을 이용하여 DB update : 일반 사용자 정보
                    userDao.update(
                        TorangPreference().getUserId(context),
                        user.userName!!,
                        user.profile_pic_url!!
                    )
                    response = EditProfileResponse.SUCCESS
                }
            }
        } catch (e: Exception) {
            Logger.e(e.toString())
        }

        return response
    }


    override fun getUser(): LiveData<LoggedInUserData?> {
        return loggedInUserDao.getLoggedInUserData()
    }
}

@Module
@InstallIn(SingletonComponent::class)
abstract class EditProfileRepositoryModule {
    @Binds
    abstract fun provideEditProfileRepository(editProfileRepositoryImpl: EditProfileRepositoryImpl): EditProfileRepository
}
