package ru.tesserakt.bot.rainbow.core

import org.jooq.DSLContext
import org.jooq.impl.DSL
import java.net.URI


object Database{
    private val dbURL : String
    private val user : String
    private val password : String

    init {
        val dbURI = URI(System.getenv("DATABASE_URL"))
        val userData = dbURI.userInfo.split(':')
        user = userData[0]
        password = userData[1]
        dbURL = "jdbc:postgresql://${dbURI.host}:${dbURI.port}${dbURI.path}?sslmode=require"
    }

    fun <R> connect(block : DSLContext.() -> R) : R {
        val context =  DSL.using(dbURL, user, password)
        val result = block(context)
        context.close()
        return result
    }
}
