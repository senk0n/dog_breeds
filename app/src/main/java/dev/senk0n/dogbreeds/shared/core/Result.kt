package dev.senk0n.dogbreeds.shared.core

sealed interface ResultState<out T> {

    /**
     * Converts ResultState<T> into ResultState<R>.
     */
    fun <R> map(mapper: ((T) -> R)? = null): ResultState<R> = when (this) {
        is Pending -> Pending
        is Empty -> Empty
        is Success -> {
            requireNotNull(mapper) { "Can't map to Success result without mapper." }
            Success(mapper(this.value))
        }
        is Error -> Error(cause)
    }

    val valueOrNull: T? get() = if (this is Success) this.value else null

    val isFinished: Boolean get() = this is Success || this is Error
}

object Pending : ResultState<Nothing>

object Empty : ResultState<Nothing>

sealed interface Result<T> : ResultState<T>

class Success<T>(val value: T) : Result<T>

class Error<T>(val cause: Throwable) : Result<T>
