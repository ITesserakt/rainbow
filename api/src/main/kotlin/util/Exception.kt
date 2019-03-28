package util

/**
 * An exception that shows it\`s too low permissions to run anything
 */
class NoPermissionsException(message: String = "Недостаточно привелегий", cause: Throwable? = null) :
    IllegalStateException(message, cause)