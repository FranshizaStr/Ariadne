package com.franshizastr.records.models

import android.text.format.DateFormat
import java.util.Calendar

data class RecordVO(
    val id: String,
    val time: String,
    val longitude: String,
    val latitude: String,
    val altitude: String
)

fun RecordModel.map(): RecordVO {
    val calendar = Calendar.getInstance()
    calendar.timeInMillis = this.time
    val time = DateFormat.format("yyyy-MM-dd HH:mm:ss",calendar).toString()
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