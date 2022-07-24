package com.example.torangrepository

import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.torang_core.data.model.LoggedInUserData
import com.example.torang_core.repository.ReportReason
import kotlinx.coroutines.runBlocking

import org.junit.Test
import org.junit.runner.RunWith

import org.junit.Assert.*

/**
 * Instrumented test, which will execute on an Android device.
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
@RunWith(AndroidJUnit4::class)
class ExampleInstrumentedTest {
    @Test
    fun useAppContext() {
        // Context of the app under test.
        val appContext = InstrumentationRegistry.getInstrumentation().targetContext
        assertEquals("com.example.torangrepository.test", appContext.packageName)
    }

    @Test
    fun test() {
        /*val appContext = InstrumentationRegistry.getInstrumentation().targetContext
        val feedRepository = FeedRepositoryImpl(appContext)

        runBlocking {
            feedRepository.loadFeed()
        }*/
    }

    /*@Test
    fun sendReportReasonTest() {
        val appContext = InstrumentationRegistry.getInstrumentation().targetContext
        val reportRepository =
            ReportRepositoryImpl(appContext, LocalReportService().create(appContext))

        runBlocking {
            assertEquals(true, reportRepository.sendReportReason(ReportReason.SPAM, 10))
        }
    }*/
}