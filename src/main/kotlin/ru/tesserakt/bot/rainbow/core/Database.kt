package ru.tesserakt.bot.rainbow.core

import org.jooq.DSLContext
import org.jooq.impl.DSL
import java.net.URI


object Database{
    private val dbURL : String
    private val user : String
    private val password : String

    private val connection : DSLContext

    init {
        val dbURI = URI(System.getenv("DATABASE_URL"))
        val userData = dbURI.userInfo.split(':')
        user = userData[0]
        password = userData[1]
        dbURL = "jdbc:postgresql://${dbURI.host}:${dbURI.port}${dbURI.path}?sslmode=require"

        connection = DSL.using(dbURL, user, password)
    }

    fun <R> connect(block : DSLContext.() -> R) : R {
        return block(connection)
    }

    internal fun close() {
        connection.close()
    }
}
