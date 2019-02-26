package util

import com.mongodb.client.MongoDatabase
import org.litote.kmongo.KMongo

object Database {
    fun forceCreateClient() = connect { mongoDatabase -> mongoDatabase.name }

    private val client = KMongo.createClient("localhost", 27017)

    @JvmStatic
    val database: MongoDatabase = client.getDatabase("rainbow")

    @JvmStatic
    inline fun connect(block : (MongoDatabase) -> Unit) = block(database)

    @JvmStatic
    fun close() = client.close()
}