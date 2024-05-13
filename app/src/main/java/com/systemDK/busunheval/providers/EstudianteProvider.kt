package com.systemDK.busunheval.providers

import android.net.Uri
import android.util.Log
import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.systemDK.busunheval.models.Estudiante

import java.io.File

class EstudianteProvider {

    val db = Firebase.firestore.collection("Estudiantes")

    fun create(estudiante: Estudiante):Task<Void>{
        return db.document(estudiante.id!!).set(estudiante)
    }

    fun getClienById(id: String): Task<DocumentSnapshot> {
        return db.document(id).get()
    }


}