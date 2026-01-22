package com.example.pmobileprojeck

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.example.pmobileprojeck.databinding.ActivityAddWisataBinding
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.firebase.database.FirebaseDatabase

class AddWisataActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAddWisataBinding
    private lateinit var fusedLocation: FusedLocationProviderClient

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // ViewBinding
        binding = ActivityAddWisataBinding.inflate(layoutInflater)
        setContentView(binding.root)

        fusedLocation = LocationServices.getFusedLocationProviderClient(this)
        val db = FirebaseDatabase.getInstance().getReference("wisata")

        binding.btnSimpan.setOnClickListener {

            // Validasi input
            if (
                binding.etNama.text.isEmpty() ||
                binding.etDeskripsi.text.isEmpty() ||
                binding.etFasilitas.text.isEmpty()
            ) {
                Toast.makeText(this, "Semua data harus diisi", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // Cek permission lokasi
            if (ActivityCompat.checkSelfPermission(
                    this,
                    Manifest.permission.ACCESS_FINE_LOCATION
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                ActivityCompat.requestPermissions(
                    this,
                    arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                    101
                )
                return@setOnClickListener
            }

            // Ambil lokasi user
            fusedLocation.lastLocation.addOnSuccessListener { location ->
                if (location == null) {
                    Toast.makeText(this, "Lokasi tidak ditemukan", Toast.LENGTH_SHORT).show()
                    return@addOnSuccessListener
                }

                val id = db.push().key ?: return@addOnSuccessListener

                val wisata = Wisata(
                    id = id,
                    nama = binding.etNama.text.toString(),
                    deskripsi = binding.etDeskripsi.text.toString(),
                    fasilitas = binding.etFasilitas.text.toString(),
                    latitude = location.latitude,
                    longitude = location.longitude
                )

                db.child(id).setValue(wisata)
                    .addOnSuccessListener {
                        Toast.makeText(this, "Data wisata berhasil disimpan", Toast.LENGTH_SHORT).show()
                        finish()
                    }
                    .addOnFailureListener {
                        Toast.makeText(this, "Gagal menyimpan data", Toast.LENGTH_SHORT).show()
                    }
            }
        }
    }
}
