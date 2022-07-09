package com.example.torangrepository

import com.example.torang_core.data.dao.RestaurantDao
import com.example.torang_core.data.model.*
import com.example.torang_core.repository.FindRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class FindRepositoryImpl @Inject constructor(
    private val restaurantDao: RestaurantDao,
    private val restaurantService: RestaurantService
) : FindRepository {
    //최초 위치요쳥을 false로 설정 시 화면단에서 요청해야함
    val isFirstRequestLocation = MutableStateFlow(false)
    val isRequestingLocation = MutableStateFlow(false)

    val restaurants = MutableStateFlow<List<Restaurant>>(ArrayList())

    /**
     * 화면 첫 진입 시 위치를 요청해야하는지에 대한 상태
     */
    override fun getIsFirstRequestLocation(): StateFlow<Boolean> {
        return isFirstRequestLocation
    }

    /**
     * 위치를 요청했다고 알려줌
     */
    override suspend fun notifyRequestLocation() {
        isFirstRequestLocation.emit(true)
        isRequestingLocation.emit(true)
    }

    /**
     * 현재 위치를 요청중인지 상태
     */
    override fun isRequestingLocation(): StateFlow<Boolean> {
        return isRequestingLocation
    }

    /**
     * 위치를 받았다고 알려줌
     */
    override suspend fun notifyReceiveLocation() {
        isRequestingLocation.emit(false)
    }

    override fun getSearchedRestaurant(): Flow<List<Restaurant>> {
        return restaurants
    }

    override suspend fun searchRestaurant(
        distances: Distances?,
        restaurantType: ArrayList<RestaurantType>?,
        prices: Prices?,
        ratings: ArrayList<Ratings>?,
        latitude: Double,
        longitude: Double,
        northEastLatitude: Double,
        northEastLongitude: Double,
        southWestLatitude: Double,
        southWestLongitude: Double,
        searchType: Filter.SearchType
    ) {
        val filter = Filter().apply {
            distances?.let { this.distances = it }
            restaurantType?.let { this.restaurantTypes = restaurantType }
            prices?.let { this.prices = prices }
            ratings?.let { this.ratings = ratings }
            this.lat = latitude
            this.lon = longitude
            this.searchType = searchType
            this.northEastLongitude = northEastLongitude
            this.northEastLatitude = northEastLatitude
            this.southWestLongitude = southWestLongitude
            this.southWestLatitude = southWestLatitude
        }
        val result = restaurantService.getFilterRestaurant(filter)
        val list = ArrayList<RestaurantData>()
        for (restaurant in result) {
            list.add(RestaurantData.parse(restaurant))
        }
        restaurantDao.insertAllRestaurant(list)
        restaurants.emit(result)
    }
}