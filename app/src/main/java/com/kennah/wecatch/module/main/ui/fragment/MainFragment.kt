package com.kennah.wecatch.module.main.ui.fragment

import android.Manifest
import android.annotation.SuppressLint
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.widget.Button
import android.widget.ImageView
import butterknife.BindView
import butterknife.OnClick
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.MapView
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.*
import com.kennah.wecatch.R
import com.kennah.wecatch.core.base.BaseFragment
import com.kennah.wecatch.core.helper.LocationHelper
import com.kennah.wecatch.local.model.Pokemon
import com.kennah.wecatch.core.hasPermission
import com.kennah.wecatch.core.utils.CommonUtils
import com.kennah.wecatch.core.utils.ResourceUtils
import com.kennah.wecatch.core.utils.TimeUtils
import com.kennah.wecatch.core.withDelay
import com.kennah.wecatch.local.model.Gym
import com.kennah.wecatch.module.main.contract.MainContract
import com.kennah.wecatch.module.main.ui.view.GymMarker
import com.kennah.wecatch.module.main.ui.view.MapGymWindow
import com.kennah.wecatch.module.main.ui.view.MapPokemonWindow
import org.jetbrains.anko.toast
import java.util.*
import javax.inject.Inject
import android.view.animation.Animation
import android.view.animation.LinearInterpolator
import android.view.animation.RotateAnimation
import android.widget.RelativeLayout
import com.kennah.wecatch.core.utils.LogUtils
import com.kennah.wecatch.module.filter.ui.activity.FilterActivity
import io.reactivex.Flowable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.functions.Action
import io.reactivex.functions.Consumer
import io.reactivex.schedulers.Schedulers
import org.jetbrains.anko.support.v4.startActivity
import org.jetbrains.anko.support.v4.toast
import java.util.concurrent.TimeUnit

