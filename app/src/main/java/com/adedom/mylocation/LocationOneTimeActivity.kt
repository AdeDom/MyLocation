package com.adedom.mylocation

import android.location.Geocoder
import android.os.Bundle
import androidx.lifecycle.lifecycleScope
import com.adedom.mylocation.databinding.ActivityLocationOneTimeBinding
import com.google.android.gms.location.LocationServices
import kotlinx.coroutines.launch
import java.util.*

class LocationOneTimeActivity : BaseLocationActivity() {

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
            val locationOneTime = location?.asString()
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
    }
}