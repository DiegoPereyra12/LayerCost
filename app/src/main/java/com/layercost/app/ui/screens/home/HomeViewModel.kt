package com.layercost.app.ui.screens.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.layercost.app.data.repository.WarehouseRepository
import com.layercost.app.domain.model.InventoryItem
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

data class HomeUiState(
    val items: List<InventoryItem> = emptyList(),
    val totalInventoryValue: Float = 0f,
    val totalEstimatedProfit: Float = 0f
)

class HomeViewModel(private val repository: WarehouseRepository) : ViewModel() {
    val uiState: StateFlow<HomeUiState> = repository.getAllItems()
        .map { items -> 
            val totalInventoryValue = items.filter { it.stock > 0 }.sumOf { (it.salePrice * it.stock).toDouble() }.toFloat()
            val totalEstimatedProfit = items.filter { it.stock > 0 }.sumOf { 
                val cost = it.costBreakdown?.totalCost ?: 0f
                ((it.salePrice - cost) * it.stock).toDouble() 
            }.toFloat()

            HomeUiState(
                items = items,
                totalInventoryValue = totalInventoryValue,
                totalEstimatedProfit = totalEstimatedProfit
            ) 
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = HomeUiState()
        )

    fun deleteItem(item: InventoryItem) {
        viewModelScope.launch {
            repository.deleteItem(item)
        }
    }
}
