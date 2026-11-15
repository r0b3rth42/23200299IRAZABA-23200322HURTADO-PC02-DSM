package dev.roberth.a23200299irazaba_23200322hurtado_pc02_dsm.data.remote.dto

import com.google.gson.annotations.SerializedName
import dev.roberth.a23200299irazaba_23200322hurtado_pc02_dsm.data.local.entity.AsteroidEntity

data class AsteroidFeedResponseDto(
    @SerializedName("element_count") val elementCount: Int?,
    @SerializedName("near_earth_objects") val nearEarthObjects: Map<String, List<AsteroidDto>>?
)

data class AsteroidDto(
    @SerializedName("id") val id: String?,
    @SerializedName("name") val name: String?,
    @SerializedName("absolute_magnitude_h") val absoluteMagnitude: Double?,
    @SerializedName("estimated_diameter") val estimatedDiameter: EstimatedDiameterDto?,
    @SerializedName("is_potentially_hazardous_asteroid") val isPotentiallyHazardous: Boolean?,
    @SerializedName("close_approach_data") val closeApproachData: List<CloseApproachDataDto>?
)

data class EstimatedDiameterDto(
    @SerializedName("kilometers") val kilometers: KilometersDto?
)

data class KilometersDto(
    @SerializedName("estimated_diameter_min") val min: Double?,
    @SerializedName("estimated_diameter_max") val max: Double?
)

data class CloseApproachDataDto(
    @SerializedName("close_approach_date") val closeApproachDate: String?,
    @SerializedName("relative_velocity") val relativeVelocity: RelativeVelocityDto?,
    @SerializedName("miss_distance") val missDistance: MissDistanceDto?
)

data class RelativeVelocityDto(
    @SerializedName("kilometers_per_hour") val kilometersPerHour: String?
)

data class MissDistanceDto(
    @SerializedName("kilometers") val kilometers: String?
)

/**
 * Extension function to map AsteroidDto to Room AsteroidEntity securely with null handling.
 */
fun AsteroidDto.toEntity(): AsteroidEntity {
    val approach = closeApproachData?.firstOrNull()
    return AsteroidEntity(
        id = id.orEmpty(),
        name = name ?: "Asteroide Desconocido",
        absoluteMagnitude = absoluteMagnitude ?: 0.0,
        estimatedDiameterKmMin = estimatedDiameter?.kilometers?.min ?: 0.0,
        estimatedDiameterKmMax = estimatedDiameter?.kilometers?.max ?: 0.0,
        isPotentiallyHazardous = isPotentiallyHazardous ?: false,
        closeApproachDate = approach?.closeApproachDate ?: "Fecha no disponible",
        relativeVelocityKmH = approach?.relativeVelocity?.kilometersPerHour?.toDoubleOrNull() ?: 0.0,
        missDistanceKm = approach?.missDistance?.kilometers?.toDoubleOrNull() ?: 0.0
    )
}