class MainFragment : BaseFragment(),
        MainContract.View,
        OnMapReadyCallback,
        GoogleMap.InfoWindowAdapter,
        GoogleMap.OnInfoWindowClickListener,
        GoogleMap.OnCameraMoveListener {

    private val DEFAULT = LatLng(22.296039, 114.172416)

    @BindView(R.id.map)
    lateinit var mViewMap: MapView
    @BindView(R.id.loading)
    lateinit var mImageLoading: ImageView
    @BindView(R.id.loading_wrapper)
    lateinit var mLayoutLoadingWrapper: RelativeLayout

    @Inject
    lateinit var mPresenter: MainContract.Presenter
    @Inject
    lateinit var mLocationHelper: LocationHelper

    private lateinit var mMap: GoogleMap
    private var mLastKnownLocation: LatLng? = null
    private var mPokemonMarkerList: MutableList<Marker> = mutableListOf()
    private var mExpireCheckFlag = true

    private var mScanning = false

    override fun afterViews(savedInstanceState: Bundle?) {

        mViewMap.onCreate(savedInstanceState)
        mViewMap.getMapAsync(this)
    }

    override fun onResume() {
        super.onResume()
        mViewMap.onResume()
        mExpireCheckFlag = true
        expireCheck()
    }

    override fun onPause() {
        super.onPause()
        mViewMap.onPause()
        mLocationHelper.onStop()
        mExpireCheckFlag = false
    }

    override fun onDestroy() {
        super.onDestroy()
        mViewMap.onDestroy()
        mPresenter.detach()
    }

    @OnClick(R.id.filter)
    fun onFilterClick() {
        startActivity<FilterActivity>()
    }

    @OnClick(R.id.scan)
    fun onScanClick() {

        if (mScanning) return

        showLoading()
        mExpireCheckFlag = false
        mPresenter.getPokemon(mMap.projection.visibleRegion.latLngBounds, mMap.cameraPosition.zoom)
        mScanning = true
    }

    override fun onCameraMove() {
        LogUtils.debug(this, "zoom=[%f]", mMap.cameraPosition.zoom)
    }

    override fun onError(errorCode: Int) {
        mScanning = false
        hideLoading()
        toast("$errorCode")
    }

    override fun onPokemonFound(pokemonList: List<Pokemon>, gymList: List<Gym>) {

        hideLoading()
        mMap.clear()

        Flowable.fromIterable(pokemonList)
                .subscribeOn(Schedulers.computation())
                .onBackpressureBuffer(1000)
                .concatMap {
                    Flowable.just<Pokemon>(it).delay(50, TimeUnit.MILLISECONDS)
                }
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ pokemon ->
                    val options = MarkerOptions().position(LatLng(pokemon.latitude, pokemon.longitude))
                    options.icon(BitmapDescriptorFactory.fromResource(ResourceUtils.getDrawableResource(activity, "pkm_${pokemon.pokemonId}")))
                    val marker: Marker = mMap.addMarker(options)
                    marker.tag = pokemon
                    mPokemonMarkerList.add(marker)
                }, { _ ->
                    LogUtils.debug(this, "ERROR")
                }, {
                    mScanning = false
                    mExpireCheckFlag = true
                    expireCheck()
                })

        Flowable.fromIterable(gymList)
                .subscribeOn(Schedulers.computation())
                .onBackpressureBuffer(1000)
                .concatMap {
                    Flowable.just<Gym>(it).delay(50, TimeUnit.MILLISECONDS)
                }
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe { gym ->
                    val gymMarker = GymMarker(activity)
                    gymMarker.bind(gym)

                    val options = MarkerOptions().position(LatLng(gym.location[1], gym.location[0]))
                    options.icon(BitmapDescriptorFactory.fromBitmap(CommonUtils.getBitmapFromView(gymMarker)))

                    val marker: Marker = mMap.addMarker(options)
                    marker.tag = gym
                }

    }

    override fun getInfoContents(marker: Marker): View? {

        return when (marker.tag) {
            is Pokemon -> {
                val pokemon = marker.tag as Pokemon
                val window = MapPokemonWindow(activity.applicationContext)
                window.apply {
                    bind(pokemon, marker)
                }
                window
            }
            is Gym -> {
                val gym = marker.tag as Gym
                val window = MapGymWindow(activity.applicationContext)
                window.apply {
                    bind(gym, marker)
                }
                window
            }
            else -> null
        }
    }

    override fun getInfoWindow(marker: Marker): View? {
        return null
    }

    override fun onInfoWindowClick(marker: Marker) {
        val message = String.format(Locale.getDefault(), "%f,%f", marker.position.latitude, marker.position.longitude)
        CommonUtils.saveToClipboard(activity.applicationContext, message)
        toast(message)
    }

    override fun layout(): Int = R.layout.fragment_main

    @SuppressLint("MissingPermission")
    override fun onMapReady(googleMap: GoogleMap) {

        mMap = googleMap

        // start the location service
        mLocationHelper.onStart { location ->
            mLastKnownLocation = location
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(mLastKnownLocation, 15F))
        }

        mMap.apply {
            moveCamera(CameraUpdateFactory.newLatLngZoom(DEFAULT, 15f))
            isBuildingsEnabled = false
            isIndoorEnabled = false
            isTrafficEnabled = false
            uiSettings.isMapToolbarEnabled = false
            setInfoWindowAdapter(this@MainFragment)
            setOnInfoWindowClickListener(this@MainFragment)
            setOnCameraMoveListener(this@MainFragment)

            hasPermission(activity, Manifest.permission.ACCESS_FINE_LOCATION) {
                uiSettings.isMyLocationButtonEnabled = true
                isMyLocationEnabled = true
            }
        }
    }

    private fun expireCheck() {

        withDelay(5000) {
            if (mExpireCheckFlag) {

                if (mPokemonMarkerList.isNotEmpty()) {

                    mPokemonMarkerList.filter {
                        val pokemon = it.tag as? Pokemon
                        TimeUtils.isExpired(pokemon?.expireTime)
                    }.forEach {
                        it.remove()
                        mPokemonMarkerList.remove(it)
                    }
                }

                expireCheck()
            }
        }
    }

    private fun hideLoading() {
        mLayoutLoadingWrapper.visibility = View.GONE
        mImageLoading.animation = null
    }

    private fun showLoading() {

        mLayoutLoadingWrapper.visibility = View.VISIBLE

        val anim = RotateAnimation(0.0f, 360.0f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f)
        anim.interpolator = LinearInterpolator()
        anim.repeatCount = Animation.INFINITE
        anim.duration = 700

        mImageLoading.startAnimation(anim)
    }
}