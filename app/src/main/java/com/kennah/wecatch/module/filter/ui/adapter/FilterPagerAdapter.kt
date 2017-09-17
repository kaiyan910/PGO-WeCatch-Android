package com.kennah.wecatch.module.filter.ui.adapter

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentStatePagerAdapter
import com.kennah.wecatch.local.Constant
import com.kennah.wecatch.local.FilterManager
import com.kennah.wecatch.local.filter.FilterHandlerFactory
import com.kennah.wecatch.module.filter.ui.fragment.FilterFragment
import com.kennah.wecatch.module.filter.ui.fragment.FilterGymFragment
import com.kennah.wecatch.module.filter.ui.fragment.FilterPokemonFragment

class FilterPagerAdapter(manager: FragmentManager, private val titles: Array<String>) : FragmentStatePagerAdapter(manager) {

    private var mFragmentList: ArrayList<FilterFragment<*, *>> = ArrayList()
    private var mShowGYM = true

    override fun getItem(position: Int): Fragment = mFragmentList[position]

    override fun getCount(): Int = mFragmentList.size

    override fun getPageTitle(position: Int): CharSequence {
        return if (mShowGYM) {
            titles[position]
        } else {
            titles[position+1]
        }
    }

    fun setup(showGYM: Boolean=true, filterType: Int = FilterHandlerFactory.NORMAL) {

        mShowGYM = showGYM

        var pokemonFilterStartPosition = 0

        if (mShowGYM) {
            pokemonFilterStartPosition = 1
            val gym = FilterGymFragment().apply {
                mFragmentPosition = 0
                mData = Constant.GYM
            }
            mFragmentList.add(gym)
        }

        for (i in 0 until Constant.POKEMON_GENERATION) {
            val pokemon = FilterPokemonFragment().apply {
                mFragmentPosition = pokemonFilterStartPosition
                mFilterType = filterType
                mData = Constant.GENERATION[i]
                mGeneration = i + 1
            }
            pokemonFilterStartPosition++
            mFragmentList.add(pokemon)
        }
    }
}