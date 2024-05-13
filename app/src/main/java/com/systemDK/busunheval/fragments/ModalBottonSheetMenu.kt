package com.systemDK.busunheval.fragments

import android.content.DialogInterface
import android.content.Intent
import android.health.connect.datatypes.DataOrigin
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import com.daniel.apibusunheval.R
import com.daniel.apibusunheval.activities.ConsultasActivity
import com.daniel.apibusunheval.activities.HistoriesActivity
import com.daniel.apibusunheval.activities.MainActivity
import com.daniel.apibusunheval.activities.MapActivity
import com.daniel.apibusunheval.activities.MapTripActivity
import com.daniel.apibusunheval.activities.ProfileActivity
import com.daniel.apibusunheval.models.Conductor
import com.daniel.apibusunheval.models.Estudiante
import com.daniel.apibusunheval.providers.AuthProvider
import com.daniel.apibusunheval.providers.BookingProvider
import com.daniel.apibusunheval.providers.EstudianteProvider
import com.daniel.apibusunheval.providers.GeoProvider
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.systemDK.busunheval.R

class ModalBottonSheetMenu: BottomSheetDialogFragment() {

    var textViewUserName: TextView? = null
    var linearLayoutLogout: LinearLayout? = null
    var linearLayoutProfile: LinearLayout? = null
    var linearLayoutHistory: LinearLayout? = null
    var linearLayoutConsultas: LinearLayout? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.modal_botton_sheet_menu,container, false)

        textViewUserName = view.findViewById(R.id.textViewUserName)
        linearLayoutLogout = view.findViewById(R.id.linearLayoutLogout)
        linearLayoutProfile = view.findViewById(R.id.linearLayoutProfile)
        linearLayoutHistory = view.findViewById(R.id.linearLayoutHistori)
        linearLayoutConsultas = view.findViewById(R.id.linearLayoutConsultas)

        getEstudiante()

        linearLayoutLogout?.setOnClickListener { goToMain() }
        linearLayoutProfile?.setOnClickListener { goToProfile() }
        linearLayoutHistory?.setOnClickListener { goToHistories() }
        linearLayoutConsultas?.setOnClickListener { goTOConsultas() }

        return view
    }

    private fun goToProfile(){
        val i = Intent(activity, ProfileActivity::class.java)
        startActivity(i)
    }

    private fun goToHistories(){
        val i = Intent(activity, HistoriesActivity::class.java)
        startActivity(i)
    }

    private fun goTOConsultas(){
        val i = Intent(activity, ConsultasActivity::class.java)
        startActivity(i)
    }

    private fun goToMain(){
        authProvider.logout()
        val i = Intent(activity, MainActivity::class.java)
        i.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(i)
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