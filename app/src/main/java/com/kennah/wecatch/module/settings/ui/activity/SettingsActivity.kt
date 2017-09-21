package com.kennah.wecatch.module.settings.ui.activity

import android.support.v4.app.Fragment
import android.support.v7.widget.Toolbar
import butterknife.BindView
import com.kennah.wecatch.R
import com.kennah.wecatch.core.base.BaseActivity
import com.kennah.wecatch.module.settings.ui.fragment.SettingsFragment
import dagger.android.AndroidInjector
import dagger.android.DispatchingAndroidInjector
import dagger.android.support.HasSupportFragmentInjector
import javax.inject.Inject


class SettingsActivity: BaseActivity(), HasSupportFragmentInjector {

    @BindView(R.id.toolbar)
    lateinit var mToolbar: Toolbar

    @Inject
    lateinit var mFragmentInjector: DispatchingAndroidInjector<Fragment>

    private var mNeedRestart = false

    override fun afterViews() {
        super.afterViews()
        setupBackNavigation()
        setupFragment()
    }

    override fun onNavigationPressed() {

        if (supportFragmentManager.backStackEntryCount > 0) {
            supportFragmentManager.popBackStack()
        } else {

            super.onNavigationPressed()
        }
    }

    override fun title(): String = getString(R.string.settings_title)

    override fun toolbar(): Toolbar? = mToolbar

    override fun layout(): Int = R.layout.activity_settings

    override fun supportFragmentInjector(): AndroidInjector<Fragment> = mFragmentInjector

    private fun setupFragment() {

        supportFragmentManager.beginTransaction()
                .replace(R.id.container, SettingsFragment(), "SettingsFragment")
                .commit()
    }
}