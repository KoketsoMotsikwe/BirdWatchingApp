//Authors:
//Lonwabo Gade
//Koketso Motsikwe
//OPSC7312 POE(Final Part)
//Sources: Git(Paths to git repos can be provided upon request) and ChatGPT

package com.example.tweetybirdie

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.os.Bundle
import android.util.Log
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import com.example.tweetybirdie.Models.HotspotModel
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.MapView
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.*
import com.google.android.material.floatingactionbutton.FloatingActionButton
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import java.io.IOException
import kotlin.concurrent.thread


class Hotpots : AppCompatActivity(), OnMapReadyCallback, LocationDataCallback {

    private lateinit var locationName: String
    private var isPermissionGranted = false
    private lateinit var mMap: GoogleMap
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var mapView: MapView
    private val LOCATION_PERMISSION_REQUEST_CODE = 1
    private var lat = 0.0
    private var lon = 0.0
    private var destlat = 0.0
    private var destlon = 0.0

    private lateinit var fabMenu: FloatingActionButton
    private lateinit var menuSettings: FloatingActionButton
    private lateinit var menuAddObservation: FloatingActionButton
    private lateinit var menuMyObs: FloatingActionButton
    private var mapStyleChosen = 0

    private lateinit var fabClose: Animation
    private lateinit var fabOpen: Animation
    private lateinit var fabClock: Animation
    private lateinit var fabAnticlock: Animation
    private var isOpen = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_hotspots)

        mapView = findViewById(R.id.mapView)
        mapView.onCreate(savedInstanceState)
        mapView.getMapAsync(this)
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)


        fabClose = AnimationUtils.loadAnimation(applicationContext, R.anim.fab_close)
        fabOpen = AnimationUtils.loadAnimation(applicationContext, R.anim.fab_open)
        fabClock = AnimationUtils.loadAnimation(applicationContext, R.anim.fab_rotate_clock)
        fabAnticlock = AnimationUtils.loadAnimation(applicationContext, R.anim.fab_rotate_anticlock)
        fabMenu = findViewById(R.id.fabMenu)
        menuSettings = findViewById(R.id.menu_settings)
        menuAddObservation = findViewById(R.id.menu_addObservation)
        menuMyObs = findViewById(R.id.menu_viewObservation)
        menuMyObs.setOnClickListener {
            val intent = Intent(this, Settings::class.java)
            intent.putExtra("desiredFragmentIndex", 1)
            startActivity(intent)
        }

        menuAddObservation.setOnClickListener {
            val intent = Intent(this, AddObservation::class.java)
            startActivity(intent)
            close()
        }
        menuSettings.setOnClickListener {
            val intent = Intent(this, Settings::class.java)
            intent.putExtra("desiredFragmentIndex", 0)
            startActivity(intent)
        }
        fabMenu.setOnClickListener {
            if (isOpen()) {
                close()
            } else {
                open()
            }
        }

        initializeMap()
    }

    override fun onLocationDataReceived() {
        Log.d("Hotpots", "Location data received")
    }

    private fun initializeMap() {

        if (isPermissionGranted) {

            getCurrentLocation { lat, lon ->
                this.lat = lat
                this.lon = lon

                ToolBox.currentLat = lat
                ToolBox.currentLng = lon

                getNearByHotspots()
                addUserObs()
                val userLocation = LatLng(lat, lon)
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(userLocation, 15f))
            }
        } else {

            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                LOCATION_PERMISSION_REQUEST_CODE
            )
        }
    }

    private fun loadMapStyle() {

        mapStyleChosen = if (ToolBox.users[0].mapStyleIsDark) {
            R.raw.dark
        } else {
            R.raw.light
        }

        try {
            val success = mMap.setMapStyle(
                MapStyleOptions.loadRawResourceStyle(
                    this, mapStyleChosen
                )
            )

            if (!success) {
                println("Style parsing failed.")
            }
        } catch (e: IOException) {
            println("Could not load style. Error: ${e.message}")
        }
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

        loadMapStyle()

        lifecycleScope.launch {
            doWork()
        }

        getCurrentLocation { lat, lon ->
            this.lat = lat
            this.lon = lon

            ToolBox.currentLat = lat
            ToolBox.currentLng = lon

            val userLocation = LatLng(lat, lon)
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(userLocation, 15f))
        }

        val customInfoWindowAdapter = CustomInfoWindowAdapter(this)
        mMap.setInfoWindowAdapter(customInfoWindowAdapter)

        mMap.setOnMarkerClickListener { marker ->
            locationName = marker.title.toString()

            ToolBox.destlat = marker.position.latitude
            ToolBox.destlng = marker.position.longitude
            ToolBox.newObslat = marker.position.latitude
            ToolBox.newObslng = marker.position.longitude
            getLocationData(marker.position.latitude, marker.position.longitude, marker)
            true
        }
    }


    suspend fun doWork() = coroutineScope {
        launch {
            getNearByHotspots()
        }
        launch {
            addUserObs()
        }
    }

    private fun addUserObs() {
        if (ToolBox.usersObservations.isNotEmpty()) {
            val filteredObservations =
                ToolBox.usersObservations.filter { !it.IsAtHotspot }

            for (location in filteredObservations) {
                mMap.addMarker(
                    MarkerOptions().position(
                        LatLng(
                            location.Location.latitude, location.Location.longitude
                        )
                    ).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE))
                        .title("User Sighting: " + location.BirdName)
                )
            }
        }
    }

    private fun getNearByHotspots() {
        val apiWorker = APIWorker()
        val scope = CoroutineScope(Dispatchers.Default)

        thread {
            scope.launch {
                val hotspots = apiWorker.getHotspots(lat, lon)
                UpdateMarkers(hotspots)
                println("getting birds")
                ToolBox.birdsInTheRegion = apiWorker.getBirds()
                ToolBox.populated = true
                println("birds saved")
            }
        }
    }

    private fun UpdateMarkers(locations: List<HotspotModel>) {
        try {
            runOnUiThread {
                for (location in locations) {
                    mMap.addMarker(
                        MarkerOptions().position(LatLng(location.Lat, location.Lon))
                            .title(location.Name)
                    )
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun getCurrentLocation(callback: (Double, Double) -> Unit) {

        if (ContextCompat.checkSelfPermission(
                this, Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {

            isPermissionGranted = true
            mMap.isMyLocationEnabled = true
            fusedLocationClient.lastLocation.addOnSuccessListener { location: Location? ->
                location?.let {
                    val lat = location.latitude
                    val lon = location.longitude
                    callback(lat, lon)
                } ?: callback(-33.9249, 18.4241)
            }
        }
    }

    private fun getLocationData(lat: Double, lng: Double, marker: Marker) {
        val apiWorker = APIWorker()
        val scope = CoroutineScope(Dispatchers.Default)
        val bottomSheet = BottomSheetHotspot.newInstance(marker.title.toString(), lat, lng)
        bottomSheet.show(supportFragmentManager, BottomSheetHotspot.TAG)

        thread {
            scope.launch {
                ToolBox.hotspotsSightings = apiWorker.getHotspotBirdData(lat, lng)

                destlat = lat
                destlon = lng

                runOnUiThread {
                    bottomSheet.updateHotspotSightings(ToolBox.hotspotsSightings)

                    bottomSheet.setButtonClickListener {
                        val intent = Intent(this@Hotpots, Navigation::class.java)
                        intent.putExtra("LATITUDE", ToolBox.currentLat)
                        intent.putExtra("LONGITUDE", ToolBox.currentLng)
                        intent.putExtra("DEST_LAT", destlat)
                        intent.putExtra("DEST_LNG", destlon)

                        startActivity(intent)
                    }
                }
            }
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int, permissions: Array<out String>, grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == LOCATION_PERMISSION_REQUEST_CODE) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                isPermissionGranted = true
                initializeMap()
            } else {

            }
        }
    }

    private fun open() {
        fabMenu.startAnimation(fabClock)
        fabMenu.isEnabled = true
        menuSettings.startAnimation(fabOpen)
        menuSettings.isEnabled = true

        menuAddObservation.startAnimation(fabOpen)
        menuAddObservation.isEnabled = true

        menuMyObs.startAnimation(fabOpen)
        menuMyObs.isEnabled = true

        isOpen = true

    }

    private fun isOpen(): Boolean {
        if (isOpen) {

            menuSettings.startAnimation(fabClose)
            menuAddObservation.startAnimation(fabClose)
            menuMyObs.startAnimation(fabClose)

            fabMenu.startAnimation(fabAnticlock)
            return true

        } else {
            fabMenu.startAnimation(fabClock)

            menuSettings.startAnimation(fabOpen)
            menuAddObservation.startAnimation(fabOpen)
            menuMyObs.startAnimation(fabOpen)

            return false
        }
    }

    private fun close() {

        menuSettings.startAnimation(fabClose)
        menuSettings.isEnabled = false

        menuAddObservation.startAnimation(fabClose)
        menuAddObservation.isEnabled = false

        menuMyObs.startAnimation(fabClose)
        menuMyObs.isEnabled = false



        fabMenu.startAnimation(fabAnticlock)
        isOpen = false
    }

    override fun onResume() {
        super.onResume()
        mapView.onResume()
    }

    override fun onPause() {
        super.onPause()
        mapView.onPause()
    }

    override fun onDestroy() {
        super.onDestroy()
        mapView.onDestroy()
    }

    override fun onLowMemory() {
        super.onLowMemory()
        mapView.onLowMemory()
    }

    @Deprecated("Deprecated in Java")
    override fun onBackPressed() {
        super.onBackPressed()
        fabMenu.isEnabled = true
    }
}

interface LocationDataCallback {
    fun onLocationDataReceived()
}