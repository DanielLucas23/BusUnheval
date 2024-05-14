package com.systemDK.busunheval.fragments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.systemDK.busunheval.R
import com.systemDK.busunheval.activities.ConsultasActivity
import com.systemDK.busunheval.activities.MainActivity
import com.systemDK.busunheval.models.Estudiante
import com.systemDK.busunheval.providers.AuthProvider
import com.systemDK.busunheval.providers.EstudianteProvider

class ModalBottonSheetMenu: BottomSheetDialogFragment() {

    val estudianteProvider = EstudianteProvider()
    val authProvider = AuthProvider()

    var textViewUserName: TextView? = null
    var linearLayoutLogout: LinearLayout? = null
    var linearLayoutAsistente: LinearLayout? = null
    var linearLayoutConsultasAR: LinearLayout? = null
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.modal_botton_sheet_menu,container, false)

        textViewUserName = view.findViewById(R.id.textViewUserName)
        linearLayoutLogout = view.findViewById(R.id.linearLayoutLogout)
        linearLayoutAsistente = view.findViewById(R.id.linearLayoutAsistente)
        linearLayoutConsultasAR = view.findViewById(R.id.linearLayoutConsultasAR)

        getEstudiante()

        linearLayoutLogout?.setOnClickListener { goToMain() }
        linearLayoutAsistente?.setOnClickListener { goToAsistente() }
        linearLayoutConsultasAR?.setOnClickListener {  goToConsultasAr() }
        return view
    }

    private fun goToMain(){
        authProvider.logout() //Cerrar la sesión
        val i = Intent(activity, MainActivity::class.java)
        i.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(i)
    }

    private fun goToAsistente(){
        val i = Intent(activity, ConsultasActivity::class.java)
        startActivity(i)
    }

    private fun goToConsultasAr(){
        //val i = Intent(activity, ARActivity::class.java)
        //artActivity(i)
    }

     private fun getEstudiante(){
        estudianteProvider.getClienById(authProvider.getId()).addOnSuccessListener { document ->

            if (document.exists()){
                val estudiante = document.toObject(Estudiante::class.java)
                textViewUserName?.text = "${estudiante?.name } ${estudiante?.lastname}"
            }

        }
    }

    companion object {
        const val TAG = "ModalBottonSheetMenu"

    }

}