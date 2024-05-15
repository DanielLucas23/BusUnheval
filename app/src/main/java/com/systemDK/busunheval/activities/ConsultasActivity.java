package com.systemDK.busunheval.activities;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.speech.tts.TextToSpeech;
import android.view.View;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import com.systemDK.busunheval.R;
import com.systemDK.busunheval.providers.Respuestas;

import java.text.Normalizer;
import java.util.ArrayList;

public class ConsultasActivity extends AppCompatActivity implements TextToSpeech.OnInitListener{

    private static final int RECONOCEDOR_VOZ = 7;
    private TextView escuchando ;
    private TextView respuesta ;
    private ArrayList<Respuestas> respuest;
    private TextToSpeech leer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_consultas);
        inicializar();
    }

    @Override
    protected void onActivityResult(int requestCode , int resultCode, Intent data ) {
        super.onActivityResult(requestCode,resultCode, data);

        if(resultCode == RESULT_OK && requestCode == RECONOCEDOR_VOZ){
            ArrayList<String> reconocido = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
            String escuchado = reconocido.get(0);
            escuchando.setText(escuchado);
            prepararRespuesta(escuchado);
        }
    }
    private void prepararRespuesta(String escuchado) {
        String normalizar = Normalizer.normalize(escuchado, Normalizer.Form.NFD);
        String sintilde = normalizar.replaceAll("[^\\p{ASCII}]", "");

        int resultado;
        String respuesta = respuest.get(0).getRespuestas();
        for (int i = 0; i < respuest.size(); i++) {
            resultado = sintilde.toLowerCase().indexOf(respuest.get(i).getCuestion());
            if(resultado != -1){
                respuesta = respuest.get(i).getRespuestas();
            }
        }
        responder(respuesta);
    }
    private void responder(String respuestita) {
        respuesta.setText(respuestita);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            leer.speak(respuestita, TextToSpeech.QUEUE_FLUSH, null, null);
        }else {
            leer.speak(respuestita, TextToSpeech.QUEUE_FLUSH, null);
        }
    }

    public void inicializar(){
        escuchando = (TextView)findViewById(R.id.tVEscuchado);
        respuesta = (TextView)findViewById(R.id.tvRespuesta);
        respuest = proveerDatos();
        leer = new TextToSpeech(this,this);
    }
    public void hablar(View v){
        Intent hablar = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        hablar.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,"es-MX");
        startActivityForResult(hablar,RECONOCEDOR_VOZ);
    }
    public ArrayList<Respuestas> proveerDatos(){
        ArrayList<Respuestas> respuestas = new ArrayList<>();

        respuestas.add(new Respuestas("defecto", "¡Aun no estoy programada para responder eso, lo siento!"));
        respuestas.add(new Respuestas("hola", "hola Bienvenido a tu asistente virtual Bus Unheval , en que puedo ayudarte"));
        respuestas.add(new Respuestas("ambo", "Los siguientes horarios son: Ambo a Huánuco hora de salida 5:30am hasta 7:00am y el conductor es Manuel Lopez de la Puente. Huánuco a Ambo desde las 8:00pm hasta las 9:45pm y el conductor es Felipe Lino Beteta."));
        respuestas.add(new Respuestas("moras", "Los siguientes horarios son: Moras a Ciudad Universitaria hora de salida 6:00am hasta 8:00am y el conductor es Teodoro Crisanto Apac. Ciudad Universitaria a Moras desde las 11:45am hasta la 1:45pm y el conductor es Hugo Velásquez Cespedes."));
        respuestas.add(new Respuestas("minutos", " El bus llegará en 10 minutos a su destino"));
        respuestas.add(new Respuestas("en donde se encuentra el bus", "Estamos en la avenida Universitaria a 500 metros de la Universidad."));
        respuestas.add(new Respuestas("chiste", "dime tus referencias xd"));
        respuestas.add(new Respuestas("adios", "Gracias por utilizar el asistente virtual Bus Unheval, hasta luego"));
        respuestas.add(new Respuestas("como estas", "bien gracias espero serte de ayuda"));
        respuestas.add(new Respuestas("estoy", "en la universidad Unheval"));

        return respuestas;
    }


    public void onInit(int i) {

    }
}




