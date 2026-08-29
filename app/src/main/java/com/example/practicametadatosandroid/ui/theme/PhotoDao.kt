package com.example.practicametadatosandroid

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface PhotoDao {

    // 1. Obtener todas las fotos (Devuelve una lista de EntityPhoto)
    @Query("SELECT * FROM photos ORDER BY dateTaken DESC")
    fun getAllPhotos(): Flow<List<EntityPhoto>>

    // 2. Obtener una foto por su ID (Recibe id_photo: Int y devuelve EntityPhoto?)
    @Query("SELECT * FROM photos WHERE id = :id_photo")
    suspend fun getPhotoById(id_photo: Int): EntityPhoto?

    // 3. Insertar una foto en la base de datos
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPhoto(photo: EntityPhoto): Long
}