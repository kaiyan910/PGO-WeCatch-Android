package com.kennah.wecatch.module.filter.ui.adapter

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentStatePagerAdapter
import com.kennah.wecatch.module.filter.ui.fragment.FilterGymFragment
import com.kennah.wecatch.module.filter.ui.fragment.FilterPokemonFragment

class FilterPagerAdapter(manager: FragmentManager, private val titles: Array<String>) : FragmentStatePagerAdapter(manager) {

    private var mFragmentList: ArrayList<Fragment> = ArrayList()

    override fun getItem(position: Int): Fragment = mFragmentList[position]

    override fun getCount(): Int = mFragmentList.size

    override fun getPageTitle(position: Int): CharSequence = titles[position]

    fun setFragmentList(fragments: ArrayList<Fragment>) {
        mFragmentList.addAll(fragments)
    }
}