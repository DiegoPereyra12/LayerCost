package com.layercost.app.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.layercost.app.domain.model.Filament
import kotlinx.coroutines.flow.Flow

@Dao
interface FilamentDao {
    @Query("SELECT * FROM filaments ORDER BY timestamp DESC")
    fun getAllFilaments(): Flow<List<Filament>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFilament(filament: Filament)

    @Delete
    suspend fun deleteFilament(filament: Filament)
}
