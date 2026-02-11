package com.layercost.app.ui.screens.itemdetail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.layercost.app.data.repository.WarehouseRepository
import com.layercost.app.domain.model.InventoryItem
import com.layercost.app.domain.model.ItemNote
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class ItemDetailUiState(
    val item: InventoryItem? = null,
    val notes: List<ItemNote> = emptyList(),
    val isEditing: Boolean = false,
    val noteTitleInput: String = "",
    val noteContentInput: String = "",
    val isAddingNote: Boolean = false,
    // Editable inputs
    val nameInput: String = "",
    val materialInput: String = "",
    val brandInput: String = "",
    val stockInput: String = "",
    val salePriceInput: String = "",
    val filamentCostInput: String = ""
)

class ItemDetailViewModel(
    private val repository: WarehouseRepository,
    private val itemId: String
) : ViewModel() {

    private val _uiState = MutableStateFlow(ItemDetailUiState())
    val uiState: StateFlow<ItemDetailUiState> = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            repository.getItem(itemId).collect { item ->
                _uiState.update { 
                    it.copy(
                        item = item,
                        stockInput = item?.stock?.toString() ?: "0"
                    ) 
                }
            }
        }
        viewModelScope.launch {
            repository.getNotesForItem(itemId).collect { notes ->
                _uiState.update { it.copy(notes = notes) }
            }
        }
    }
    
    fun toggleEditMode() {
        if (_uiState.value.isEditing) {
            // Save changes
            saveItemChanges()
        } else {
            // Enter edit mode: initialize inputs from current item
            initEditInputs()
        }
        _uiState.update { it.copy(isEditing = !it.isEditing) }
    }

    private fun initEditInputs() {
        val item = _uiState.value.item ?: return
        _uiState.update {
            it.copy(
                nameInput = item.name,
                materialInput = item.material,
                brandInput = item.brand,
                stockInput = item.stock.toString(),
                salePriceInput = item.salePrice.toString(),
                filamentCostInput = item.costBreakdown?.materialCost?.toString() ?: "0.0"
            )
        }
    }

    private fun saveItemChanges() {
        val state = _uiState.value
        val currentItem = state.item ?: return

        // Parse inputs safely
        val stock = state.stockInput.toIntOrNull() ?: currentItem.stock
        val salePrice = state.salePriceInput.toFloatOrNull() ?: currentItem.salePrice
        val filamentCost = state.filamentCostInput.toFloatOrNull() ?: (currentItem.costBreakdown?.materialCost ?: 0f)

        // Recalculate costs if filament cost changed
        val currentBreakdown = currentItem.costBreakdown
        val newBreakdown = if (currentBreakdown != null) {
            val oldSubtotal = currentBreakdown.subtotal
            // Avoid division by zero for margin calculation
            val impliedMargin = if (oldSubtotal > 0) currentBreakdown.safetyMargin / oldSubtotal else 0.10f
            
            val newSubtotal = filamentCost + currentBreakdown.electricityCost + currentBreakdown.depreciationCost
            val newSafetyMargin = newSubtotal * impliedMargin
            val newTotal = newSubtotal + newSafetyMargin
            
            currentBreakdown.copy(
                materialCost = filamentCost,
                subtotal = newSubtotal,
                safetyMargin = newSafetyMargin,
                totalCost = newTotal
            )
        } else null

        val updatedItem = currentItem.copy(
            name = state.nameInput,
            material = state.materialInput,
            brand = state.brandInput,
            stock = stock,
            salePrice = salePrice,
            costBreakdown = newBreakdown
        )

        viewModelScope.launch {
            repository.updateItem(updatedItem)
            // Update local state immediately for UI responsiveness
            _uiState.update { it.copy(item = updatedItem) }
        }
    }

    fun updateNameInput(input: String) {
        _uiState.update { it.copy(nameInput = input) }
    }

    fun updateMaterialInput(input: String) {
        _uiState.update { it.copy(materialInput = input) }
    }

    fun updateBrandInput(input: String) {
        _uiState.update { it.copy(brandInput = input) }
    }

    fun updateFilamentCostInput(input: String) {
        // Allow dots and digits
        if (input.all { it.isDigit() || it == '.' }) {
            _uiState.update { it.copy(filamentCostInput = input) }
        }
    }

    fun updateSalePriceInput(input: String) {
        if (input.all { it.isDigit() || it == '.' }) {
            _uiState.update { it.copy(salePriceInput = input) }
        }
    }

    fun updateStockInput(input: String) {
        if (input.all { it.isDigit() }) {
             _uiState.update { it.copy(stockInput = input) }
        }
    }

    fun incrementStock() {
        val current = _uiState.value.stockInput.toIntOrNull() ?: 0
        _uiState.update { it.copy(stockInput = (current + 1).toString()) }
    }

    fun decrementStock() {
        val current = _uiState.value.stockInput.toIntOrNull() ?: 0
        if (current > 0) {
            _uiState.update { it.copy(stockInput = (current - 1).toString()) }
        }
    }

    fun updateNoteTitle(input: String) {
        _uiState.update { it.copy(noteTitleInput = input) }
    }

    fun updateNoteContent(input: String) {
        _uiState.update { it.copy(noteContentInput = input) }
    }
    
    fun toggleAddNote() {
        _uiState.update { it.copy(isAddingNote = !it.isAddingNote) }
    }

    fun saveNote() {
        val state = _uiState.value
        if (state.noteTitleInput.isNotBlank() && state.noteContentInput.isNotBlank()) {
            val newNote = ItemNote(
                itemId = itemId,
                title = state.noteTitleInput,
                content = state.noteContentInput
            )
            viewModelScope.launch {
                repository.addNote(newNote)
                _uiState.update { 
                    it.copy(
                        noteTitleInput = "", 
                        noteContentInput = "",
                        isAddingNote = false
                    ) 
                }
            }
        }
    }

    fun deleteNote(note: ItemNote) {
        viewModelScope.launch {
            repository.deleteNote(note)
        }
    }
    
    // Function to calculate simple formatted date from timestamp
    fun formatDate(timestamp: Long): String {
        val sdf = java.text.SimpleDateFormat("dd/MM/yyyy", java.util.Locale.getDefault())
        return sdf.format(java.util.Date(timestamp))
    }
}
