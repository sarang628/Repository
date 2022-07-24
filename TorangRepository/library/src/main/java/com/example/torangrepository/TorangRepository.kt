package com.example.torangrepository

import android.content.Context
import androidx.lifecycle.LiveData
import com.example.torang_core.data.dao.RestaurantDao
import com.example.torang_core.data.data.Location
import com.example.torang_core.data.model.*
import com.example.torang_core.repository.InfoRepository
import com.example.torang_core.repository.MapRepository
import com.example.torang_core.repository.MenuRepository
import com.example.torang_core.repository.ReviewRepository
import com.example.torang_core.util.Logger
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
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

    private val mapClick = MutableStateFlow<Boolean>(false)
    private val location = MutableStateFlow(Location(0.0, 0.0))

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

    override fun getClickMap(): Flow<Boolean> {
        return mapClick
    }

    override suspend fun clickMap() {
        mapClick.emit(!mapClick.value)
    }

    var _northEastLatitude = 0.0
    override fun setNorthEastLatitude(latitude: Double) {
        _northEastLatitude = latitude
    }

    var _notthEastLongitude = 0.0
    override fun setNorthEastLongitude(longitude: Double) {
        _notthEastLongitude = longitude
    }

    var _southWestLatitude = 0.0
    override fun setSouthWestLatitude(latitude: Double) {
        _southWestLatitude = latitude
    }

    var _southWestLongitude = 0.0
    override fun setSouthWestLongitude(longitude: Double) {
        _southWestLongitude = longitude
    }

    override fun getNorthEastLatitude(): Double {
        return _northEastLatitude
    }

    override fun getNorthEastLongitude(): Double {
        return _notthEastLongitude
    }

    override fun getSouthWestLatitude(): Double {
        return _southWestLatitude
    }

    override fun getSouthWestLongitude(): Double {
        return _southWestLongitude
    }

    override suspend fun showCard() {
        if (mapClick.value)
            mapClick.emit(false)
    }

    override suspend fun setCurrentLocation(location: Location) {
        Logger.d("lat : ${location.latitude} , lon : ${location.longitude}")
        this.location.emit(location)
    }

    override fun getCurrentLocationFlow(): StateFlow<Location> {
        return location
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