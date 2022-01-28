package com.adedom.mylocation

import android.os.Bundle
import android.os.Looper
import android.util.Log
import com.adedom.mylocation.databinding.ActivityLocationFlowBinding
import com.google.android.gms.location.*

class LocationFlowActivity : BaseLocationActivity() {

    private lateinit var fusedLocationClient: FusedLocationProviderClient

    private lateinit var locationRequest: LocationRequest

    private lateinit var locationCallback: LocationCallback

    private val binding by lazy {
        ActivityLocationFlowBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)

        locationRequest = createLocationRequest()

        locationCallback = object : LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult) {
                for (location in locationResult.locations) {
                    val locationFlow = "${location.latitude}, ${location.longitude}"
                    binding.tvLocationFlow.text = locationFlow
                }
            }
        }
    }

    private fun createLocationRequest() = LocationRequest.create().apply {
        interval = 3000
        fastestInterval = 2000
        priority = LocationRequest.PRIORITY_HIGH_ACCURACY
    }

    override fun onResume() {
        super.onResume()
        startLocationUpdates()
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

    override fun onPause() {
        super.onPause()
        stopLocationUpdates()
    }

    private fun stopLocationUpdates() {
        fusedLocationClient.removeLocationUpdates(locationCallback)
    }
}