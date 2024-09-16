package com.franshizastr

sealed interface CleanResult<out T> {
    data class Success<out T>(val value: T) : CleanResult<T>
    data class Failure(val error: Error) : CleanResult<Nothing>

    val isFailure: Boolean
        get() = this is Failure

    val isSuccess: Boolean
        get() = this is Success

    val valueOrNull: T?
        get() = (this as? Success)?.value

    fun unwrapWithCallbacks(
        onSuccess: (result: T) -> Unit,
        onError: (error: Error) -> Unit,
    ) {
        when {
            isSuccess -> onSuccess((this as Success).value)
            isFailure -> onError((this as Failure).error)
        }
    }

    data class Error(
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