package com.adedom.mylocation

import android.Manifest
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.adedom.mylocation.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private val binding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }

    private val launcherLocationOneTime = registerForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { result ->
        val isGranted = result.entries.all { it.value }
        if (isGranted) {
            val intent = Intent(this, LocationOneTimeActivity::class.java)
            startActivity(intent)
        } else {
            startApplicationDetailSetting()
        }
    }

    private val launcherLocationFlow = registerForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { result ->
        val isGranted = result.entries.all { it.value }
        if (isGranted) {
            val intent = Intent(this, LocationFlowActivity::class.java)
            startActivity(intent)
        } else {
            startApplicationDetailSetting()
        }
    }

    private val launcherMyGoogleMap = registerForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { result ->
        val isGranted = result.entries.all { it.value }
        if (isGranted) {
            val intent = Intent(this, MyGoogleMapActivity::class.java)
            startActivity(intent)
        } else {
            startApplicationDetailSetting()
        }
    }

    private fun startApplicationDetailSetting() {
        val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
        val uri = Uri.fromParts("package", packageName, null)
        intent.data = uri
        startActivity(intent)
    }

    companion object {
        val permissions = arrayOf(
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION,
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        binding.btnGetLocationOneTime.setOnClickListener {
            launcherLocationOneTime.launch(permissions)
        }

        binding.btnGetLocationFlow.setOnClickListener {
            launcherLocationFlow.launch(permissions)
        }

        binding.btnGoogleMap.setOnClickListener {
            launcherMyGoogleMap.launch(permissions)
        }
    }
}