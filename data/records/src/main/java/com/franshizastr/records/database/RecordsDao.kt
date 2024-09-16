package com.franshizastr.records.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface RecordsDao {

    @Insert
    suspend fun insertRecord(record: RecordEntity)

    @Query("DELETE FROM $RECORD_TABLE_NAME WHERE teamId = :teamId")
    suspend fun deleteTeamRecords(teamId: String)

    @Query("SELECT * FROM $RECORD_TABLE_NAME ORDER BY time ASC")
    fun getAllTeams(): Flow<List<RecordEntity>>
}