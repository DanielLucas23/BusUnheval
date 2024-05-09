package com.systemDK.busunheval.activities

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.systemDK.busunheval.databinding.ActivityMainBinding
import com.systemDK.busunheval.providers.AuthProvider

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    val authProvider = AuthProvider()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        enableEdgeToEdge() //Para que la app se extienda a los bordes

        //Botones
        binding.btnLogin.setOnClickListener { Login() }
        
    }

    private fun Login(){
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

    override fun onStart() {
        super.onStart()
        if (authProvider.existSession()) {
            goToMap()
        }
    }
}