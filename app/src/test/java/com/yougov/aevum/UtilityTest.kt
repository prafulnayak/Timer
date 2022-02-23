package com.yougov.aevum

import org.junit.Assert
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
class UtilityTest {
    @Test
    fun test_text_to_long_conversion() {
        val timeInLong = Utility.getTimeInLong(VALID_HOUR, VALID_MIN, VALID_SEC)
        Assert.assertEquals(
            ((60 * 60 * 1000).toLong() + (10 * 60 * 1000).toLong() + (10 * 1000).toLong()),
            timeInLong
        )
    }

    @Test
    fun test_invalid_input_gives_null() {
        val timeInLong = Utility.getTimeInLong(INVALID_HOUR, VALID_MIN, VALID_SEC)
        Assert.assertNull(timeInLong)
    }

    companion object {
        const val VALID_HOUR = "1"
        const val VALID_MIN = "10"
        const val VALID_SEC = "10"
        const val INVALID_HOUR = " "
    }
}