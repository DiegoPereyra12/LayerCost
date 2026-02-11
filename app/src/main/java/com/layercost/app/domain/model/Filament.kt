package com.layercost.app.domain.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.UUID

@Entity(tableName = "filaments")
data class Filament(
    @PrimaryKey
    val id: String = UUID.randomUUID().toString(),
    val brand: String,
    val material: String, // PLA, PETG, etc
    val color: String,
    val price: Float,
    val weightGrams: Float = 1000f,
    val timestamp: Long = System.currentTimeMillis()
)
