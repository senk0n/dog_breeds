package dev.senk0n.dogbreeds.shared.core

open class AppException(
    message: String? = null, cause: Throwable? = null
) : Throwable(message, cause)