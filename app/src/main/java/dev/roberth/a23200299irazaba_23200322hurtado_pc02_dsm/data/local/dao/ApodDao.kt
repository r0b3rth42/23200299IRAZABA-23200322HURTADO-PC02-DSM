package dev.roberth.a23200299irazaba_23200322hurtado_pc02_dsm.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import dev.roberth.a23200299irazaba_23200322hurtado_pc02_dsm.data.local.entity.ApodEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface ApodDao {
    @Query("SELECT * FROM apod_table ORDER BY date DESC")
    fun getAllApods(): Flow<List<ApodEntity>>

    @Query("SELECT * FROM apod_table WHERE isFavorite = 1 ORDER BY date DESC")
    fun getFavoriteApods(): Flow<List<ApodEntity>>

    @Query("SELECT * FROM apod_table WHERE date = :date LIMIT 1")
    fun getApodByDate(date: String): Flow<ApodEntity?>

    @Query("SELECT date FROM apod_table WHERE isFavorite = 1")
    suspend fun getFavoriteDates(): List<String>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertApods(apods: List<ApodEntity>): List<Long>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertApod(apod: ApodEntity): Long

    @Query("UPDATE apod_table SET isFavorite = :isFavorite WHERE date = :date")
    suspend fun updateFavoriteStatus(date: String, isFavorite: Boolean): Int

    @Query("DELETE FROM apod_table")
    suspend fun clearApods(): Int
}
