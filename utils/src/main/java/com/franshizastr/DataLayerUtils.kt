package com.franshizastr

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import kotlin.coroutines.cancellation.CancellationException

suspend fun <T, S> safelyExecuteSuspendableDbOperation(
    entity: S?,
    operationDispatcher: CoroutineDispatcher,
    operationDescription: String,
    operation: suspend (teamsEntity: S?) -> CleanResult<T>,
): CleanResult<T> {
    return withContext(operationDispatcher) {
        try {
            operation(entity)
        } catch (ex: CancellationException) {
            throw ex
        } catch (ex: Throwable) {
            val error = CleanResult.Error(
                previousError = null,
                throwable = ex,
                level = CleanResult.Error.ErrorLevel.DATA,
                message = "exception happened while: $operationDescription"
            )
            CleanResult.Failure(
                error = error
            )
        }
    }
}

fun <T, S> safelyExecuteDbOperation(
    entity: S?,
    operationDescription: String,
    operation: (teamsEntity: S?) -> CleanResult<T>,
): CleanResult<T> {
    return try {
        operation(entity)
    } catch (ex: CancellationException) {
        throw ex
    } catch (ex: Throwable) {
        val error = CleanResult.Error(
            previousError = null,
            throwable = ex,
            level = CleanResult.Error.ErrorLevel.DATA,
            message = "exception happened while: $operationDescription"
        )
        CleanResult.Failure(
            error = error
        )
    }
}