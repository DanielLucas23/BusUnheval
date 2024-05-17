package com.systemDK.busunheval.fragments;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import androidx.annotation.Nullable;

import com.google.ar.core.AugmentedImageDatabase;
import com.google.ar.core.Config;
import com.google.ar.core.Session;
import com.google.ar.sceneform.ux.ArFragment;
import com.systemDK.busunheval.R;

public class CustomArFragment extends ArFragment {
    protected Config getSessionConfiguration(Session session) {
        Config config = new Config(session);
        config.setUpdateMode(Config.UpdateMode.LATEST_CAMERA_IMAGE);

        new LoadAugmentedImageDatabaseTask(session, config).execute();

        this.getArSceneView().setupSession(session);

        return config;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        FrameLayout frameLayout = (FrameLayout) super.onCreateView(inflater, container, savedInstanceState);

        getPlaneDiscoveryController().hide();
        getPlaneDiscoveryController().setInstructionView(null);

        return frameLayout;
    }

    private class LoadAugmentedImageDatabaseTask extends AsyncTask<Void, Void, AugmentedImageDatabase> {
        private Session session;
        private Config config;

        LoadAugmentedImageDatabaseTask(Session session, Config config) {
            this.session = session;
            this.config = config;
        }

        @Override
        protected AugmentedImageDatabase doInBackground(Void... voids) {
            AugmentedImageDatabase aid = new AugmentedImageDatabase(session);

            // Agregar múltiples imágenes al AugmentedImageDatabase
            Bitmap image1 = BitmapFactory.decodeResource(getResources(), R.drawable.image1);
            aid.addImage("image1", image1);



            Bitmap image3 = BitmapFactory.decodeResource(getResources(), R.drawable.image3);
            aid.addImage("image3", image3);



            return aid;
        }

        @Override
        protected void onPostExecute(AugmentedImageDatabase augmentedImageDatabase) {
            config.setAugmentedImageDatabase(augmentedImageDatabase);
            session.configure(config);
        }
    }
}
