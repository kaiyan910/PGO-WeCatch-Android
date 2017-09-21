package com.kennah.wecatch.module.main.ui.view

import android.annotation.SuppressLint
import android.app.Service
import android.content.Context
import android.graphics.PixelFormat
import android.view.Gravity
import android.view.MotionEvent
import android.view.View
import android.widget.ImageView
import android.widget.RelativeLayout
import butterknife.BindView
import butterknife.ButterKnife
import butterknife.OnClick
import com.kennah.wecatch.R
import android.view.WindowManager
import android.widget.FrameLayout
import com.kennah.wecatch.core.utils.CommonUtils
import com.kennah.wecatch.local.Constant
import com.kennah.wecatch.local.Prefs
import javax.inject.Inject

@SuppressLint("ViewConstructor")
class FloatingMapView @Inject constructor(context: Context, private val pokemonMapView: PokemonMapView, private val prefs: Prefs) : RelativeLayout(context) {

    @BindView(R.id.wrapper)
    lateinit var mLayoutWrapper: RelativeLayout
    @BindView(R.id.indicator)
    lateinit var mImageIndicator: ImageView
    @BindView(R.id.resize)
    lateinit var mImageResize: ImageView
    @BindView(R.id.resize_area)
    lateinit var mLayoutResizeArea: RelativeLayout
    @BindView(R.id.map_container)
    lateinit var mLayoutMapContainer: FrameLayout

    private var mCloseCallback: (() -> Unit)? = null
    private var mWindowManager: WindowManager
    private var params = WindowManager.LayoutParams(
            WindowManager.LayoutParams.WRAP_CONTENT,
            WindowManager.LayoutParams.WRAP_CONTENT,
            WindowManager.LayoutParams.TYPE_PHONE,
            WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
            PixelFormat.TRANSLUCENT
    )

    private var mScreenW = 0
    private var mScreenH = 0

    private var mInitX = 0
    private var mInitY = 0
    private var mInitTouchX = 0.toFloat()
    private var mInitTouchY = 0.toFloat()

    private var mResizeX = 0
    private var mResizeY = 0
    private var mResizeTouchX = 0.toFloat()
    private var mResizeTouchY = 0.toFloat()

    private var mSavedWidth = 0
    private var mSavedHeight = 0

    private var mOpened = false

    init {
        View.inflate(context, R.layout.view_floating_map, this)
        ButterKnife.bind(this)

        mWindowManager = context.getSystemService(Service.WINDOW_SERVICE) as WindowManager

        val screen = CommonUtils.calculateScreenSize(mWindowManager)
        mScreenW = screen[0]
        mScreenH = screen[1]

        params.apply {
            gravity = Gravity.TOP or Gravity.START
            x = 0
            y = 250
        }

        mWindowManager.addView(this, params)

        mSavedWidth = mScreenW
        mSavedHeight = mScreenH / 2

        setOnTouchListener { _, event ->
            when(event.action) {

                MotionEvent.ACTION_DOWN -> {
                    mInitX = params.x
                    mInitY = params.y
                    mInitTouchX = event.rawX
                    mInitTouchY = event.rawY
                    false
                }
                MotionEvent.ACTION_UP -> {
                    val dx = Math.abs(event.rawX - mInitTouchX).toInt()
                    val dy = Math.abs(event.rawY - mInitTouchY).toInt()
                    when {
                        dx < 1 && dy < 1 && !mOpened -> maximize()
                        !mOpened -> adjustPosition()
                    }
                    true
                }
                MotionEvent.ACTION_MOVE -> {
                    params.x = mInitX + (event.rawX - mInitTouchX).toInt()
                    params.y = mInitY + (event.rawY - mInitTouchY).toInt()
                    mWindowManager.updateViewLayout(this, params)
                    true
                }
                else -> false
            }
        }

        mLayoutResizeArea.setOnTouchListener { _, event ->
            when(event.action) {

                MotionEvent.ACTION_DOWN -> {
                    mResizeX = mSavedWidth
                    mResizeY = mSavedHeight

                    mResizeTouchX = event.rawX
                    mResizeTouchY = event.rawY
                    true
                }
                MotionEvent.ACTION_UP -> {
                    mSavedWidth = params.width
                    mSavedHeight = params.height
                    true
                }
                MotionEvent.ACTION_MOVE -> {

                    var adjustX = mResizeX + (event.rawX - mResizeTouchX).toInt()
                    var adjustY = mResizeY + (event.rawY - mResizeTouchY).toInt()

                    params.width = if (adjustX > mScreenW) {
                        mScreenW
                    } else {
                        adjustX
                    }

                    params.height = if (adjustY > mScreenH / 2) {
                        mScreenH / 2
                    } else {
                        adjustY
                    }
                    mWindowManager.updateViewLayout(this, params)
                    true
                }
                else -> false
            }
        }

        if (prefs.getMapProvider() == Constant.MAP_PROVIDER_OSM) {
            mLayoutMapContainer.addView(pokemonMapView)
            pokemonMapView.visibility = View.GONE
        }
    }

    @OnClick(R.id.scan)
    fun scan() {
        pokemonMapView.scan()
    }

    @OnClick(R.id.minimize)
    fun minimize() {

        if (prefs.getMapProvider() == Constant.MAP_PROVIDER_OSM) {
            pokemonMapView.visibility = View.GONE
        } else {
            mLayoutMapContainer.removeView(pokemonMapView)
        }

        pokemonMapView.onPause()

        mOpened = false
        mLayoutWrapper.visibility = View.GONE
        mImageIndicator.visibility = View.VISIBLE
        adjustPosition()
        // set the width/height to wrap_content
        params.width = -2
        params.height = -2
        mWindowManager.updateViewLayout(this, params)
    }

    @OnClick(R.id.close)
    fun close() {

        pokemonMapView.onDestroy()

        mCloseCallback?.invoke()
        mWindowManager.removeView(this)
    }

    fun addCloseCallback(closeCallback: () -> Unit) {
        this.mCloseCallback = closeCallback
    }

    private fun maximize() {
        if (prefs.getMapProvider() == Constant.MAP_PROVIDER_OSM) {
            pokemonMapView.visibility = View.VISIBLE
        } else {
            mLayoutMapContainer.addView(pokemonMapView)
        }
        pokemonMapView.onResume()

        mOpened = true
        mLayoutWrapper.visibility = View.VISIBLE
        mImageIndicator.visibility = View.GONE

        params.width = mSavedWidth
        params.height = mSavedHeight
        mWindowManager.updateViewLayout(this, params)
    }

    private fun adjustPosition() {
        // bounce to left / right edge according to current position
        params.x = when (Math.abs(params.x)) {
            in 0..mScreenW/2 -> 0
            else -> mScreenW
        }
        mWindowManager.updateViewLayout(this, params)
    }
}