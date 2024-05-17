package com.systemDK.busunheval.activities;
import androidx.appcompat.app.AppCompatActivity;

import android.media.MediaPlayer;
import android.os.Bundle;

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

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class ARActivity extends AppCompatActivity {

    private ExternalTexture texture;
    private MediaPlayer mediaPlayer;
    private ArFragment arFragment;
    private Scene scene;
    private ModelRenderable renderable;
    private boolean isImageDetected = false;
    private Map<String, Integer> imageToVideoMap;

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
        if (isImageDetected) return;

        Frame frame = arFragment.getArSceneView().getArFrame();
        Collection<AugmentedImage> augmentedImages = frame.getUpdatedTrackables(AugmentedImage.class);

        for (AugmentedImage image : augmentedImages) {
            if (image.getTrackingState() == TrackingState.TRACKING) {
                if (imageToVideoMap.containsKey(image.getName())) {
                    isImageDetected = true;
                    playVideo(image.createAnchor(image.getCenterPose()), image.getExtentX(), image.getExtentZ(), image.getName());
                    break;
                }
            }
        }
    }

    protected void onPause() {
        super.onPause();
        if (mediaPlayer != null && mediaPlayer.isPlaying()) {
            mediaPlayer.pause();
        }
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