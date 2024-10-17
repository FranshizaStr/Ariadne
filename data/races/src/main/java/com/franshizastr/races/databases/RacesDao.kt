package com.franshizastr.races.databases

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface RacesDao {

    @Insert
    suspend fun insertRace(race: RaceEntity)

    @Query("DELETE FROM $RACE_TABLE_NAME WHERE teamId = :teamId")
    suspend fun deleteTeamRaces(teamId: String)

    @Update
    suspend fun updateRace(race: RaceEntity)

    @Query("SELECT * FROM $RACE_TABLE_NAME WHERE teamId = :teamId")
    fun getRacesByTeamId(teamId: String): Flow<List<RaceEntity>>

    @Query("SELECT * FROM $RACE_TABLE_NAME WHERE id =:raceId")
    fun getRaceById(raceId: String): RaceEntity
}