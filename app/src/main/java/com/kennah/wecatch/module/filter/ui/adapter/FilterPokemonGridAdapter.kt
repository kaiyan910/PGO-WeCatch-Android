package com.kennah.wecatch.module.filter.ui.adapter

import android.content.Context
import android.view.ViewGroup
import com.kennah.wecatch.core.base.BaseAdapter
import com.kennah.wecatch.core.base.Wrapper
import com.kennah.wecatch.local.FilterManager
import com.kennah.wecatch.module.filter.ui.holder.FilterPokemonGridHolder

class FilterPokemonGridAdapter(context: Context, val filter: FilterManager): BaseAdapter<Int, FilterPokemonGridHolder>(context) {

    private var mOnItemClickListener: (Int, Boolean) -> Unit = { id: Int, choice: Boolean ->

        if (!choice) {
            filter.addToPokemonFilter(id)
        } else {
            filter.removeFromPokemonFilter(id)
        }
    }

    override fun onCreateItemView(parent: ViewGroup, viewType: Int): FilterPokemonGridHolder = FilterPokemonGridHolder(context)

    override fun onBindViewHolder(holder: Wrapper<FilterPokemonGridHolder>?, position: Int) {
        holder?.view?.bind(
                mData[position],
                filter.inPokemonFilter(mData[position]),
                mOnItemClickListener
        )
    }
}