package com.layercost.app.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.layercost.app.domain.model.Printer
import kotlinx.coroutines.flow.Flow

@Dao
interface PrinterDao {
    @Query("SELECT * FROM printers ORDER BY timestamp DESC")
    fun getAllPrinters(): Flow<List<Printer>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPrinter(printer: Printer)

    @Delete
    suspend fun deletePrinter(printer: Printer)
}
