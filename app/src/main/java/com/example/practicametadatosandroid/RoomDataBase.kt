package com.example.practicametadatosandroid

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
@Database(entities = [EntityPhoto::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun notificationDao(): PhotoDao
    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null
        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "my_database.db" // El nombre de tu base de datos SQLite
                )
                    // Para bases de datos pre-pobladas desde assets
                    .createFromAsset("database/my_prepopulated_database.db")
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}