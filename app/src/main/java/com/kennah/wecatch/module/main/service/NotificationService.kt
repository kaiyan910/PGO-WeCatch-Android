package com.kennah.wecatch.module.main.service

import android.app.Notification
import android.app.Service
import android.content.Intent
import android.os.IBinder
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.kennah.wecatch.local.utils.GlideApp
import android.app.NotificationManager
import android.graphics.Bitmap
import com.kennah.wecatch.module.main.ui.activity.MainActivity
import android.app.PendingIntent
import android.content.Context
import android.graphics.BitmapFactory
import android.provider.Settings
import android.support.v4.app.NotificationCompat
import android.support.v4.app.TaskStackBuilder
import android.support.v4.content.ContextCompat
import android.view.WindowManager
import com.bumptech.glide.request.target.SimpleTarget
import com.bumptech.glide.request.transition.Transition
import com.google.android.gms.maps.model.LatLngBounds
import com.kennah.wecatch.R
import com.kennah.wecatch.core.helper.LocationHelper
import com.kennah.wecatch.core.utils.CommonUtils
import com.kennah.wecatch.local.model.Gym
import com.kennah.wecatch.local.model.Pokemon
import com.kennah.wecatch.module.main.contract.MainContract
import com.kennah.wecatch.module.main.presenter.MainPresenter
import dagger.android.AndroidInjection
import javax.inject.Inject
import com.google.maps.android.SphericalUtil
import com.kennah.wecatch.core.utils.LogUtils
import com.kennah.wecatch.core.utils.ResourceUtils
import com.kennah.wecatch.core.withDelay
import com.kennah.wecatch.local.Constant
import com.kennah.wecatch.local.Prefs
import java.text.SimpleDateFormat
import java.util.*


class NotificationService : Service(), MainContract.View {

    private val FOREGROUND_ID = 999

    @Inject
    lateinit var mPresenter: MainPresenter
    @Inject
    lateinit var mLocationHelper: LocationHelper
    @Inject
    lateinit var mWindowManager: WindowManager
    @Inject
    lateinit var mPrefs: Prefs
    @Inject
    lateinit var mNotificationManager: NotificationManager

    lateinit var mBounds: LatLngBounds

    private var mDestroy: Boolean = false
    private var mDistance: Double = 0.0
    private var mDelay: Long = 0L

    private var mAlertedPokemon: MutableList<String> = mutableListOf()

