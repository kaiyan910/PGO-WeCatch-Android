package com.kennah.wecatch.core.base

import com.kennah.wecatch.core.HttpCode
import com.kennah.wecatch.core.exception.CoreException
import com.kennah.wecatch.core.exception.NetworkException
import com.kennah.wecatch.core.utils.LogUtils
import com.kennah.wecatch.local.model.WeCatch
import kotlinx.coroutines.experimental.Deferred
import kotlinx.coroutines.experimental.android.UI
import kotlinx.coroutines.experimental.async
import java.lang.ref.WeakReference

open class CommonPresenter<T : BaseView> : BasePresenter<T> {

    protected lateinit var mView: WeakReference<T>

    override fun attach(view: T) {
        mView = WeakReference(view)
    }

    override fun detach() {
        mView.clear()
    }

    override fun hasView(): Boolean {
        return mView.get() != null
    }

    override fun handleError(e: Throwable?) {

        if (e is CoreException) {
            mView.get()?.onError(e.code) ?: LogUtils.error(this, e)
        } else if (e is NetworkException) {
            mView.get()?.onError(HttpCode.NO_NETWORK) ?: LogUtils.error(this, e)
        }
    }
}