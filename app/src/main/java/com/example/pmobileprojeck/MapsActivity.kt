package com.example.pmobileprojeck

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions

class MapsActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap
    private var wisata: Wisata? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_maps)

        wisata = intent.getParcelableExtra("wisata")

        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

        wisata?.let {
            val lokasi = LatLng(it.latitude, it.longitude)

            mMap.addMarker(
                MarkerOptions()
                    .position(lokasi)
                    .title(it.nama)
            )

            mMap.moveCamera(
                CameraUpdateFactory.newLatLngZoom(lokasi, 15f)
            )
        }
    }
}
