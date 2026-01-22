package com.example.pmobileprojeck

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class WisataAdapter(
    private val listWisata: List<Wisata>,
    private val onItemClick: (Wisata) -> Unit,
    private val onDeleteClick: (Wisata) -> Unit
) : RecyclerView.Adapter<WisataAdapter.ViewHolder>() {

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        private val tvNama: TextView = itemView.findViewById(R.id.tvNamaWisata)
        private val tvDeskripsi: TextView = itemView.findViewById(R.id.tvDeskripsi)
        private val btnDelete: Button = itemView.findViewById(R.id.btnDelete)

        fun bind(wisata: Wisata) {
            // Set data ke view
            tvNama.text = wisata.nama
            tvDeskripsi.text = wisata.deskripsi

            // Klik item → Maps
            itemView.setOnClickListener {
                onItemClick(wisata)
            }

            // Klik hapus
            btnDelete.setOnClickListener {
                onDeleteClick(wisata)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_wisata, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(listWisata[position])
    }

    override fun getItemCount(): Int = listWisata.size
}
