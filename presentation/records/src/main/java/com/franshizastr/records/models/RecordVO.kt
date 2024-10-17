package com.franshizastr.records.models

data class RecordVO(
    val id: String,
    val time: String,
    val longitude: String,
    val latitude: String,
    val altitude: String
)

fun RecordModel.map(): RecordVO {
    return with(this) {
        RecordVO(
            id = id,
            latitude = latitude.toString().substring(0, 8),
            longitude = longitude.toString().substring(0, 8),
            time = time,
            altitude = altitude.toString()
        )
    }
}