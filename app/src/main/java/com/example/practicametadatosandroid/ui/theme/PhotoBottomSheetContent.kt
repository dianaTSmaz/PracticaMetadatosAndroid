package com.example.practicametadatosandroid.ui.theme


import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel

@Composable
fun PhotoBottomSheetContent(
    bitmaps: List<PhotoInfo>,
    modifier: Modifier = Modifier,
    //asImageBitMap: Int.() -> ImageBitmap
)
{
    // Guarda la fotografía que seleccionó el usuario
    var selectedPhoto by remember {
        mutableStateOf<PhotoInfo?>(null)
    }

    if(bitmaps.isEmpty()){
        Box(
            modifier = modifier
                .padding(16.dp),
                contentAlignment = Alignment.Center
        ){
            Text("NO PHOTOS TO SHOW")
        }
    }else{
        // Si el usuario seleccionó una fotografía
        if (selectedPhoto != null) {

            PhotoMetadataCard(
                photo = selectedPhoto!!,
                onBack = {selectedPhoto = null},
                onSaveName = {nuevoNombre ->
                   selectedPhoto!!.nombreFoto = nuevoNombre
                    // 2. Cierra la tarjeta para regresar al grid
                    selectedPhoto = null
                }
            ) //{
                //selectedPhoto = null
            //}
        }else{
                LazyVerticalStaggeredGrid(
                    columns = StaggeredGridCells.Fixed(2),
                    horizontalArrangement = Arrangement.spacedBy(16.dp),
                    verticalItemSpacing = 16.dp,
                    contentPadding = PaddingValues(16.dp)
                ) {
                    items(bitmaps){bitmap ->
                        Image(
                            bitmap = bitmap.bitmap.asImageBitmap(),
                            contentDescription = null,
                            modifier = Modifier
                                .clip(RoundedCornerShape(10.dp))
                                .clickable{selectedPhoto = bitmap}
                        )
                       /* Spacer(modifier = Modifier.width(16.dp))

                        Column {
                            Text("Fecha: ${bitmap.dateTaken}")
                            Text("Orientación: ${bitmap.orientation}")
                            Text("Dimensiones: ${bitmap.dimensions}")
                        }*/
                    }
                }
            }

       }


    }


/**
 * Tarjeta blanca que muestra la información
 * de la fotografía seleccionada.
 */
@Composable
fun PhotoMetadataCard(
    photo: PhotoInfo,
    onBack: () -> Unit,
    onSaveName: (String) -> Unit
) {

    Surface (

        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        color = Color.White,
        shape = RoundedCornerShape(16.dp),
        shadowElevation = 8.dp
    ) {

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(24.dp)
        ) {
            var textNombre by remember {mutableStateOf(photo.nombreFoto)}

            Text(
                text = "INFORMACIÓN DE LA FOTOGRAFÍA"
            )

            Spacer(
                modifier = Modifier.height(20.dp)
            )
            OutlinedTextField(
                    value = textNombre,
                    onValueChange = {textNombre = it},
                    label = {Text("Label")},
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(8.dp))

            // Botón para guardar el nombre ingresado
            Button(
                onClick = { onSaveName(textNombre) },
                enabled = textNombre.isNotBlank(),
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Guardar nombre")
            }
            Spacer(
                modifier = Modifier.height(20.dp)
            )

            Text(
                text = "Fecha"
            )

            Text(
                text = photo.dateTaken
            )

            Spacer(
                modifier = Modifier.height(16.dp)
            )

            Text(
                text = "Orientación"
            )

            Text(
                text = "${photo.orientation}°"
            )

            Spacer(
                modifier = Modifier.height(16.dp)
            )

            Text(
                text = "Dimensiones"
            )

            Text(
                text = photo.dimensions
            )

            Spacer(
                modifier = Modifier.height(24.dp)
            )

            Button(
                onClick = onBack,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Regresar")
            }
        }
    }
}
