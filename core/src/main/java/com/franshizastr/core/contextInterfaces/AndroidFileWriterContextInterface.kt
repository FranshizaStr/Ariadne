package com.franshizastr.core.contextInterfaces

import android.net.Uri
import com.franshizastr.CleanResult

interface AndroidFileWriterContextInterface {
    fun launchNewWrite(
        fileName: String,
        newWriteToFileLambda: (uri: Uri?) -> Unit
    ): CleanResult<Unit>

    fun writeToFile(uri: Uri?)
}