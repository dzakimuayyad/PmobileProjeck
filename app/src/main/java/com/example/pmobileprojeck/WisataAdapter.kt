package com.example.pmobileprojeck

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView

class WisataAdapter(
    private val listWisata: List<Wisata>,
    private val onItemClick: (Wisata) -> Unit,
    private val onDeleteClick: (Wisata) -> Unit
) : RecyclerView.Adapter<WisataAdapter.ViewHolder>() {

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(wisata: Wisata) {
            itemView.setOnClickListener {
                onItemClick(wisata)
            }

            // Tombol hapus
            itemView.findViewById<ImageView>(R.id.btnDelete).setOnClickListener {
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
