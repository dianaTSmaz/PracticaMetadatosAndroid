package com.example.practicametadatosandroid

import com.google.firebase.auth.FirebaseAuth


//import com.google.firebase.auth.FirebaseAuth

class ManagerAuthentication{
    private val auth: FirebaseAuth = FirebaseAuth.getInstance()

    //Function to allow the user Login in the app
    fun login(
        email : String,
        password: String,
        onSuccess: () -> Unit,
        onError : (String) -> Unit
    ){
        auth.signInWithEmailAndPassword(email,password)
            .addOnCompleteListener{
                task ->

                if(task.isSuccessful) {
                  onSuccess()
                }else{
                    onError(
                        task.exception?.message
                            ?: "Error al iniciar sesión"
                    )
                }
            }
        }

    fun register(
        email: String,
        password: String,
        onSuccess: () -> Unit,
        onError: (String) -> Unit
    ){
       auth.createUserWithEmailAndPassword(email,password)
           .addOnCompleteListener {
               task ->
               if(task.isSuccessful){
                   onSuccess()
               }else{
                   onError(
                       task.exception?.message
                           ?: "Ocurrió un error al crear la cuenta"
                   )
               }
           }
    }

    //function to close the session
    fun logout(){
        auth.signOut()
    }

}