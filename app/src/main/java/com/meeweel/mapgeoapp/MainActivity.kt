package com.meeweel.mapgeoapp

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.meeweel.mapgeoapp.databinding.ActivityMainBinding
import com.yandex.mapkit.Animation
import com.yandex.mapkit.MapKitFactory
import com.yandex.mapkit.geometry.Point
import com.yandex.mapkit.map.CameraPosition
import com.yandex.mapkit.mapview.MapView


class MainActivity : AppCompatActivity() {

    lateinit var binding: ActivityMainBinding
    private lateinit var mapview: MapView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initYandexMapKits()
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initMapView()

        binding.mapButton.setOnClickListener {
            binding.mapButton.visibility = View.GONE
            binding.mapView.visibility = View.VISIBLE
        }
    }

    private fun initYandexMapKits() {
        MapKitFactory.setApiKey(BuildConfig.YAPI_KEY)
        MapKitFactory.initialize(this)
    }

    private fun initMapView() {
        mapview = binding.mapView
        mapview.map.move(
            CameraPosition(Point(55.751574, 37.573856), 11.0f, 0.0f, 0.0f),
            Animation(Animation.Type.SMOOTH, 0f),
            null
        )
    }

    override fun onStop() {
        super.onStop()
        mapview.onStop()
        MapKitFactory.getInstance().onStop()
    }

    override fun onStart() {
        super.onStart()
        mapview.onStart()
        MapKitFactory.getInstance().onStart()
    }
}