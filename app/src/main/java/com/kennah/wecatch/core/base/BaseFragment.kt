package com.kennah.wecatch.core.base

import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import butterknife.ButterKnife
import com.squareup.otto.Bus
import dagger.android.support.AndroidSupportInjection
import javax.inject.Inject

abstract class BaseFragment: Fragment() {

    @Inject
    lateinit var mBus: Bus

    override fun onAttach(context: Context?) {
        AndroidSupportInjection.inject(this)
        super.onAttach(context)
    }

    override fun onResume() {
        super.onResume()
        mBus.register(this)
    }

    override fun onPause() {
        super.onPause()
        mBus.unregister(this)
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater!!.inflate(layout(), container, false)
        ButterKnife.bind(this, view)
        return view
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        afterViews(savedInstanceState)
    }

    protected abstract fun afterViews(savedInstanceState: Bundle?)
    protected abstract fun layout(): Int
}