package ru.tesserakt.bot.rainbow.core

import java.net.URI
import java.sql.Connection
import java.sql.DriverManager
import java.sql.Statement

object Database{
    private val connection : Connection
    private val statement : Statement

    init {
        val dbUri = URI(System.getenv("DATABASE_URL"))
        val userData = dbUri.userInfo.split(':')
        val userName = userData[0]
        val password = userData[1]
        val dbUrl = "jdbc:postgresql://${dbUri.host}:${dbUri.port}${dbUri.path}?sslmode=require"

        connection = DriverManager.getConnection(dbUrl, userName, password)
        statement = connection.createStatement()
    }

    fun disconnect() {
        statement.close()
        connection.close()
    }

    fun execute(command : String) =
        statement.executeQuery(command)
}
