package com.kennah.wecatch.core.base

import android.content.Context
import android.widget.RelativeLayout

open class BaseHolder<T: Any>(context: Context) : RelativeLayout(context) {

    lateinit var mData: T

    open fun bind(data: T) {
        mData = data
    }
}