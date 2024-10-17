package com.franshizastr

sealed interface CleanResult<out T> {
    data class Success<out T>(val value: T) : CleanResult<T>
    data class Failure(val error: Error) : CleanResult<Nothing>

    val valueOrNull: T?
        get() = (this as? Success)?.value

    fun <S> unwrapWithCallbacks(
        onSuccess: (result: T) -> S,
        onError: (error: Failure) -> S,
    ): S {
        return when(this) {
            is Success -> onSuccess(value)
            is Failure -> onError(this)
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

data class ErrorVO(
    val value: String
)

fun CleanResult.Error.map(): ErrorVO? {
    return this.message?.uppercase()?.let { ErrorVO(it) }
}

fun String.toErrorVO(): ErrorVO {
    return ErrorVO(this.uppercase())
}