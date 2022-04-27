package com.meeweel.mapgeoapp

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.meeweel.mapgeoapp.databinding.MainFragmentLayoutBinding

class MainFragment : BaseMainFragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = MainFragmentLayoutBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initMapView()
        mapObjectCollection = mapView.map.mapObjects.addCollection()
        initListeners()

        binding.listOfMarksButton.setOnClickListener {
            initFragment(MarksFragment(marks))
        }
    }
}