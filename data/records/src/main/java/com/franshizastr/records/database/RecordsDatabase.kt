package com.franshizastr.records.database

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(
    entities = [RecordEntity::class],
    version = 1,
    exportSchema = false
)
abstract class RecordsDatabase : RoomDatabase() {

    abstract val dao: RecordsDao
}