package com.kennah.wecatch.core

object HttpCode {

    @JvmField
    val NO_NETWORK:Int = 0

    @JvmField
    val SUCCESS:Int = 200
    @JvmField
    val UNAUTHORIZED:Int = 401
    @JvmField
    val NOT_FOUND:Int = 400
    @JvmField
    val INTERNAL_SERVER_ERROR:Int = 500
    @JvmField
    val SERVER_UNAVAILABLE:Int = 503
}