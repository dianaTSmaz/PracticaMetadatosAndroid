package com.example.practicametadatosandroid

import androidx.camera.view.LifecycleCameraController
import androidx.camera.view.PreviewView
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.compose.LocalLifecycleOwner

/*@Composable
fun CameraPreview(
    controller : LifecycleCameraController,
    modifier: Modifier = Modifier
){
   val lifeCycleOwner = LocalLifecycleOwner.current
    AndroidView(
        factory = {
            PreviewView(it).apply {
                this.controller = controller
                controller.bindToLifecycle(lifeCycleOwner)
            }
        }
    )
}*/
@Composable
fun CameraPreview(
    controller: LifecycleCameraController,
    modifier: Modifier = Modifier
) {
    val lifecycleOwner = LocalLifecycleOwner.current

    // 1. Vincula el controlador al ciclo de vida con un DisposableEffect
    DisposableEffect(lifecycleOwner) {
        controller.bindToLifecycle(lifecycleOwner)
        onDispose {
            controller.unbind()
        }
    }

    // 2. Aplica el modifier a AndroidView para que ocupe todo el espacio asignado
    AndroidView(
        modifier = modifier, // <-- AQUÍ FALTABA EL MODIFIER
        factory = { context ->
            PreviewView(context).apply {
                // Forzar COMPATIBLE es necesario para evitar pantallas negras en API 27 / emulador
               // implementationMode = PreviewView.ImplementationMode.COMPATIBLE
                this.controller = controller
            }
        }
    )
}