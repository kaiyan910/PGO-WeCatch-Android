package com.kennah.wecatch.module.main.ui.activity

import android.Manifest
import android.support.v4.app.Fragment
import android.support.v7.widget.Toolbar
import android.widget.FrameLayout
import butterknife.BindView
import butterknife.OnClick
import com.kennah.wecatch.R
import com.kennah.wecatch.core.base.BaseActivity
import com.kennah.wecatch.module.main.ui.fragment.MainFragment
import dagger.android.AndroidInjector
import dagger.android.DispatchingAndroidInjector
import dagger.android.support.HasSupportFragmentInjector
import permissions.dispatcher.RuntimePermissions
import javax.inject.Inject
import permissions.dispatcher.NeedsPermission
import org.jetbrains.anko.alert
import org.jetbrains.anko.noButton
import org.jetbrains.anko.yesButton
import permissions.dispatcher.OnShowRationale
import permissions.dispatcher.PermissionRequest
import permissions.dispatcher.OnPermissionDenied

@RuntimePermissions
class MainActivity : BaseActivity(), HasSupportFragmentInjector {

    @BindView(R.id.container)
    lateinit var mLayoutContainer: FrameLayout

    @Inject
    lateinit var fragmentInjector: DispatchingAndroidInjector<Fragment>

    override fun afterViews() {
        super.afterViews()
        MainActivityPermissionsDispatcher.showLocationWithCheck(this)
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        MainActivityPermissionsDispatcher.onRequestPermissionsResult(this, requestCode, grantResults)
    }

    @NeedsPermission(Manifest.permission.ACCESS_FINE_LOCATION)
    fun showLocation() {
        setupFragment()
    }

    @OnPermissionDenied(Manifest.permission.ACCESS_FINE_LOCATION)
    fun showDeniedForLocation() {
        setupFragment()
    }

    @OnShowRationale(Manifest.permission.ACCESS_FINE_LOCATION)
    fun showRationaleForLocation(request: PermissionRequest) {

        alert("Please give me LOCATION permission", "Permission needed!") {
            yesButton { request.proceed() }
            noButton { request.cancel() }
        }.show()
    }

    private fun setupFragment() {

        supportFragmentManager.beginTransaction()
                .replace(R.id.container, MainFragment(), "MainFragment")
                .addToBackStack(null)
                .commit()
    }

    override fun supportFragmentInjector(): AndroidInjector<Fragment> = fragmentInjector

    override fun layout(): Int = R.layout.activity_main

    override fun toolbar(): Toolbar? = null

    override fun title(): String {
        return resources.getString(R.string.app_name)
    }

}
