package com.franshizastr.records.models

import android.location.Location
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.UUID


data class RecordModel(
    val id: String,
    val teamId: String,
    val raceId: String,
    val time: String,
    val longitude: Double,
    val latitude: Double,
    val altitude: Double,
)

fun Location.map(
    teamId: String,
    raceId: String
): RecordModel {
    return with(this) {
        RecordModel(
            id = UUID.randomUUID().toString(),
            teamId = teamId,
            longitude = this.longitude,
            latitude = this.latitude,
            time = getISOTime(),
            altitude = this.altitude,
            raceId = raceId
        )
    }
}

private fun getISOTime(): String {
    val sdf = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ", Locale.ROOT)
    val formattedDate: String = sdf.format(Date())
    return formattedDate
}