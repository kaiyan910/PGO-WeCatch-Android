package com.kennah.wecatch.core.utils

import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit

object TimeUtils {

    fun getTimeLeft(expireTime: Long, expired: String): String {
        val value = expireTime - System.currentTimeMillis()
        return if (value > 0) {
            val date = Date(value)
            SimpleDateFormat("mm:ss", Locale.getDefault()).format(date)
        } else {
            expired
        }
    }

    fun getTimeLeftWithHour(expireTime: Long, expired: String): String {
        val millis = expireTime - System.currentTimeMillis()
        return if (millis > 0) {
            String.format("%02d:%02d:%02d",
                    TimeUnit.MILLISECONDS.toHours(millis),
                    TimeUnit.MILLISECONDS.toMinutes(millis) -
                            TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(millis)),
                    TimeUnit.MILLISECONDS.toSeconds(millis) -
                            TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(millis)))
        } else {
            expired
        }
    }

    fun isExpired(expireTime: Long?): Boolean {

        if (expireTime == null) return true

        return expireTime - System.currentTimeMillis() <= 0
    }
}