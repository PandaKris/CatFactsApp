package com.kristantosean.catfactsapp.data

import com.google.gson.annotations.SerializedName
import com.kristantosean.catfactsapp.data.local.CatFactLocal
import com.kristantosean.catfactsapp.utils.LocalDateConverter
import org.threeten.bp.LocalDateTime

data class CatFact(
    @SerializedName("_id") val id: String,
    @SerializedName("text") val text: String,
    @SerializedName("updatedAt") val updatedAt: LocalDateTime?,
    @SerializedName("deleted") val isDeleted: Boolean,
    @SerializedName("source") val source: String?
)

fun CatFact.asDatabaseModel(): CatFactLocal {
    return CatFactLocal(id, text, updatedAt?.format(LocalDateConverter.FORMATTER), isDeleted, source)
}

fun List<CatFact>.asDatabaseModel(): List<CatFactLocal> {
    return map {
        CatFactLocal(
            it.id,
            it.text,
            it.updatedAt?.format(LocalDateConverter.FORMATTER),
            it.isDeleted,
            it.source
        )
    }
}