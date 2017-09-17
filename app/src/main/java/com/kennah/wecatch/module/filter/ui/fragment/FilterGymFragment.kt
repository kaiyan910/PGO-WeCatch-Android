package com.kennah.wecatch.module.filter.ui.fragment

import android.os.Bundle
import android.support.v7.widget.RecyclerView
import com.kennah.wecatch.core.SimpleDividerItemDecoration
import com.kennah.wecatch.core.base.BaseAdapter
import com.kennah.wecatch.core.base.ListFragment
import com.kennah.wecatch.core.utils.LogUtils
import com.kennah.wecatch.local.FilterManager
import com.kennah.wecatch.module.filter.ui.adapter.FilterGymListAdapter
import com.kennah.wecatch.module.filter.ui.event.SelectChangeEvent
import com.kennah.wecatch.module.filter.ui.holder.FilterGymListHolder
import com.squareup.otto.Bus
import com.squareup.otto.Subscribe
import javax.inject.Inject

class FilterGymFragment: FilterFragment<Int, FilterGymListHolder>() {

    @Inject
    lateinit var mListAdapter: FilterGymListAdapter
    @Inject
    lateinit var mItemDecoration: RecyclerView.ItemDecoration
    @Inject
    lateinit var mFilterManager: FilterManager

    private var mSelectAllFlag = false

    override fun afterViews(savedInstanceState: Bundle?) {
        super.afterViews(savedInstanceState)
        mRecyclerView.addItemDecoration(mItemDecoration)
        setData(listOf(mData).flatten())
    }

    override fun enableRefresh(): Boolean = false

    override fun getListAdapter(): BaseAdapter<Int, FilterGymListHolder> = mListAdapter

    override fun onSelectChanged(selectAll: Boolean) {
        mListAdapter.notifyDataSetChanged()
    }

    @Subscribe
    fun onSelectChanged(event: SelectChangeEvent) {
        when {
            event.position == mFragmentPosition && mSelectAllFlag -> {
                mFilterManager.removeAllGymFilter()
                mSelectAllFlag = !mSelectAllFlag
            }
            event.position == mFragmentPosition && !mSelectAllFlag -> {
                mFilterManager.addAllGymFilter()
                mSelectAllFlag = !mSelectAllFlag
            }
        }
        mListAdapter.notifyDataSetChanged()
    }
}