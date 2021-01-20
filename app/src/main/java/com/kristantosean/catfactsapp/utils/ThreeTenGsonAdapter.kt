package com.kristantosean.catfactsapp.utils

import com.google.gson.GsonBuilder
import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import com.google.gson.JsonParseException
import org.threeten.bp.LocalDateTime
import org.threeten.bp.format.DateTimeFormatter
import java.lang.reflect.Type

object ThreeTenGsonAdapter {

    fun registerLocalDate(gsonBuilder: GsonBuilder): GsonBuilder {
        return gsonBuilder.registerTypeAdapter(LocalDateTime::class.java, LocalDateConverter())
    }
}

class LocalDateConverter : JsonDeserializer<LocalDateTime> {

    @Throws(JsonParseException::class)
    override fun deserialize(
        json: JsonElement,
        typeOfT: Type,
        context: JsonDeserializationContext
    ): LocalDateTime {
        return LocalDateTime.parse(json.asString, FORMATTER)
    }

    companion object {
        val FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
    }
}
