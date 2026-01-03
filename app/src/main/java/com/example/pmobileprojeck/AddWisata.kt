package com.example.pmobileprojeck

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.database.FirebaseDatabase

class AddWisataActivity : AppCompatActivity() {

    private lateinit var etNama: EditText
    private lateinit var etDeskripsi: EditText
    private lateinit var etFasilitas: EditText
    private lateinit var etLat: EditText
    private lateinit var etLng: EditText
    private lateinit var btnSimpan: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_wisata)

        // Inisialisasi View
        etNama = findViewById(R.id.etNama)
        etDeskripsi = findViewById(R.id.etDeskripsi)
        etFasilitas = findViewById(R.id.etFasilitas)
        etLat = findViewById(R.id.etLat)
        etLng = findViewById(R.id.etLng)
        btnSimpan = findViewById(R.id.btnSimpan)

        val db = FirebaseDatabase.getInstance().getReference("wisata")

        btnSimpan.setOnClickListener {

            if (
                etNama.text.isEmpty() ||
                etDeskripsi.text.isEmpty() ||
                etFasilitas.text.isEmpty() ||
                etLat.text.isEmpty() ||
                etLng.text.isEmpty()
            ) {
                Toast.makeText(this, "Semua data harus diisi", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val id = db.push().key ?: return@setOnClickListener

            val wisata = Wisata(
                id = id,
                nama = etNama.text.toString(),
                deskripsi = etDeskripsi.text.toString(),
                fasilitas = etFasilitas.text.toString(),
                latitude = etLat.text.toString().toDouble(),
                longitude = etLng.text.toString().toDouble()
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
