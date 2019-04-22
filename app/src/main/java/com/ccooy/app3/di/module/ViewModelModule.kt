package com.ccooy.app3.di.module

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.ccooy.app3.base.loadings.CommonLoadingViewModel
import com.ccooy.app3.base.loadings.ILoadingDelegate
import com.ccooy.app3.base.viewmodel.ViewModelFactory
import com.ccooy.app3.di.ViewModelKey
import com.ccooy.app3.ui.login.LoginViewModel
import com.ccooy.app3.ui.main.MainViewModel
import com.ccooy.app3.ui.splash.*
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract class ViewModelModule {

    @Binds
    abstract fun bindViewModelFactory(factory: ViewModelFactory): ViewModelProvider.Factory

    @Binds
    @IntoMap
    @ViewModelKey(SplashViewModel::class)
    abstract fun bindSplashViewModel(splashViewModel: SplashViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(LoginViewModel::class)
    abstract fun bindLoginViewModel(loginViewModel: LoginViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(MainViewModel::class)
    abstract fun bindMainViewModel(mainViewModel: MainViewModel): ViewModel
}