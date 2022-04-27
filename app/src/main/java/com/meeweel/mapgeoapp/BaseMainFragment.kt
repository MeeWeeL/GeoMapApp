package com.meeweel.mapgeoapp

import android.Manifest
import android.R
import android.graphics.Color
import android.graphics.PointF
import android.os.Bundle
import androidx.annotation.DrawableRes
import androidx.fragment.app.Fragment
import com.meeweel.mapgeoapp.databinding.ActivityMainBinding
import com.meeweel.mapgeoapp.databinding.MainFragmentLayoutBinding
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

abstract class BaseMainFragment : Fragment(), UserLocationObjectListener {

    private val TARGET_LOCATION = Point(59.945933, 30.320045)

    internal var _binding: MainFragmentLayoutBinding? = null
    internal val binding
        get() = _binding!!

    internal lateinit var mapView: MapView
    private lateinit var userLocationLayer: UserLocationLayer
    internal lateinit var mapObjectCollection: MapObjectCollection
    private var markerTapListener: MapObjectTapListener? = null
    val marks = mutableListOf<PlacemarkMapObject>()

    private val listener = object : InputListener {
        override fun onMapTap(p0: Map, p1: Point) {
            addMarker(p1.latitude, p1.longitude, R.drawable.ic_delete, )
        }

        override fun onMapLongTap(p0: Map, p1: Point) {
        }
    }

    fun addMarker(
        latitude: Double,
        longitude: Double,
        @DrawableRes imageRes: Int,
        userData: Any? = null
    ): PlacemarkMapObject {
        val marker = mapObjectCollection.addPlacemark(
            Point(latitude, longitude),
            ImageProvider.fromResource(requireContext(), imageRes)
        )
        marker.userData = userData
        markerTapListener?.let { marker.addTapListener(it) }
        marks.add(marker)
        return marker
    }

    private fun initUserLocation() {
        val mapKit = MapKitFactory.getInstance()
        userLocationLayer = mapKit.createUserLocationLayer(mapView.mapWindow)
        userLocationLayer.isVisible = true
        userLocationLayer.isHeadingEnabled = true
        userLocationLayer.setObjectListener(this)
    }

    internal fun initListeners() {
        markerTapListener = MapObjectTapListener { mapObject, _ ->
            mapObjectCollection.remove(mapObject)
            marks.remove(mapObject)
            return@MapObjectTapListener true
        }
        binding.mapButton.setOnClickListener {
            initUserLocation()
        }
    }

    internal fun initMapView() {
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
                requireContext(), R.drawable.ic_delete
            )
        )

        val pinIcon: CompositeIcon = userLocationView.pin.useCompositeIcon()

        pinIcon.setIcon(
            "icon",
            ImageProvider.fromResource(requireContext(), R.drawable.ic_dialog_info),
            IconStyle().setAnchor(PointF(0f, 0f))
                .setRotationType(RotationType.ROTATE)
                .setZIndex(0f)
                .setScale(1f)
        )

        pinIcon.setIcon(
            "pin",
            ImageProvider.fromResource(requireContext(), R.drawable.ic_menu_search),
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

    internal fun initFragment(fragment: Fragment = MainFragment()) {
        requireActivity().supportFragmentManager.beginTransaction()
            .add((requireActivity() as MainActivity).getContainer(), fragment)
            .commitNow()
    }
}