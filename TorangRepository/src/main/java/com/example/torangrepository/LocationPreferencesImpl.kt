package com.example.torangrepository

import android.content.Context
import android.content.SharedPreferences
import com.example.torang_core.data.LocationPreferences
import com.example.torang_core.util.Logger
import dagger.hilt.android.qualifiers.ApplicationContext
import java.util.prefs.Preferences
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class LocationPreferencesImpl @Inject constructor(@ApplicationContext val context: Context) :
    LocationPreferences {
    override suspend fun isFirstRequestLocationPermission(): Boolean {
        Logger.d("")
        val b = getPref().getBoolean("isFirstRequestLocationPermission", false)
        return b
    }

    override fun requestLocationPermission() {
        getPref().edit()
            .putBoolean("isFirstRequestLocationPermission", true)
            .commit()
    }

    fun getPref(): SharedPreferences {
        return context.getSharedPreferences("torang", Context.MODE_PRIVATE)
    }
}