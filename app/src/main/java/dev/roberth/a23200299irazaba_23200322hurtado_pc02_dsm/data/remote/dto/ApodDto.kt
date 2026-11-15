package dev.roberth.a23200299irazaba_23200322hurtado_pc02_dsm.data.remote.dto

import com.google.gson.annotations.SerializedName
import dev.roberth.a23200299irazaba_23200322hurtado_pc02_dsm.data.local.entity.ApodEntity

data class ApodDto(
    @SerializedName("date") val date: String?,
    @SerializedName("post_id") val postId: Long?,
    @SerializedName("title") val title: String?,
    @SerializedName("permalink") val permalink: String?,
    @SerializedName("media_type") val mediaType: String?,
    @SerializedName("explanation") val explanation: String?,
    @SerializedName("credit") val credit: String?,
    @SerializedName("copyright") val copyright: String?,
    @SerializedName("alt") val alt: String?,
    @SerializedName("url") val url: String?,
    @SerializedName("hdurl") val hdUrl: String?
)

/**
 * Extension function to map ApodDto to Room ApodEntity securely with null handling.
 */
fun ApodDto.toEntity(): ApodEntity {
    val displayUrl = url ?: hdUrl ?: permalink.orEmpty()
    val displayCredit = when {
        !copyright.isNullOrBlank() -> copyright
        !credit.isNullOrBlank() -> credit
        else -> "NASA / Público"
    }

    return ApodEntity(
        date = date.orEmpty(),
        title = title ?: "Sin título",
        explanation = explanation ?: "Sin descripción disponible.",
        url = displayUrl,
        mediaType = mediaType ?: "image",
        hdUrl = hdUrl ?: url,
        copyright = displayCredit
    )
}
