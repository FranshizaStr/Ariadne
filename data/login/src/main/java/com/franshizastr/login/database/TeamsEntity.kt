package com.franshizastr.login.database

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.franshizastr.login.models.TeamModel

const val TEAMS_TABLE_NAME = "teams"

@Entity(tableName = TEAMS_TABLE_NAME)
data class TeamsEntity(
    @PrimaryKey
    val id: String,
    val name: String
)

fun TeamsEntity.map(): TeamModel {
    return with(this) {
        TeamModel(
            id = id,
            teamName = name
        )
    }
}

fun TeamModel.map(): TeamsEntity {
    return with(this) {
        TeamsEntity(
            id = id,
            name = teamName
        )
    }
}