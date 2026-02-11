package com.layercost.app.domain.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.UUID

@Entity(tableName = "item_notes")
data class ItemNote(
    @PrimaryKey
    val id: String = UUID.randomUUID().toString(),
    val itemId: String,
    val title: String,
    val content: String,
    val timestamp: Long = System.currentTimeMillis()
)
