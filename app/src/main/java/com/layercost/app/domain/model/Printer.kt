package com.layercost.app.domain.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.UUID

@Entity(tableName = "printers")
data class Printer(
    @PrimaryKey
    val id: String = UUID.randomUUID().toString(),
    val name: String,
    val lifespanHours: Float,
    val powerWatts: Float,
    val price: Float,
    val timestamp: Long = System.currentTimeMillis()
)
