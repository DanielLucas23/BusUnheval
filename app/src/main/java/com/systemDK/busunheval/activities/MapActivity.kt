package com.systemDK.busunheval.activities


import android.Manifest
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.drawable.Drawable
import android.location.Location
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ImageView
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.easywaylocation.EasyWayLocation
import com.example.easywaylocation.Listener
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.Priority
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptor
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.google.firebase.firestore.DocumentId
import com.google.firebase.firestore.GeoPoint
import com.systemDK.busunheval.R
import com.systemDK.busunheval.databinding.ActivityMapBinding
import com.systemDK.busunheval.fragments.ModalBottonSheetMenu
import com.systemDK.busunheval.models.ConductorLocation
import com.systemDK.busunheval.providers.AuthProvider
import com.systemDK.busunheval.providers.GeoProvider
import com.systemDK.busunheval.utils.CarMoveAnim
import org.imperiumlabs.geofirestore.callbacks.GeoQueryEventListener

class MapActivity : AppCompatActivity(), OnMapReadyCallback, Listener {

    private lateinit var binding: ActivityMapBinding
    private var googleMap : GoogleMap? = null
    private var easyWayLocation: EasyWayLocation? = null
    private var myLocationLatLng: LatLng? = null
    private var markerEstudiante: Marker? = null
    private val geoProvider = GeoProvider()
    private val authProvider = AuthProvider()

    private val modalMenu = ModalBottonSheetMenu()

    private val conductoresMarkers = ArrayList<Marker>()
    private val conductoresLocation = ArrayList<ConductorLocation>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMapBinding.inflate(layoutInflater)
        setContentView(binding.root)
        enableEdgeToEdge()

        val mapFragment = supportFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

        //Contiene la configuración para utilizar los servicios de localización
        val locationRequest = LocationRequest.create().apply {
            interval = 0
            fastestInterval = 0
            priority = Priority.PRIORITY_HIGH_ACCURACY
            smallestDisplacement = 1f
        }

        easyWayLocation = EasyWayLocation(this,locationRequest, false,false,this)

        locationPermission.launch(arrayOf(
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION
        ))

        //Imagen Emergente
        val btnOpenPopup = findViewById<ImageView>(R.id.imageViewInfo)
        btnOpenPopup.setOnClickListener {
            imgEmergente()
        }

