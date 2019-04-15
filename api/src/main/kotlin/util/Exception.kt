package util

/**
 * An exception that shows it\`s too low permissions to run anything
 */
class NoPermissionsException(message: String = "Недостаточно привелегий", cause: Throwable? = null) :
    IllegalStateException(message, cause)

class CommandException(cause: Throwable) : RuntimeException(cause) {
    override fun getLocalizedMessage(): String {
        tailrec fun getError(error: Throwable?): String =
            if (error?.message.isNullOrEmpty() || error?.message == "null")
                getError(error?.cause)
            else error?.message ?: "неизвестно"

        return getError(this.cause)
    }
}