package com.franshizastr.records.models

import android.location.Location
import com.franshizastr.TimeUtils
import java.util.UUID

data class RecordModel(
    val id: String,
    val teamId: String,
    val time: Long,
    val longitude: Double,
    val latitude: Double,
    val altitude: Double
)

fun Location.map(
    teamId: String,
): RecordModel {
    return with(this) {
        RecordModel(
            id = UUID.randomUUID().toString(),
            teamId = teamId,
            longitude = this.longitude,
            latitude = this.longitude,
            time = TimeUtils.getTimeInstant(),
            altitude = this.altitude
        )
    }
}