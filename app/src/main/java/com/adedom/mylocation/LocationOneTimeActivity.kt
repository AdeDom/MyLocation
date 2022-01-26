package com.adedom.mylocation

import android.location.Geocoder
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.adedom.mylocation.databinding.ActivityLocationOneTimeBinding
import com.google.android.gms.location.LocationServices
import kotlinx.coroutines.launch
import java.util.*

class LocationOneTimeActivity : AppCompatActivity() {

    private val binding by lazy {
        ActivityLocationOneTimeBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        val locationProviderClient = LocationServices
            .getFusedLocationProviderClient(this)

        lifecycleScope.launch {
            val location = locationProviderClient.awaitLastLocation()
            val latitude = location.latitude
            val longitude = location.longitude
            val locationOneTime = "$latitude, $longitude"
            binding.tvLocationOneTime.text = locationOneTime

            val geocoder = Geocoder(baseContext, Locale.getDefault())
            val addresses = geocoder.getFromLocation(latitude, longitude, 1)
            val hasAddress = !addresses.isNullOrEmpty()
            if (hasAddress) {
                binding.tvLocation.text = addresses[0].locality
            }
        }
    }
}