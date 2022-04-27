package com.meeweel.mapgeoapp

import android.Manifest
import android.R
import android.graphics.Color
import android.graphics.PointF
import android.os.Build
import android.os.Bundle
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.DrawableRes
import androidx.appcompat.app.AppCompatActivity
import com.meeweel.mapgeoapp.databinding.ActivityMainBinding
import com.yandex.mapkit.Animation
import com.yandex.mapkit.MapKitFactory
import com.yandex.mapkit.geometry.Point
import com.yandex.mapkit.layers.ObjectEvent
import com.yandex.mapkit.map.*
import com.yandex.mapkit.map.Map
import com.yandex.mapkit.mapview.MapView
import com.yandex.mapkit.user_location.UserLocationLayer
import com.yandex.mapkit.user_location.UserLocationObjectListener
import com.yandex.mapkit.user_location.UserLocationView
import com.yandex.runtime.image.ImageProvider

private val TARGET_LOCATION = Point(59.945933, 30.320045)

class MainActivity : AppCompatActivity(), UserLocationObjectListener {

    lateinit var binding: ActivityMainBinding
    private lateinit var mapView: MapView
    private lateinit var userLocationLayer: UserLocationLayer
    private lateinit var mapObjectCollection: MapObjectCollection
    private var markerTapListener: MapObjectTapListener? = null

    private val listener = object : InputListener {
        override fun onMapTap(p0: Map, p1: Point) {
            addMarker(p1.latitude, p1.longitude, R.drawable.ic_delete, )
        }

        override fun onMapLongTap(p0: Map, p1: Point) {
        }

    }


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
        initMapView()
        locationPermissionRequest.launch(arrayOf(
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION))
        mapObjectCollection = mapView.map.mapObjects.addCollection()
        initListeners()
    }

    fun addMarker(
        latitude: Double,
        longitude: Double,
        @DrawableRes imageRes: Int,
        userData: Any? = null
    ): PlacemarkMapObject {
        val marker = mapObjectCollection.addPlacemark(
            Point(latitude, longitude),
            ImageProvider.fromResource(this, imageRes)
        )
        marker.userData = userData
        markerTapListener?.let { marker.addTapListener(it) }
        return marker
    }

    private fun initUserLocation() {
        val mapKit = MapKitFactory.getInstance()
        userLocationLayer = mapKit.createUserLocationLayer(mapView.mapWindow)
        userLocationLayer.isVisible = true
        userLocationLayer.isHeadingEnabled = true
        userLocationLayer.setObjectListener(this)
    }

    private fun initListeners() {
        markerTapListener = MapObjectTapListener { mapObject, _ ->
            mapObjectCollection.remove(mapObject)
            return@MapObjectTapListener true
        }
        binding.mapButton.setOnClickListener {
            initUserLocation()
        }
    }

    private fun initYandexMapKits() {
        MapKitFactory.setApiKey(BuildConfig.YAPI_KEY)
        MapKitFactory.initialize(this)
    }

    private fun initMapView() {
        mapView = binding.mapView
        mapView.map.move(
            CameraPosition(TARGET_LOCATION, 11.0f, 0.0f, 0.0f),
            Animation(Animation.Type.SMOOTH, 0f),
            null
        )
        mapView.map.addInputListener(listener)
    }

    override fun onStop() {
        super.onStop()
        mapView.onStop()
        MapKitFactory.getInstance().onStop()
    }

    override fun onStart() {
        super.onStart()
        mapView.onStart()
        MapKitFactory.getInstance().onStart()
    }

    override fun onObjectAdded(userLocationView: UserLocationView) {
        userLocationLayer.setAnchor(
            PointF((mapView.width * 0.5).toFloat(), (mapView.height * 0.5).toFloat()),
            PointF((mapView.width * 0.5).toFloat(), (mapView.height * 0.83).toFloat())
        )

        userLocationView.arrow.setIcon(
            ImageProvider.fromResource(
                this, R.drawable.ic_delete
            )
        )

        val pinIcon: CompositeIcon = userLocationView.pin.useCompositeIcon()

        pinIcon.setIcon(
            "icon",
            ImageProvider.fromResource(this, R.drawable.ic_dialog_info),
            IconStyle().setAnchor(PointF(0f, 0f))
                .setRotationType(RotationType.ROTATE)
                .setZIndex(0f)
                .setScale(1f)
        )

        pinIcon.setIcon(
            "pin",
            ImageProvider.fromResource(this, R.drawable.ic_menu_search),
            IconStyle().setAnchor(PointF(0.5f, 0.5f))
                .setRotationType(RotationType.ROTATE)
                .setZIndex(1f)
                .setScale(0.5f)
        )

        userLocationView.accuracyCircle.fillColor = Color.BLUE and -0x66000001
    }

    override fun onObjectRemoved(p0: UserLocationView) {

    }

    override fun onObjectUpdated(p0: UserLocationView, p1: ObjectEvent) {

    }
}