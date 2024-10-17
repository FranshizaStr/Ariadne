package com.franshizastr.records.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface RecordsDao {

    @Insert
    suspend fun insertRecord(record: RecordEntity)

    @Query("DELETE FROM $RECORD_TABLE_NAME WHERE teamId = :teamId and raceId = :raceId")
    suspend fun deleteTeamRecords(teamId: String, raceId: String)

    @Query("SELECT * FROM $RECORD_TABLE_NAME WHERE teamId = :teamId and raceId = :raceId ORDER BY time ASC")
    fun getAllTeamRecords(teamId: String, raceId: String): Flow<List<RecordEntity>>
}