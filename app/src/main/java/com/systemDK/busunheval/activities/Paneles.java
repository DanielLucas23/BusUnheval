package com.systemDK.busunheval.activities;

import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;

import androidx.fragment.app.FragmentActivity;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.systemDK.busunheval.R;
import com.systemDK.busunheval.databinding.ActivityPanelBinding;

public class Paneles extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;

    private ActivityPanelBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityPanelBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.panel);
        mapFragment.getMapAsync(this);
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        // Crear un icono personalizado para el marcador
        BitmapDrawable bitmapdraw=(BitmapDrawable)getResources().getDrawable(R.drawable.panel);
        Bitmap b=bitmapdraw.getBitmap();

        // Escala el Bitmap al tamaño deseado
        Bitmap smallMarker = Bitmap.createScaledBitmap(b, 100, 100, false); // Tamaño ajustable (100x100)

        // Convierte el Bitmap a un BitmapDescriptor
        BitmapDescriptor smallMarkerIcon = BitmapDescriptorFactory.fromBitmap(smallMarker);

        LatLng sydney = new LatLng(-9.948849075458554, -76.24972006103866);
        mMap.addMarker(new MarkerOptions().position(sydney).title("PANEL N°5").icon(smallMarkerIcon));
        LatLng sydney1 = new LatLng(-9.949906735708277, -76.2487781747961);
        mMap.addMarker(new MarkerOptions().position(sydney1).title("PANEL N°4").icon(smallMarkerIcon));
        LatLng sydney2 = new LatLng(-9.948391267869601, -76.24865074647028);
        mMap.addMarker(new MarkerOptions().position(sydney2).title("PANEL N°3").icon(smallMarkerIcon));
        LatLng sydney3 = new LatLng(-9.948209738334787, -76.25004526473602);
        mMap.addMarker(new MarkerOptions().position(sydney3).title("PANEL N°2").icon(smallMarkerIcon));
        LatLng sydney4 = new LatLng(-9.947617221830505, -76.24893336277047);
        mMap.addMarker(new MarkerOptions().position(sydney4).title("PANEL N°1").icon(smallMarkerIcon));

        mMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(sydney, 17));
    }
}