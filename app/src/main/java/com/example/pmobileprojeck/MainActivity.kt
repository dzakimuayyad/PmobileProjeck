package com.example.pmobileprojeck

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.pmobileprojeck.databinding.ActivityMainBinding
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.firebase.database.*

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var database: DatabaseReference
    private lateinit var fusedLocationClient: FusedLocationProviderClient

    private val listWisata = mutableListOf<Wisata>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // ViewBinding
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Firebase
        database = FirebaseDatabase.getInstance().getReference("wisata")

        // Location (GPS)
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)

        // Adapter RecyclerView
        val adapter = WisataAdapter(
            listWisata,
            onItemClick = { wisata ->
                Toast.makeText(this, wisata.nama, Toast.LENGTH_SHORT).show()
            },
            onDeleteClick = { wisata ->
                wisata.id?.let { id ->
                    database.child(id).removeValue()
                }
            }
        )

        // RecyclerView setup
        binding.rvWisata.layoutManager = LinearLayoutManager(this)
        binding.rvWisata.adapter = adapter

        // FAB tambah data
        binding.fabAdd.setOnClickListener {
            startActivity(Intent(this, AddWisataActivity::class.java))
        }

        // Tombol cari wisata terdekat
        binding.buttonPanel.setOnClickListener {
            cekIzinLokasi()
        }

        // Ambil data dari Firebase
        database.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                listWisata.clear()
                for (data in snapshot.children) {
                    val wisata = data.getValue(Wisata::class.java)
                    wisata?.let { listWisata.add(it) }
                }
                adapter.notifyDataSetChanged()
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(this@MainActivity, "Gagal ambil data", Toast.LENGTH_SHORT).show()
            }
        })
    }

    // =============================
    // LOCATION & GOOGLE MAPS
    // =============================

    private fun cekIzinLokasi() {
        if (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                100
            )
        } else {
            bukaGoogleMapsWisataTerdekat()
        }
    }

    private fun bukaGoogleMapsWisataTerdekat() {
        fusedLocationClient.lastLocation.addOnSuccessListener { location ->
            if (location != null) {
                val lat = location.latitude
                val lng = location.longitude

                val uri = Uri.parse("geo:$lat,$lng?q=objek wisata terdekat")
                val intent = Intent(Intent.ACTION_VIEW, uri)
                intent.setPackage("com.google.android.apps.maps")

                startActivity(intent)
            } else {
                Toast.makeText(this, "Lokasi tidak ditemukan", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        if (requestCode == 100 &&
            grantResults.isNotEmpty() &&
            grantResults[0] == PackageManager.PERMISSION_GRANTED
        ) {
            bukaGoogleMapsWisataTerdekat()
        } else {
            Toast.makeText(this, "Izin lokasi ditolak", Toast.LENGTH_SHORT).show()
        }
    }
}
