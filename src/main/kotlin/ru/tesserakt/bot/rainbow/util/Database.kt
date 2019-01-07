package ru.tesserakt.bot.rainbow.util

import reactor.core.publisher.Mono
import reactor.core.publisher.toMono
import reactor.core.scheduler.Schedulers
import java.net.URI
import java.sql.Connection
import java.sql.DriverManager
import java.sql.Statement

object Database {
    private val connection: Mono<Connection>
    private val statement: Mono<Statement>

    init {
        val dbURI = URI(System.getenv("DATABASE_URL"))
        val userData = dbURI.userInfo.split(':')
        val user = userData[0]
        val password = userData[1]
        val dbURL = "jdbc:postgresql://${dbURI.host}:${dbURI.port}${dbURI.path}?sslmode=require"

        Class.forName("org.postgresql.Driver")

        connection = DriverManager.getConnection(dbURL, user, password).toMono()
        statement = connection.map { it.createStatement() }
    }

    fun <R> execute(statement: String): R {
        @Suppress("UNCHECKED_CAST")
        val result = this.statement.subscribeOn(Schedulers.single())
                .filter { !it.isClosed }.switchIfEmpty(connection.map { it.createStatement() })
                .map { it.executeQuery(statement) }
                .filter { it.next() }
                .map { it.getObject(1) as R }
                .doFinally { this.statement.map { statement -> statement.cancel() } }

        result.subscribe()
        return result.block()!!
    }

    fun close() {
        statement.filter { !it.isClosed }.map { it.cancel() }
        connection.filter { !it.isClosed }.map { it.close() }
    }
}