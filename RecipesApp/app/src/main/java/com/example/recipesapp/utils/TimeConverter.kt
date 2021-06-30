package com.example.recipesapp.utils

class TimeConverter {
    fun longToString(milliseconds: Long): String {
        val h = ((milliseconds / (1000 * 60 * 60)) % 24).toInt()
        val m = ((milliseconds / (1000 * 60)) % 60).toInt()
        return if (h > 0)
            "${h}h ${m}m"
        else
            "${m}m"
    }

    fun hourAndMinuteToLong(h: Int, m: Int) = (h * 60 + m) * 1000L * 60
}