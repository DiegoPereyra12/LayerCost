package com.layercost.app.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.layercost.app.domain.model.ItemNote
import kotlinx.coroutines.flow.Flow

@Dao
interface ItemNoteDao {
    @Query("SELECT * FROM item_notes WHERE itemId = :itemId ORDER BY timestamp DESC")
    fun getNotesForItem(itemId: String): Flow<List<ItemNote>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertNote(note: ItemNote)

    @Update
    suspend fun updateNote(note: ItemNote)

    @Delete
    suspend fun deleteNote(note: ItemNote)
}
