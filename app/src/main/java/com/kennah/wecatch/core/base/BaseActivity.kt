package com.kennah.wecatch.core.base

import android.content.Context
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import butterknife.ButterKnife
import com.kennah.wecatch.core.HttpCode
import com.kennah.wecatch.core.utils.LocaleUtils
import dagger.android.AndroidInjection
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.toast

abstract class BaseActivity: AppCompatActivity(), BaseView, AnkoLogger {

    override fun onCreate(savedInstanceState: Bundle?) {

        AndroidInjection.inject(this)
        super.onCreate(savedInstanceState)
        setContentView(layout())
    }

    override fun setContentView(layoutResID: Int) {
        super.setContentView(layoutResID)
        ButterKnife.bind(this)
        afterViews()
    }

    override fun attachBaseContext(newBase: Context) {
        super.attachBaseContext(LocaleUtils.onAttach(newBase))
    }

    override fun onError(errorCode: Int) {

        when (errorCode) {
            HttpCode.NO_NETWORK -> toast("Error Code $errorCode")
            else -> return
        }
    }

    override fun onBackPressed() {
        onNavigationPressed()
    }

    open fun afterViews() {

    }

    protected fun setupBackNavigation() {

        val toolbar = toolbar()

        toolbar.let {

            toolbar.apply {
                title = title()
                setSupportActionBar(this)
            }

            supportActionBar!!.setDisplayHomeAsUpEnabled(true)
            supportActionBar!!.setDisplayShowHomeEnabled(true)
            toolbar!!.setNavigationOnClickListener { onNavigationPressed() }
        }
    }

    open fun onNavigationPressed() {
        finish()
    }

    abstract fun title(): String
    abstract fun toolbar(): Toolbar?
    abstract fun layout(): Int
}