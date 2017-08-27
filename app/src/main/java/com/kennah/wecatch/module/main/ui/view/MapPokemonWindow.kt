package com.kennah.wecatch.module.main.ui.view

import android.content.Context
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import butterknife.BindView
import butterknife.ButterKnife
import com.google.android.gms.maps.model.Marker
import com.kennah.wecatch.R
import com.kennah.wecatch.core.utils.ResourceUtils
import com.kennah.wecatch.core.utils.TimeUtils
import com.kennah.wecatch.core.withDelay
import com.kennah.wecatch.local.model.Pokemon
import java.text.SimpleDateFormat
import java.util.*

class MapPokemonWindow(context: Context) : LinearLayout(context) {

    @BindView(R.id.name)
    lateinit var mTextName: TextView
    @BindView(R.id.expire_time)
    lateinit var mTextExpireTime: TextView

    init {
        View.inflate(context, R.layout.view_map_pokemon_window, this)
        ButterKnife.bind(this)
    }

    fun bind(pokemon: Pokemon, marker: Marker) {

        val name = ResourceUtils.getStringResource(context, "pokemon_" + pokemon.pokemonId)
        val timeLeft = TimeUtils.getTimeLeft(pokemon.expireTime, "-")

        mTextName.text = String.format("%s (%s)", name, timeLeft)
        mTextExpireTime.text = SimpleDateFormat("hh:mm:ss a", Locale.getDefault()).format(pokemon.expireTime)

        withDelay(1000) {

            if (marker.isInfoWindowShown && !TimeUtils.isExpired(pokemon.expireTime)) {
                marker.hideInfoWindow()
                marker.showInfoWindow()
            }
        }
    }
}