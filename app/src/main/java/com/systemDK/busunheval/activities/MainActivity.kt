package com.systemDK.busunheval.activities

import android.R
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.systemDK.busunheval.databinding.ActivityMainBinding
import com.systemDK.busunheval.providers.AuthProvider
import android.speech.tts.TextToSpeech

class MainActivity : AppCompatActivity(), TextToSpeech.OnInitListener {

    private lateinit var textToSpeech: TextToSpeech //Para la lectura de voz de agrego
    private lateinit var binding: ActivityMainBinding
    val authProvider = AuthProvider()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        enableEdgeToEdge() //Para que la app se extienda a los bordes

        textToSpeech = TextToSpeech(this, this)//parte del la voz
        //Botones
        binding.btnLogin.setOnClickListener {  Login()  }


    }


    private fun Login(){
        //Al iniciar click leera el texto definido
        textToSpeech.speak("Bienvenido a la app BUS UNHEVAL", TextToSpeech.QUEUE_FLUSH, null, null)

        //Obtener los datos ingresados de los inputs
        val email = "daniel@gmail.com"
        //binding.textFieldEmail.text.toString()
        val password ="123456"
        //binding.textFieldPassword.text.toString()

        if(isValidForm(email, password)){
            authProvider.login(email, password).addOnCompleteListener {
                if(it.isSuccessful){
                    goToMap()
                }else{
                    Toast.makeText(this@MainActivity, "Error al Iniciar Sesion", Toast.LENGTH_SHORT).show()
                    Log.d("FIREBASE", "ERROR: + ${it.exception.toString()}")
                }
            }
        }
    }

    //Validar Formulario

    private fun isValidForm(email:String, password:String):Boolean{
        if (email.isEmpty()){
            Toast.makeText(this, "Ingresa tu correo electronico", Toast.LENGTH_SHORT).show()
            return false
        }
        if (password.isEmpty()){
            Toast.makeText(this, "Ingresa tu contraseña", Toast.LENGTH_SHORT).show()
            return false
        }
        return true
    }

    //Enviar a la pantalla del menú y map
    fun goToMap(){
        val i = Intent(this, MapActivity::class.java)
        i.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK //Para eliminar el historial de pantallas
        startActivity(i)
    }

    override fun onInit(status: Int) {
        if (status == TextToSpeech.SUCCESS) {
            textToSpeech.language = java.util.Locale.getDefault()
        }
    }

    override fun onDestroy() {
        if (textToSpeech.isSpeaking) {
            textToSpeech.stop()
        }
        textToSpeech.shutdown()
        super.onDestroy()
    }

}
   // override fun onStart() {
   //     super.onStart()
   //    if (authProvider.existSession()) {
   //         goToMap()
   //     }
   // }
