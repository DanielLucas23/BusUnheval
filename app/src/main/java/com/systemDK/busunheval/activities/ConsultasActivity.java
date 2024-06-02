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

        respuestas.add(new Respuestas("hola", "Hola Bienvenido a tu Asistente Virtual Bus Unheval , en que puedo ayudarte:"));
        respuestas.add(new Respuestas("defecto", "Aun no estoy programado para responder eso, lo siento"));
        respuestas.add(new Respuestas("rutas", "Las rutas son: ruta 1:Huánuco Centro, ruta 2: Santa Maria del Valle y ruta 3: Ambo"));
        respuestas.add(new Respuestas("horario de la ruta 1", "Turno Mañana: hora de salida de la universidad 6:30 a.m. hora de llegada a centro de huánuco 7:00 a.m, " +
                "Turno Tarde: hora de salida de la universidad 12:00 p.m. hora de llegada a centro de huánuco 1:00 p.m " +
                "y Turno Noche: hora de salida de la universidad 7:00 p.m. hora de llegada a centro de huánuco 7:30 p.m"));
        respuestas.add(new Respuestas("horario de la ruta 2", "Turno Mañana: hora de salida de santa maria del valle 5:00 a.m. hora de llegada a la universidad 7:00 a.m, " +
                "y Turno Noche: hora de salida de la universidad 7:00 p.m. hora de llegada a santa maria del valle 8:30 p.m"));
        respuestas.add(new Respuestas("horario de la ruta 3", "Turno Mañana: hora de salida de ambo 5:00 a.m. hora de llegada a la universidad 7:00 a.m, " +
                "y Turno Noche: hora de salida de la universidad 7:00 p.m. hora de llegada a ambo 8:30 p.m"));
        respuestas.add(new Respuestas("en donde se encuentra el bus", "Estamos en la avenida Universitaria a 300 metros de la Universidad."));
        respuestas.add(new Respuestas("chiste", "dime tus referencias xd"));
        respuestas.add(new Respuestas("adios", "Gracias por utilizar el asistente virtual Bus Unheval, hasta luego"));
        respuestas.add(new Respuestas("como estas", "bien gracias, espero serte de ayuda"));
        respuestas.add(new Respuestas("estoy", "en la universidad Unheval"));

        return respuestas;
    }


    public void onInit(int i) {

    }
}




