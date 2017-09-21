package com.kennah.wecatch.local.map

import android.content.Context
import android.content.SharedPreferences
import android.graphics.BitmapFactory
import android.graphics.drawable.BitmapDrawable
import android.support.v4.content.ContextCompat
import android.util.Log
import android.view.View
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.*
import com.kennah.wecatch.R
import com.kennah.wecatch.core.helper.LocationHelper
import com.kennah.wecatch.core.utils.CommonUtils
import com.kennah.wecatch.core.utils.LogUtils
import com.kennah.wecatch.core.utils.ResourceUtils
import com.kennah.wecatch.core.utils.TimeUtils
import com.kennah.wecatch.local.model.Gym
import com.kennah.wecatch.local.model.Pokemon
import com.kennah.wecatch.module.main.ui.view.GymMarker
import com.kennah.wecatch.module.main.ui.view.OsmGymWindow
import com.kennah.wecatch.module.main.ui.view.OsmPokemonWindow
import io.reactivex.Flowable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import org.osmdroid.config.Configuration
import org.osmdroid.events.MapEventsReceiver
import org.osmdroid.tileprovider.constants.OpenStreetMapTileProviderConstants
import org.osmdroid.tileprovider.tilesource.TileSourceFactory
import org.osmdroid.views.MapView
import org.osmdroid.views.overlay.Marker
import java.util.ArrayList
import java.util.concurrent.TimeUnit
import javax.inject.Inject
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.overlay.MapEventsOverlay
import org.osmdroid.views.overlay.infowindow.InfoWindow
import org.osmdroid.views.overlay.mylocation.GpsMyLocationProvider
import org.osmdroid.views.overlay.mylocation.MyLocationNewOverlay


class OpenStreetMapProvider @Inject constructor (
        private val context: Context,
        private val locationHelper: LocationHelper,
        private val preference: SharedPreferences): MapProvider, MapEventsReceiver {

    private val DEFAULT = LatLng(22.296039, 114.172416)

    private lateinit var mViewMap: MapView

    private var mPokemonMarkerList: MutableList<Marker> = mutableListOf()
    private var mCacheFromIntent: ArrayList<Pokemon> = ArrayList()

    private var mOnMapReadyCB: (() -> Unit)? = null
    private var mOnPokemonFoundCB: (() -> Unit)? = null

    override fun getMapView(): View = mViewMap

    override fun onCreate() {

        Configuration.getInstance().load(context, preference)
        mViewMap = MapView(context)
        mViewMap.setTileSource(TileSourceFactory.MAPNIK)

        mViewMap.overlays.add(0, MapEventsOverlay(this))

        mViewMap.setMultiTouchControls(true)
        mViewMap.controller.apply {
            setZoom(14)
            setCenter(GeoPoint(DEFAULT.latitude, DEFAULT.longitude))
        }

        locationHelper.onStart { location ->
            val geoPoint = GeoPoint(location.latitude, location.longitude)
            mViewMap.controller.animateTo(geoPoint)
            createMyLocation(geoPoint)
        }
    }

    override fun onResume() {
        Configuration.getInstance().load(context, preference)
    }

    override fun onPause() {
        locationHelper.onStop()
    }

    override fun onDestroy() {
        mViewMap.onDetach()
    }

    override fun longPressHelper(p: GeoPoint?): Boolean {
        return false
    }

    override fun singleTapConfirmedHelper(p: GeoPoint?): Boolean {
        InfoWindow.closeAllInfoWindowsOn(mViewMap)
        return false
    }

    override fun addCachedPokemon(pokemonList: List<Pokemon>) {
        mCacheFromIntent.addAll(pokemonList)
    }

    override fun setCallback(onMapReadyCallback: () -> Unit, onPokemonFoundCallback: () -> Unit) {
        mOnMapReadyCB = onMapReadyCallback
        mOnPokemonFoundCB = onPokemonFoundCallback
    }

    override fun onPokemonFound(pokemonList: List<Pokemon>, gymList: List<Gym>) {

        removeAllMarkers()

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
                    createGymMarker(gym)
                }
    }

    override fun doExpireCheck() {

        if (mPokemonMarkerList.isNotEmpty()) {

            mPokemonMarkerList.filter {
                val marker = it.infoWindow as OsmPokemonWindow
                TimeUtils.isExpired(marker.pokemon.expireTime)
            }.forEach {
                mViewMap.overlays.remove(it)
                mPokemonMarkerList.remove(it)
            }
        }
    }

    override fun getBounds(): LatLngBounds {

        val sw = LatLng(mViewMap.boundingBox.latSouth, mViewMap.boundingBox.lonWest)
        val ne = LatLng(mViewMap.boundingBox.latNorth, mViewMap.boundingBox.lonEast)

        return LatLngBounds(sw, ne)
    }

    override fun getZoom(): Float = mViewMap.zoomLevel.toFloat()

    private fun createPokemonMarker(pokemon: Pokemon) {
        val marker = Marker(mViewMap)

        val location = GeoPoint(pokemon.latitude, pokemon.longitude)
        val resource = ResourceUtils.getDrawableResource(context, "pkm_${pokemon.pokemonId}")

        marker.setIcon(ContextCompat.getDrawable(context, resource))
        marker.position = location
        marker.setAnchor(org.osmdroid.views.overlay.Marker.ANCHOR_CENTER, org.osmdroid.views.overlay.Marker.ANCHOR_BOTTOM)

        mViewMap.overlays.add(marker)
        mViewMap.invalidate()

        marker.infoWindow = OsmPokemonWindow(pokemon, marker, R.layout.view_map_pokemon_window, mViewMap)

        mPokemonMarkerList.add(marker)
    }

    private fun createGymMarker(gym: Gym) {

        val gymMarker = GymMarker(context)
        gymMarker.bind(gym)

        val marker = Marker(mViewMap)
        val location = GeoPoint(gym.location?.get(1) ?: 0.0, gym.location?.get(0) ?: 0.0)
        val iconDrawable = BitmapDrawable(mViewMap.context.resources, CommonUtils.getBitmapFromView(gymMarker))

        marker.setIcon(iconDrawable)
        marker.position = location
        marker.setAnchor(org.osmdroid.views.overlay.Marker.ANCHOR_CENTER, org.osmdroid.views.overlay.Marker.ANCHOR_BOTTOM)

        mViewMap.overlays.add(marker)
        mViewMap.invalidate()

        marker.infoWindow = OsmGymWindow(gym, marker, R.layout.view_map_gym_window, mViewMap)
    }

    private fun createMyLocation(geoPoint: GeoPoint) {

        val marker = Marker(mViewMap)
        marker.setIcon(mViewMap.context.getDrawable(R.drawable.ic_my_location))
        marker.position = geoPoint
        marker.setAnchor(org.osmdroid.views.overlay.Marker.ANCHOR_CENTER, org.osmdroid.views.overlay.Marker.ANCHOR_CENTER)

        mViewMap.overlays.add(marker)
        mViewMap.invalidate()
    }

    private fun removeAllMarkers() {
        mPokemonMarkerList.forEach {
            it.remove(mViewMap)
        }
        mPokemonMarkerList.clear()
    }
}