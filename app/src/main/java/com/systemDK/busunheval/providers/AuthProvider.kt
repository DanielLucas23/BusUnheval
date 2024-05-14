package com.systemDK.busunheval.providers

import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth

class AuthProvider {

    //Metodos de Firebase para Autenticar un Usuario

    val auth: FirebaseAuth = FirebaseAuth.getInstance()

    fun login(email: String, password: String): Task<AuthResult> {
        return auth.signInWithEmailAndPassword(email, password)
    }

    fun register(email: String, password: String): Task<AuthResult> {
        return auth.createUserWithEmailAndPassword(email, password)
    }

    fun getId(): String {
        //NULL POINTER EXCEPTION

        return auth.currentUser?.uid ?: ""
    }

    fun existSession(): Boolean {
        var exist = false
        if (auth.currentUser != null){ //Sesion esta iniciada
            exist = true
        }
        return exist
    }

    fun logout(){
        auth.signOut()
    }

}