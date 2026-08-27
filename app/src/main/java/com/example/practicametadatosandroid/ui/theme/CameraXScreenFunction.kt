package com.example.practicametadatosandroid.ui.theme

import android.Manifest
import android.content.Context
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import androidx.camera.core.ImageProxy
import androidx.camera.view.CameraController
import androidx.camera.view.LifecycleCameraController
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Camera
import androidx.compose.material.icons.filled.Cameraswitch
import androidx.compose.material.icons.filled.Photo
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.exifinterface.media.ExifInterface
import com.example.practicametadatosandroid.ui.theme.MainViewModel
import com.example.practicametadatosandroid.ui.theme.PhotoBottomSheetContent
import com.example.practicametadatosandroid.ui.theme.PhotoInfo
import kotlinx.coroutines.launch
import java.io.ByteArrayInputStream
import com.example.practicametadatosandroid.CameraPreview

//Camera permissions array
val CAMERAX_PERMISSIONS = arrayOf(
    Manifest.permission.CAMERA,
    Manifest.permission.RECORD_AUDIO
)

//Function to validate all the camera permission are allowed
fun Context.hasRequiredPermissions(): Boolean {
    return CAMERAX_PERMISSIONS.all {
        ContextCompat.checkSelfPermission(this, it) == android.content.pm.PackageManager.PERMISSION_GRANTED
    }
}

//composable function, inside this function there is the logic for the camera
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CameraXScreenFunction(viewModel: MainViewModel) {

    //declare the context of the app
    val context = LocalContext.current
    //Declare the lifecycles of the camera
    val lifecycleOwner = androidx.lifecycle.compose.LocalLifecycleOwner.current

    //call the function to verify the state of the permissions
    var hasCameraPermission by remember {
        mutableStateOf(context.hasRequiredPermissions())
    }

    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        hasCameraPermission = permissions[Manifest.permission.CAMERA] == true
    }

    LaunchedEffect(Unit) {
        if (!hasCameraPermission) {
            launcher.launch(CAMERAX_PERMISSIONS)
        }
    }
 //in case the permissions are not granted
    if (!hasCameraPermission) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Text("Se requieren permisos de cámara para continuar.")
        }
        return
    }

    val scope = rememberCoroutineScope()
    val scaffoldState = rememberBottomSheetScaffoldState()

    // Vinculamos el ciclo de vida explícitamente para solucionar la pantalla negra
    val controller = remember {
        LifecycleCameraController(context).apply {
            setEnabledUseCases(
                CameraController.IMAGE_CAPTURE or CameraController.VIDEO_CAPTURE
            )
            bindToLifecycle(lifecycleOwner)
        }
    }

    //we check the photos which are in the gallery
    val _photos by viewModel.photos.collectAsState()

    BottomSheetScaffold(
        scaffoldState = scaffoldState,
        sheetPeekHeight = 0.dp,
        sheetContent = {
            //we use this
            PhotoBottomSheetContent(
                bitmaps = _photos,
                modifier = Modifier.fillMaxWidth()
            )
        }
    ) { padding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            //we call the camera view to show the camera in the screen
            CameraPreview(
                controller = controller,
                modifier = Modifier.fillMaxSize()
            )

            //Botton to change between the front and back camera
            IconButton(
                onClick = {
                    controller.cameraSelector =
                        if (controller.cameraSelector == CameraSelector.DEFAULT_BACK_CAMERA) {
                            CameraSelector.DEFAULT_FRONT_CAMERA
                        } else {
                            CameraSelector.DEFAULT_BACK_CAMERA
                        }
                },
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(16.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Cameraswitch,
                    contentDescription = "Cambio de camara"
                )
            }
            //we add a row to organised the UI
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.BottomCenter) //with this alignment the bottons are in the box
                    .padding(bottom = 24.dp, start = 16.dp, end = 16.dp),
                horizontalArrangement = Arrangement.SpaceAround,
                verticalAlignment = Alignment.CenterVertically
            ) {
                //Botton to show the galley og the pictures
                IconButton(
                    onClick = {
                        // we used the scope declared before to expand it and show the pictures
                        scope.launch { scaffoldState.bottomSheetState.expand() }
                    }
                ) {
                    Icon(
                        imageVector = Icons.Default.Photo,
                        contentDescription = "Gallery Pictures"
                    )
                }

                //Botton to take the picture
                IconButton(
                    onClick = {
                        takePhoto(
                            context = context,
                            controller = controller,
                            //call the fucntion to take the picture
                            onPhotoTaken = viewModel::onTakePhoto
                        )
                    }
                ) {
                    Icon(
                        imageVector = Icons.Default.Camera,
                        contentDescription = "Take Picture"
                    )
                }
            }
        }
    }
}

//The function most important to take the picture
fun takePhoto(
    context: Context,
    controller: LifecycleCameraController,
    onPhotoTaken: (PhotoInfo) -> Unit
) {
    controller.takePicture(
        ContextCompat.getMainExecutor(context),
        object : ImageCapture.OnImageCapturedCallback() {
            override fun onCaptureSuccess(image: ImageProxy) {
                super.onCaptureSuccess(image)


                val bufferImageDta = image.planes[0].buffer
                val bytes = ByteArray(bufferImageDta.remaining())
                bufferImageDta.get(bytes)
                //Initialize the data which is going to be set in the photoInfo model(the medtadata we are going to set)
                var nombreFoto = "Undefined"
                var dateTaken = "Desconocida"
                var orientation = "Normal"
                val width = image.width
                val height = image.height

                try {
                    ByteArrayInputStream(bytes).use { stream ->
                        //USE OF ExifInterface to get the photo's metadata
                        val exInterface = ExifInterface(stream)
                        //set the values
                        dateTaken = exInterface.getAttribute(ExifInterface.TAG_DATETIME)
                            ?: "NO DATETIME REGISTERED"
                        val rawOrientation = exInterface.getAttributeInt(
                            ExifInterface.TAG_ORIENTATION,
                            ExifInterface.ORIENTATION_UNDEFINED
                        )

                        orientation = when (rawOrientation) {
                            ExifInterface.ORIENTATION_ROTATE_90 -> "90°"
                            ExifInterface.ORIENTATION_ROTATE_180 -> "180°"
                            ExifInterface.ORIENTATION_ROTATE_270 -> "270°"
                            else -> "Normal (0°)"
                        }
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                }
                //Once the photo has been taken ww convert the imege to bitmap is order to be able to show it
                onPhotoTaken(
                    PhotoInfo(
                        bitmap = image.toBitmap(),
                        dateTaken = dateTaken,
                        orientation = orientation,
                        dimensions = "(W: ${width}, H:${height})px"
                    )
                )

                image.close()
            }

            override fun onError(exception: ImageCaptureException) {
                super.onError(exception)
            }
        }
    )
}