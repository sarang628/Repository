package com.example.torangrepository

import com.example.torang_core.data.dao.FeedDao
import com.example.torang_core.repository.ReportAfterSupport
import com.example.torang_core.repository.ReportReason
import com.example.torang_core.repository.ReportRepository
import com.example.torang_core.util.Logger
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ReportRepositoryImpl @Inject constructor(
    private val reportService: ReportService,
    private val feedDao: FeedDao
) : ReportRepository {

    override suspend fun sendReportReason(reportReason: ReportReason, reviewId: Int): Boolean {
        val map = HashMap<String, String>().apply {
            put("reportReason", reportReason.name)
            put("reviewId", reviewId.toString())
        }

        if (reportService.reportReason(map)) {
            //신고 성공 후 해당 피드 로컬 데이터에서 삭제하기
            feedDao.deleteFeed(reviewId)
            return true
        }
        return false
    }

    override suspend fun sendReportAfterSupport(
        reportAfterSupport: ReportAfterSupport,
        reviewId: Int
    ): Boolean {
        return true
    }

    override suspend fun hasFeed(reviewId: Int): Boolean {
        return try {
            reportService.hasFeed(HashMap<String, String>().apply {
                put("review_id", reviewId.toString())
            })
        } catch (e: Exception) {
            Logger.e(e.toString())
            false
        }
        return false
    }
}

@Module
@InstallIn(SingletonComponent::class)
abstract class ReportRepositoryModule {
    @Binds
    abstract fun provideReportRepository(reportRepositoryImpl: ReportRepositoryImpl): ReportRepository
}