package com.paytabs.memoria

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.paytabs.memoria.databinding.ActivitySelectLatLngBinding


class SelectLatLngActivity : AppCompatActivity(), OnMapReadyCallback {

    lateinit var mMap: GoogleMap
    lateinit var binding: ActivitySelectLatLngBinding
    var selectedLatLng: LatLng? = null
    var marker: Marker? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySelectLatLngBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

        binding.confirmLatLng.setOnClickListener {
            val i = Intent()
            i.putExtra("latLng", selectedLatLng)
            setResult(RESULT_OK, i)
            finish()
        }
    }

    override fun onMapReady(p0: GoogleMap) {
        mMap = p0
        mMap.setOnMapClickListener {
            if (!binding.confirmLatLng.isVisible) binding.confirmLatLng.isVisible = true
            if (marker == null) {
                marker =
                    mMap.addMarker(
                        MarkerOptions()
                            .title("Selected point")
                            .position(it)
                    )
            } else {
                marker?.position = it
            }
            selectedLatLng = it
        }
    }
}