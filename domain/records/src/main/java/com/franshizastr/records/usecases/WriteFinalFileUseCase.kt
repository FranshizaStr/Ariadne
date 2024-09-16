package com.franshizastr.records.usecases

import android.net.Uri
import com.franshizastr.CleanResult
import com.franshizastr.ContextProvider
import com.franshizastr.records.models.RecordModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import javax.inject.Inject

class WriteFinalFileUseCase @Inject constructor(
    private val contextProvider: ContextProvider,
    private val dispatcher: CoroutineDispatcher
) {

    suspend fun execute(
        records: List<RecordModel>,
        uri: Uri?
    ): CleanResult<Unit> {
        val context = contextProvider.context
        uri ?: return CleanResult.Failure(
            error = CleanResult.Error(
                previousError = null,
                level = CleanResult.Error.ErrorLevel.DOMAIN,
                throwable = null,
                message = "error while writing to file - no URI"
            )
        )
        return withContext(dispatcher) {
            try {
                context.contentResolver.openOutputStream(uri)?.writer()?.run {
                    records.forEach { record ->
                        write("${record.latitude}, ${record.longitude}, ${record.time}\r\n")
                    }
                    flush()
                    close()
                }
                CleanResult.Success(Unit)
            } catch (ex: Throwable) {
                CleanResult.Failure(
                    error = CleanResult.Error(
                        previousError = null,
                        message = "error while writing to file - IO error",
                        throwable = ex,
                        level = CleanResult.Error.ErrorLevel.DOMAIN
                    )
                )
            }
        }
    }
}