package com.kennah.wecatch.local.filter

import com.kennah.wecatch.local.FilterManager

class FilterHandlerFactory(private val filterManager: FilterManager) {

    companion object {
        @JvmStatic
        val NORMAL = 0
        @JvmStatic
        val NOTIFICATION = 1
    }

    fun create(type: Int): FilterHandler = when (type) {
        NORMAL -> NormalFilterHandler(filterManager)
        else -> NotificationFilterHandler(filterManager)
    }
}