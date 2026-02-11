package com.layercost.app.data.repository

import com.layercost.app.data.local.dao.InventoryDao
import com.layercost.app.data.local.dao.FilamentDao
import com.layercost.app.data.local.dao.PrinterDao
import com.layercost.app.domain.model.InventoryItem
import com.layercost.app.domain.model.Filament
import com.layercost.app.domain.model.Printer
import kotlinx.coroutines.flow.Flow

import com.layercost.app.data.local.dao.ItemNoteDao
import com.layercost.app.domain.model.ItemNote

class WarehouseRepository(
    private val inventoryDao: InventoryDao,
    private val filamentDao: FilamentDao,
    private val printerDao: PrinterDao,
    private val itemNoteDao: ItemNoteDao
) {

    // Inventory
    // Inventory
    fun getAllItems(): Flow<List<InventoryItem>> = inventoryDao.getAllItems()
    fun getItem(id: String): Flow<InventoryItem?> = inventoryDao.getItem(id)
    suspend fun addItem(item: InventoryItem) = inventoryDao.insertItem(item)
    suspend fun updateItem(item: InventoryItem) = inventoryDao.updateItem(item)
    suspend fun deleteItem(item: InventoryItem) = inventoryDao.deleteItem(item)

    // Notes
    fun getNotesForItem(itemId: String): Flow<List<ItemNote>> = itemNoteDao.getNotesForItem(itemId)
    suspend fun addNote(note: ItemNote) = itemNoteDao.insertNote(note)
    suspend fun updateNote(note: ItemNote) = itemNoteDao.updateNote(note)
    suspend fun deleteNote(note: ItemNote) = itemNoteDao.deleteNote(note)

    // Filaments
    fun getAllFilaments(): Flow<List<Filament>> = filamentDao.getAllFilaments()
    suspend fun addFilament(filament: Filament) = filamentDao.insertFilament(filament)
    suspend fun deleteFilament(filament: Filament) = filamentDao.deleteFilament(filament)

    // Printers
    fun getAllPrinters(): Flow<List<Printer>> = printerDao.getAllPrinters()
    suspend fun addPrinter(printer: Printer) = printerDao.insertPrinter(printer)
    suspend fun deletePrinter(printer: Printer) = printerDao.deletePrinter(printer)
}
