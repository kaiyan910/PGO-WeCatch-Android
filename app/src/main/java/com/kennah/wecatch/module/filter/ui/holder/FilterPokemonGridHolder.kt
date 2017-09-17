package com.kennah.wecatch.module.filter.ui.holder

import android.content.Context
import android.support.v7.widget.AppCompatCheckBox
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import butterknife.BindView
import butterknife.ButterKnife
import butterknife.OnCheckedChanged
import butterknife.OnClick
import com.kennah.wecatch.R
import com.kennah.wecatch.core.base.BaseHolder
import com.kennah.wecatch.core.utils.LogUtils
import com.kennah.wecatch.core.utils.ResourceUtils

class FilterPokemonGridHolder(context: Context): BaseHolder<Int>(context) {

    @BindView(R.id.background)
    lateinit var mLayoutBackground: LinearLayout
    @BindView(R.id.icon)
    lateinit var mImageIcon: ImageView
    @BindView(R.id.name)
    lateinit var mTextName: TextView
    @BindView(R.id.choice)
    lateinit var mCheckBoxChoice: AppCompatCheckBox
    @BindView(R.id.number)
    lateinit var mTextNumber: TextView

    private lateinit var mCallback: (Int, Boolean) -> Unit
    private var mInit = false

    init {

        inflate(context, R.layout.holder_filter_pokemon, this)
        ButterKnife.bind(this)
    }

    @OnCheckedChanged(R.id.choice)
    fun onChoiceChange(choice: Boolean) {

        if (mInit) {
            LogUtils.debug("DEBUG#FilterPokemonGridHolder", "onChoiceChange=[$mData]")
            mCallback(mData, choice)
        }
    }

    @OnClick(R.id.background)
    fun onBackgroundClick() {
        mCheckBoxChoice.isChecked = !mCheckBoxChoice.isChecked
    }

    fun bind(data: Int, filtered: Boolean, cb: (Int, Boolean) -> Unit) {
        super.bind(data)

        mCallback = cb

        mTextNumber.text = data.toString()
        mTextName.text = ResourceUtils.getStringResource(context, "pokemon_$data")
        mImageIcon.setImageResource(ResourceUtils.getDrawableResource(context, "pkm_$data"))

        mCheckBoxChoice.isChecked = !filtered

        mLayoutBackground.setBackgroundResource(if (data % 2 == 0) {
            R.drawable.bg_holder_even
        } else {
            R.drawable.bg_holder_odd
        })

        mInit = true
    }
}