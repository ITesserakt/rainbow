package util

class NoPermissionsException(message: String = "Недостаточно привелегий", cause: Throwable? = null)
    : IllegalStateException(message, cause)