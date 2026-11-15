package dev.roberth.a23200299irazaba_23200322hurtado_pc02_dsm.data.repository

import dev.roberth.a23200299irazaba_23200322hurtado_pc02_dsm.data.local.dao.AsteroidDao
import dev.roberth.a23200299irazaba_23200322hurtado_pc02_dsm.data.local.entity.AsteroidEntity
import dev.roberth.a23200299irazaba_23200322hurtado_pc02_dsm.data.remote.NeoWsApiService
import dev.roberth.a23200299irazaba_23200322hurtado_pc02_dsm.data.remote.dto.toEntity
import kotlinx.coroutines.flow.Flow
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class AsteroidRepository(
    private val asteroidDao: AsteroidDao,
    private val apiService: NeoWsApiService
) {
    val allAsteroids: Flow<List<AsteroidEntity>> = asteroidDao.getAllAsteroids()
    val favoriteAsteroids: Flow<List<AsteroidEntity>> = asteroidDao.getFavoriteAsteroids()

    fun getAsteroidById(id: String): Flow<AsteroidEntity?> = asteroidDao.getAsteroidById(id)

    suspend fun refreshAsteroids() {
        val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.US)
        val calendar = Calendar.getInstance()
        val startDateStr = dateFormat.format(calendar.time)
        calendar.add(Calendar.DAY_OF_YEAR, 6)
        val endDateStr = dateFormat.format(calendar.time)

        try {
            val response = apiService.getAsteroidFeed(startDate = startDateStr, endDate = endDateStr)
            val nearEarthObjects = response.nearEarthObjects ?: emptyMap()
            val favoriteIds = asteroidDao.getFavoriteIds().toSet()

            val flattenedAsteroids = nearEarthObjects.values.flatten()
            val entities = flattenedAsteroids.map { dto ->
                val entity = dto.toEntity()
                if (favoriteIds.contains(entity.id)) {
                    entity.copy(isFavorite = true)
                } else {
                    entity
                }
            }
            if (entities.isNotEmpty()) {
                asteroidDao.insertAsteroids(entities)
            }
        } catch (e: Exception) {
        }
    }

    suspend fun toggleFavorite(id: String, currentStatus: Boolean) {
        asteroidDao.updateFavoriteStatus(id, !currentStatus)
    }
}
