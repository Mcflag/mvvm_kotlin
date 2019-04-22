package com.ccooy.app3.di.module

import com.ccooy.app3.ui.login.ILoginLocalDataSource
import com.ccooy.app3.ui.login.ILoginRemoteDataSource
import com.ccooy.app3.ui.login.LoginLocalDataSource
import com.ccooy.app3.ui.login.LoginRemoteDataSource
import com.ccooy.app3.ui.main.IReposLocalDataSource
import com.ccooy.app3.ui.main.IReposRemoteDataSource
import com.ccooy.app3.ui.main.ReposLocalDataSource
import com.ccooy.app3.ui.main.ReposRemoteDataSource
import com.ccooy.app3.ui.splash.ISplashLocalDataSource
import com.ccooy.app3.ui.splash.SplashLocalDataSource
import dagger.Binds
import dagger.Module

@Module
abstract class RepositoryModule {

    @Binds
    abstract fun bindSplashLocalRepository(splashLocalDataSource: SplashLocalDataSource): ISplashLocalDataSource

    @Binds
    abstract fun bindLoginLocalRepository(loginLocalDataSource: LoginLocalDataSource): ILoginLocalDataSource

    @Binds
    abstract fun bindLoginRemoteRepository(loginRemoteDataSource: LoginRemoteDataSource): ILoginRemoteDataSource

    @Binds
    abstract fun bindReposLocalRepository(reposLocalDataSource: ReposLocalDataSource): IReposLocalDataSource

    @Binds
    abstract fun bindReposRemoteRepository(reposRemoteDataSource: ReposRemoteDataSource): IReposRemoteDataSource
}