        //Llamar al menu
        binding.imageViewMenu.setOnClickListener { showModalMenu() }

    }

    val locationPermission = registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) {permission ->
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N){
            when{
                permission.getOrDefault(Manifest.permission.ACCESS_FINE_LOCATION, false)->{
                    Log.d("LOCALIZACIÓN", "Permiso concedido")
                    easyWayLocation?.startLocation()

                }
                permission.getOrDefault(Manifest.permission.ACCESS_COARSE_LOCATION, false)->{
                    Log.d("LOCALIZACIÓN", "Permiso concedido con limitación")

                }
                else -> {
                    Log.d("LOCALIZACIÓN", "Permiso no concedido")
                }
            }
        }
    }

    //Para llamar el menu de opciones
    private fun showModalMenu(){
        modalMenu.show(supportFragmentManager, ModalBottonSheetMenu.TAG)
    }

    //Para obtener la ubicación de los conductores
    private fun getNearbyConductores(){

        if (myLocationLatLng == null) return

        geoProvider.getNearbyConductor(myLocationLatLng!!, 20.0).addGeoQueryEventListener(object: GeoQueryEventListener {

            override fun onKeyEntered(documentID: String, location: GeoPoint) {

                //Cuando se encuentre un conductor
                for (marker in conductoresMarkers){
                    if (marker.tag != null){
                        if (marker.tag == documentID){
                            return
                        }
                    }
                }

                //Creamos un nuevo marcador para el conductor conectado
                val conductorLatLng = LatLng(location.latitude, location.longitude)
                val marker = googleMap?.addMarker(
                    MarkerOptions().position(conductorLatLng).title("Bus Disponible").icon(
                        BitmapDescriptorFactory.fromResource(R.drawable.icon_autobus)
                    )
                )

                binding.btnOperativo.visibility = View.VISIBLE //MOSTRAR EL BOTON ACTIVO
                binding.btnInactivo.visibility = View.GONE //OCULTAR EL BOTON INACTIVO

                marker?.tag = documentID
                conductoresMarkers.add(marker!!)

                val dl = ConductorLocation()
                dl.id = documentID
                conductoresLocation.add(dl)


            }

            override fun onKeyExited(documentID: String) {
                for (marker in conductoresMarkers){
                    if (marker.tag != null){
                        if (marker.tag == documentID){
                            marker.remove()
                            conductoresMarkers.remove(marker)
                            conductoresLocation.removeAt(getPositionConductor(documentID))
                            return
                        }
                    }
                }

                binding.btnOperativo.visibility = View.GONE
                binding.btnInactivo.visibility = View.VISIBLE

            }

            override fun onKeyMoved(documentID: String, location: GeoPoint) {

                for (marker in conductoresMarkers){

                    val start = LatLng(location.latitude, location.longitude)
                    var end: LatLng? = null
                    val position = getPositionConductor(marker.tag.toString())

                    if (marker.tag != null) {
                        if (marker.tag == documentID) {
                            //marker.position = LatLng(location.latitude, location.longitude)
                            if (conductoresLocation[position].latlng != null){
                                end = conductoresLocation[position].latlng
                            }
                            conductoresLocation[position].latlng = LatLng(location.latitude, location.longitude)
                            if (end != null){
                                CarMoveAnim.carAnim(marker, end, start)
                            }
                        }
                    }
                }
            }

            override fun onGeoQueryError(exception: Exception) {

            }

            override fun onGeoQueryReady() {

            }



        })
    }

    private fun getPositionConductor(id:String): Int{
        var position = 0
        for (i in conductoresLocation.indices){
            if (id == conductoresLocation[i].id){
                position = i
                break
            }
        }
        return position
    }

    private fun imgEmergente(){
        // Creación de un cuadro de diálogo emergente
        val alertDialog = AlertDialog.Builder(this)
        alertDialog.setTitle("Recorrido del Bus Universitario")
        val imageView = ImageView(this)
        imageView.setImageResource(R.drawable.recorrido_bus)
        alertDialog.setView(imageView)
        alertDialog.show()
    }


    private fun addMarker(){
        val drawable = ContextCompat.getDrawable(applicationContext,R.drawable.ic_person_ubi)
        val markerIcon = getMarkerFromDrawable(drawable!!)

        if (markerEstudiante != null){
            markerEstudiante?.remove() //No redibujar el icono
        }

        if (myLocationLatLng != null){
            markerEstudiante = googleMap?.addMarker(
                MarkerOptions()
                    .position(myLocationLatLng!!)
                    .anchor(0.5f, 0.5f)
                    .flat(true)
                    .icon(markerIcon)
            )
        }
    }

    private fun getMarkerFromDrawable(drawable: Drawable): BitmapDescriptor{
        val canvas = Canvas()
        val bitmap = Bitmap.createBitmap(
            100,
            100,
            Bitmap.Config.ARGB_8888
        )
        canvas.setBitmap(bitmap)
        drawable.setBounds(0, 0, 100, 100)
        drawable.draw(canvas)
        return BitmapDescriptorFactory.fromBitmap(bitmap)
    }

    override fun onResume() {
        super.onResume() //Abrimos la pantalla actual

    }

    override fun onDestroy() { //Cierra la Aplicación o pasamos a otra actividad
        super.onDestroy()
        easyWayLocation?.endUpdates()
    }

    override fun onMapReady(map: GoogleMap) {
        googleMap = map
        googleMap?.uiSettings?.isZoomControlsEnabled = true
        easyWayLocation?.startLocation()

        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            return
        }
        googleMap?.isMyLocationEnabled = false
    }

    override fun locationOn() {

    }

    override fun currentLocation(location: Location) { //Actulización de la ubicación en tiempo real
        myLocationLatLng = LatLng(location.latitude, location.longitude) //Latitud y Longitud de la Posición Actual

        googleMap?.moveCamera(CameraUpdateFactory.newCameraPosition(
            CameraPosition.builder().target(myLocationLatLng!!).zoom(17f).build()
        ))
        getNearbyConductores()
        addMarker()
    }

    override fun locationCancelled() {

    }

}