package com.ccooy.app3.ui.login

import com.ccooy.app3.base.repository.BaseRepositoryBoth
import com.ccooy.app3.base.repository.ILocalDataSource
import com.ccooy.app3.base.repository.IRemoteDataSource
import com.ccooy.app3.data.cache.UserCache
import com.ccooy.app3.data.local.db.repo.LoginDatabaseRepository
import com.ccooy.app3.data.local.prefs.PreferencesRepository
import com.ccooy.app3.data.model.db.LoginEntity
import com.ccooy.app3.data.model.response.LoginUser
import com.ccooy.app3.data.model.vo.Errors
import com.ccooy.app3.data.remote.GithubService
import com.ccooy.app3.ext.functional.Either
import com.ccooy.app3.util.net.BasicAuthInterceptor
import com.ccooy.app3.util.rx.RxSchedulers
import io.reactivex.Completable
import io.reactivex.Flowable
import io.reactivex.Single
import javax.inject.Inject
import javax.inject.Singleton

interface ILoginRemoteDataSource : IRemoteDataSource {
    fun login(username: String, password: String): Flowable<Either<Errors, LoginUser>>
}

interface ILoginLocalDataSource : ILocalDataSource {
    fun savePrefsUser(username: String, password: String): Completable

    fun fetchPrefsUser(): Flowable<Either<Errors, LoginEntity>>

    fun isAutoLogin(): Single<Boolean>
}

@Singleton
class LoginRepository @Inject constructor(
    remoteDataSource: ILoginRemoteDataSource,
    localDataSource: ILoginLocalDataSource
) : BaseRepositoryBoth<ILoginRemoteDataSource, ILoginLocalDataSource>(remoteDataSource, localDataSource) {
    fun login(username: String, password: String): Flowable<Either<Errors, LoginUser>> =
        remoteDataSource.login(username, password)
            .doOnNext { either ->
                either.fold({

                }, {
                    UserCache.INSTANCE = it
                })
            }
            .flatMap {
                localDataSource.savePrefsUser(username, password)
                    .andThen(Flowable.just(it))
            }

    fun prefsUser(): Flowable<Either<Errors, LoginEntity>> =
        localDataSource.fetchPrefsUser()

    fun prefsAutoLogin(): Single<Boolean> =
        localDataSource.isAutoLogin()
}

class LoginRemoteDataSource @Inject constructor(
    private val service: GithubService
) : ILoginRemoteDataSource {

    @Inject
    lateinit var auth: BasicAuthInterceptor

    override fun login(username: String, password: String): Flowable<Either<Errors, LoginUser>> {
        auth.login(username, password)
        return service.login()
            .subscribeOn(RxSchedulers.io)
            .map {
                Either.right(it)
            }
    }

}

class LoginLocalDataSource @Inject constructor(
    private val database: LoginDatabaseRepository,
    private val prefs: PreferencesRepository
) : ILoginLocalDataSource {
    override fun isAutoLogin(): Single<Boolean> =
        Single.just(prefs.autoLogin)

    override fun savePrefsUser(username: String, password: String): Completable =
        Completable.fromAction {
            prefs.username = username
            prefs.password = password
        }

    override fun fetchPrefsUser(): Flowable<Either<Errors, LoginEntity>> =
        Flowable.just(prefs)
            .map {
                when (it.username.isNotEmpty() && it.password.isNotEmpty()) {
                    true -> Either.right(LoginEntity(1, it.username, it.password))
                    false -> Either.left(Errors.EmptyResultsError)
                }
            }

}