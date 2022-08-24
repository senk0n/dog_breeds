package dev.senk0n.dogbreeds.shared.core

sealed interface Option<out T> {
    val valueOrNull: T? get() = if (this is Some) this.value else null
}

class Some<T>(val value: T) : Option<T>

object None : Option<Nothing>
