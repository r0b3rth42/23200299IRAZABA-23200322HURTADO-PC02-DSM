package dev.roberth.a23200299irazaba_23200322hurtado_pc02_dsm.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "apod_table")
data class ApodEntity(
    @PrimaryKey val date: String,
    val title: String,
    val explanation: String,
    val url: String,
    val mediaType: String,
    val hdUrl: String?,
    val copyright: String?,
    val isFavorite: Boolean = false
)
