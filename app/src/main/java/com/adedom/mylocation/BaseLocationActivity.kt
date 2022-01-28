package com.adedom.mylocation

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.location.LocationManager
import android.os.Bundle
import android.provider.Settings
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity

abstract class BaseLocationActivity : AppCompatActivity() {

    private val broadcastReceiver: BroadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            if (LocationManager.PROVIDERS_CHANGED_ACTION == intent.action) {
                verifyStatusLocation {
                    val intentLocationSettings = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
                    launcherLocationSourceSettings.launch(intentLocationSettings)
                }
            }
        }
    }

    private val launcherLocationSourceSettings = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) {
        verifyStatusLocation {
            finishAffinity()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        verifyStatusLocation {
            val intent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
            launcherLocationSourceSettings.launch(intent)
        }
    }

    override fun onResume() {
        super.onResume()

        val intentFilter = IntentFilter(LocationManager.PROVIDERS_CHANGED_ACTION)
        intentFilter.addAction(Intent.ACTION_PROVIDER_CHANGED)
        registerReceiver(broadcastReceiver, intentFilter)
    }

    override fun onPause() {
        super.onPause()

        unregisterReceiver(broadcastReceiver)
    }

    private fun verifyStatusLocation(isProviderEnabled: () -> Unit) {
        val locationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager
        val isNetworkProviderEnabled = locationManager.isProviderEnabled(
            LocationManager.NETWORK_PROVIDER
        )
        val isGpsProviderEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)

        when {
            // wifi
            !isNetworkProviderEnabled -> {
                isProviderEnabled()
            }
            // mobile data
            !isNetworkProviderEnabled && !isGpsProviderEnabled -> {
                isProviderEnabled()
            }
        }
    }
}