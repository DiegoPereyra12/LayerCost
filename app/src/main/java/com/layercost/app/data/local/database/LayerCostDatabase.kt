package com.layercost.app.data.local.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.layercost.app.data.local.dao.InventoryDao
import com.layercost.app.data.local.dao.FilamentDao
import com.layercost.app.data.local.dao.PrinterDao
import com.layercost.app.domain.model.InventoryItem
import com.layercost.app.domain.model.Filament
import com.layercost.app.domain.model.Printer

@Database(
    entities = [InventoryItem::class, Filament::class, Printer::class], 
    version = 3, 
    exportSchema = false
)
abstract class LayerCostDatabase : RoomDatabase() {
    abstract fun inventoryDao(): InventoryDao
    abstract fun filamentDao(): FilamentDao
    abstract fun printerDao(): PrinterDao

    companion object {
        @Volatile
        private var Instance: LayerCostDatabase? = null

        fun getDatabase(context: Context): LayerCostDatabase {
            return Instance ?: synchronized(this) {
                Room.databaseBuilder(
                    context,
                    LayerCostDatabase::class.java,
                    "layercost_database"
                )
                .fallbackToDestructiveMigration()
                .build()
                .also { Instance = it }
            }
        }
    }
}
