package com.kennah.wecatch.module.filter.ui.adapter

import android.content.Context
import android.view.ViewGroup
import com.kennah.wecatch.core.base.BaseAdapter
import com.kennah.wecatch.core.base.Wrapper
import com.kennah.wecatch.core.utils.LogUtils
import com.kennah.wecatch.local.FilterManager
import com.kennah.wecatch.local.filter.FilterHandler
import com.kennah.wecatch.module.filter.ui.holder.FilterPokemonGridHolder

class FilterPokemonGridAdapter(
        context: Context,
        val filter: FilterManager): BaseAdapter<Int, FilterPokemonGridHolder>(context) {

    private lateinit var mFilterHandler: FilterHandler
    private val mListener: ((Int, Boolean) -> Unit) = { id, choice ->
        if (!choice) {

            LogUtils.debug("DEBUG#FilterPokemonGridAdapter", "number=[$id]")
            mFilterHandler.add(id)
        } else {
            mFilterHandler.remove(id)
        }
    }


    override fun onCreateItemView(parent: ViewGroup, viewType: Int): FilterPokemonGridHolder = FilterPokemonGridHolder(context)

    override fun onBindViewHolder(holder: Wrapper<FilterPokemonGridHolder>?, position: Int) {

        holder?.view?.bind(
                mData[position],
                mFilterHandler.isFiltered(mData[position]),
                mListener
        )
    }

    fun setFilterHandler(handler: FilterHandler) {
        mFilterHandler = handler
    }
}