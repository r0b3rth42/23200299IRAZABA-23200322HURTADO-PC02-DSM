package dev.roberth.a23200299irazaba_23200322hurtado_pc02_dsm.data.remote

import dev.roberth.a23200299irazaba_23200322hurtado_pc02_dsm.data.remote.dto.AsteroidFeedResponseDto
import retrofit2.http.GET
import retrofit2.http.Query

interface NeoWsApiService {
    @GET("neo/rest/v1/feed")
    suspend fun getAsteroidFeed(
        @Query("start_date") startDate: String,
        @Query("end_date") endDate: String
    ): AsteroidFeedResponseDto
}
