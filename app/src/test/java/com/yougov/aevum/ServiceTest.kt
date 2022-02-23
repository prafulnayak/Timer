package com.yougov.aevum

import androidx.test.core.app.ActivityScenario
import org.junit.Assert
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner


@RunWith(RobolectricTestRunner::class)
class ServiceTest {

    @Test
    fun testActivity(){
        val scenario = ActivityScenario.launch(MainActivity::class.java)
        Assert.assertNotNull(scenario)
        scenario.close()
    }
}