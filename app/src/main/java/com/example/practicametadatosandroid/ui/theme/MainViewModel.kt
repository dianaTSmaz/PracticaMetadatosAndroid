package com.example.practicametadatosandroid.ui.theme

import android.graphics.Bitmap
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

//Important class to process the new photos taken
class MainViewModel: ViewModel() {

    private val _photos = MutableStateFlow<List<PhotoInfo>>(emptyList())

    //The value of the current picture is preserved in the state
    val photos = _photos.asStateFlow()

    fun onTakePhoto(photoInfo : PhotoInfo){
        //Add the new element in the list which is showed in the appp
        _photos.value = _photos.value + photoInfo
    }
}