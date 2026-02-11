package com.layercost.app.domain.model

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.layercost.app.domain.calculators.CostBreakdown
import java.util.UUID

@Entity(tableName = "inventory_items")
data class InventoryItem(
    @PrimaryKey
    val id: String = UUID.randomUUID().toString(),
    val name: String,
    val color: String,
    @Embedded(prefix = "cost_")
    val costBreakdown: CostBreakdown?,
    val timestamp: Long = System.currentTimeMillis()
)
