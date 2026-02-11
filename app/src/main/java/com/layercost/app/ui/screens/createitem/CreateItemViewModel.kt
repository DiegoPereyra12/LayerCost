package com.layercost.app.ui.screens.createitem

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.layercost.app.data.repository.WarehouseRepository
import com.layercost.app.domain.calculators.CostBreakdown
import com.layercost.app.domain.calculators.CostCalculator
import com.layercost.app.domain.calculators.JobSpecs
import com.layercost.app.domain.calculators.MaterialProfile
import com.layercost.app.domain.calculators.OverheadProfile
import com.layercost.app.domain.calculators.PrinterProfile
import com.layercost.app.domain.model.InventoryItem
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class CreateItemUiState(
    val name: String = "",
    val color: String = "",
    val gramsInput: String = "",
    val timeInput: String = "",
    val selectedFilamentOption: String = "Custom",
    val materialType: String = "PLA",
    val customFilamentPriceInput: String = "450",
    val costBreakdown: CostBreakdown? = null,
    val availableFilaments: List<com.layercost.app.domain.model.Filament> = emptyList()
)

class CreateItemViewModel(private val repository: WarehouseRepository) : ViewModel() {

    private val _uiState = MutableStateFlow(CreateItemUiState())
    val uiState: StateFlow<CreateItemUiState> = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            repository.getAllFilaments().collect { filaments ->
                _uiState.update { it.copy(availableFilaments = filaments) }
            }
        }
    }

    fun updateName(input: String) {
        _uiState.update { it.copy(name = input) }
    }

    fun updateColor(input: String) {
        _uiState.update { it.copy(color = input) }
    }

    fun updateGrams(input: String) {
        _uiState.update { it.copy(gramsInput = input) }
        calculate()
    }

    fun updateTime(input: String) {
        _uiState.update { it.copy(timeInput = input) }
        calculate()
    }

    fun updateMaterialType(input: String) {
        _uiState.update { 
            it.copy(
                materialType = input,
                selectedFilamentOption = "Custom" // Reset to Custom
            ) 
        }
        calculate()
    }

    fun updateFilamentOption(option: String) {
        val state = _uiState.value
        
        if (option == "Custom") {
             _uiState.update { it.copy(selectedFilamentOption = option) }
        } else {
             // Reconstruct the logic to find matching filament from option string
             // Format: "Brand Color - $Price"
             val selected = state.availableFilaments.find { 
                 "${it.brand} ${it.color} - $${it.price}" == option
             }
             
             if (selected != null) {
                 _uiState.update { 
                     it.copy(
                         selectedFilamentOption = option,
                         customFilamentPriceInput = selected.price.toString()
                     ) 
                 }
             }
        }
        calculate()
    }

    fun updateCustomFilamentPrice(input: String) {
        _uiState.update { it.copy(customFilamentPriceInput = input) }
        calculate()
    }

    private fun calculate() {
        val state = _uiState.value
        val grams = state.gramsInput.toFloatOrNull()
        val time = state.timeInput.toFloatOrNull()
        val materialPrice = state.customFilamentPriceInput.toFloatOrNull()

        if (grams != null && time != null && materialPrice != null) {
            val breakdown = CostCalculator.calculate(
                printer = PrinterProfile(),
                material = MaterialProfile(
                    type = state.materialType,
                    costPerKg = materialPrice
                ),
                overhead = OverheadProfile(),
                job = JobSpecs(gramsUsed = grams, printTimeHours = time)
            )
            _uiState.update { it.copy(costBreakdown = breakdown) }
        } else {
            _uiState.update { it.copy(costBreakdown = null) }
        }
    }

    fun saveItem(onSaved: () -> Unit) {
        val state = _uiState.value
        
        if (state.name.isNotBlank()) {
            val newItem = InventoryItem(
                name = state.name,
                color = state.color,
                costBreakdown = state.costBreakdown
            )
            viewModelScope.launch {
                repository.addItem(newItem)
                onSaved()
            }
        }
    }
}
