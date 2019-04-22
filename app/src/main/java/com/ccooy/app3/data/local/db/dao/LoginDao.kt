package com.ccooy.app3.data.local.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.ccooy.app3.data.model.db.LoginEntity
import io.reactivex.Flowable

/**
 * Not used.
 */
@Dao
abstract class LoginDao {

    @Query("SELECT * FROM login_user WHERE username = :username")
    abstract fun findUserByName(username: String): Flowable<LoginEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract fun insert(user: LoginEntity): Long
}