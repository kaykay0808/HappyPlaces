package com.kay.happyplaces.models

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "happyPlaceTable")
data class HappyPlaceModelEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    val title: String,
    val image: String,
    val description: String,
    val date: String,
    val location: String,
    val latitude: Double,
    val longitude: Double
)
