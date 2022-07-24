package com.example.torangrepository

import com.example.torang_core.data.NationItem
import com.example.torang_core.data.dao.RestaurantDao
import com.example.torang_core.data.model.Restaurant
import com.example.torang_core.repository.NationRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class NationRepositoryImpl @Inject constructor(
    private val restaurantDao: RestaurantDao,
    private val restaurantService: RestaurantService
) : NationRepository, MapSharedRepositoryImpl(restaurantDao, restaurantService) {

    private val selectNationItem: MutableStateFlow<NationItem> = MutableStateFlow(NationItem())

    override suspend fun getNationItems(): List<NationItem> {
        TODO("Not yet implemented")
    }

    override suspend fun findRestaurant(): List<Restaurant> {
        TODO("Not yet implemented")
    }

    override fun getSelectNationItem(): StateFlow<NationItem> {
        return selectNationItem
    }

    override suspend fun selectNationItem(nationItem: NationItem) {
        selectNationItem.emit(nationItem)
    }

}

