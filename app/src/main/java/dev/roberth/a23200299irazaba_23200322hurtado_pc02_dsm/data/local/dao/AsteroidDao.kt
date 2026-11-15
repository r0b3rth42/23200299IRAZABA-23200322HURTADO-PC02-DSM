package dev.roberth.a23200299irazaba_23200322hurtado_pc02_dsm.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import dev.roberth.a23200299irazaba_23200322hurtado_pc02_dsm.data.local.entity.AsteroidEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface AsteroidDao {
    @Query("SELECT * FROM asteroid_table ORDER BY closeApproachDate DESC")
    fun getAllAsteroids(): Flow<List<AsteroidEntity>>

    @Query("SELECT * FROM asteroid_table WHERE isFavorite = 1 ORDER BY closeApproachDate DESC")
    fun getFavoriteAsteroids(): Flow<List<AsteroidEntity>>

    @Query("SELECT * FROM asteroid_table WHERE isPotentiallyHazardous = 1 ORDER BY closeApproachDate DESC")
    fun getHazardousAsteroids(): Flow<List<AsteroidEntity>>

    @Query("SELECT * FROM asteroid_table WHERE id = :id LIMIT 1")
    fun getAsteroidById(id: String): Flow<AsteroidEntity?>

    @Query("SELECT id FROM asteroid_table WHERE isFavorite = 1")
    suspend fun getFavoriteIds(): List<String>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAsteroids(asteroids: List<AsteroidEntity>): List<Long>

    @Query("UPDATE asteroid_table SET isFavorite = :isFavorite WHERE id = :id")
    suspend fun updateFavoriteStatus(id: String, isFavorite: Boolean): Int

    @Query("DELETE FROM asteroid_table")
    suspend fun clearAsteroids(): Int
}