    override fun onBind(p0: Intent?): IBinder? {
        return null
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {

        startForeground(FOREGROUND_ID, createNotification())
        return START_STICKY
    }

    override fun onCreate() {
        AndroidInjection.inject(this)
        super.onCreate()

        mDistance = (mPrefs.getNotifyDistance() * 1000).toDouble()
        mDelay = (mPrefs.getNotifyDelay() * 60 * 1000).toLong()

        mPresenter.attach(this)
        mLocationHelper.onStart { location ->

            LogUtils.debug(this, "Location=${location.latitude},${location.longitude} Distance=[$mDistance] Delay=[$mDelay]")

            val sw = SphericalUtil.computeOffset(location, mDistance, 225.0)
            val ne = SphericalUtil.computeOffset(location, mDistance, 45.0)
            mBounds = LatLngBounds(sw, ne)

            val zoom = calculateZoomLevel().toFloat()

            mPresenter.getPokemonWithoutGym(mBounds, zoom, mAlertedPokemon)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        stopForeground(true)
        mDestroy = true
        mPresenter.detach()
        mLocationHelper.onStop()
    }

    override fun onError(errorCode: Int) {
        setupErrorNotification(errorCode)
        scheduleCall()
    }

    override fun onPokemonFound(pokemonList: List<Pokemon>, gymList: List<Gym>) {

        if (pokemonList.isEmpty()) {

            setupEmptyNotification()

        } else {

            pokemonList.forEach {
                mAlertedPokemon.add("${it.pokemonId}|${it.latitude}|${it.longitude}")
            }

            val markers = constructMarkerString(pokemonList)
            val center = "${mLocationHelper.getLastLocation()!!.latitude},${mLocationHelper.getLastLocation()!!.longitude}"
            val zoom = calculateZoomLevel()
            val url = constructStaticMapUrl(center, zoom.toDouble(), markers)
            val nameList = pokemonList.map({ ResourceUtils.getStringResource(applicationContext, "pokemon_${it.pokemonId}") }).distinct().joinToString(" ")

            LogUtils.debug(this, "url=$url")

            downloadStaticMap(url, pokemonList, nameList)
        }

        scheduleCall()
    }

    private fun scheduleCall() {
        if (!mDestroy) {
            withDelay(mDelay) {
                mLocationHelper.getLastKnowLocation()
            }
        }
    }

    private fun downloadStaticMap(url: String, pokemonList: List<Pokemon>, nameList: String) {

        GlideApp.with(applicationContext)
                .asBitmap()
                .load(url)
                .skipMemoryCache(true)
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .into(object : SimpleTarget<Bitmap>() {
                    override fun onResourceReady(resource: Bitmap, transition: Transition<in Bitmap>?) {
                        setupNotification(resource, pokemonList, nameList)
                    }
                })
    }

    /**
     * Stackoverflow on how to calculate the zoom level given a map bounds
     * https://stackoverflow.com/questions/6048975/google-maps-v3-how-to-calculate-the-zoom-level-for-a-given-bounds
     */
    private fun calculateZoomLevel(): Int {

        var angle = mBounds.northeast.longitude - mBounds.southwest.longitude
        if (angle < 0) angle += 360

        val width = CommonUtils.calculateScreenSize(mWindowManager)[0]

        return Math.round(Math.log(width * 360 / angle / 256) / 0.6931471805599453).toInt()
    }

    private fun constructStaticMapUrl(center: String, zoom: Double, markers: String): String {
        return "https://maps.googleapis.com/maps/api/staticmap?center=$center&zoom=$zoom&size=512x256&$markers&key=${applicationContext.getString(R.string.map_api_key)}"
    }

    private fun constructMarkerString(markerList: List<Pokemon>): String = markerList.joinToString("&") {

        val name = ResourceUtils.getStringResource(applicationContext, "pokemon_${it.pokemonId}").toLowerCase()

        "markers=${it.latitude},${it.longitude}"
    }

    private fun setupErrorNotification(errorCode: Int) {

        val now = Constant.DATE_FORMATTER.format(Date())
        val title = resources.getString(R.string.notification_title_error, now)
        val message = resources.getString(R.string.notification_message_error, errorCode)

        val mBuilder = NotificationCompat.Builder(this, "wecatch-pokemon")
                .setSmallIcon(R.drawable.ic_notification)
                .setColor(ContextCompat.getColor(applicationContext, R.color.colorPrimary))
                .setLargeIcon(BitmapFactory.decodeResource(resources, R.mipmap.ic_launcher))
                .setContentTitle(title)
                .setContentText(message)

        mNotificationManager.notify(FOREGROUND_ID, mBuilder.build())
    }

    private fun setupEmptyNotification() {

        val now = Constant.DATE_FORMATTER.format(Date())
        val title = resources.getString(R.string.notification_title_empty, now)

        val mBuilder = NotificationCompat.Builder(this, "wecatch-pokemon")
                .setSmallIcon(R.drawable.ic_notification)
                .setColor(ContextCompat.getColor(applicationContext, R.color.colorPrimary))
                .setLargeIcon(BitmapFactory.decodeResource(resources, R.mipmap.ic_launcher))
                .setContentTitle(title)
                .setContentText(getString(R.string.notification_message_empty))

        mNotificationManager.notify(FOREGROUND_ID, mBuilder.build())
    }

    private fun setupNotification(bitmap: Bitmap, pokemonList: List<Pokemon>, nameList: String) {

        val intent = Intent(this, MainActivity::class.java)

        intent.putParcelableArrayListExtra(Constant.NOTIFICATION_RESULT, ArrayList(pokemonList))

        val stackBuilder = TaskStackBuilder.create(this)

        stackBuilder.addNextIntent(intent)
        val uniqueInt = (System.currentTimeMillis() and 0xfffffff).toInt()
        val resultPendingIntent = stackBuilder.getPendingIntent(
                uniqueInt,
                PendingIntent.FLAG_UPDATE_CURRENT
        )

        val now = Constant.DATE_FORMATTER.format(Date())
        val title = resources.getString(R.string.notification_title, pokemonList.size, now)

        val mBuilder = NotificationCompat.Builder(this, "wecatch-pokemon")
                .setSmallIcon(R.drawable.ic_notification)
                .setColor(ContextCompat.getColor(applicationContext, R.color.colorPrimary))
                .setLargeIcon(BitmapFactory.decodeResource(resources, R.mipmap.ic_launcher))
                .setContentTitle(title)
                .setContentText(nameList)
                .setVibrate(kotlin.LongArray(2, { (it + 1) * 500L }))
                .setSound(Settings.System.DEFAULT_NOTIFICATION_URI)
                .addAction(createStopAction())
                .setPriority(Notification.PRIORITY_HIGH)

        val style = NotificationCompat.BigPictureStyle().bigPicture(bitmap)
        mBuilder.setStyle(style)
        mBuilder.setContentIntent(resultPendingIntent)

        mNotificationManager.notify(FOREGROUND_ID, mBuilder.build())
    }

    private fun createNotification(): Notification {

        val intent = Intent(this, MainActivity::class.java)

        return NotificationCompat.Builder(this, "wecatch-pokemon")
                .setContentTitle(getText(R.string.app_name))
                .setContentText(getText(R.string.notification_foreground_message))
                .setSmallIcon(R.drawable.ic_notification)
                .setColor(ContextCompat.getColor(applicationContext, R.color.colorPrimaryDark))
                .setContentIntent(PendingIntent.getActivity(this, 0, intent, 0))
                .addAction(createStopAction())
                .build()
    }

    private fun createStopAction(): NotificationCompat.Action {

        val intent = Intent()
        intent.action = Constant.NOTIFICATION_ACTION_STOP

        val intentPending = PendingIntent.getBroadcast(this, Constant.NOTIFICATION_BROADCAST, intent, PendingIntent.FLAG_UPDATE_CURRENT)

        return NotificationCompat.Action.Builder(R.drawable.ic_stop, resources.getString(R.string.notification_foreground_stop), intentPending).build()
    }
}