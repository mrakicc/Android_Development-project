package com.example.projekat.users

import androidx.datastore.core.CorruptionException
import androidx.datastore.core.Serializer
import kotlinx.serialization.SerializationException
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import com.example.projekat.networking.serialization.AppJson
import java.io.InputStream
import java.io.OutputStream
import java.nio.charset.StandardCharsets

class UsersDataSerializer() : Serializer<UsersData> {
    private val json: Json = AppJson

    override val defaultValue: UsersData = UsersData.EMPTY
    override suspend fun readFrom(input: InputStream): UsersData {
        val text = String(input.readBytes(), charset= StandardCharsets.UTF_8)
        return try{
            json.decodeFromString<UsersData>(text)
        }catch (error: SerializationException){
            throw CorruptionException(message = "Unable to deserialize file", cause = error)
        }catch (error: IllegalArgumentException) {
            throw CorruptionException(message = "Unable to deserialize file.", cause = error)
        }
    }

    override suspend fun writeTo(t: UsersData, output: OutputStream) {
        val text = json.encodeToString(t)
        output.write(text.toByteArray())
    }


}