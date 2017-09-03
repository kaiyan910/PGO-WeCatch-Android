package com.kennah.wecatch.module.main.ui.fragment

import android.os.Bundle
import android.widget.FrameLayout
import butterknife.BindView
import butterknife.OnClick
import com.google.android.gms.maps.model.*
import com.kennah.wecatch.R
import com.kennah.wecatch.core.base.BaseFragment
import com.kennah.wecatch.core.helper.LocationHelper
import com.kennah.wecatch.module.main.contract.MainContract
import javax.inject.Inject
import com.kennah.wecatch.module.filter.ui.activity.FilterActivity
import com.kennah.wecatch.module.main.ui.activity.MainActivity
import com.kennah.wecatch.module.main.ui.view.PokemonMapView
import org.jetbrains.anko.support.v4.startActivity

class MainFragment : BaseFragment() {

    @BindView(R.id.map_container)
    lateinit var mLayoutMapContainer: FrameLayout

    @Inject
    lateinit var mPresenter: MainContract.Presenter
    @Inject
    lateinit var mPokemonMapView: PokemonMapView


    override fun afterViews(savedInstanceState: Bundle?) {
        mLayoutMapContainer.addView(mPokemonMapView)
    }

    override fun onResume() {
        super.onResume()
        mPokemonMapView.onResume()

    }

    override fun onPause() {
        super.onPause()
        mPokemonMapView.onPause()
    }

    override fun onDestroy() {
        super.onDestroy()
        mPokemonMapView.onDestroy()
    }

    @OnClick(R.id.filter)
    fun onFilterClick() {
        startActivity<FilterActivity>()
    }

    @OnClick(R.id.scan)
    fun onScanClick() {
        mPokemonMapView.scan()
    }

    @OnClick(R.id.floating)
    fun onFloatingClick() {
        val parent = activity as MainActivity
        parent.checkPermission()
    }

    override fun layout(): Int = R.layout.fragment_main

}