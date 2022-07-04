package com.example.torangrepository

import android.content.Context
import androidx.lifecycle.LiveData
import com.example.torang_core.data.dao.SearchDao
import com.example.torang_core.data.model.Search
import com.example.torang_core.repository.SearchRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SearchRepositoryImpl @Inject constructor(
    @ApplicationContext context: Context,
    private val searchDao: SearchDao
) :
    SearchRepository {
    override fun getHistoryKeywords(): LiveData<List<Search>> {
        return searchDao.getHistoryKeywords()
    }

    override suspend fun saveHistory(keyword: String) {
        val search = Search(keyword = keyword)
        searchDao.insertAll(search)
    }

    override suspend fun removeKeyword(search: Search) {
        searchDao.delete(search)
    }
}

@Module
@InstallIn(SingletonComponent::class)
abstract class SearchRepositoryModule {
    @Binds
    abstract fun provideSearchRepositoryImpl(searchRepositoryImpl: SearchRepositoryImpl): SearchRepository
}
