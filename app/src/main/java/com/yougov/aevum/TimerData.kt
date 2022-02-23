package com.yougov.aevum

import java.io.Serializable

data class TimerData(
    val id: Int,
    var time: Long
): Serializable