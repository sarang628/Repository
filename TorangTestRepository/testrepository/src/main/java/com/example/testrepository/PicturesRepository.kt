package com.example.testrepository

import android.content.Context
import androidx.lifecycle.LiveData
import com.example.testrepository.getRawToString.getRawtoString
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.example.torang_core.data.model.Picture
import com.example.torang_core.data.model.ReviewImage
import com.example.torang_core.util.Logger
import com.example.torang_core.repository.PicturesRepository

private class PicturesRepositoryImpl constructor(val context: Context) :
    PicturesRepository {
    override suspend fun getPictures(restaurantId: Int): ArrayList<Picture> {
        Logger.d("load pictures $restaurantId")
        val type = TypeToken.getParameterized(ArrayList::class.java, Picture::class.java).type
        val list: ArrayList<Picture> =
            Gson().fromJson(context.getRawtoString(R.raw.pictures), type)
        return list
    }

    override fun getFeedPicture(reviewId: Int): LiveData<List<ReviewImage>> {
        TODO("Not yet implemented")
    }
}