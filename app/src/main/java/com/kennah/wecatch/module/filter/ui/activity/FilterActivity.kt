package com.kennah.wecatch.module.filter.ui.activity

import android.support.design.widget.TabLayout
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentStatePagerAdapter
import android.support.v4.view.ViewPager
import android.support.v7.widget.Toolbar
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import butterknife.BindView
import com.kennah.wecatch.R
import com.kennah.wecatch.core.base.BaseActivity
import com.kennah.wecatch.local.FilterManager
import dagger.android.AndroidInjector
import dagger.android.DispatchingAndroidInjector
import dagger.android.support.HasSupportFragmentInjector
import javax.inject.Inject

class FilterActivity: BaseActivity(), HasSupportFragmentInjector {

    @BindView(R.id.toolbar)
    lateinit var mToolbar: Toolbar
    @BindView(R.id.tab)
    lateinit var mTabLayout: TabLayout
    @BindView(R.id.view_pager)
    lateinit var mViewPager: ViewPager

    @Inject
    lateinit var fragmentInjector: DispatchingAndroidInjector<Fragment>
    @Inject
    lateinit var mPagerAdapter: FragmentStatePagerAdapter
    @Inject
    lateinit var mFilterManager: FilterManager

    override fun afterViews() {
        super.afterViews()

        setupBackNavigation()

        mViewPager.apply {
            adapter = mPagerAdapter
            offscreenPageLimit = 2
        }

        mTabLayout.setupWithViewPager(mViewPager)
    }

    override fun onNavigationPressed() {
        mFilterManager.save()
        super.onNavigationPressed()
    }

    override fun title(): String = getString(R.string.filter_title)

    override fun toolbar(): Toolbar? = mToolbar

    override fun layout(): Int = R.layout.activity_filter

    override fun supportFragmentInjector(): AndroidInjector<Fragment> = fragmentInjector
}