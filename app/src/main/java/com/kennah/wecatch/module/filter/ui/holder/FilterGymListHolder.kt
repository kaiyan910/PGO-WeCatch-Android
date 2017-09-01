package com.kennah.wecatch.module.filter.ui.holder

import android.content.Context
import android.support.v7.widget.AppCompatCheckBox
import android.widget.ImageView
import android.widget.TextView
import butterknife.BindString
import butterknife.BindView
import butterknife.ButterKnife
import butterknife.OnCheckedChanged
import com.kennah.wecatch.R
import com.kennah.wecatch.core.base.BaseHolder

class FilterGymListHolder(context: Context) : BaseHolder<Int>(context) {

    @BindView(R.id.icon)
    lateinit var mImageIcon: ImageView
    @BindView(R.id.raid)
    lateinit var mTextRaid: TextView
    @BindView(R.id.choice)
    lateinit var mCheckBoxChoice: AppCompatCheckBox

    @BindString(R.string.filter_gym)
    lateinit var mStringGym: String
    @BindString(R.string.filter_raid)
    lateinit var mStringRaid: String

    private lateinit var mCallback: (Int, Boolean) -> Unit

    init {

        inflate(context, R.layout.holder_filter_gym, this)
        ButterKnife.bind(this)
    }

    @OnCheckedChanged(R.id.choice)
    fun onChoiceChange(choice: Boolean) = mCallback(mData, choice)

    fun bind(data: Int, filtered: Boolean, cb: (Int, Boolean) -> Unit) {
        super.bind(data)

        mCallback = cb

        mImageIcon.setImageResource(when (data) {
           0 -> R.drawable.ic_gym_0
           in 1..2 -> R.drawable.ic_raid_normal
           in 3..4 -> R.drawable.ic_raid_rare
           else -> R.drawable.ic_raid_legendary
        })

        mTextRaid.text = when (data) {
            0 -> mStringGym
            else -> String.format(mStringRaid, data)
        }

        mCheckBoxChoice.isChecked = !filtered
    }
}