package com.systemDK.busunheval.activities;
import androidx.appcompat.app.AppCompatActivity;

import android.animation.Animator;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.view.View;
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

        // Initialize TextToSpeech
        textToSpeech = new TextToSpeech(this, status -> {
            if (status == TextToSpeech.SUCCESS) {
                textToSpeech.setLanguage(Locale.getDefault());
            }
        });

        // Initialize LottieAnimationView
        animationView = findViewById(R.id.animation_view);
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
        Frame frame = arFragment.getArSceneView().getArFrame();
        Collection<AugmentedImage> augmentedImages = frame.getUpdatedTrackables(AugmentedImage.class);

        for (AugmentedImage image : augmentedImages) {
            if (image.getTrackingState() == TrackingState.TRACKING) {
                String imageName = image.getName();
                if (imageToVideoMap.containsKey(imageName) && imagesFoundCount == 0) {
                    imagesFoundCount++;
                    playVideo(image.createAnchor(image.getCenterPose()), image.getExtentX(), image.getExtentZ(), imageName);
                    notifyUser("¡Muy bien! Encontraste la primera imagen. Sigue buscando la segunda imagen.");
                    break;
                } else if (imageToVideoMap.containsKey(imageName) && imagesFoundCount == 1 && !imageName.equals("image1")) { // Example for second image
                    imagesFoundCount++;
                    playVideo(image.createAnchor(image.getCenterPose()), image.getExtentX(), image.getExtentZ(), imageName);
                    notifyUser("¡Muy bien! Encontraste la segunda imagen. Sigue buscando la siguiente imagen.");
                    break;
                }
                // Add additional conditions for subsequent images here
            }
        }
    }

    private void notifyUser(String message) {
        // Show a toast (optional)
        Toast.makeText(this, message, Toast.LENGTH_LONG).show();

        // Speak the message
        textToSpeech.speak(message, TextToSpeech.QUEUE_FLUSH, null, null);

        // Play the animation
        animationView.setVisibility(View.VISIBLE);
        animationView.playAnimation();
        animationView.addAnimatorListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {}

            @Override
            public void onAnimationEnd(Animator animation) {
                animationView.setVisibility(View.GONE);
            }

            @Override
            public void onAnimationCancel(Animator animation) {}

            @Override
            public void onAnimationRepeat(Animator animation) {}
        });
    }

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