package dev.senk0n.dogbreeds.shared.core

open class AppException(
    message: String? = null, cause: Throwable? = null
) : Exception(message, cause)

class ConnectionException(cause: Throwable) : AppException("Connection lost", cause)

class ParseResponseException(
    cause: Throwable
) : AppException("Server's response can't be handled", cause)

open class ServerException(val code: Short, message: String) : AppException(message)

class EmptyStateException : AppException()