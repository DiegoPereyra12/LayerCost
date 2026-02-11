package com.layercost.app.ui.screens.createitem

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.Card
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.compose.runtime.collectAsState
import com.layercost.app.domain.calculators.CostBreakdown
import com.layercost.app.ui.AppViewModelProvider
import com.layercost.app.domain.model.Filament

@Composable
fun CreateItemScreen(
    viewModel: CreateItemViewModel = viewModel(factory = AppViewModelProvider.Factory),
    onNavigateBack: () -> Unit = {}
) {
    val state by viewModel.uiState.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(rememberScrollState())
    ) {
        Text(
            text = "Crear Pieza de Inventario",
            style = MaterialTheme.typography.headlineSmall,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        // Nombre
        OutlinedTextField(
            value = state.name,
            onValueChange = { viewModel.updateName(it) },
            label = { Text("Nombre de la Pieza") },
            maxLines = 1,
            modifier = Modifier.fillMaxWidth()
        )
        
        Spacer(modifier = Modifier.height(8.dp))

        // Color
        OutlinedTextField(
            value = state.color,
            onValueChange = { viewModel.updateColor(it) },
            label = { Text("Color") },
            maxLines = 1,
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(8.dp))

        // Gramos
        OutlinedTextField(
            value = state.gramsInput,
            onValueChange = { viewModel.updateGrams(it) },
            label = { Text("Gramos de filamento (g)") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            modifier = Modifier.fillMaxWidth()
        )
        
        Spacer(modifier = Modifier.height(8.dp))

        // Tiempo
        OutlinedTextField(
            value = state.timeInput,
            onValueChange = { viewModel.updateTime(it) },
            label = { Text("Tiempo de Impresión (Horas, ej. 1.5)") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))
        
        // Selector de Tipo de Material (PLA/PETG)
        Text("Tipo de Material:", style = MaterialTheme.typography.labelMedium)
        var materialExpanded by remember { mutableStateOf(false) }
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 4.dp)
        ) {
            OutlinedTextField(
                value = state.materialType,
                onValueChange = {},
                readOnly = true,
                label = { Text("Material") },
                trailingIcon = { Icon(Icons.Default.ArrowDropDown, "Expandir") },
                modifier = Modifier.fillMaxWidth()
            )
            Box(
                modifier = Modifier
                    .matchParentSize()
                    .clickable { materialExpanded = true }
            )
            DropdownMenu(
                expanded = materialExpanded,
                onDismissRequest = { materialExpanded = false }
            ) {
                listOf("PLA", "PETG").forEach { type ->
                    DropdownMenuItem(
                        text = { Text(text = type) },
                        onClick = {
                            viewModel.updateMaterialType(type)
                            materialExpanded = false
                        }
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Selector de Filamento
        Text("Filamento (Marca/Color):", style = MaterialTheme.typography.labelLarge)
        
        // Filter options based on selected Material Type
        val filteredFilaments = state.availableFilaments.filter { 
             it.material.contains(state.materialType, ignoreCase = true) 
        }
        
        val options = mutableListOf("Custom")
        filteredFilaments.forEach { 
            options.add("${it.brand} ${it.color} - $${it.price}")
        }
        
        var expanded by remember { mutableStateOf(false) }

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 4.dp)
        ) {
            OutlinedTextField(
                value = state.selectedFilamentOption,
                onValueChange = {},
                readOnly = true,
                label = { Text("Selección de Filamento") },
                trailingIcon = { Icon(Icons.Default.ArrowDropDown, "Expandir") },
                modifier = Modifier.fillMaxWidth()
            )
            
            Box(
                modifier = Modifier
                    .matchParentSize()
                    .clickable { expanded = true }
            )

            DropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false }
            ) {
                options.forEach { selectionOption ->
                    DropdownMenuItem(
                        text = { Text(text = selectionOption) },
                        onClick = {
                            viewModel.updateFilamentOption(selectionOption)
                            expanded = false
                        }
                    )
                }
            }
        }

        // Campo condicional para Costo (Custom editable, otros no)
        val isCustom = state.selectedFilamentOption == "Custom"
        
        Spacer(modifier = Modifier.height(8.dp))
        OutlinedTextField(
            value = state.customFilamentPriceInput,
            onValueChange = { if (isCustom) viewModel.updateCustomFilamentPrice(it) },
            label = { Text("Costo del Rollo (1kg MXN)") },
            readOnly = !isCustom,
            enabled = isCustom,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(24.dp))

        Button(
            onClick = {
                viewModel.saveItem {
                    onNavigateBack()
                }
            },
            enabled = state.name.isNotBlank(),
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Agregar")
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Resultados
        state.costBreakdown?.let { breakdown ->
            CostResultCard(breakdown)
        }
    }
}

@Composable
fun CostResultCard(breakdown: CostBreakdown) {
    Card(
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                text = "Costo Real de Producción:",
                style = MaterialTheme.typography.titleMedium
            )
            Spacer(modifier = Modifier.height(8.dp))
            
            ResultRow("Material:", breakdown.materialCost)
            ResultRow("Luz:", breakdown.electricityCost)
            ResultRow("Desgaste:", breakdown.depreciationCost)
            
            Spacer(modifier = Modifier.height(8.dp))
            ResultRow("Margen Seguridad (10%):", breakdown.safetyMargin)
            
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "Total: $${"%.2f".format(breakdown.totalCost)} MXN",
                style = MaterialTheme.typography.headlineSmall,
                color = MaterialTheme.colorScheme.primary
            )
            
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "Si cobras menos de esto, estás perdiendo dinero.",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.error
            )
        }
    }
}

@Composable
fun ResultRow(label: String, value: Float) {
    Text(
        text = "$label $${"%.2f".format(value)}",
        style = MaterialTheme.typography.bodyMedium
    )
}

@Preview(showBackground = true)
@Composable
fun CreateItemPreview() {
    CreateItemScreen()
}
