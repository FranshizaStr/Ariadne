package com.franshizastr.records.models

import android.app.Activity
import com.franshizastr.core.contextInterfaces.AndroidFileWriterContextInterface

sealed class RecordsScreenEvent {
    data class SaveCSVFileWithRecords(val fileWriter: AndroidFileWriterContextInterface) : RecordsScreenEvent()
    data class ChangeRecordingStatus(val activity: Activity) : RecordsScreenEvent()
    data object OnErrorEventShown: RecordsScreenEvent()
    data object DeleteRecords: RecordsScreenEvent()
}