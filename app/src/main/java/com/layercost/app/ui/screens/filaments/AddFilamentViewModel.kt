package com.layercost.app.ui.screens.filaments

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.layercost.app.data.repository.WarehouseRepository
import com.layercost.app.domain.model.Filament
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class AddFilamentUiState(
    val color: String = "", // Used for "Nombre/Color"
    val brand: String = "",
    val finish: String = "", // "Acabado"
    val materialType: String = "PLA", // "Material" (PLA, PETG)
    val price: String = ""
)

class AddFilamentViewModel(private val repository: WarehouseRepository) : ViewModel() {

    private val _uiState = MutableStateFlow(AddFilamentUiState())
    val uiState: StateFlow<AddFilamentUiState> = _uiState.asStateFlow()

    fun updateColor(input: String) {
        _uiState.update { it.copy(color = input) }
    }

    fun updateBrand(input: String) {
        _uiState.update { it.copy(brand = input) }
    }

    fun updateFinish(input: String) {
        _uiState.update { it.copy(finish = input) }
    }

    fun updateMaterialType(input: String) {
        _uiState.update { it.copy(materialType = input) }
    }

    fun updatePrice(input: String) {
        _uiState.update { it.copy(price = input) }
    }

    fun saveFilament(onSaved: () -> Unit) {
        val state = _uiState.value
        val priceFloat = state.price.toFloatOrNull() ?: 0f

        if (state.color.isNotBlank() && state.brand.isNotBlank()) {
            val filament = Filament(
                brand = state.brand,
                color = state.color,
                material = "${state.materialType} ${state.finish}".trim(),
                price = priceFloat
            )
            viewModelScope.launch {
                repository.addFilament(filament)
                onSaved()
            }
        }
    }
}
