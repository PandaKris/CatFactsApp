package com.kristantosean.catfactsapp.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.kristantosean.catfactsapp.data.CatFact
import com.kristantosean.catfactsapp.utils.LocalDateConverter
import com.kristantosean.catfactsapp.utils.ThreeTenGsonAdapter
import org.threeten.bp.LocalDateTime

@Entity
data class CatFactLocal(
    @PrimaryKey
    val id: String,
    val text: String,
    val updatedAt: String?,
    val isDeleted: Boolean,
    val source: String?
)

fun CatFactLocal.asDomainModel(): CatFact {
    return CatFact(id, text, LocalDateTime.parse(updatedAt, LocalDateConverter.FORMATTER), isDeleted, source)
}

fun List<CatFactLocal>.asDomainModel(): List<CatFact> {
    return map {
        CatFact(
            it.id,
            it.text,
            LocalDateTime.parse(it.updatedAt, LocalDateConverter.FORMATTER),
            it.isDeleted,
            it.source
        )
    }
}