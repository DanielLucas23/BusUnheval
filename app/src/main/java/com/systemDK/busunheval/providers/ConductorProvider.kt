package com.systemDK.busunheval.providers

import android.net.Uri
import android.util.Log
import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.systemDK.busunheval.models.Conductor
import java.io.File

class ConductorProvider {

    val db = Firebase.firestore.collection("Conductores")

    fun create(conductor: Conductor):Task<Void>{
        return db.document(conductor.id!!).set(conductor)
    }

    fun getConductor(idConductor: String): Task<DocumentSnapshot> {
        return db.document(idConductor).get()
    }

}