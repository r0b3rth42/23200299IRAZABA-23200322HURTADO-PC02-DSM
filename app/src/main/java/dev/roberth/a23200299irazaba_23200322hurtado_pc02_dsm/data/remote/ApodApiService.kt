package dev.roberth.a23200299irazaba_23200322hurtado_pc02_dsm.data.remote

import dev.roberth.a23200299irazaba_23200322hurtado_pc02_dsm.data.remote.dto.ApodDto
import retrofit2.http.GET
import retrofit2.http.Query

interface ApodApiService {
    @GET("planetary/apod")
    suspend fun getApod(
        @Query("date") date: String? = null
    ): ApodDto

    @GET("planetary/apod")
    suspend fun getApodRange(
        @Query("start_date") startDate: String,
        @Query("end_date") endDate: String
    ): List<ApodDto>
}
