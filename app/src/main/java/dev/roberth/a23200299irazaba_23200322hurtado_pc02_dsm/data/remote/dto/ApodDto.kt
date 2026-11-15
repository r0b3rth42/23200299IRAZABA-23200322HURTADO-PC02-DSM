package dev.roberth.a23200299irazaba_23200322hurtado_pc02_dsm.data.remote.dto

import androidx.core.text.HtmlCompat
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

private fun String?.cleanHtml(): String {
    if (this.isNullOrBlank()) return ""
    return HtmlCompat.fromHtml(this, HtmlCompat.FROM_HTML_MODE_LEGACY).toString().trim()
}

/**
 * Extension function to map ApodDto to Room ApodEntity securely with null handling and HTML stripping.
 */
fun ApodDto.toEntity(): ApodEntity {
    val directImageUrl = hdUrl ?: url ?: permalink.orEmpty()
    val rawCredit = when {
        !copyright.isNullOrBlank() -> copyright
        !credit.isNullOrBlank() -> credit
        else -> "NASA / Público"
    }

    val cleanedTitle = title.cleanHtml().ifEmpty { "Sin título" }
    val cleanedExplanation = explanation.cleanHtml().ifEmpty { "Sin descripción disponible." }
    val cleanedCredit = rawCredit.cleanHtml().ifEmpty { "NASA / Público" }

    return ApodEntity(
        date = date.orEmpty(),
        title = cleanedTitle,
        explanation = cleanedExplanation,
        url = directImageUrl,
        mediaType = mediaType ?: "image",
        hdUrl = hdUrl ?: url,
        copyright = cleanedCredit
    )
}
