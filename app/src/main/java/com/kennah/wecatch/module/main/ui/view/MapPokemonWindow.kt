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
import com.kennah.wecatch.local.model.Pokemon
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
    @BindView(R.id.iv)
    lateinit var mTextIv: TextView
    @BindView(R.id.iv_attack)
    lateinit var mTextIvAttack: TextView
    @BindView(R.id.iv_defense)
    lateinit var mTextIvDefense: TextView
    @BindView(R.id.iv_stamina)
    lateinit var mTextIvStamina: TextView
    @BindView(R.id.move1)
    lateinit var mTextMove1: TextView
    @BindView(R.id.move2)
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
            mTextIv.text = String.format(mStringIv, pokemon.iv)
            mTextIv.textColor = when(pokemon.iv) {
                in 80..89 -> 0xFF006DF0.toInt()
                in 90..99 -> 0xFF91DC5A.toInt()
                100 -> 0xFFD80027.toInt()
                else -> 0xFF000000.toInt()
            }
            mTextIvAttack.text = pokemon.attack.toString()
            mTextIvDefense.text = pokemon.defense.toString()
            mTextIvStamina.text = pokemon.stamina.toString()
            mTextMove1.text = ResourceUtils.getStringResource(context, "move_" + pokemon.move1)
            mTextMove2.text = ResourceUtils.getStringResource(context, "move_" + pokemon.move2)
        }
    }
}