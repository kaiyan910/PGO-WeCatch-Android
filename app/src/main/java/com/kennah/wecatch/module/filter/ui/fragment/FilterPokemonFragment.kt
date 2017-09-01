package com.kennah.wecatch.module.filter.ui.fragment

import android.os.Bundle
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.RecyclerView
import com.kennah.wecatch.core.base.BaseAdapter
import com.kennah.wecatch.core.base.ListFragment
import com.kennah.wecatch.core.utils.LogUtils
import com.kennah.wecatch.local.FilterManager
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
    lateinit var mOttoBus: Bus

    private var mSelectAllFlag = false

    override fun afterViews(savedInstanceState: Bundle?) {
        super.afterViews(savedInstanceState)

        val data = listOf(1..251).flatten()
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

    override fun getListAdapter(): BaseAdapter<Int, FilterPokemonGridHolder> = mGridAdapter

    override fun layoutManager(): RecyclerView.LayoutManager = GridLayoutManager(activity, 3)

    override fun onSelectChanged(selectAll: Boolean) {
        mGridAdapter.notifyDataSetChanged()
    }

    @Subscribe
    fun onSelectChanged(event: SelectChangeEvent) {
        LogUtils.debug(this, "event=[%d]", event.position)
        when {
            event.position == 1 && mSelectAllFlag -> mFilterManager.removeAllPokemonFilter()
            event.position == 1 && !mSelectAllFlag -> mFilterManager.addAllPokemonFilter()
        }
        mSelectAllFlag = !mSelectAllFlag
        mGridAdapter.notifyDataSetChanged()
    }
}