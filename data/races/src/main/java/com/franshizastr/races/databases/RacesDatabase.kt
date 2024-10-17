package com.franshizastr.races.databases

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(
    entities = [RaceEntity::class],
    version = 1,
    exportSchema = false
)
abstract class RacesDatabase : RoomDatabase() {

    abstract val dao: RacesDao
}