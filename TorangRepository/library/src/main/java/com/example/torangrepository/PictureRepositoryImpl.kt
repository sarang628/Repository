package com.example.torangrepository

import androidx.lifecycle.LiveData
import com.example.torang_core.data.dao.PictureDao
import com.example.torang_core.data.model.Picture
import com.example.torang_core.data.model.ReviewImage
import com.example.torang_core.repository.PicturesRepository
import com.example.torang_core.util.Logger
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PicturesRepositoryImpl @Inject constructor(
    private val restaurantService: RestaurantService,
    private val pictureDao: PictureDao
) :
    PicturesRepository {
    override suspend fun getPictures(restaurantId: Int): ArrayList<Picture> {
        return restaurantService.getPictures(HashMap<String, String>().apply {
            put("restaurant_id", restaurantId.toString())
        })
    }

    override fun getFeedPicture(reviewId: Int): LiveData<List<ReviewImage>> {
        Logger.d(reviewId)
        return pictureDao.getFeedImage(reviewId)
    }
}

@Module
@InstallIn(SingletonComponent::class)
abstract class PictureRepositoryModule {
    @Binds
    abstract fun bindPictureRepositoryModule(pictureRepositoryImpl: PicturesRepositoryImpl)
            : PicturesRepository
}