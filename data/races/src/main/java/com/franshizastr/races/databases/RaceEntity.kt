package com.franshizastr.races.databases

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.franshizastr.races.models.RaceModel

const val RACE_TABLE_NAME = "races"

@Entity(tableName = RACE_TABLE_NAME)
data class RaceEntity(
    @PrimaryKey
    val id: String,
    val teamId: String,
    val raceName: String
)

fun RaceEntity.map(): RaceModel {
    return with(this) {
        RaceModel(
            id = id,
            teamId = teamId,
            raceName = raceName
        )
    }
}

fun RaceModel.map(): RaceEntity {
    return with(this) {
        RaceEntity(
            id = id,
            teamId = teamId,
            raceName = raceName
        )
    }
}
