package com.example.torangrepository.modules

import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class RetrofitModule @Inject constructor() {
    /** 레트로핏 제공 */
    fun getRetrofit(httpClient: OkHttpClient, API_URL: String): Retrofit {
        return Retrofit.Builder()
            .client(httpClient)
            .baseUrl(API_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }
}