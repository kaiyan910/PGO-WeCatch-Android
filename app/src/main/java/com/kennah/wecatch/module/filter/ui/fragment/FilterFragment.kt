package com.kennah.wecatch.module.filter.ui.fragment

import com.kennah.wecatch.core.base.BaseHolder
import com.kennah.wecatch.core.base.ListFragment
import com.kennah.wecatch.local.filter.FilterHandlerFactory


abstract class FilterFragment<T, V>: ListFragment<T, V>() where T: Any, V: BaseHolder<T> {

    var mFragmentPosition = -1
    var mData: IntRange = (0..0)
    var mFilterType = FilterHandlerFactory.NORMAL

    abstract fun onSelectChanged(selectAll: Boolean)
}