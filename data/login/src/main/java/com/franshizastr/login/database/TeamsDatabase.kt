package com.franshizastr.login.database

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(
    entities = [TeamsEntity::class],
    version = 1,
    exportSchema = false
)
abstract class TeamsDatabase : RoomDatabase() {

    abstract val dao: TeamsDao
}