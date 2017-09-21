package com.kennah.wecatch.module.main.ui.view

import android.view.View
import android.widget.RelativeLayout
import android.widget.TextView
import butterknife.BindString
import butterknife.BindView
import butterknife.ButterKnife
import com.kennah.wecatch.R
import com.kennah.wecatch.core.utils.LogUtils
import com.kennah.wecatch.core.utils.ResourceUtils
import com.kennah.wecatch.core.utils.TimeUtils
import com.kennah.wecatch.core.withDelay
import com.kennah.wecatch.local.model.Pokemon
import org.osmdroid.views.MapView
import org.osmdroid.views.overlay.Marker
import org.osmdroid.views.overlay.infowindow.InfoWindow
import java.text.SimpleDateFormat
import java.util.*

class OsmPokemonWindow(val pokemon: Pokemon, val marker: Marker, val layout: Int, map: MapView) : InfoWindow(layout, map) {

    @BindView(R.id.name)
    lateinit var mTextName: TextView
    @BindView(R.id.expire_time)
    lateinit var mTextExpireTime: TextView

    @BindView(R.id.pokemon_details)
    lateinit var mLayoutPokemonDetails: RelativeLayout
    @BindView(R.id.details)
    lateinit var mTextDetails: TextView
    @BindView(R.id.move_1)
    lateinit var mTextMove1: TextView
    @BindView(R.id.move_2)
    lateinit var mTextMove2: TextView

    @BindString(R.string.pokemonWindow_iv)
    lateinit var mStringIv: String
    @BindString(R.string.pokemonWindow_iv_details)
    lateinit var mStringIvDetails: String

    init {
        ButterKnife.bind(this, mView)
    }

    override fun onOpen(item: Any?) {

        InfoWindow.closeAllInfoWindowsOn(mMapView)

        val context = mView.context

        val name = ResourceUtils.getStringResource(context, "pokemon_" + pokemon.pokemonId)
        val timeLeft = TimeUtils.getTimeLeft(pokemon.expireTime, "-")

        mTextName.text = String.format("%s (%s)", name, timeLeft)
        mTextExpireTime.text = SimpleDateFormat("hh:mm:ss a", Locale.getDefault()).format(pokemon.expireTime)

        withDelay(1000) {

            if (isOpen && !TimeUtils.isExpired(pokemon.expireTime)) {
                marker.showInfoWindow()
            }
        }

        if (pokemon.iv != -1) {
            mLayoutPokemonDetails.visibility = View.VISIBLE

            mTextDetails.text = mView.context.resources.getString(R.string.pokemonWindow_iv_details,
                    pokemon.iv,
                    pokemon.attack,
                    pokemon.defense,
                    pokemon.stamina,
                    pokemon.cp
            )

            mTextMove1.text = ResourceUtils.getStringResource(context, "move_" + pokemon.move1)
            mTextMove2.text = ResourceUtils.getStringResource(context, "move_" + pokemon.move2)
        }
    }

    override fun onClose() {
    }

}