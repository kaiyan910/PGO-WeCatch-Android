package com.kennah.wecatch.core.utils

import android.content.Context
import com.kennah.wecatch.core.HttpCode
import com.kennah.wecatch.core.exception.CoreException
import com.kennah.wecatch.core.exception.NetworkException
import retrofit2.Response
import java.io.IOException

object ServiceUtils {

    @Throws(NetworkException::class, CoreException::class)
    fun <T> call(context: Context, invoke: () -> Response<T>): T {

        CommonUtils.checkNetwork(context)

        try {

            val response:Response<T> = invoke()

            when {
                response.isSuccessful -> {
                    return response.body()!!
                }
                else -> throw CoreException(response.code())
            }

        } catch (e: IOException) {

            LogUtils.error(this, e)
            throw CoreException(HttpCode.SERVER_UNAVAILABLE)

        } catch (e: IllegalArgumentException) {

            LogUtils.error(this, e)
            throw CoreException(HttpCode.SERVER_UNAVAILABLE)
        }
    }
}