@file:OptIn(ExperimentalMaterial3Api::class)

package com.example.practicametadatosandroid

import android.Manifest
import android.content.pm.PackageManager
import androidx.exifinterface.media.ExifInterface
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import androidx.camera.core.ImageProxy
import androidx.camera.view.CameraController
import androidx.camera.view.LifecycleCameraController
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Camera
import androidx.compose.material.icons.filled.Cameraswitch
import androidx.compose.material.icons.filled.Photo
import androidx.compose.material3.BottomSheetScaffold
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Surface
import androidx.compose.material3.rememberBottomSheetScaffoldState
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.practicametadatosandroid.ui.theme.CameraXScreenFunction
import com.example.practicametadatosandroid.ui.theme.LoginScreen
import com.example.practicametadatosandroid.ui.theme.MainViewModel
import kotlinx.coroutines.launch
import com.example.practicametadatosandroid.ui.theme.PhotoBottomSheetContent
import com.example.practicametadatosandroid.ui.theme.PhotoInfo
import com.example.practicametadatosandroid.ui.theme.PracticaMetadatosAndroidTheme
import com.example.practicametadatosandroid.ui.theme.authenticationBiometric

import java.io.ByteArrayInputStream

@OptIn(ExperimentalMaterial3Api::class)
class MainActivity : FragmentActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)



        setContent {
            PracticaMetadatosAndroidTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = androidx.compose.material3.MaterialTheme.colorScheme.background
                ) {
                    var autenticadoUser by remember { mutableStateOf(false) }
                    var showRegister by remember { mutableStateOf(false) }

                    // Renderizado condicional estricto
                    if (!autenticadoUser) {

                        LoginScreen(
                            onAuthenticate = {
                                try {
                                    authenticationBiometric(
                                        activity = this@MainActivity,
                                        onSuccessfulAuthentication = {
                                            autenticadoUser = true
                                        },
                                        onErrorAuthenticationCustomised = { mensaje ->
                                            Toast.makeText(
                                                this@MainActivity,
                                                mensaje,
                                                Toast.LENGTH_SHORT
                                            ).show()
                                        }
                                    )
                                } catch (e: Exception) {
                                    Log.e("MainActivity", "Error en biometría: ${e.message}")
                                    // Si falla la biometría en el emulador, permite el paso manual para probar
                                    Toast.makeText(
                                        this@MainActivity,
                                        "Error biométrico en emulador. Accediendo...",
                                        Toast.LENGTH_SHORT
                                    ).show()

                                }
                            }
                        )


                    } else {
                        // CameraX SOLO se carga cuando autenticadoUser es true
                        val viewModelF = viewModel<MainViewModel>()
                        CameraXScreenFunction(viewModelF)
                    }

                }
            }
        }
    }
}






