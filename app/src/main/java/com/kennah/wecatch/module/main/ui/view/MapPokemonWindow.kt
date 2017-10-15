package com.kennah.wecatch.module.main.ui.view

import android.content.Context
import android.view.View
import android.widget.LinearLayout
import android.widget.RelativeLayout
import android.widget.TextView
import butterknife.BindString
import butterknife.BindView
import butterknife.ButterKnife
import com.google.android.gms.maps.model.Marker
import com.kennah.wecatch.R
import com.kennah.wecatch.core.utils.ResourceUtils
import com.kennah.wecatch.core.utils.TimeUtils
import com.kennah.wecatch.core.withDelay
import com.kennah.wecatch.local.Constant
import com.kennah.wecatch.local.model.Pokemon
import com.kennah.wecatch.local.utils.ColorUtils
import org.jetbrains.anko.textColor
import java.text.SimpleDateFormat
import java.util.*

class MapPokemonWindow(context: Context) : LinearLayout(context) {

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

        if (pokemon.iv != -1) {
            mLayoutPokemonDetails.visibility = View.VISIBLE

            mTextDetails.text = resources.getString(R.string.pokemonWindow_iv_details,
                    pokemon.iv,
                    pokemon.attack,
                    pokemon.defense,
                    pokemon.stamina,
                    pokemon.cp
            )

            mTextMove1.text = if (pokemon.move1 > Constant.MOVE_SET) {
                ResourceUtils.getStringResource(context, "move_0")
            } else {
                ResourceUtils.getStringResource(context, "move_" + pokemon.move1)
            }

            mTextMove2.text = if (pokemon.move2 > Constant.MOVE_SET) {
                ResourceUtils.getStringResource(context, "move_0")
            } else {
                ResourceUtils.getStringResource(context, "move_" + pokemon.move2)
            }
        }
    }
}