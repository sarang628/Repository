package com.example.torangrepository

import android.content.Context
import androidx.lifecycle.LiveData
import com.example.torang_core.data.dao.RestaurantDao
import com.example.torang_core.data.model.*
import com.example.torang_core.repository.InfoRepository
import com.example.torang_core.repository.MapRepository
import com.example.torang_core.repository.MenuRepository
import com.example.torang_core.repository.ReviewRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TorangRepository @Inject constructor(
    @ApplicationContext context: Context,
    private val restaurantService: RestaurantService,
    private val restaurantDao: RestaurantDao
) :
    InfoRepository, ReviewRepository, MenuRepository,
    MapRepository {


    override suspend fun loadRestaurant(restaurantId: Int): Restaurant {
        return restaurantService.getRestaurant(HashMap<String, String>().apply {
            put("restaurant_id", restaurantId.toString())
        })
    }

    override suspend fun loadMenus(restaurantId: Int): ArrayList<Menu> {
        return restaurantService.getMenus(HashMap<String, String>().apply {
            put("restaurant_id", restaurantId.toString())
        })
    }

    override suspend fun loadHours(restaurantId: Int): ArrayList<HoursOfOperation> {
        return restaurantService.getHoursOfOperation(HashMap<String, String>().apply {
            put("restaurant_id", restaurantId.toString())
        })
    }

    override suspend fun getReviews(restaurantId: Int): ArrayList<Review> {
        return restaurantService.getReviews(HashMap<String, String>().apply {
            put("restaurant_id", restaurantId.toString())
        })
    }

    override suspend fun getMenus(restaurantId: Int): ArrayList<Menu> {
        return restaurantService.getMenus(HashMap<String, String>().apply {
            put("restaurant_id", restaurantId.toString())
        })
    }

    override fun getRestaurant(): LiveData<List<RestaurantData>> {
        return restaurantDao.getRestaurant()
    }

    override suspend fun loadRestaurant() {
        val list = restaurantService.getAllRestaurant(HashMap())
        val data = ArrayList<RestaurantData>()
        for (restaurant in list) {
            data.add(RestaurantData.parse(restaurant))
        }
        restaurantDao.insertAllRestaurant(data)
    }
}

@Module
@InstallIn(SingletonComponent::class)
abstract class TorangRepositoryModule {
    @Binds
    abstract fun provideInfoRepository(torangRepository: TorangRepository): InfoRepository

    @Binds
    abstract fun provideReviewRepository(torangRepository: TorangRepository): ReviewRepository

    @Binds
    abstract fun provideMenuRepository(torangRepository: TorangRepository): MenuRepository

    @Binds
    abstract fun provideMapRepository(torangRepository: TorangRepository): MapRepository
}