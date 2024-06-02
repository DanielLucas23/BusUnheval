package com.systemDK.busunheval.activities;

import android.os.Bundle;

import androidx.fragment.app.FragmentActivity;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.systemDK.busunheval.R;

import com.systemDK.busunheval.activities.databinding.ActivityMapsBinding;

public class Paneles extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private ActivityMapsBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMapsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
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

        // Add a marker in Sydney and move the camera
        LatLng sydney = new LatLng(-9.948849075458554, -76.24972006103866);
        mMap.addMarker(new MarkerOptions().position(sydney).title("Facultad de Derecho y Ciencias Políticas"));
        LatLng sydney1 = new LatLng(-9.94904985788437, -76.2492640854414);
        mMap.addMarker(new MarkerOptions().position(sydney1).title("Facultad de Educacion"));
        LatLng sydney2 = new LatLng(-9.949574699224387, -76.24871318743453);
        mMap.addMarker(new MarkerOptions().position(sydney2).title("Facultad De Psicologia"));
        LatLng sydney3 = new LatLng(-9.949906735708277, -76.2487781747961);
        mMap.addMarker(new MarkerOptions().position(sydney3).title("Escuela de posgrado ODONTOLOGÍA"));
        LatLng sydney4 = new LatLng(-9.950144658429565, -76.24893384126709);
        mMap.addMarker(new MarkerOptions().position(sydney4).title("Facultad de Enfermería"));
        LatLng sydney5 = new LatLng(-9.950171581967533, -76.24893101357144);
        mMap.addMarker(new MarkerOptions().position(sydney5).title("Biblioteca de la UNHEVAL"));
        LatLng sydney7 = new LatLng(-9.949281082908445, -76.2481373653548);
        mMap.addMarker(new MarkerOptions().position(sydney7).title("Facultad de Medicina Humana"));
        LatLng sydney8 = new LatLng(-9.948391267869601, -76.24865074647028);
        mMap.addMarker(new MarkerOptions().position(sydney8).title("Facultad de Ingenieria Civil y Arquitectura"));
        LatLng sydney9 = new LatLng(-9.948588574148662, -76.24944993180424);
        mMap.addMarker(new MarkerOptions().position(sydney9).title("Teatrin"));
        LatLng sydney10 = new LatLng(-9.947771693657266, -76.25072329127835);
        mMap.addMarker(new MarkerOptions().position(sydney10).title("Escuela de Post Grado UNHEVAL"));
        LatLng sydney11 = new LatLng(-9.948209738334787, -76.25004526473602);
        mMap.addMarker(new MarkerOptions().position(sydney11).title("Facultad Ingenieria Industrial y de Sistemas"));
        LatLng sydney12 = new LatLng(-9.948082527836982, -76.24944785702912);
        mMap.addMarker(new MarkerOptions().position(sydney12).title("FACULTAD DE CIENCIAS CONTABLES Y FINANCIERAS"));
        LatLng sydney13 = new LatLng(-9.94711823978485, -76.25000039208699);
        mMap.addMarker(new MarkerOptions().position(sydney13).title("Loza Deportivas"));
        LatLng sydney14 = new LatLng(-9.947617221830505, -76.24893336277047);
        mMap.addMarker(new MarkerOptions().position(sydney14).title("Bienestar"));
        LatLng sydney15 = new LatLng(-9.950790686196745, -76.24838286780691);
        mMap.addMarker(new MarkerOptions().position(sydney15).title("Facultad de Ciencias Agrarias - UNHEVAL"));
        mMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(sydney, 17));
    }
}