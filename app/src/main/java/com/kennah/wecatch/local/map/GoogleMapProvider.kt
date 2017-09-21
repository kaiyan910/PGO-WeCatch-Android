package com.kennah.wecatch.local.map

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.graphics.BitmapFactory
import android.util.Log
import android.view.View
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.MapView
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.*
import com.kennah.wecatch.core.hasPermission
import com.kennah.wecatch.core.helper.LocationHelper
import com.kennah.wecatch.core.utils.CommonUtils
import com.kennah.wecatch.core.utils.LogUtils
import com.kennah.wecatch.core.utils.ResourceUtils
import com.kennah.wecatch.core.utils.TimeUtils
import com.kennah.wecatch.local.Constant
import com.kennah.wecatch.local.model.Gym
import com.kennah.wecatch.local.model.Pokemon
import com.kennah.wecatch.local.utils.ColorUtils
import com.kennah.wecatch.module.main.ui.view.GymMarker
import com.kennah.wecatch.module.main.ui.view.MapGymWindow
import com.kennah.wecatch.module.main.ui.view.MapPokemonWindow
import io.reactivex.Flowable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import java.lang.ref.WeakReference
import java.util.*
import java.util.concurrent.TimeUnit
import javax.inject.Inject


class GoogleMapProvider @Inject constructor (private val context: Context, private val locationHelper: LocationHelper): MapProvider,
        OnMapReadyCallback,
        GoogleMap.OnMarkerClickListener,
        GoogleMap.InfoWindowAdapter,
        GoogleMap.OnInfoWindowClickListener {

    private val DEFAULT = LatLng(22.296039, 114.172416)

    private var mPokemonMarkerList: MutableList<Marker> = mutableListOf()
    private var mCacheFromIntent: ArrayList<Pokemon> = ArrayList()

    private lateinit var mViewMap: MapView
    private lateinit var mMap: GoogleMap

    private var mLastKnownLocation: LatLng? = null

    private var mOnMapReadyCB: (() -> Unit)? = null
    private var mOnPokemonFoundCB: (() -> Unit)? = null

    override fun onCreate() {

        mViewMap = MapView(context)
        mViewMap.onCreate(null)
        mViewMap.getMapAsync(this)
    }

    override fun onResume() {
        mViewMap.onResume()
    }

    override fun onPause() {

        locationHelper.onStop()
        mViewMap.onPause()
    }

    override fun onDestroy() {
        mViewMap.onDestroy()
    }

    override fun getMapView(): View = mViewMap

    @SuppressLint("MissingPermission")
    override fun onMapReady(map: GoogleMap) {

        mMap = map

        locationHelper.onStart { location ->
            mLastKnownLocation = location
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(mLastKnownLocation, 15F))
        }

        mMap.apply {
            moveCamera(CameraUpdateFactory.newLatLngZoom(DEFAULT, 15f))
            isBuildingsEnabled = false
            isIndoorEnabled = false
            isTrafficEnabled = false
            uiSettings.isMapToolbarEnabled = false
            setInfoWindowAdapter(this@GoogleMapProvider)
            setOnInfoWindowClickListener(this@GoogleMapProvider)
            setOnMarkerClickListener(this@GoogleMapProvider)

            hasPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) {
                uiSettings.isMyLocationButtonEnabled = true
                isMyLocationEnabled = true
            }
        }

        if (mCacheFromIntent.size > 0) {
            onPokemonFound(mCacheFromIntent, emptyList())
        }

        mOnMapReadyCB?.invoke()
    }

    override fun onMarkerClick(marker: Marker): Boolean {
        val zoom = mMap.cameraPosition.zoom
        val cameraUpdate = CameraUpdateFactory
                .newLatLngZoom(
                        LatLng(marker.position.latitude + 120 / Math.pow(2.toDouble(), zoom.toDouble()), marker.position.longitude),
                        zoom
                )
        mMap.animateCamera(cameraUpdate, 500, null)

        marker.showInfoWindow()

        return true
    }

    override fun getInfoContents(marker: Marker): View? {
        return when (marker.tag) {
            is Pokemon -> {
                val pokemon = marker.tag as Pokemon
                val window = MapPokemonWindow(context)
                window.apply {
                    bind(pokemon, marker)
                }
                window
            }
            is Gym -> {
                val gym = marker.tag as Gym
                val window = MapGymWindow(context)
                window.apply {
                    bind(gym, marker)
                }
                window
            }
            else -> null
        }
    }

    override fun getInfoWindow(p0: Marker?): View? {
        return null
    }

    override fun onInfoWindowClick(marker: Marker) {
        val message = String.format(Locale.getDefault(), "%f,%f", marker.position.latitude, marker.position.longitude)
        CommonUtils.saveToClipboard(context, message)
    }


    override fun addCachedPokemon(pokemonList: List<Pokemon>) {
        mCacheFromIntent.addAll(pokemonList)
    }

    override fun setCallback(onMapReadyCallback: () -> Unit, onPokemonFoundCallback: () -> Unit) {
        mOnMapReadyCB = onMapReadyCallback
        mOnPokemonFoundCB = onPokemonFoundCallback
    }

    override fun onPokemonFound(pokemonList: List<Pokemon>, gymList: List<Gym>) {

        mMap.clear()

        LogUtils.debug(this, "${pokemonList.size} on pokemonList")

        Flowable.fromIterable(pokemonList)
                .subscribeOn(Schedulers.computation())
                .onBackpressureBuffer(1000)
                .concatMap {
                    Flowable.just<Pokemon>(it).delay(50, TimeUnit.MILLISECONDS)
                }
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ pokemon ->
                    createPokemonMarker(pokemon)
                }, { e->
                    Log.e("CREATE MARKER", "ERROR", e)
                }, {
                    mOnPokemonFoundCB?.invoke()
                    if (mCacheFromIntent.size > 0) mCacheFromIntent.clear()
                })

        Flowable.fromIterable(gymList)
                .subscribeOn(Schedulers.computation())
                .onBackpressureBuffer(1000)
                .concatMap {
                    Flowable.just<Gym>(it).delay(50, TimeUnit.MILLISECONDS)
                }
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe { gym ->
                    val gymMarker = GymMarker(context)
                    gymMarker.bind(gym)

                    val location = gym.location ?: arrayOf(0.0, 0.0)

                    val options = MarkerOptions().position(LatLng(location[1], location[0]))

                    when (gym.raidLevel) {
                        4 or 5 -> options.zIndex(999F)
                        else -> { }
                    }

                    options.icon(BitmapDescriptorFactory.fromBitmap(CommonUtils.getBitmapFromView(gymMarker)))

                    val marker: Marker = mMap.addMarker(options)
                    marker.tag = gym
                }
    }

    override fun doExpireCheck() {

        if (mPokemonMarkerList.isNotEmpty()) {

            mPokemonMarkerList.filter {
                val pokemon = it.tag as? Pokemon
                TimeUtils.isExpired(pokemon?.expireTime)
            }.forEach {
                it.remove()
                mPokemonMarkerList.remove(it)
            }
        }
    }

    override fun getBounds(): LatLngBounds = mMap.projection.visibleRegion.latLngBounds

    override fun getZoom(): Float = mMap.cameraPosition.zoom

    private fun createPokemonMarker(pokemon: Pokemon) {
        val options = MarkerOptions()
                .position(LatLng(pokemon.latitude, pokemon.longitude))

        if (pokemon.pokemonId in Constant.DEFAULT_RARE_SEARCH) {
            options.zIndex(999F)
        }

        val resource = ResourceUtils.getDrawableResource(context, "pkm_${pokemon.pokemonId}")

        if (pokemon.iv >= 80) {

            val color = ColorUtils.getPokemonColor(pokemon.iv)

            val bm = BitmapFactory.decodeResource(context.resources, resource)
            options.icon(BitmapDescriptorFactory.fromBitmap(CommonUtils.highlightImage(bm, color)))
        } else {
            options.icon(BitmapDescriptorFactory.fromResource(ResourceUtils.getDrawableResource(context, "pkm_${pokemon.pokemonId}")))
        }

        val marker: Marker = mMap.addMarker(options)
        marker.tag = pokemon
        mPokemonMarkerList.add(marker)
    }
}