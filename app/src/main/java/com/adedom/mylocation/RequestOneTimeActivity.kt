package com.adedom.mylocation

import android.location.Location
import android.os.Bundle
import android.os.Looper
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.adedom.mylocation.databinding.ActivityRequestOneTimeBinding
import com.google.android.gms.location.*

class RequestOneTimeActivity : AppCompatActivity() {

    private lateinit var fusedLocationClient: FusedLocationProviderClient

    private lateinit var locationRequest: LocationRequest

    private lateinit var locationCallback: LocationCallback

    private val binding by lazy {
        ActivityRequestOneTimeBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)

        fusedLocationClient.lastLocation
            .addOnSuccessListener { location: Location? ->
                if (location == null) {
                    startLocationUpdates()
                } else {
                    val locationOneTime = "${location.latitude}, ${location.longitude}"
                    binding.tvLocationOneTime.text = locationOneTime
                }
            }
            .addOnFailureListener { exception ->
                val message = exception.message
                Log.d(this::class.java.name, "addOnFailureListener: $message")
            }

        locationRequest = createLocationRequest()

        locationCallback = object : LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult) {
                super.onLocationResult(locationResult)
                val location = locationResult.lastLocation
                val locationOneTime = "${location.latitude}, ${location.longitude}"
                binding.tvLocationOneTime.text = locationOneTime

                stopLocationUpdates()
            }
        }
    }

    private fun createLocationRequest() = LocationRequest.create().apply {
        interval = 1000
        fastestInterval = 500
        priority = LocationRequest.PRIORITY_HIGH_ACCURACY
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