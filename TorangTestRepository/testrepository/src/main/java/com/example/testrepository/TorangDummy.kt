package com.example.testrepository

import android.content.Context
import com.example.testrepository.getRawToString.getRawtoString
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.example.torang_core.data.model.Review

object TorangDummy {
    fun getReview(context: Context): Review {
        val type = TypeToken.getParameterized(ArrayList::class.java, Review::class.java).type
        val list: ArrayList<Review> =
            Gson().fromJson(context.getRawtoString(R.raw.reviews), type)

        return list[0]
    }
}