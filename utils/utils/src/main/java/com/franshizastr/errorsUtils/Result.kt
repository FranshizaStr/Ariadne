package com.franshizastr.errorsUtils

sealed interface CleanResult<out T> {
    data class Success<out T>(val value: T) : CleanResult<T>
    data class Failure<out T>(val error: Error) : CleanResult<T>
    data class Progress<out T>(val progress: Int): CleanResult<T>

    val isFailure: Boolean
        get() = this is Failure

    val isSuccess: Boolean
        get() = this is Success

    val isProgress: Boolean
        get() = this is Progress

    val valueOrNull: T?
        get() = (this as? Success)?.value

    fun unwrapWithCallbacks(
        onSuccess: (result: T) -> Unit,
        onError: (error: Error) -> Unit,
        onProgress: (progress: Int) -> Unit
    ) {
        when {
            isSuccess -> onSuccess((this as Success).value)
            isFailure -> onError((this as Failure).error)
            isProgress -> onProgress((this as Progress).progress)
        }
    }

    abstract class Error(
        val previousError: Error?,
        val throwable: Throwable?,
        val message: String?,
        val level: ErrorLevel,
    ) {
        enum class ErrorLevel {
            DOMAIN,
            DATA,
            PRESENTATION
        }
    }
}