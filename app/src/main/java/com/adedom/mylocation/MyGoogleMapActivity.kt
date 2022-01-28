package com.adedom.mylocation

import android.location.Location
import android.os.Bundle
import android.os.Looper
import android.util.Log
import com.adedom.mylocation.databinding.ActivityMyGoogleMapBinding
import com.google.android.gms.location.*
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions

class MyGoogleMapActivity : BaseLocationActivity() {

    private lateinit var fusedLocationClient: FusedLocationProviderClient

    private lateinit var locationRequest: LocationRequest

    private lateinit var locationCallback: LocationCallback

    private var mGoogleMap: GoogleMap? = null
    private var mMarker: Marker? = null

    private val binding by lazy {
        ActivityMyGoogleMapBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        binding.mapView.onCreate(savedInstanceState)
        binding.mapView.getMapAsync { googleMap ->
            mGoogleMap = googleMap
            mGoogleMap?.setMinZoomPreference(12F)
            mGoogleMap?.setMaxZoomPreference(16F)

            val latLng = LatLng(13.7244416, 100.3529132)
            val cameraUpdate = CameraUpdateFactory.newLatLngZoom(latLng, 14F)
            mGoogleMap?.animateCamera(cameraUpdate)
        }

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)

        locationRequest = createLocationRequest()

        locationCallback = object : LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult) {
                for (location in locationResult.locations) {
                    if (mMarker == null) {
                        val markerOptions = MarkerOptions()
                        markerOptions.icon(
                            BitmapDescriptorFactory.defaultMarker(
                                BitmapDescriptorFactory.HUE_BLUE
                            )
                        )
                        markerOptions.position(LatLng(location.latitude, location.longitude))
                        mMarker = mGoogleMap?.addMarker(markerOptions)
                    } else {
                        mMarker?.position = LatLng(location.latitude, location.longitude)
                    }
                }
            }
        }

        fusedLocationClient.lastLocation
            .addOnSuccessListener { location: Location? ->
                location?.let {
                    val latLng = LatLng(location.latitude, location.longitude)
                    val cameraUpdate = CameraUpdateFactory.newLatLngZoom(latLng, 14F)
                    mGoogleMap?.animateCamera(cameraUpdate)

                    val markerOptions = MarkerOptions()
                    markerOptions.icon(BitmapDescriptorFactory.defaultMarker())
                    markerOptions.position(latLng)
                    mGoogleMap?.addMarker(markerOptions)
                }
            }
            .addOnFailureListener { exception ->
                val message = exception.message
                Log.d(this::class.java.name, "addOnFailureListener: $message")
            }
    }

    private fun createLocationRequest() = LocationRequest.create().apply {
        interval = 3000
        fastestInterval = 2000
        priority = LocationRequest.PRIORITY_HIGH_ACCURACY
    }

    override fun onResume() {
        binding.mapView.onResume()
        super.onResume()
        startLocationUpdates()
    }

    override fun onPause() {
        super.onPause()
        binding.mapView.onPause()
        stopLocationUpdates()
    }

    override fun onLowMemory() {
        super.onLowMemory()
        binding.mapView.onLowMemory()
    }

    private fun startLocationUpdates() {
        fusedLocationClient.requestLocationUpdates(
            locationRequest,
            locationCallback,
            Looper.getMainLooper()
        ).addOnSuccessListener {
            Log.d(this::class.java.name, "addOnSuccessListener: ")
        }.addOnFailureListener { exception ->
            val message = exception.message
            Log.d(this::class.java.name, "addOnFailureListener: $message")
        }
    }

    private fun stopLocationUpdates() {
        fusedLocationClient.removeLocationUpdates(locationCallback)
    }
}