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
    val time: Long,
    val longitude: Double,
    val latitude: Double
)

fun RecordEntity.map(): RecordModel {
    return with(this) {
        RecordModel(
            id = id,
            teamId = teamId,
            time = time,
            longitude = longitude,
            latitude = latitude
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
            latitude = latitude
        )
    }
}