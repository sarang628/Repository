package com.example.torangrepository

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.location.LocationManager
import com.example.torang_core.data.LocationPreferences
import com.example.torang_core.data.dao.RestaurantDao
import com.example.torang_core.data.model.*
import com.example.torang_core.repository.FindRepository
import com.example.torang_core.repository.RequestLocationResult
import com.example.torang_core.util.Logger
import dagger.hilt.android.qualifiers.ApplicationContext
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

    @Singleton
    class FindRepositoryImpl @Inject constructor(
        private val restaurantDao: RestaurantDao,
        private val restaurantService: RestaurantService,
        private val locationPreferences: LocationPreferences,
        @ApplicationContext private val context: Context
    ) : FindRepository {
        // 최초 위치요쳥을 false로 설정 시 화면단에서 요청해야함
        private val isFirstRequestLocation = MutableStateFlow(false)
        private val isRequestingLocation = MutableStateFlow(false)
        private val currentPosition = MutableStateFlow(0)
        private val showRestaurantCardAndFilter = MutableStateFlow(true)

        // 위치요청을 처음 했는지 여부
        private val isFirstRequestLocationPermission = MutableStateFlow(false)

        // 권한이 있는지 여부
        private val hasGrantPermission: MutableStateFlow<Int> =
            MutableStateFlow<Int>(context.checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION))

        private val restaurants = MutableStateFlow<List<Restaurant>>(ArrayList())

        private val manager: LocationManager =
            context.getSystemService(Context.LOCATION_SERVICE) as LocationManager

        override suspend fun searchIfRestaurantEmpty() {
            if (restaurants.value.isEmpty()) {
                restaurants.emit(restaurantService.getAllRestaurant(HashMap()))
            }
        }

        /**
         * 화면 첫 진입 시 위치를 요청해야하는지에 대한 상태
         */
        override fun getIsFirstRequestLocation(): StateFlow<Boolean> {
            return isFirstRequestLocation
        }

        /**
         * 다른화면과의 상태 공유를 위해 저장소에서 위치요청을 했다고 알려줘야합니다.
         */
        override suspend fun notifyRequestLocation(): RequestLocationResult {
            // 위치 요청이 처음이라면 권한 필요 팝업 요청
            if (!locationPreferences.isFirstRequestLocationPermission()) {
                locationPreferences.requestLocationPermission()
                return RequestLocationResult.NEED_LOCATION_PERMISSION
            }

            // 첫 권한 필요 팝업을 거부했거나 권한이 없을경우 다른 권한 필요 팝업 요청
            if (context.checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_DENIED) {
                return RequestLocationResult.PERMISSION_DENIED
            }

            val statusOfGPS: Boolean = manager.isProviderEnabled(LocationManager.GPS_PROVIDER)

            if (!statusOfGPS) {
                return RequestLocationResult.GPS_OFF
            }

            // Map화면과 공유하기위한 상태 변경
            isFirstRequestLocation.emit(true)
            isRequestingLocation.emit(true)

            //성공 리턴
            return RequestLocationResult.SUCCESS
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

        override suspend fun setCurrentPosition(position: Int) {
            currentPosition.emit(position)
            //추가 카드 보여지게 변경
            if (!showRestaurantCardAndFilter.value)
                showRestaurantCardAndFilter.emit(true)
        }

        override fun getCurrentPosition(): StateFlow<Int> {
            return currentPosition
        }

        override suspend fun requestLocationPermission(b: Boolean) {
            locationPreferences.requestLocationPermission()
            isFirstRequestLocationPermission.emit(locationPreferences.isFirstRequestLocationPermission())
        }

        override fun hasGrantPermission(): MutableStateFlow<Int> {
            return hasGrantPermission
        }

        override suspend fun permissionGranated() {
            Logger.d("check emit hasGrantPermission = ${hasGrantPermission.value}, PackageManager.PERMISSION_GRANTED = ${PackageManager.PERMISSION_GRANTED}")
            hasGrantPermission.emit(PackageManager.PERMISSION_GRANTED)
        }

        override fun showRestaurantCardAndFilter(): StateFlow<Boolean> {
            return showRestaurantCardAndFilter
        }

        override suspend fun clickMap() {
            showRestaurantCardAndFilter.emit(!showRestaurantCardAndFilter.value)
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
            searchType: SearchType
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

    override suspend fun clickMap() {
        TODO("Not yet implemented")
    }

    override fun getCurrentPosition(): StateFlow<Int> {
        TODO("Not yet implemented")
    }

    override fun getIsFirstRequestLocation(): StateFlow<Boolean> {
        TODO("Not yet implemented")
    }

    override fun getSearchedRestaurant(): Flow<List<Restaurant>> {
        TODO("Not yet implemented")
    }

    override fun hasGrantPermission(): MutableStateFlow<Int> {
        TODO("Not yet implemented")
    }

    override fun isRequestingLocation(): StateFlow<Boolean> {
        TODO("Not yet implemented")
    }

    override suspend fun notifyReceiveLocation() {
        TODO("Not yet implemented")
    }

    override suspend fun notifyRequestLocation(): RequestLocationResult {
        TODO("Not yet implemented")
    }

    override suspend fun permissionGranated() {
        TODO("Not yet implemented")
    }

    override suspend fun requestLocationPermission(b: Boolean) {
        TODO("Not yet implemented")
    }

    override suspend fun searchIfRestaurantEmpty() {
        TODO("Not yet implemented")
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
        searchType: SearchType
    ) {
        TODO("Not yet implemented")
    }

    override suspend fun setCurrentPosition(position: Int) {
        TODO("Not yet implemented")
    }

    override fun showRestaurantCardAndFilter(): StateFlow<Boolean> {
        TODO("Not yet implemented")
    }
}