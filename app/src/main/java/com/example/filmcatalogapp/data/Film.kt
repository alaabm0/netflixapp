package com.example.filmcatalogapp.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "films")
data class Film(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val title: String,
    val year: Int,
    val description: String,
    val genre: String,
    val duration: String,
    val imageResId: Int,
    val isFavorite: Boolean = false
)