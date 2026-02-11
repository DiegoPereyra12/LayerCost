package com.layercost.app.ui.screens.createitem

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
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
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.layercost.app.R
import com.layercost.app.ui.AppViewModelProvider
import com.layercost.app.ui.components.CostResultCard
import com.layercost.app.ui.components.ImagePickerSection

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
            text = stringResource(R.string.create_item_title),
            style = MaterialTheme.typography.headlineSmall,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        // Nombre
        OutlinedTextField(
            value = state.name,
            onValueChange = { viewModel.updateName(it) },
            label = { Text(stringResource(R.string.item_name_label)) },
            maxLines = 1,
            modifier = Modifier.fillMaxWidth()
        )
        
        Spacer(modifier = Modifier.height(16.dp))

        // Image Picker
        ImagePickerSection(
            imageUri = state.imageUri,
            onImageSelected = { viewModel.updateImageUri(it) }
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Dropdowns de Costos (Desgaste y Energía)
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = androidx.compose.foundation.layout.Arrangement.spacedBy(8.dp)
        ) {
            // Costo Desgaste
            Box(modifier = Modifier.weight(1f).padding(top = 4.dp)) {
                var wearExpanded by remember { mutableStateOf(false) }
                OutlinedTextField(
                    value = state.wearCostOption,
                    onValueChange = {},
                    readOnly = true,
                    label = { Text(stringResource(R.string.wear_cost_label)) },
                    trailingIcon = { Icon(Icons.Default.ArrowDropDown, "Expandir") },
                    modifier = Modifier.fillMaxWidth()
                )
                Box(
                    modifier = Modifier
                        .matchParentSize()
                        .clickable { wearExpanded = true }
                )
                DropdownMenu(
                    expanded = wearExpanded,
                    onDismissRequest = { wearExpanded = false }
                ) {
                    listOf("Default").forEach { option ->
                        DropdownMenuItem(
                            text = { 
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = androidx.compose.foundation.layout.Arrangement.SpaceBetween
                                ) {
                                    Text(text = option)
                                    Text(
                                        text = "${stringResource(R.string.currency_symbol)}3.00", 
                                        color = MaterialTheme.colorScheme.secondary
                                    )
                                }
                            },
                            onClick = {
                                viewModel.updateWearCostOption(option)
                                wearExpanded = false
                            }
                        )
                    }
                }
            }
            
            // Costo Energetico
            Box(modifier = Modifier.weight(1f).padding(top = 4.dp)) {
                var energyExpanded by remember { mutableStateOf(false) }
                OutlinedTextField(
                    value = state.energyCostOption,
                    onValueChange = {},
                    readOnly = true,
                    label = { Text(stringResource(R.string.energy_cost_label)) },
                    trailingIcon = { Icon(Icons.Default.ArrowDropDown, "Expandir") },
                    modifier = Modifier.fillMaxWidth()
                )
                Box(
                    modifier = Modifier
                        .matchParentSize()
                        .clickable { energyExpanded = true }
                )
                DropdownMenu(
                    expanded = energyExpanded,
                    onDismissRequest = { energyExpanded = false }
                ) {
                    listOf("Default").forEach { option ->
                        DropdownMenuItem(
                            text = { 
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = androidx.compose.foundation.layout.Arrangement.SpaceBetween
                                ) {
                                    Text(text = option)
                                    Text(
                                        text = "${stringResource(R.string.currency_symbol)}2.50", 
                                        color = MaterialTheme.colorScheme.secondary
                                    )
                                }
                            },
                            onClick = {
                                viewModel.updateEnergyCostOption(option)
                                energyExpanded = false
                            }
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        // Gramos
        OutlinedTextField(
            value = state.gramsInput,
            onValueChange = { viewModel.updateGrams(it) },
            label = { Text(stringResource(R.string.grams_label)) },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            modifier = Modifier.fillMaxWidth()
        )
        
        Spacer(modifier = Modifier.height(8.dp))

        // Tiempo
        OutlinedTextField(
            value = state.timeInput,
            onValueChange = { viewModel.updateTime(it) },
            label = { Text(stringResource(R.string.time_label)) },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))
        
        // Selector de Tipo de Material (PLA/PETG)
        Text(stringResource(R.string.material_type_label), style = MaterialTheme.typography.labelMedium)
        var materialExpanded by remember { mutableStateOf(false) }
        Box(modifier = Modifier.fillMaxWidth().padding(top = 4.dp)) {
            OutlinedTextField(
                value = state.materialType,
                onValueChange = {},
                readOnly = true,
                label = { Text(stringResource(R.string.material_type_label)) },
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
        Text(stringResource(R.string.filament_selection_label), style = MaterialTheme.typography.labelLarge)
        
        // Filter options based on selected Material Type
        val filteredFilaments = state.availableFilaments.filter { 
             it.material.contains(state.materialType, ignoreCase = true) 
        }
        
        val options = mutableListOf("Custom")
        filteredFilaments.forEach { 
            options.add("${it.brand} ${it.color} - ${stringResource(R.string.currency_symbol)}${it.price}")
        }
        
        var expanded by remember { mutableStateOf(false) }

        Box(modifier = Modifier.fillMaxWidth().padding(top = 4.dp)) {
            OutlinedTextField(
                value = state.selectedFilamentOption,
                onValueChange = {},
                readOnly = true,
                label = { Text(stringResource(R.string.filament_selection_label)) },
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
            label = { Text(stringResource(R.string.roll_cost_label)) },
            readOnly = !isCustom,
            enabled = isCustom,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))
        
        // Precio de Venta
        OutlinedTextField(
            value = state.salePriceInput,
            onValueChange = { viewModel.updateSalePrice(it) },
            label = { Text(stringResource(R.string.sale_price_label)) },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
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
            Text(stringResource(R.string.add_button))
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Resultados
        state.costBreakdown?.let { breakdown ->
             val salePrice = state.salePriceInput.toFloatOrNull() ?: 0f
             CostResultCard(breakdown, salePrice)
        }
    }
}

@Preview(showBackground = true)
@Composable
fun CreateItemPreview() {
    CreateItemScreen()
}
