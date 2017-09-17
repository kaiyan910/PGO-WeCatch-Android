package com.kennah.wecatch.core.base

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import com.kennah.wecatch.core.utils.LogUtils
import com.kennah.wecatch.module.filter.ui.holder.FilterPokemonGridHolder
import java.util.ArrayList

abstract class BaseAdapter<T, V>(protected var context: Context) : RecyclerView.Adapter<Wrapper<V>>() where T : Any, V : BaseHolder<T> {

    protected var mData: MutableList<T> = ArrayList()

    override fun getItemCount(): Int {
        return mData.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Wrapper<V> {
        return Wrapper(onCreateItemView(parent, viewType))
    }

    protected abstract fun onCreateItemView(parent: ViewGroup, viewType: Int): V

    override fun onBindViewHolder(holder: Wrapper<V>?, position: Int) {
        holder?.view?.bind(mData[position])
    }

    fun setData(data: List<T>) {

        LogUtils.debug(this, "data=$data")

        mData.clear()
        mData.addAll(data)

        notifyDataSetChanged()
    }

}
