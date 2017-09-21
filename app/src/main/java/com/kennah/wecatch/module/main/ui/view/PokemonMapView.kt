package com.kennah.wecatch.module.main.ui.view

import android.annotation.SuppressLint
import android.content.Context
import android.view.View
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.Toast
import butterknife.BindView
import butterknife.ButterKnife
import com.google.android.gms.maps.MapView
import com.kennah.wecatch.R
import com.kennah.wecatch.core.withDelay
import com.kennah.wecatch.local.map.MapProvider
import com.kennah.wecatch.local.model.Gym
import com.kennah.wecatch.local.model.Pokemon
import com.kennah.wecatch.local.utils.AnimateUtils
import com.kennah.wecatch.module.main.contract.MainContract
import javax.inject.Inject
import kotlin.collections.ArrayList

@SuppressLint("ViewConstructor")
class PokemonMapView @Inject constructor(context: Context,
                                         private val presenter: MainContract.Presenter,
                                         private val mapProvider: MapProvider) : RelativeLayout(context), MainContract.View {

    @BindView(R.id.container)
    lateinit var mLayoutContainer: RelativeLayout
    @BindView(R.id.loading)
    lateinit var mImageLoading: ImageView

    private var mExpireCheckFlag = true
    private var mScanning = false
    private var mStartedOnce = false

    var onStatusChangeListener: ((loading: Boolean) -> Unit)? = null
    var showLoadingIcon: Boolean = true

    init {

        View.inflate(context, R.layout.view_pokemon_map, this)
        ButterKnife.bind(this)

        mapProvider.onCreate()

        mLayoutContainer.addView(mapProvider.getMapView(), 0)

        presenter.attach(this)

        mapProvider.setCallback({
            mStartedOnce = true
        }, {
            mScanning = false
            mExpireCheckFlag = true
            expireCheck()

            onStatusChangeListener?.invoke(false)
            if (showLoadingIcon) hideLoading()
        })
    }

    fun onResume() {
        mapProvider.onResume()
        mExpireCheckFlag = true
        expireCheck()
    }

    fun onPause() {

        mapProvider.onPause()
        mExpireCheckFlag = false
    }

    fun onDestroy() {
        mapProvider.onDestroy()
        presenter.detach()
    }

    fun scan() {

        if (mScanning) return

        onStatusChangeListener?.invoke(true)
        if (showLoadingIcon) showLoading()
        mExpireCheckFlag = false
        presenter.getPokemon(mapProvider.getBounds(), mapProvider.getZoom())
        mScanning = true
    }

    fun cache(pokemonList: ArrayList<Pokemon>) {

        if (!mStartedOnce) {
            mapProvider.addCachedPokemon(pokemonList)
        } else {
            onPokemonFound(pokemonList, emptyList())
        }
    }

    override fun onError(errorCode: Int) {

        onStatusChangeListener?.invoke(false)
        if (showLoadingIcon) hideLoading()
        mScanning = false
        Toast.makeText(context.applicationContext, errorCode.toString(), Toast.LENGTH_SHORT).show()
    }


    override fun onPokemonFound(pokemonList: List<Pokemon>, gymList: List<Gym>) {
        mapProvider.onPokemonFound(pokemonList, gymList)
    }


    private fun expireCheck() {

        withDelay(5000) {
            if (mExpireCheckFlag) {

                mapProvider.doExpireCheck()
                expireCheck()
            }
        }
    }


    private fun hideLoading() {
        mImageLoading.animation = null
        mImageLoading.visibility = View.GONE
    }

    private fun showLoading() {

        mImageLoading.visibility = View.VISIBLE


        mImageLoading.startAnimation(AnimateUtils.rotateAnimate())
    }
}