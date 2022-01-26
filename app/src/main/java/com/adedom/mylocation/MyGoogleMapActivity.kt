package com.adedom.mylocation

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.adedom.mylocation.databinding.ActivityMyGoogleMapBinding
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

class MyGoogleMapActivity : AppCompatActivity() {

    private var googleMap: GoogleMap? = null
    private var marker: Marker? = null

    private val binding by lazy {
        ActivityMyGoogleMapBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        binding.mapView.onCreate(savedInstanceState)

        val locationProviderClient = LocationServices
            .getFusedLocationProviderClient(this)

        lifecycleScope.launch {
            val location = locationProviderClient.awaitLastLocation()
            val latLng = LatLng(location.latitude, location.longitude)
            val cameraUpdate = CameraUpdateFactory.newLatLngZoom(latLng, 14F)

            googleMap = binding.mapView.getGoogleMap()
            googleMap?.setMinZoomPreference(12F)
            googleMap?.setMaxZoomPreference(16F)
            googleMap?.animateCamera(cameraUpdate)

            val markerOptions = MarkerOptions()
            markerOptions.icon(BitmapDescriptorFactory.defaultMarker())
            markerOptions.position(latLng)
            googleMap?.addMarker(markerOptions)
        }

        locationProviderClient
            .locationFlow()
            .onEach { location ->
                if (marker == null) {
                    val markerOptions = MarkerOptions()
                    markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE))
                    markerOptions.position(LatLng(location.latitude, location.longitude))
                    marker = googleMap?.addMarker(markerOptions)
                } else {
                    marker?.position = LatLng(location.latitude, location.longitude)
                }
            }
            .catch { throwable ->
                Toast.makeText(baseContext, "${throwable.message}", Toast.LENGTH_SHORT).show()
            }
            .launchIn(lifecycleScope)
    }

    override fun onResume() {
        binding.mapView.onResume()
        super.onResume()
    }

    override fun onPause() {
        super.onPause()
        binding.mapView.onPause()
    }

    override fun onLowMemory() {
        super.onLowMemory()
        binding.mapView.onLowMemory()
    }
}