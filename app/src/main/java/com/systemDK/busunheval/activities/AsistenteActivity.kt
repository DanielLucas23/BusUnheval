package com.systemDK.busunheval.activities

import android.content.Intent
import android.os.Bundle
import android.speech.RecognizerIntent
import android.speech.tts.TextToSpeech
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.firebase.firestore.FirebaseFirestore
import com.systemDK.busunheval.R
import org.simmetrics.StringMetric
import org.simmetrics.metrics.StringMetrics
import java.util.Locale

class AsistenteActivity : AppCompatActivity(), TextToSpeech.OnInitListener {

    private lateinit var questionTextView: TextView
    private lateinit var answerTextView: TextView
    private lateinit var startButton: ImageView
    private lateinit var  imageViewBack: ImageView
    private lateinit var tts: TextToSpeech
    private lateinit var db: FirebaseFirestore
    private val similarityThreshold = 0.5f  // Umbral de similitud para considerar una pregunta como "parecida"
    private val metric: StringMetric = StringMetrics.levenshtein()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_asistente)

        imageViewBack = findViewById(R.id.imageViewBack)
        questionTextView = findViewById(R.id.question)
        answerTextView = findViewById(R.id.answer)
        startButton = findViewById(R.id.startButton) // Asignación como ImageView

        // Inicializar Text-to-Speech
        tts = TextToSpeech(this, this)

        // Inicializar Firestore
        db = FirebaseFirestore.getInstance()

        imageViewBack.setOnClickListener { finish() }

        startButton.setOnClickListener {
            startVoiceRecognition()
        }
    }

    // Función para iniciar el reconocimiento de voz
    private fun startVoiceRecognition() {
        val intent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH)
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM)
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault())
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT, "Haz tu pregunta")
        startActivityForResult(intent, 1000)
    }

    // Manejar el resultado del reconocimiento de voz
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 1000 && resultCode == RESULT_OK && data != null) {
            val result = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS)
            val question = result?.get(0) ?: "No se entendió la pregunta"
            questionTextView.text = question
            fetchResponseFromFirestore(question)
        }
    }

    // Consultar Firestore para obtener la respuesta basada en la pregunta del usuario
    private fun fetchResponseFromFirestore(question: String) {
        db.collection("Preguntas")
            .get()
            .addOnSuccessListener { documents ->
                if (documents != null && !documents.isEmpty) {
                    var bestMatch: Pair<String, Float>? = null
                    for (document in documents) {
                        val dbQuestion = document.getString("pregunta")
                        if (dbQuestion != null) {
                            // Verificar si la pregunta contiene la pregunta almacenada como subcadena
                            if (question.contains(dbQuestion, ignoreCase = true) || dbQuestion.contains(question, ignoreCase = true)) {
                                val answer = document.getString("respuesta") ?: "No hay respuesta disponible."
                                answerTextView.text = answer
                                speakOut(answer)
                                return@addOnSuccessListener
                            }
                            // Calcular similitud usando métrica de Levenshtein
                            val similarity = metric.compare(question, dbQuestion)
                            if (similarity >= similarityThreshold) {
                                if (bestMatch == null || similarity > bestMatch.second) {
                                    bestMatch = Pair(document.getString("respuesta") ?: "No hay respuesta disponible.", similarity)
                                }
                            }
                        }
                    }
                    if (bestMatch != null) {
                        val answer = bestMatch.first
                        answerTextView.text = answer
                        speakOut(answer)
                    } else {
                        val noAnswer = "Lo siento, no tengo una respuesta para esa pregunta."
                        answerTextView.text = noAnswer
                        speakOut(noAnswer)
                    }
                } else {
                    val noAnswer = "Lo siento, no tengo una respuesta para esa pregunta."
                    answerTextView.text = noAnswer
                    speakOut(noAnswer)
                }
            }
            .addOnFailureListener { exception ->
                val errorMsg = "Error al obtener respuesta: ${exception.message}"
                answerTextView.text = errorMsg
                speakOut(errorMsg)
            }
    }

    // Inicialización del Text-to-Speech
    override fun onInit(status: Int) {
        if (status == TextToSpeech.SUCCESS) {
            val result = tts.setLanguage(Locale.getDefault())
            if (result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                // Manejar error
            }
        } else {
            // Inicialización fallida
        }
    }

    // Función para pronunciar el texto usando Text-to-Speech
    private fun speakOut(text: String) {
        tts.speak(text, TextToSpeech.QUEUE_FLUSH, null, "")
    }

    override fun onDestroy() {
        // Shutdown TTS when activity is destroyed
        if (::tts.isInitialized) {
            tts.stop()
            tts.shutdown()
        }
        super.onDestroy()
    }

}