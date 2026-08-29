package com.example.practicametadatosandroid.ui.theme

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp


@Composable
fun LoginScreenOld(
    onAuthenticate : () -> Unit
){
    //Add this column to organize the UI elements in the login page
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        //Text to show the labels
        Text(
            text = "Inicio de Sesión",
            style = MaterialTheme.typography.headlineMedium
        )
        Spacer(
            modifier = Modifier.height(30.dp)
        )
        //Normal button to authenticate the user
        Button(
            onClick = {
                onAuthenticate()
            },
            modifier = Modifier.fillMaxWidth()
        ){
            Text("Authenticate with Biometry")
        }
    }
}