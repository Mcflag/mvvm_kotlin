package com.ccooy.app3.ui.splash

import com.ccooy.app3.R
import com.ccooy.app3.base.repository.BaseRepositoryLocal
import com.ccooy.app3.base.repository.ILocalDataSource
import io.reactivex.Single
import java.util.*
import javax.inject.Inject
import javax.inject.Singleton

val TRANSITION_URLS = arrayOf(R.drawable.ads1, R.drawable.ads2)

interface ISplashLocalDataSource : ILocalDataSource {

    fun getPicUrl(): Single<Int>
}

class SplashLocalDataSource @Inject constructor() : ISplashLocalDataSource {

    override fun getPicUrl(): Single<Int> =
        Single.just(TRANSITION_URLS[Random().nextInt(TRANSITION_URLS.size)])
}

@Singleton
class SplashRepository @Inject constructor(
    localDataSource: ISplashLocalDataSource
) : BaseRepositoryLocal<ISplashLocalDataSource>(localDataSource) {

    fun getPicUrl(): Single<Int> =
        localDataSource.getPicUrl()
}
