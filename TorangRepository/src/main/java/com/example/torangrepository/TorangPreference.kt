package com.example.torangrepository

import android.content.Context
import android.content.SharedPreferences

open class TorangPreference {
    private fun getPreference(context: Context): SharedPreferences {
        return context.getSharedPreferences("Torang", Context.MODE_PRIVATE)
    }

    fun saveAccessToken(context: Context?, accessToken: String) {
        context?.let {
            getPreference(it).edit().putString("accessToken", accessToken).apply()
        }
    }

    fun getAccessToken(context: Context): String? {
        return getPreference(context).getString("accessToken", "")
    }

    fun saveUserId(context: Context?, userId: Int) {
        context?.let {
            getPreference(it).edit().putInt("userId", userId).apply()
        }
    }

    fun getUserId(context: Context?): Int {
        return if (context != null)
            getPreference(context).getInt("userId", -1)
        else
            0
    }

    open fun logout(context: Context) {
        context.let {
            getPreference(context).edit().putInt("userId", -1)
                .commit()
        }
    }
}