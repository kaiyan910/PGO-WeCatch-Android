package com.kennah.wecatch.local

import java.text.SimpleDateFormat
import java.util.*

object Constant {

    @JvmField
    val NOTIFICATION_RESULT = "notification_result"
    @JvmField
    val NOTIFICATION_ACTION_STOP = "wecatch_android.notification.stop"
    @JvmField
    val NOTIFICATION_ACTION_STOP_MAP = "wecatch_android.notification.stop.map"
    @JvmField
    val NOTIFICATION_BROADCAST = 88833
    @JvmField
    val MAP_PROVIDER_GOOGLE = "GOOGLE"
    @JvmField
    val MAP_PROVIDER_OSM = "OSM"

    @JvmField
    val POKEMON_GENERATION = 3

    @JvmField
    val MAX_POKEMON = 386
    @JvmField
    val GYM = (0..5)
    @JvmField
    val GEN_1 = (1..151)
    @JvmField
    val GEN_2 = (152..251)
    @JvmField
    val GEN_3 = (252..386)
    @JvmField
    val GENERATION = listOf(GEN_1, GEN_2, GEN_3)
    @JvmField
    val MOVE_SET = 281

    @JvmField
    val REQ_FILTER = 10001

    @JvmField
    val DEFAULT_RARE_SEARCH = arrayOf(
            3, 6, 9, 26, 31, 34, 45, 59, 62, 65, 68, 71, 76, 94, 103, 112, 113, 130, 131, 134, 135, 136, 137, 143, 147, 148, 149,
            154, 157, 160, 176, 179, 180, 181, 201, 217, 221, 229, 232, 233, 241, 242, 246, 247, 248
    )

    @JvmField
    val DATE_FORMATTER = SimpleDateFormat("HH:mm:ss dd/MM/yyyy", Locale.getDefault())
}