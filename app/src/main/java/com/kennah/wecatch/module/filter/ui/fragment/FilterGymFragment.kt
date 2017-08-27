package com.kennah.wecatch.module.filter.ui.fragment

import android.os.Bundle
import android.support.v7.widget.RecyclerView
import com.kennah.wecatch.core.SimpleDividerItemDecoration
import com.kennah.wecatch.core.base.BaseAdapter
import com.kennah.wecatch.core.base.ListFragment
import com.kennah.wecatch.local.FilterManager
import com.kennah.wecatch.module.filter.ui.adapter.FilterGymListAdapter
import com.kennah.wecatch.module.filter.ui.holder.FilterGymListHolder
import javax.inject.Inject

class FilterGymFragment: ListFragment<Int, FilterGymListHolder>() {

    @Inject
    lateinit var mListAdapter: FilterGymListAdapter
    @Inject
    lateinit var mItemDecoration: RecyclerView.ItemDecoration

    override fun afterViews(savedInstanceState: Bundle?) {
        super.afterViews(savedInstanceState)
        mRecyclerView.addItemDecoration(mItemDecoration)

        val data = listOf(0, 1, 2, 3, 4, 5)
        setData(data)
    }

    override fun enableRefresh(): Boolean = false

    override fun getListAdapter(): BaseAdapter<Int, FilterGymListHolder> = mListAdapter
}