package com.adedom.mylocation

import android.os.Bundle
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import com.adedom.mylocation.databinding.ActivityLocationFlowBinding
import com.google.android.gms.location.LocationServices
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

class LocationFlowActivity : BaseLocationActivity() {

    private val binding by lazy {
        ActivityLocationFlowBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        val locationProviderClient = LocationServices
            .getFusedLocationProviderClient(this)

        locationProviderClient
            .locationFlow()
            .onEach { location ->
                val locationFlow = location?.asString()
                binding.tvLocationFlow.text = locationFlow
            }
            .catch { throwable ->
                Toast.makeText(baseContext, "${throwable.message}", Toast.LENGTH_SHORT).show()
            }
            .launchIn(lifecycleScope)
    }
}