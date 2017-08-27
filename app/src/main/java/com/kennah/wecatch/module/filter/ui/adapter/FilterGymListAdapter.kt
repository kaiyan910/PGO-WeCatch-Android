package com.kennah.wecatch.module.filter.ui.adapter

import android.content.Context
import android.view.ViewGroup
import com.kennah.wecatch.core.base.BaseAdapter
import com.kennah.wecatch.core.base.Wrapper
import com.kennah.wecatch.local.FilterManager
import com.kennah.wecatch.module.filter.ui.holder.FilterGymListHolder

class FilterGymListAdapter(context: Context, val filter: FilterManager) : BaseAdapter<Int, FilterGymListHolder>(context) {

    private var mOnItemClickListener: (Int, Boolean) -> Unit = { id: Int, choice: Boolean ->

        if (!choice) {
            filter.addToGymFilter(id)
        } else {
            filter.removeFromGymFilter(id)
        }
    }

    override fun onCreateItemView(parent: ViewGroup, viewType: Int): FilterGymListHolder = FilterGymListHolder(context)

    override fun onBindViewHolder(holder: Wrapper<FilterGymListHolder>?, position: Int) {
        holder?.view?.bind(
                mData[position],
                filter.inGymFilter(mData[position]),
                mOnItemClickListener
        )
    }
}