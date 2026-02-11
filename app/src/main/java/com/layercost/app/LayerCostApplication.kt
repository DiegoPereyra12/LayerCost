package com.layercost.app

import android.app.Application
import com.layercost.app.data.local.database.LayerCostDatabase
import com.layercost.app.data.repository.WarehouseRepository

class LayerCostApplication : Application() {
    lateinit var container: AppContainer

    override fun onCreate() {
        super.onCreate()
        container = AppDefaultContainer(this)
    }
}

interface AppContainer {
    val warehouseRepository: WarehouseRepository
}

class AppDefaultContainer(private val context: android.content.Context) : AppContainer {
    override val warehouseRepository: WarehouseRepository by lazy {
        val database = LayerCostDatabase.getDatabase(context)
        WarehouseRepository(
            inventoryDao = database.inventoryDao(),
            filamentDao = database.filamentDao(),
            printerDao = database.printerDao(),
            itemNoteDao = database.itemNoteDao()
        )
    }
}
