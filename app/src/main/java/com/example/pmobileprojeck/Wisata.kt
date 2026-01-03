package com.example.pmobileprojeck

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Wisata(
    val id: String? = "",
    val nama: String = "",
    val deskripsi: String = "",
    val fasilitas: String = "",
    val latitude: Double = 0.0,
    val longitude: Double = 0.0
) : Parcelable
