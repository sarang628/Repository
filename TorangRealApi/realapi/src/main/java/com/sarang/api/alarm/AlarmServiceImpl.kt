package com.sarang.api.alarm

import com.sarang.api.Constants
import com.example.torang_core.data.model.Alarm
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Response
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST
import java.io.IOException
import java.util.concurrent.TimeUnit

interface AlarmServiceImpl /*: AlarmService */ {

    @FormUrlEncoded
    @POST("/getAlarms")
    suspend fun getMyAlarms(@Field("user_id") userId: Int): ArrayList<Alarm>

    companion object {
        fun create(): AlarmServiceImpl {

            val httpClient = OkHttpClient.Builder()
            val logger = HttpLoggingInterceptor()
            logger.setLevel(HttpLoggingInterceptor.Level.HEADERS)
            logger.setLevel(HttpLoggingInterceptor.Level.BODY)
            httpClient.addInterceptor(logger)
            httpClient.writeTimeout(10, TimeUnit.SECONDS)
            httpClient.connectTimeout(10, TimeUnit.SECONDS)
            httpClient.writeTimeout(10, TimeUnit.SECONDS)
            httpClient.readTimeout(10, TimeUnit.SECONDS)

            httpClient.addInterceptor(object : Interceptor {
                @Throws(IOException::class)
                override fun intercept(chain: Interceptor.Chain): Response {
                    val original = chain.request()
                    val request = original.newBuilder()
                        .header("User-Agent", "android")
                        .header(
                            "accessToken",  /*BananaPreference.getInstance(application).getAccessToken()*/
                            ""
                        )
                        .method(original.method, original.body)
                        .build()
                    return chain.proceed(request)
                }
            })


            return Retrofit.Builder()
                .baseUrl(Constants.API_URL)
                .client(httpClient.build())
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(AlarmServiceImpl::class.java)
        }
    }
}