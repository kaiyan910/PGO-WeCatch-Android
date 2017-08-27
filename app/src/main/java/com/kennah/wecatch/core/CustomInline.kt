package com.kennah.wecatch.core

import android.content.Context
import android.content.pm.PackageManager
import android.os.Handler
import android.support.v4.app.ActivityCompat

inline fun hasPermission(context: Context, permission: String, block: () -> Unit) {
    if (ActivityCompat.checkSelfPermission(context, permission) == PackageManager.PERMISSION_GRANTED) block()
}

inline fun withDelay(delay: Long, crossinline block: () -> Unit) {
    Handler().postDelayed({ block() }, delay)
}