package com.kennah.wecatch.module.filter.ui.adapter

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentStatePagerAdapter
import com.kennah.wecatch.module.filter.ui.fragment.FilterGymFragment
import com.kennah.wecatch.module.filter.ui.fragment.FilterPokemonFragment

class FilterPagerAdapter(manager: FragmentManager) : FragmentStatePagerAdapter(manager) {

    override fun getItem(position: Int): Fragment = when(position) {
        0 -> FilterGymFragment()
        else -> FilterPokemonFragment()
    }

    override fun getCount(): Int = 2

    override fun getPageTitle(position: Int): CharSequence = when (position) {
        0 -> "RAID"
        else -> "POKEMON"
    }
}