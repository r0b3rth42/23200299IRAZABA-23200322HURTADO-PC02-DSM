package dev.roberth.a23200299irazaba_23200322hurtado_pc02_dsm.data.repository

import dev.roberth.a23200299irazaba_23200322hurtado_pc02_dsm.data.local.dao.ApodDao
import dev.roberth.a23200299irazaba_23200322hurtado_pc02_dsm.data.local.entity.ApodEntity
import dev.roberth.a23200299irazaba_23200322hurtado_pc02_dsm.data.remote.ApodApiService
import dev.roberth.a23200299irazaba_23200322hurtado_pc02_dsm.data.remote.dto.toEntity
import kotlinx.coroutines.flow.Flow
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class ApodRepository(
    private val apodDao: ApodDao,
    private val apiService: ApodApiService
) {
    val allApods: Flow<List<ApodEntity>> = apodDao.getAllApods()
    val favoriteApods: Flow<List<ApodEntity>> = apodDao.getFavoriteApods()

    fun getApodByDate(date: String): Flow<ApodEntity?> = apodDao.getApodByDate(date)

    suspend fun refreshApods() {
        val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.US)
        val calendar = Calendar.getInstance()
        val endDateStr = dateFormat.format(calendar.time)
        calendar.add(Calendar.DAY_OF_YEAR, -6)
        val startDateStr = dateFormat.format(calendar.time)

        try {
            val responseDtos = apiService.getApodRange(startDate = startDateStr, endDate = endDateStr)
            val favoriteDates = apodDao.getFavoriteDates().toSet()

            val entities = responseDtos.map { dto ->
                val entity = dto.toEntity()
                if (favoriteDates.contains(entity.date)) {
                    entity.copy(isFavorite = true)
                } else {
                    entity
                }
            }
            apodDao.insertApods(entities)
        } catch (e: Exception) {
            try {
                val singleDto = apiService.getApod()
                val favoriteDates = apodDao.getFavoriteDates().toSet()
                val entity = singleDto.toEntity()
                val updatedEntity = if (favoriteDates.contains(entity.date)) entity.copy(isFavorite = true) else entity
                apodDao.insertApod(updatedEntity)
            } catch (ignored: Exception) {
            }
        }
    }

    suspend fun toggleFavorite(date: String, currentStatus: Boolean) {
        apodDao.updateFavoriteStatus(date, !currentStatus)
    }
}
