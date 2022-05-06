package com.meeweel.mapgeoapp

import android.Manifest
import android.os.Build
import android.os.Bundle
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.meeweel.mapgeoapp.databinding.ActivityMainBinding
import com.yandex.mapkit.MapKitFactory

class MainActivity : AppCompatActivity() {


    private lateinit var binding: ActivityMainBinding
    private val locationPermissionRequest = registerForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            when {
                permissions.getOrDefault(Manifest.permission.ACCESS_FINE_LOCATION, false) -> {
                    // Precise location access granted.
                }
                permissions.getOrDefault(Manifest.permission.ACCESS_COARSE_LOCATION, false) -> {
                    // Only approximate location access granted.
                } else -> {
                // No location access granted.
            }
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initYandexMapKits()
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        locationPermissionRequest.launch(arrayOf(
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION))
        initFragment()
    }

    private fun initYandexMapKits() {
        MapKitFactory.setApiKey(BuildConfig.YAPI_KEY)
        MapKitFactory.initialize(this)
    }

    private fun initFragment(fragment: Fragment = MainFragment()) {
        supportFragmentManager.beginTransaction()
            .replace(binding.container.id, fragment)
            .commitNow()
    }

    fun getContainer() : Int {
        return binding.container.id
    }
}