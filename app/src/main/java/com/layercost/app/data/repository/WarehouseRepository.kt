package com.layercost.app.data.repository

import com.layercost.app.data.local.dao.InventoryDao
import com.layercost.app.data.local.dao.FilamentDao
import com.layercost.app.data.local.dao.PrinterDao
import com.layercost.app.domain.model.InventoryItem
import com.layercost.app.domain.model.Filament
import com.layercost.app.domain.model.Printer
import kotlinx.coroutines.flow.Flow

class WarehouseRepository(
    private val inventoryDao: InventoryDao,
    private val filamentDao: FilamentDao,
    private val printerDao: PrinterDao
) {

    // Inventory
    fun getAllItems(): Flow<List<InventoryItem>> = inventoryDao.getAllItems()
    suspend fun addItem(item: InventoryItem) = inventoryDao.insertItem(item)
    suspend fun deleteItem(item: InventoryItem) = inventoryDao.deleteItem(item)

    // Filaments
    fun getAllFilaments(): Flow<List<Filament>> = filamentDao.getAllFilaments()
    suspend fun addFilament(filament: Filament) = filamentDao.insertFilament(filament)
    suspend fun deleteFilament(filament: Filament) = filamentDao.deleteFilament(filament)

    // Printers
    fun getAllPrinters(): Flow<List<Printer>> = printerDao.getAllPrinters()
    suspend fun addPrinter(printer: Printer) = printerDao.insertPrinter(printer)
    suspend fun deletePrinter(printer: Printer) = printerDao.deletePrinter(printer)
}
