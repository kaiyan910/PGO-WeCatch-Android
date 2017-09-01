package com.kennah.wecatch.module.filter.ui.fragment

import com.kennah.wecatch.core.base.BaseHolder
import com.kennah.wecatch.core.base.ListFragment


abstract class FilterFragment<T, V>: ListFragment<T, V>() where T: Any, V: BaseHolder<T> {

    abstract fun onSelectChanged(selectAll: Boolean)
}