package dev.roberth.a23200299irazaba_23200322hurtado_pc02_dsm.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "asteroid_table")
data class AsteroidEntity(
    @PrimaryKey val id: String,
    val name: String,
    val absoluteMagnitude: Double,
    val estimatedDiameterKmMin: Double,
    val estimatedDiameterKmMax: Double,
    val isPotentiallyHazardous: Boolean,
    val closeApproachDate: String,
    val relativeVelocityKmH: Double,
    val missDistanceKm: Double,
    val isFavorite: Boolean = false
)
