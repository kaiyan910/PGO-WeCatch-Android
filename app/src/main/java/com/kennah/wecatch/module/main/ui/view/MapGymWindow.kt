package com.kennah.wecatch.module.main.ui.view

import android.content.Context
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import butterknife.BindView
import butterknife.ButterKnife
import com.google.android.gms.maps.model.Marker
import com.kennah.wecatch.R
import com.kennah.wecatch.core.utils.ResourceUtils
import com.kennah.wecatch.core.utils.TimeUtils
import com.kennah.wecatch.core.withDelay
import com.kennah.wecatch.local.model.Gym
import java.text.SimpleDateFormat
import java.util.*

class MapGymWindow(context: Context) : LinearLayout(context) {

    @BindView(R.id.raid_wrapper)
    lateinit var mLayoutRaidWrapper: LinearLayout
    @BindView(R.id.name)
    lateinit var mTextName: TextView
    @BindView(R.id.start_time)
    lateinit var mTextStartTime: TextView
    @BindView(R.id.expire_time)
    lateinit var mTextExpireTime: TextView
    @BindView(R.id.no_raid)
    lateinit var mTextNoRaid: TextView
    @BindView(R.id.team_logo)
    lateinit var mImageTeamLogo: ImageView

    init {
        inflate(context, R.layout.view_map_gym_window, this)
        ButterKnife.bind(this)
    }

    fun bind(gym: Gym, marker: Marker) {

        if (gym.raidLevel == 0) {
            mTextNoRaid.visibility = View.VISIBLE
        } else {
            mLayoutRaidWrapper.visibility = View.VISIBLE

            val name = if (gym.raidPokemonId != 0) {
                ResourceUtils.getStringResource(context, "pokemon_" + gym.raidPokemonId)
            } else {
                "-"
            }
            val timeLeft = TimeUtils.getTimeLeftWithHour(gym.raidEndMs ?: 0, "-")

            mTextName.text = String.format("%s (%s)", name, timeLeft)
            mTextStartTime.text = SimpleDateFormat("hh:mm:ss a", Locale.getDefault()).format(gym.raidBattleMs)
            mTextExpireTime.text = SimpleDateFormat("hh:mm:ss a", Locale.getDefault()).format(gym.raidEndMs)

            mImageTeamLogo.setImageResource(ResourceUtils.getDrawableResource(context, "logo_team_" + gym.team))

            withDelay(1000) {

                if (marker.isInfoWindowShown && !TimeUtils.isExpired(gym.raidEndMs)) {
                    marker.hideInfoWindow()
                    marker.showInfoWindow()
                }
            }
        }
    }
}