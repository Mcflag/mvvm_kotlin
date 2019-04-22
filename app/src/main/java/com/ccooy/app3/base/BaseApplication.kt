package com.ccooy.app3.base

import android.app.Activity
import android.app.Application
import com.ccooy.app3.BuildConfig
import com.ccooy.app3.di.AppInjector
import com.ccooy.app3.ext.logger.initLogger
import com.squareup.leakcanary.LeakCanary
import dagger.android.AndroidInjector
import dagger.android.DispatchingAndroidInjector
import dagger.android.HasActivityInjector
import javax.inject.Inject

class BaseApplication : Application(), HasActivityInjector {

    @Inject
    lateinit var dispatchingAndroidInjector: DispatchingAndroidInjector<Activity>

    override fun activityInjector() = dispatchingAndroidInjector

    override fun onCreate() {
        super.onCreate()
        INSTANCE = this
        AppInjector.init(this)

        initLogger(BuildConfig.DEBUG)
    }

    private fun initLeakCanary() {
        if (LeakCanary.isInAnalyzerProcess(this)) {
            return
        }
        LeakCanary.install(this)
    }

    companion object {
        lateinit var INSTANCE: BaseApplication private set
    }
}