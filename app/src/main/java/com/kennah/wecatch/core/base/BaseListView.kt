package com.kennah.wecatch.core.base

interface BaseListView<in T>: BaseView {

    fun showLoading()
    fun showData()
    fun showEmpty()

    fun update(data: List<T>)
    fun setData(data: List<T>)
}