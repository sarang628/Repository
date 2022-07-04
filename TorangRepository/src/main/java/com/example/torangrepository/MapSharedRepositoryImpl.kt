package com.example.torangrepository

import com.example.torang_core.data.dao.RestaurantDao
import com.example.torang_core.data.model.Filter
import com.example.torang_core.data.model.RestaurantData
import com.example.torang_core.repository.MapSharedRepository
import com.example.torang_core.util.Logger
import com.example.torangrepository.DistanceManager.getDistance
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
open class MapSharedRepositoryImpl @Inject constructor(
    private val restaurantDao: RestaurantDao,
    private val restaurantService: RestaurantService
) :
    MapSharedRepository {

    override suspend fun searchRestaurant(
        latitude: Double,
        longitude: Double
    ): List<RestaurantData> {
        Logger.d("$latitude, $longitude")
        val list = restaurantDao.getRestaurantDistance()
        return list.sortedWith { o1, o2 ->
            when {
                getDistance(latitude, longitude, o1.lat!!, o1.lon!!) <
                        getDistance(latitude, longitude, o2.lat!!, o2.lon!!) -> -1
                getDistance(latitude, longitude, o1.lat!!, o1.lon!!) ==
                        getDistance(latitude, longitude, o2.lat!!, o2.lon!!) -> 0
                else -> 1
            }
        }
    }

    override suspend fun searchRestaurant(keyword: String): List<RestaurantData> {
        val filter = Filter()
        filter.keyword = keyword
        val list = ArrayList<RestaurantData>()
        val restaurants = restaurantService.getFilterRestaurant(filter)
        for (restaurant in restaurants) {
            list.add(RestaurantData.parse(restaurant))
        }
        return list
    }

    override suspend fun getFilterRestaurant(filter: Filter): List<RestaurantData> {
        val list = ArrayList<RestaurantData>()
        val restaurants = restaurantService.getFilterRestaurant(filter)
        for (restaurant in restaurants) {
            list.add(RestaurantData.parse(restaurant))
        }
        restaurantDao.insertAllRestaurant(list)
        return list
    }
}

@Module
@InstallIn(SingletonComponent::class)
abstract class MapShareRepositoryProvider {
    @Binds
    abstract fun provideMapShareRepository(mapSharedRepositoryImpl: MapSharedRepositoryImpl): MapSharedRepository
}