package com.franshizastr.login.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface TeamsDao {

    @Insert
    suspend fun insertTeam(team: TeamsEntity)

    @Update
    suspend fun updateTeam(team: TeamsEntity)

    @Query("DELETE FROM $TEAMS_TABLE_NAME WHERE id IN (:ids)")
    suspend fun deleteTeam(ids: List<String>)

    @Query("SELECT * FROM $TEAMS_TABLE_NAME")
    fun getAllTeams(): Flow<List<TeamsEntity>>

    @Query("SELECT * FROM $TEAMS_TABLE_NAME WHERE id==:teamId")
    fun getTeamById(teamId: String): TeamsEntity
}