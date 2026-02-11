package com.layercost.app.ui.screens.filaments

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.layercost.app.data.repository.WarehouseRepository
import com.layercost.app.domain.model.Filament
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

data class FilamentsUiState(
    val filaments: List<Filament> = emptyList()
)

class FilamentsViewModel(private val repository: WarehouseRepository) : ViewModel() {
    val uiState: StateFlow<FilamentsUiState> = repository.getAllFilaments()
        .map { FilamentsUiState(filaments = it) }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = FilamentsUiState()
        )
    
    fun deleteFilament(filament: Filament) {
        viewModelScope.launch {
            repository.deleteFilament(filament)
        }
    }
}
