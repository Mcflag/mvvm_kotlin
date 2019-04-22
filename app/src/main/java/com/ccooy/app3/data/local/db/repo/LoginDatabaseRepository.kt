package com.ccooy.app3.data.local.db.repo

import com.ccooy.app3.data.local.db.AppDatabase
import com.ccooy.app3.data.model.db.LoginEntity
import io.reactivex.Flowable
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class LoginDatabaseRepository @Inject
constructor(private val mAppDatabase: AppDatabase) {
    fun findUserByName(name: String): Flowable<LoginEntity> =
        mAppDatabase.loginDao().findUserByName(name)

    fun insertUser(user: LoginEntity): Flowable<Long> =
        Flowable.fromCallable<Long> {
            mAppDatabase.loginDao().insert(user)
        }
}