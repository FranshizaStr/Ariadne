package com.franshizastr.records.models

import android.app.Activity
import com.franshizastr.records.AndroidFileWriter

sealed class RecordsScreenEvent {
    data class SaveCSVFileWithRecords(val fileWriter: AndroidFileWriter) : RecordsScreenEvent()
    data class TakeNewRecord(val activity: Activity) : RecordsScreenEvent()
    data object OnErrorEventShown: RecordsScreenEvent()
    data object DeleteRecords: RecordsScreenEvent()
}