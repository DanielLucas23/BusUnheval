package com.systemDK.busunheval.activities;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import android.widget.Button;
import android.animation.Animator;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.speech.tts.TextToSpeech;
import android.speech.tts.UtteranceProgressListener;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.ar.core.Anchor;
import com.google.ar.core.AugmentedImage;
import com.google.ar.core.Frame;
import com.google.ar.core.TrackingState;
import com.google.ar.sceneform.AnchorNode;
import com.google.ar.sceneform.FrameTime;
import com.google.ar.sceneform.Scene;
import com.google.ar.sceneform.math.Vector3;
import com.google.ar.sceneform.rendering.Color;
import com.google.ar.sceneform.rendering.ExternalTexture;
import com.google.ar.sceneform.rendering.ModelRenderable;
import com.google.ar.sceneform.ux.ArFragment;
import com.systemDK.busunheval.R;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import com.airbnb.lottie.LottieAnimationView;



public class ARActivity extends AppCompatActivity {
    private ExternalTexture texture;
    private MediaPlayer mediaPlayer;
    Button maps;
    private ArFragment arFragment;
    private Scene scene;
    private ModelRenderable renderable;
    private int imagesFoundCount = 0;
    private Map<String, Integer> imageToVideoMap;
    private TextToSpeech textToSpeech;
    private LottieAnimationView animationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ar);

        setupImageToVideoMap();

        texture = new ExternalTexture();

        arFragment = (com.systemDK.busunheval.fragments.CustomArFragment)
                getSupportFragmentManager().findFragmentById(R.id.arFragment);

        scene = arFragment.getArSceneView().getScene();
        scene.addOnUpdateListener(this::onUpdate);

        ModelRenderable.builder()
                .setSource(this, R.raw.video_screen)
                .build()
                .thenAccept(modelRenderable -> {
                    modelRenderable.getMaterial().setExternalTexture("videoTexture", texture);
                    modelRenderable.getMaterial().setFloat4("keyColor", new Color(0.01843f, 1f, 0.098f));
                    renderable = modelRenderable;
                });

        // Inicializar el LottieAnimationView y cargar la animación desde el directorio raw
        animationView = findViewById(R.id.lottie_animation_view);
        animationView.setVisibility(View.VISIBLE); // Hacer visible el LottieAnimationView
        animationView.setAnimation(R.raw.animation); // Nombre del archivo JSON de la animación en raw
        animationView.playAnimation(); // Reproducir la animación

        // Inicializar el TextToSpeech
        textToSpeech = new TextToSpeech(this, status -> {
            if (status == TextToSpeech.SUCCESS) {
                textToSpeech.setLanguage(Locale.getDefault());
                // Reproducir el mensaje de voz después de un cierto tiempo
                new Handler().postDelayed(() -> {
                    textToSpeech.speak("Hola, encuentra los 5 paneles informativos dentro de la universidad", TextToSpeech.QUEUE_FLUSH, null, "messageID");
                }, 2000); // Esperar 2 segundos antes de reproducir el mensaje
            } else {
                Toast.makeText(this, "No se pudo inicializar el TextToSpeech", Toast.LENGTH_SHORT).show();
            }
        });

        // Agregar un Listener al TextToSpeech para detectar cuando la voz ha terminado de hablar
        textToSpeech.setOnUtteranceProgressListener(new UtteranceProgressListener() {
            @Override
            public void onStart(String utteranceId) {}

            @Override
            public void onDone(String utteranceId) {
                // Ocultar la animación cuando el TextToSpeech haya terminado de hablar
                runOnUiThread(() -> {
                    animationView.setVisibility(View.GONE);
                });
            }

            @Override
            public void onError(String utteranceId) {}
        });


        ImageView maps= findViewById(R.id.maps);
        maps.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent i = new Intent( ARActivity.this,Paneles.class);
                startActivity(i);
            }
        });
    }

    private void setupImageToVideoMap() {
        imageToVideoMap = new HashMap<>();
        imageToVideoMap.put("image1", R.raw.video1);
        imageToVideoMap.put("image2", R.raw.video2);
        imageToVideoMap.put("image3", R.raw.video3);
        imageToVideoMap.put("image4", R.raw.video4);
        imageToVideoMap.put("image5", R.raw.video5);
    }

    private void onUpdate(FrameTime frameTime) {
        // Obtener el fotograma actual y las imágenes aumentadas actualizadas
        Frame frame = arFragment.getArSceneView().getArFrame();
        Collection<AugmentedImage> augmentedImages = frame.getUpdatedTrackables(AugmentedImage.class);

        // Iterar sobre las imágenes aumentadas
        for (AugmentedImage image : augmentedImages) {
            // Verificar si la imagen está siendo rastreada
            if (image.getTrackingState() == TrackingState.TRACKING) {
                // Obtener el nombre de la imagen
                String imageName = image.getName();

                // Verificar si se ha encontrado la primera imagen
                if (imageToVideoMap.containsKey(imageName) && imagesFoundCount == 0) {
                    // Incrementar el contador de imágenes encontradas
                    imagesFoundCount++;
                    // Reproducir el video asociado a la imagen
                    playVideo(image.createAnchor(image.getCenterPose()), image.getExtentX(), image.getExtentZ(), imageName);
                    // Mostrar la animación después de reproducir el video
                    break;

                } else if (imageToVideoMap.containsKey(imageName) && imagesFoundCount == 1 && !imageName.equals("image1")) {
                    // Verificar si se ha encontrado la segunda imagen (ejemplo)
                    // Incrementar el contador de imágenes encontradas
                    imagesFoundCount++;
                    // Reproducir el video asociado a la imagen
                    playVideo(image.createAnchor(image.getCenterPose()), image.getExtentX(), image.getExtentZ(), imageName);
                    // Salir del bucle
                    break;
                }
                else if (imageToVideoMap.containsKey(imageName) && imagesFoundCount == 1 && !imageName.equals("image2")) {
                    // Verificar si se ha encontrado la segunda imagen (ejemplo)
                    // Incrementar el contador de imágenes encontradas
                    imagesFoundCount++;
                    // Reproducir el video asociado a la imagen
                    playVideo(image.createAnchor(image.getCenterPose()), image.getExtentX(), image.getExtentZ(), imageName);
                    // Salir del bucle
                    break;
                }
                else if (imageToVideoMap.containsKey(imageName) && imagesFoundCount == 1 && !imageName.equals("image3")) {
                    // Verificar si se ha encontrado la segunda imagen (ejemplo)
                    // Incrementar el contador de imágenes encontradas
                    imagesFoundCount++;
                    // Reproducir el video asociado a la imagen
                    playVideo(image.createAnchor(image.getCenterPose()), image.getExtentX(), image.getExtentZ(), imageName);
                    // Salir del bucle
                    break;
                }
                // Agregar condiciones adicionales para imágenes posteriores aquí
            }
        }
    }

    //Imagen Emergente

    @Override
    protected void onPause() {
        super.onPause();
        if (mediaPlayer != null && mediaPlayer.isPlaying()) {
            mediaPlayer.pause();
        }
    }

    @Override
    protected void onDestroy() {
        if (textToSpeech != null) {
            textToSpeech.stop();
            textToSpeech.shutdown();
        }
        super.onDestroy();
    }
    private void imgEmergente() {
        // Creación de un cuadro de diálogo emergente
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
        alertDialog.setTitle("Guia de paneles informativos - UNHEVAL");
        ImageView imageView = new ImageView(this);
        imageView.setImageResource(R.drawable.recorrido_bus);
        alertDialog.setView(imageView);
        alertDialog.show();
    }
    private void playVideo(Anchor anchor, float extentX, float extentZ, String imageName) {
        if (mediaPlayer != null) {
            mediaPlayer.release();
        }

        mediaPlayer = MediaPlayer.create(this, imageToVideoMap.get(imageName));
        mediaPlayer.setSurface(texture.getSurface());
        mediaPlayer.setLooping(true);
        mediaPlayer.start();

        AnchorNode anchorNode = new AnchorNode(anchor);
        texture.getSurfaceTexture().setOnFrameAvailableListener(surfaceTexture -> {
            anchorNode.setRenderable(renderable);
            texture.getSurfaceTexture().setOnFrameAvailableListener(null);
        });

        anchorNode.setWorldScale(new Vector3(extentX, 1f, extentZ));
        scene.addChild(anchorNode);
    }
}