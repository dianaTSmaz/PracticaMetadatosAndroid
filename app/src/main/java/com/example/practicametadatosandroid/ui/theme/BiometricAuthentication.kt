package com.example.practicametadatosandroid.ui.theme

import androidx.biometric.BiometricManager
import androidx.biometric.BiometricPrompt
import androidx.core.app.PendingIntentCompat.getActivity
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentActivity
import com.example.practicametadatosandroid.MainActivity


fun authenticationBiometric(
    //The fragment activities are the ones who are smaller than the activities and are executed inside of the activities
    //it is commonly used for reusable code
    activity: FragmentActivity,
    onSuccessfulAuthentication: () -> Unit,
    onErrorAuthenticationCustomised: (String) -> Unit
) {
    activity.runOnUiThread {
        val executor = ContextCompat.getMainExecutor(activity)
        val biometricManager = BiometricManager.from(activity)

        //Type of authentication which are accepted
        val authenticators = BiometricManager.Authenticators.BIOMETRIC_STRONG or //Authentication type 3 - biometrics
                             BiometricManager.Authenticators.DEVICE_CREDENTIAL // can use pwd or pin used to unlock mobile

        if (biometricManager.canAuthenticate(authenticators) == BiometricManager.BIOMETRIC_SUCCESS) {
            val promptInfo = BiometricPrompt.PromptInfo.Builder()
                .setTitle("Autenticación biométrica")
                .setSubtitle("Ingresa para continuar")
                .setAllowedAuthenticators(authenticators)
                .build()

            //Prompt which shows the information
            val biometricPrompt = BiometricPrompt(
                activity,
                executor,
                object : BiometricPrompt.AuthenticationCallback() {

                    //funcion which is run when the authentication is sucessful
                    override fun onAuthenticationSucceeded(result: BiometricPrompt.AuthenticationResult) {
                        super.onAuthenticationSucceeded(result)
                        // dentro del hilo principal se suelen ejecutar hilos secundarios
                        activity.runOnUiThread {
                            onSuccessfulAuthentication()
                        }

                    }

                    //action error when the auth does not passed
                    override fun onAuthenticationError(errorCode: Int, errString: CharSequence) {
                        super.onAuthenticationError(errorCode, errString)
                        activity.runOnUiThread {
                            onErrorAuthenticationCustomised("Error $errorCode: $errString")
                        }
                    }


                }
            )
            biometricPrompt.authenticate(promptInfo)
        } else {
            onErrorAuthenticationCustomised("Biometría no disponible o sin configurar.")
        }

    }
}
