package com.kennah.wecatch.core.base

interface BasePresenter<in T> {
    fun attach(view: T)
    fun detach()
    fun hasView(): Boolean
    fun handleError(e: Throwable?)
}