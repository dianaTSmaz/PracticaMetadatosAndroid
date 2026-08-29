package com.example.practicametadatosandroid.ui.theme
import android.graphics.Bitmap


data class PhotoInfo(

    //bitmap which contains the photoPixels
    val bitmap: Bitmap,
    val dateTaken: String = "Desconocida",
    val orientation: String = "Normal",
    var nombreFoto : String = "Indefinido",
    val dimensions: String = "Desconocidas",
    val id_photo : Int
)
