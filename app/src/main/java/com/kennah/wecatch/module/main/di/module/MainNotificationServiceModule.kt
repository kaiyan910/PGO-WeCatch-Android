package com.kennah.wecatch.module.main.di.module

import android.app.Application
import android.app.Service
import android.view.WindowManager
import com.kennah.wecatch.App
import com.kennah.wecatch.core.helper.LocationHelper
import com.kennah.wecatch.local.FilterManager
import com.kennah.wecatch.local.Prefs
import com.kennah.wecatch.local.service.DataService
import com.kennah.wecatch.module.main.contract.MainContract
import com.kennah.wecatch.module.main.di.MainNotificationServiceScope
import com.kennah.wecatch.module.main.presenter.MainPresenter
import dagger.Module
import dagger.Provides

@Module
class MainNotificationServiceModule {

    @Provides
    @MainNotificationServiceScope
    fun provideLocationHelper(app: Application): LocationHelper = LocationHelper(app)

    @Provides
    @MainNotificationServiceScope
    fun provideMainPresenter(service: DataService, filterManager: FilterManager, prefs: Prefs): MainContract.Presenter {
        return MainPresenter(service, filterManager, prefs)
    }

    @Provides
    @MainNotificationServiceScope
    fun provideWindowManager(app: Application): WindowManager = app.getSystemService(Service.WINDOW_SERVICE) as WindowManager
}