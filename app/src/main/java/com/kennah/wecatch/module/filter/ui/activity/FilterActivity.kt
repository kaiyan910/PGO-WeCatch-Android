package com.kennah.wecatch.module.filter.ui.activity

import android.support.design.widget.TabLayout
import android.support.v4.app.Fragment
import android.support.v4.view.ViewPager
import android.support.v7.widget.Toolbar
import android.view.Menu
import android.view.MenuItem
import butterknife.BindView
import com.kennah.wecatch.R
import com.kennah.wecatch.core.base.BaseActivity
import com.kennah.wecatch.local.Constant
import com.kennah.wecatch.local.FilterManager
import com.kennah.wecatch.local.filter.FilterHandlerFactory
import com.kennah.wecatch.module.filter.ui.adapter.FilterPagerAdapter
import com.kennah.wecatch.module.filter.ui.event.SelectChangeEvent
import com.squareup.otto.Bus
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
    lateinit var mPagerAdapter: FilterPagerAdapter
    @Inject
    lateinit var mFilterManager: FilterManager
    @Inject
    lateinit var mOttoBus: Bus

    private val mNeedGym: Boolean by lazy {
        intent.getBooleanExtra(EXTRA_GYM, true)
    }

    private val mFilterType: Int by lazy {
        intent.getIntExtra(EXTRA_FILTER_TYPE, FilterHandlerFactory.NORMAL)
    }

    companion object {
        val EXTRA_GYM = "GYM"
        val EXTRA_FILTER_TYPE = "FILTER_TYPE"
    }

    override fun afterViews() {
        super.afterViews()

        setupBackNavigation()

        mPagerAdapter.setup(mNeedGym, mFilterType)

        mViewPager.apply {
            adapter = mPagerAdapter
            offscreenPageLimit = if (mNeedGym) {
                Constant.POKEMON_GENERATION + 1
            } else {
                Constant.POKEMON_GENERATION
            }
        }

        mTabLayout.setupWithViewPager(mViewPager)
    }

    override fun onNavigationPressed() {
        mFilterManager.save()
        setResult(RESULT_OK)
        super.onNavigationPressed()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_filter, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.menu_select_all -> {
                mOttoBus.post(SelectChangeEvent(mViewPager.currentItem))
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun title(): String = getString(R.string.filter_title)

    override fun toolbar(): Toolbar? = mToolbar

    override fun layout(): Int = R.layout.activity_filter

    override fun supportFragmentInjector(): AndroidInjector<Fragment> = fragmentInjector
}