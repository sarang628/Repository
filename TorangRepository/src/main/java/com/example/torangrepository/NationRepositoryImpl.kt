package com.example.torangrepository

import com.example.torang_core.data.NationItem
import com.example.torang_core.data.dao.RestaurantDao
import com.example.torang_core.data.model.Restaurant
import com.example.torang_core.repository.NationRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class NationRepositoryImpl @Inject constructor(
    private val restaurantDao: RestaurantDao,
    private val restaurantService: RestaurantService
) :
    NationRepository,
    MapSharedRepositoryImpl(restaurantDao, restaurantService) {
    override suspend fun getNationItems(): List<NationItem> {
        TODO("Not yet implemented")
    }

    override suspend fun findRestaurant(): List<Restaurant> {
        TODO("Not yet implemented")
    }

}

@Module
@InstallIn(SingletonComponent::class)
abstract class NationRepositoryProvider {
    @Binds
    abstract fun provideNationRepository(nationRepositoryImpl: NationRepositoryImpl): NationRepository
}

