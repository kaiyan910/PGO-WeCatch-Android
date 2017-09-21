package com.kennah.wecatch.core.helper

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.os.Bundle
import android.support.v4.app.ActivityCompat
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.api.GoogleApiClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.model.LatLng
import com.kennah.wecatch.core.hasPermission
import com.kennah.wecatch.core.utils.LogUtils

import javax.inject.Inject

class LocationHelper @Inject constructor (val context: Context):
        GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {

    private val mGoogleApiClient: GoogleApiClient = with(GoogleApiClient.Builder(context)) {

        addConnectionCallbacks(this@LocationHelper)
        addOnConnectionFailedListener(this@LocationHelper)
        addApi(LocationServices.API)
        build()
    }

    private var mLastLocation: Location? = null
    private var callback: ((location: LatLng) -> Unit)? = null

    override fun onConnectionSuspended(p0: Int) {
    }

    override fun onConnectionFailed(p0: ConnectionResult) {
    }

    override fun onConnected(p0: Bundle?) {
        getLastKnowLocation()
    }

    fun onStart(callback: (location: LatLng) -> Unit) {
        LogUtils.debug(this, "LocationHelper >>> onStart()")
        mGoogleApiClient.connect()
        this.callback = callback
    }

    @SuppressLint("MissingPermission")
    fun getLastKnowLocation() {

        hasPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) {
            mLastLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient)
            mLastLocation?.let {
                callback?.invoke(LatLng(mLastLocation!!.latitude, mLastLocation!!.longitude))
            }
        }
    }

    fun getLastLocation(): Location? = mLastLocation

    fun onStop() {
        mGoogleApiClient.disconnect()
        callback = null
    }
}