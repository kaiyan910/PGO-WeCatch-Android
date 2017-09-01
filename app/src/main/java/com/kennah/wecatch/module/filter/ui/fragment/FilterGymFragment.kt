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
    @Inject
    lateinit var mOttoBus: Bus

    private var mSelectAllFlag = false

    override fun afterViews(savedInstanceState: Bundle?) {
        super.afterViews(savedInstanceState)
        mRecyclerView.addItemDecoration(mItemDecoration)

        val data = listOf(0, 1, 2, 3, 4, 5)
        setData(data)
    }

    override fun onResume() {
        super.onResume()
        mOttoBus.register(this)
    }

    override fun onPause() {
        super.onPause()
        mOttoBus.unregister(this)
    }

    override fun enableRefresh(): Boolean = false

    override fun getListAdapter(): BaseAdapter<Int, FilterGymListHolder> = mListAdapter

    override fun onSelectChanged(selectAll: Boolean) {
        mListAdapter.notifyDataSetChanged()
    }

    @Subscribe
    fun onSelectChanged(event: SelectChangeEvent) {

        LogUtils.debug(this, "event=[%d]", event.position)
        LogUtils.debug(this, "mSelectAllFlag start=[%s]", mSelectAllFlag)
        when {
            event.position == 0 && mSelectAllFlag -> mFilterManager.removeAllGymFilter()
            event.position == 0 && !mSelectAllFlag -> mFilterManager.addAllGymFilter()
        }
        mSelectAllFlag = !mSelectAllFlag
        LogUtils.debug(this, "mSelectAllFlag end=[%s]", mSelectAllFlag)
        mListAdapter.notifyDataSetChanged()
    }
}