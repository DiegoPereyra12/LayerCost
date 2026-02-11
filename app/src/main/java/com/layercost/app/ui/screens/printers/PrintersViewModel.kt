package com.layercost.app.ui.screens.printers

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.layercost.app.data.repository.WarehouseRepository
import com.layercost.app.domain.model.Printer
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

data class PrintersUiState(
    val printers: List<Printer> = emptyList()
)

class PrintersViewModel(private val repository: WarehouseRepository) : ViewModel() {
    val uiState: StateFlow<PrintersUiState> = repository.getAllPrinters()
        .map { PrintersUiState(printers = it) }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = PrintersUiState()
        )
    
    fun deletePrinter(printer: Printer) {
        viewModelScope.launch {
            repository.deletePrinter(printer)
        }
    }
}
