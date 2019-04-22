package com.ccooy.app3.di.builder

import com.ccooy.app3.ui.login.LoginActivity
import com.ccooy.app3.ui.main.MainActivity
import com.ccooy.app3.ui.splash.SplashActivity
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
internal abstract class ActivityBuilder {

//    @ContributesAndroidInjector(modules = [AboutFragmentProvider::class, RateUsDialogProvider::class])
//    internal abstract fun bindMainActivity(): MainActivity

    @ContributesAndroidInjector
    internal abstract fun bindSplashActivity(): SplashActivity

    @ContributesAndroidInjector
    internal abstract fun bindLoginActivity(): LoginActivity

    @ContributesAndroidInjector
    internal abstract fun bindMainActivity(): MainActivity

}