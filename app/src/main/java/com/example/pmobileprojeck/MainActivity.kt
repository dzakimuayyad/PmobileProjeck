package com.example.pmobileprojeck

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.pmobileprojeck.databinding.ActivityMainBinding
import com.google.firebase.database.*

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var database: DatabaseReference
    private val listWisata = mutableListOf<Wisata>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // ViewBinding
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        database = FirebaseDatabase.getInstance().getReference("wisata")

        val adapter = WisataAdapter(
            listWisata,
            onItemClick = { wisata ->
                startActivity(
                    Intent(this, MapsActivity::class.java)
                        .putExtra("wisata", wisata)
                )
            },
            onDeleteClick = { wisata ->
                wisata.id?.let { id ->
                    database.child(id).removeValue()
                }
            }
        )

        binding.rvWisata.layoutManager = LinearLayoutManager(this)
        binding.rvWisata.adapter = adapter

        binding.fabAdd.setOnClickListener {
            startActivity(Intent(this, AddWisataActivity::class.java))
        }

        database.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                listWisata.clear()
                for (data in snapshot.children) {
                    val wisata = data.getValue(Wisata::class.java)
                    wisata?.let {
                        listWisata.add(it)
                    }
                }
                adapter.notifyDataSetChanged()
            }

            override fun onCancelled(error: DatabaseError) {
                // Handle error if needed
            }
        })
    }
}
