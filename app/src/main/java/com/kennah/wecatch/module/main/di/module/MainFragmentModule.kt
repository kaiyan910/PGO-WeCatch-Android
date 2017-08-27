package com.kennah.wecatch.module.main.di.module

import com.kennah.wecatch.local.FilterManager
import com.kennah.wecatch.local.service.DataService
import com.kennah.wecatch.module.main.contract.MainContract
import com.kennah.wecatch.module.main.di.MainFragmentScope
import com.kennah.wecatch.module.main.presenter.MainPresenter
import com.kennah.wecatch.module.main.ui.fragment.MainFragment
import dagger.Module
import dagger.Provides

@Module
class MainFragmentModule {

    @Provides
    @MainFragmentScope
    fun provideMainView(mainFragment: MainFragment): MainContract.View {
        return mainFragment
    }

    @Provides
    @MainFragmentScope
    fun provideMainPresenter(view: MainContract.View, service: DataService, filterManager: FilterManager): MainContract.Presenter {
        return MainPresenter(view, service,filterManager)
    }
}