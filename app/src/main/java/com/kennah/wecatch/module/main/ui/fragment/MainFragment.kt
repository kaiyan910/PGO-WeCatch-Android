package com.kennah.wecatch.module.main.ui.fragment

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import android.view.View
import android.view.animation.Animation
import android.view.animation.LinearInterpolator
import android.view.animation.RotateAnimation
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.TextView
import butterknife.BindView
import butterknife.OnClick
import com.kennah.wecatch.R
import com.kennah.wecatch.core.base.BaseFragment
import com.kennah.wecatch.core.utils.LogUtils
import com.kennah.wecatch.local.Constant
import com.kennah.wecatch.local.Prefs
import com.kennah.wecatch.local.model.Pokemon
import com.kennah.wecatch.local.utils.AnimateUtils
import com.kennah.wecatch.module.main.contract.MainContract
import javax.inject.Inject
import com.kennah.wecatch.module.filter.ui.activity.FilterActivity
import com.kennah.wecatch.module.main.service.NotificationService
import com.kennah.wecatch.module.main.ui.activity.MainActivity
import com.kennah.wecatch.module.main.ui.view.PokemonMapView
import com.kennah.wecatch.module.settings.ui.activity.SettingsActivity
import com.squareup.otto.Subscribe
import org.jetbrains.anko.support.v4.startActivity
import org.jetbrains.anko.support.v4.toast
import org.jetbrains.anko.textColor

class MainFragment : BaseFragment() {

    @BindView(R.id.map_container)
    lateinit var mLayoutMapContainer: FrameLayout
    @BindView(R.id.scan)
    lateinit var mImageScan: ImageView
    @BindView(R.id.notify)
    lateinit var mTextNotify: TextView

    @Inject
    lateinit var mPresenter: MainContract.Presenter
    @Inject
    lateinit var mPokemonMapView: PokemonMapView

    @Inject
    lateinit var mPrefs: Prefs

    override fun afterViews(savedInstanceState: Bundle?) {

        mPokemonMapView.apply {

            onStatusChangeListener = { loading ->

                if (loading) {
                    mImageScan.startAnimation(AnimateUtils.rotateAnimate())
                } else {
                    mImageScan.animation = null
                }

            }

            showLoadingIcon = false
        }

        mLayoutMapContainer.addView(mPokemonMapView)
    }

    override fun onResume() {
        super.onResume()
        extraFromIntent()
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

    @OnClick(R.id.settings)
    fun onSettingsClick() {
        startActivity<SettingsActivity>()
    }

    @OnClick(R.id.notify)
    fun onNotifyClick() {

        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            (activity as MainActivity).checkLocationPermission()
            return
        }

        val notify = mPrefs.getNotifySwitch()
        LogUtils.debug("DEBUG#MainFragment", "notify=[$notify]")

        if (!notify) {
            activity.apply {
                startService(Intent(activity, NotificationService::class.java))
                finish()
            }
        } else {
            activity.stopService(Intent(activity, NotificationService::class.java))
        }

        mPrefs.setNotifySwitch(!notify)
    }

    override fun layout(): Int = R.layout.fragment_main

    private fun extraFromIntent() {

        if (activity.intent.hasExtra(Constant.NOTIFICATION_RESULT)) {

            val pokemonList = activity.intent.getParcelableArrayListExtra<Pokemon>(Constant.NOTIFICATION_RESULT)
            LogUtils.debug(this, "pokemon size = ${pokemonList.size}")
            mPokemonMapView.cache(pokemonList)
        }
    }
}