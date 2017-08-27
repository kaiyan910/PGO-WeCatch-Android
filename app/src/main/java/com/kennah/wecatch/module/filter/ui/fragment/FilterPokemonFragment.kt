package com.kennah.wecatch.module.filter.ui.fragment

import android.os.Bundle
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.RecyclerView
import com.kennah.wecatch.core.base.BaseAdapter
import com.kennah.wecatch.core.base.ListFragment
import com.kennah.wecatch.local.FilterManager
import com.kennah.wecatch.module.filter.ui.adapter.FilterPokemonGridAdapter
import com.kennah.wecatch.module.filter.ui.holder.FilterPokemonGridHolder
import javax.inject.Inject

class FilterPokemonFragment : ListFragment<Int, FilterPokemonGridHolder>() {

    @Inject
    lateinit var mGridAdapter: FilterPokemonGridAdapter

    override fun afterViews(savedInstanceState: Bundle?) {
        super.afterViews(savedInstanceState)

        val data = listOf(1..251).flatten()
        setData(data)
    }

    override fun enableRefresh(): Boolean = false

    override fun getListAdapter(): BaseAdapter<Int, FilterPokemonGridHolder> = mGridAdapter

    override fun layoutManager(): RecyclerView.LayoutManager = GridLayoutManager(activity, 3)
}