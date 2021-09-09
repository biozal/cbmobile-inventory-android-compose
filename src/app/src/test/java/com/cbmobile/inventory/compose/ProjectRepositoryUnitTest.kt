package com.cbmobile.inventory.compose

import android.app.Application
import android.content.Context
import androidx.test.core.app.ApplicationProvider
import com.cbmobile.inventory.compose.data.projects.ProjectRepositoryDb
import org.junit.Test
import org.junit.Assert.*

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class ProjectRepositoryUnitTest {
    private val repository = ProjectRepositoryDb(ApplicationProvider.getApplicationContext<Context>())

    @Test
    fun testLocations() {
        //val results = repository.getLocations()
        //assertNotNull(results)
        //assert(results.count() > 0) { "Error: locations shouldn't be zero" }
    }
}