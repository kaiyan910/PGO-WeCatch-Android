package com.kennah.wecatch.module.main.ui.view

import android.content.Context
import android.view.View
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import butterknife.BindView
import butterknife.ButterKnife
import com.kennah.wecatch.R
import com.kennah.wecatch.core.utils.ResourceUtils
import com.kennah.wecatch.core.utils.TimeUtils
import com.kennah.wecatch.local.model.Gym

class GymMarker(context: Context): RelativeLayout(context) {

    @BindView(R.id.team)
    lateinit var mImageTeam: ImageView
    @BindView(R.id.number)
    lateinit var mTextNumber: TextView
    @BindView(R.id.boss)
    lateinit var mImageRaidBoss: ImageView
    @BindView(R.id.focus)
    lateinit var mImageFocus: ImageView

    init {
        View.inflate(context, R.layout.view_gym_marker, this)
        ButterKnife.bind(this)
    }

    fun bind(gym: Gym) {

        val raidLevel = gym.raidLevel ?: 0

        if (raidLevel != 0 && !TimeUtils.isExpired(gym.raidEndMs ?: 0)) {
            val raidImage = when (raidLevel) {
                in 1..2 -> "ic_raid_normal"
                in 3..4 -> "ic_raid_rare"
                else -> "ic_raid_legendary"
            }

            if (raidLevel >= 4) {
                mImageFocus.setImageResource(R.drawable.ic_focus_rare)
            }

            mImageFocus.visibility = View.VISIBLE
            mImageTeam.setImageResource(ResourceUtils.getDrawableResource(context, raidImage))
            if (gym.raidPokemonId != 0)
                mImageRaidBoss.setImageResource(ResourceUtils.getDrawableResource(context, "pkm_${gym.raidPokemonId}"))
        } else {
            mImageTeam.setImageResource(ResourceUtils.getDrawableResource(context, "ic_gym_${gym.team}"))
        }

        mTextNumber.text = gym.details?.size.toString()
    }
}