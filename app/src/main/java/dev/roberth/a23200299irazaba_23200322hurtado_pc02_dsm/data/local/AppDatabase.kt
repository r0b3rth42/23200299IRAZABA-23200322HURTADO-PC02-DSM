package dev.roberth.a23200299irazaba_23200322hurtado_pc02_dsm.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import dev.roberth.a23200299irazaba_23200322hurtado_pc02_dsm.data.local.dao.ApodDao
import dev.roberth.a23200299irazaba_23200322hurtado_pc02_dsm.data.local.dao.AsteroidDao
import dev.roberth.a23200299irazaba_23200322hurtado_pc02_dsm.data.local.entity.ApodEntity
import dev.roberth.a23200299irazaba_23200322hurtado_pc02_dsm.data.local.entity.AsteroidEntity

@Database(
    entities = [ApodEntity::class, AsteroidEntity::class],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {

    abstract fun apodDao(): ApodDao
    abstract fun asteroidDao(): AsteroidDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "diario_espacial_database"
                )
                    .fallbackToDestructiveMigration()
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}
