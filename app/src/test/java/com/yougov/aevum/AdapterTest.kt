package com.yougov.aevum

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import org.junit.Assert
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
class AdapterTest {
    @Test
    fun adapter_position_reversed_test() = runBlocking(Dispatchers.IO) {
        val adapter = TimerAdapter()
        Assert.assertEquals(1, timerList[0].id)
        Assert.assertEquals(2, timerList[1].id)

        adapter.submitTimerList(timerList)

        val timerData1 = adapter.currentList[0]
        Assert.assertEquals(2, timerData1.id)
        val timerData2 = adapter.currentList[1]
        Assert.assertEquals(1, timerData2.id)
    }

    companion object {
        val timerList = listOf<TimerData>(TimerData(1, 5000), TimerData(2, 2000))
    }
}