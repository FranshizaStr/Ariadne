package com.franshizastr.records.database

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.franshizastr.records.models.RecordModel

const val RECORD_TABLE_NAME = "records"

@Entity(tableName = RECORD_TABLE_NAME)
data class RecordEntity(
    @PrimaryKey
    val id: String,
    val teamId: String,
    val raceId: String,
    val time: String,
    val longitude: Double,
    val latitude: Double,
    val altitude: Double
)

fun RecordEntity.map(): RecordModel {
    return with(this) {
        RecordModel(
            id = id,
            teamId = teamId,
            time = time,
            longitude = longitude,
            latitude = latitude,
            altitude = altitude,
            raceId = raceId
        )
    }
}

fun RecordModel.map(): RecordEntity {
    return with(this) {
        RecordEntity(
            id = id,
            teamId = teamId,
            time = time,
            longitude = longitude,
            latitude = latitude,
            altitude = altitude,
            raceId = raceId
        )
    }
}