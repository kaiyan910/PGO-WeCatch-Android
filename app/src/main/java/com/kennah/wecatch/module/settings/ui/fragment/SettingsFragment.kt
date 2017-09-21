package com.kennah.wecatch.module.settings.ui.fragment

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.text.InputType
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import butterknife.BindView
import butterknife.OnClick
import com.afollestad.materialdialogs.MaterialDialog
import com.kennah.wecatch.R
import com.kennah.wecatch.core.base.BaseFragment
import com.kennah.wecatch.core.utils.LogUtils
import com.kennah.wecatch.local.Constant
import com.kennah.wecatch.local.Prefs
import com.kennah.wecatch.local.filter.FilterHandlerFactory
import com.kennah.wecatch.module.filter.ui.activity.FilterActivity
import com.kennah.wecatch.module.main.ui.activity.MainActivity
import org.jetbrains.anko.support.v4.startActivityForResult
import javax.inject.Inject

class SettingsFragment : BaseFragment() {

    @BindView(R.id.range_container)
    lateinit var mLayoutRange: LinearLayout
    @BindView(R.id.range_summary)
    lateinit var mTextRangeSummary: TextView
    @BindView(R.id.interval_summary)
    lateinit var mTextIntervalSummary: TextView
    @BindView(R.id.filter_summary)
    lateinit var mTextFilterSummary: TextView
    @BindView(R.id.map_provider_summary)
    lateinit var mTextMapProviderSummary: TextView

    @Inject
    lateinit var mPrefs: Prefs

    override fun afterViews(savedInstanceState: Bundle?) {
        initPreferenceView()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            updatePokemonListNumber()
        }
    }

    override fun layout(): Int = R.layout.fragment_settings

    @OnClick(R.id.range_container)
    fun onRangeContainerClick() {

        val builder = MaterialDialog.Builder(activity)

        builder.apply {

            title(R.string.settings_notifyRange_title)
            content(R.string.settings_notifyRange_dialog)
            inputType(InputType.TYPE_CLASS_NUMBER)
            input(null, mPrefs.getNotifyDistance().toString()) { _, input ->
                val newValue = input.toString().toInt()
                mTextRangeSummary.text = resources.getString(R.string.settings_unit_km, newValue)
                mPrefs.setNotifyDistance(newValue)
            }
            show()
        }
    }


    @OnClick(R.id.interval_container)
    fun onIntervalContainerClick() {

        val builder = MaterialDialog.Builder(activity)

        builder.apply {

            title(R.string.settings_notifyInterval_title)
            content(R.string.settings_notifyInterval_dialog)
            inputType(InputType.TYPE_CLASS_NUMBER)
            input(null, mPrefs.getNotifyDelay().toString()) { _, input ->
                val newValue = input.toString().toInt()
                mTextIntervalSummary.text = resources.getString(R.string.settings_unit_minute, newValue)
                mPrefs.setNotifyDelay(newValue)
            }
            show()
        }
    }

    @OnClick(R.id.filter_container)
    fun onFilterContainerClick() {

        startActivityForResult<FilterActivity>(
                Constant.REQ_FILTER,
                FilterActivity.EXTRA_GYM to false,
                FilterActivity.EXTRA_FILTER_TYPE to FilterHandlerFactory.NOTIFICATION)
    }

    @OnClick(R.id.map_provider_container)
    fun onMapProviderContainerClick() {

        val builder = MaterialDialog.Builder(activity)

        builder.apply {
            title(R.string.settings_mapProvider_title)
            items(listOf(Constant.MAP_PROVIDER_GOOGLE, Constant.MAP_PROVIDER_OSM))
            itemsCallback { _, _, _, text ->
                mPrefs.setMapProvider(text.toString())
                mTextMapProviderSummary.text = text.toString()

                val intent = Intent(activity, MainActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                startActivity(intent)
            }
            show()
        }
    }

    private fun initPreferenceView() {

        mTextRangeSummary.text = resources.getString(R.string.settings_unit_km, mPrefs.getNotifyDistance())
        mTextIntervalSummary.text = resources.getString(R.string.settings_unit_minute, mPrefs.getNotifyDelay())
        mTextMapProviderSummary.text = mPrefs.getMapProvider()

        updatePokemonListNumber()
    }

    private fun updatePokemonListNumber() {
        val numberOfPokemon = if (mPrefs.getPokemonNotifyFilter().isEmpty()) {
            Constant.MAX_POKEMON
        } else {
            Constant.MAX_POKEMON - mPrefs.getPokemonNotifyFilter().split(",").size
        }

        mTextFilterSummary.text = resources.getString(R.string.settings_filter_summary, numberOfPokemon)
    }

}