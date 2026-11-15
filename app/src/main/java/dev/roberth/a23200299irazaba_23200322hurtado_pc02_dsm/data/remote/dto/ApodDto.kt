package dev.roberth.a23200299irazaba_23200322hurtado_pc02_dsm.data.remote.dto

import com.google.gson.annotations.SerializedName
import dev.roberth.a23200299irazaba_23200322hurtado_pc02_dsm.data.local.entity.ApodEntity

data class ApodDto(
    @SerializedName("date") val date: String?,
    @SerializedName("title") val title: String?,
    @SerializedName("explanation") val explanation: String?,
    @SerializedName("url") val url: String?,
    @SerializedName("media_type") val mediaType: String?,
    @SerializedName("hdurl") val hdUrl: String?,
    @SerializedName("copyright") val copyright: String?
)

/**
 * Extension function to map ApodDto to Room ApodEntity securely with null handling.
 */
fun ApodDto.toEntity(): ApodEntity {
    return ApodEntity(
        date = date.orEmpty(),
        title = title ?: "Sin título",
        explanation = explanation ?: "Sin descripción disponible.",
        url = url.orEmpty(),
        mediaType = mediaType ?: "image",
        hdUrl = hdUrl,
        copyright = copyright ?: "NASA / Público"
    )
}
