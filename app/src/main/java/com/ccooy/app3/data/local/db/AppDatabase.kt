package com.ccooy.app3.data.local.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.ccooy.app3.data.local.db.dao.LoginDao
import com.ccooy.app3.data.model.db.LoginEntity

@Database(
    entities = [LoginEntity::class],
    version = 1
)
abstract class AppDatabase : RoomDatabase() {

    abstract fun loginDao(): LoginDao
}