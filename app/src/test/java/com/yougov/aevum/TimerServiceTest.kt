package com.yougov.aevum

import android.content.Intent
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.Robolectric
import org.robolectric.RobolectricTestRunner
import org.robolectric.RuntimeEnvironment
import org.robolectric.android.controller.ServiceController


@RunWith(RobolectricTestRunner::class)
class TimerServiceTest {
    private var service: TimerService? = null
    private var controller: ServiceController<TimerService>? = null

    @Before
    fun setUp() {
        controller = Robolectric.buildService(TimerService::class.java)
        service = controller?.create()?.get()
    }

    @Test
    fun testWithIntent() {
        runBlocking {
            val intent = Intent(RuntimeEnvironment.application, TimerService::class.java)
            intent.putExtra("timer", TimerData(1, 5000))
            controller!!.withIntent(intent).startCommand(0, 0)
            service?.timerLiveList?.observeForever { }
            val list = service?.timerLiveList?.value
            Assert.assertNotNull(list)

            val timerData = list?.find { it.id == 1 }
            Assert.assertEquals(timerData?.id, 1)
        }
    }

    @Test
    fun testServiceStopped() {
        runBlocking {
            val intent = Intent(RuntimeEnvironment.application, TimerService::class.java)
            intent.putExtra("timer", TimerData(1, 5000))
            controller!!.withIntent(intent).startCommand(0, 0)
            service?.timerLiveList?.observeForever { }
            val list = service?.timerLiveList?.value
            Assert.assertNotNull(list)
            service?.stopRunning()
            service?.let { Assert.assertFalse(it.getRunningStatus()) }

        }
    }

    @After
    fun tearDown() {
        controller?.destroy()
    }
}