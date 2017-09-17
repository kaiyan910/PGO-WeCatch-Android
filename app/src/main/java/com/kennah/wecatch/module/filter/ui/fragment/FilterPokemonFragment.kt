package com.kennah.wecatch.module.filter.ui.fragment

import android.os.Bundle
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.RecyclerView
import com.kennah.wecatch.core.base.BaseAdapter
import com.kennah.wecatch.core.base.ListFragment
import com.kennah.wecatch.core.utils.LogUtils
import com.kennah.wecatch.local.FilterManager
import com.kennah.wecatch.local.filter.FilterHandler
import com.kennah.wecatch.local.filter.FilterHandlerFactory
import com.kennah.wecatch.module.filter.ui.adapter.FilterPokemonGridAdapter
import com.kennah.wecatch.module.filter.ui.event.SelectChangeEvent
import com.kennah.wecatch.module.filter.ui.holder.FilterPokemonGridHolder
import com.squareup.otto.Bus
import com.squareup.otto.Subscribe
import javax.inject.Inject

class FilterPokemonFragment : FilterFragment<Int, FilterPokemonGridHolder>() {

    @Inject
    lateinit var mGridAdapter: FilterPokemonGridAdapter
    @Inject
    lateinit var mFilterManager: FilterManager
    @Inject
    lateinit var mFilterHandlerFactory: FilterHandlerFactory

    private lateinit var mFilterHandler: FilterHandler
    private var mSelectAllFlag = false

    var mGeneration = 0

    override fun afterViews(savedInstanceState: Bundle?) {
        super.afterViews(savedInstanceState)

        mFilterHandler = mFilterHandlerFactory.create(mFilterType)
        mGridAdapter.setFilterHandler(mFilterHandler)
        setData(listOf(mData).flatten())
    }

    override fun enableRefresh(): Boolean = false

    override fun getListAdapter(): BaseAdapter<Int, FilterPokemonGridHolder> = mGridAdapter

    override fun layoutManager(): RecyclerView.LayoutManager = GridLayoutManager(activity, 3)

    override fun onSelectChanged(selectAll: Boolean) {
        mGridAdapter.notifyDataSetChanged()
    }

    @Subscribe
    fun onSelectChanged(event: SelectChangeEvent) {
        when {
            event.position == mFragmentPosition && mSelectAllFlag -> {
                mFilterHandler.removeAll(mGeneration)
                mSelectAllFlag = !mSelectAllFlag
            }
            event.position == mFragmentPosition && !mSelectAllFlag -> {
                mFilterHandler.addAll(mGeneration)
                mSelectAllFlag = !mSelectAllFlag
            }
        }
        mGridAdapter.notifyDataSetChanged()
    }
}