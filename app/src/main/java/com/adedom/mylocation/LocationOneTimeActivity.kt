package com.adedom.mylocation

import android.location.Geocoder
import android.location.Location
import android.os.Bundle
import android.util.Log
import com.adedom.mylocation.databinding.ActivityLocationOneTimeBinding
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import java.util.*

class LocationOneTimeActivity : BaseLocationActivity() {

    private lateinit var fusedLocationClient: FusedLocationProviderClient

    private val binding by lazy {
        ActivityLocationOneTimeBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)

        fusedLocationClient.lastLocation
            .addOnSuccessListener { location: Location? ->
                val locationOneTime = "${location?.latitude}, ${location?.longitude}"
                binding.tvLocationOneTime.text = locationOneTime

                val geocoder = Geocoder(baseContext, Locale.getDefault())
                val addresses = geocoder.getFromLocation(
                    location?.latitude ?: 0.0,
                    location?.longitude ?: 0.0,
                    5
                )
                val hasAddress = !addresses.isNullOrEmpty()
                if (hasAddress) {
                    val locality = addresses[0].locality
                    val subLocality = addresses[0].subLocality.orEmpty()
                    binding.tvLocation.text = locality ?: subLocality
                }
            }
            .addOnFailureListener { exception ->
                val message = exception.message
                Log.d(this::class.java.name, "addOnFailureListener: $message")
            }
    }
}