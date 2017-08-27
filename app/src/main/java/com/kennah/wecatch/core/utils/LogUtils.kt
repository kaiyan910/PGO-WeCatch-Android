package com.kennah.wecatch.core.utils

import android.util.Log
import com.kennah.wecatch.BuildConfig

object LogUtils {

    fun debug(tag: String, message: String, vararg args: Any) {
        if (BuildConfig.DEBUG) {
            Log.d(tag, String.format(message, *args))
        }
    }

    fun error(tag: String, exception: Exception) {
        if (BuildConfig.DEBUG) {
            Log.e(tag, exception.message, exception)
        }
    }

    fun debug(instance: Any, message: String, vararg args: Any) {
        debug(instance.javaClass.simpleName, message, *args)
    }

    fun error(instance: Any, exception: Exception) {
        error(instance.javaClass.simpleName, exception)
    }
}
