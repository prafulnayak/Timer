package com.yougov.aevum

import java.lang.Exception

object Utility {

    fun getTimeInLong(hours:String, minutes:String, second: String) : Long?{
        return try{
            (hours.toLong() * 60 * 60 * 1000) + (minutes.toLong() * 60 * 1000) + (second.toLong() * 1000)
        }catch (e:Exception){
            null
        }
    }

}