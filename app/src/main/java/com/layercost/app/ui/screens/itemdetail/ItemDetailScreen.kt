package com.layercost.app.ui.screens.itemdetail

import androidx.compose.foundation.clickable
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Remove
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material.icons.filled.Save
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.layercost.app.R
import com.layercost.app.domain.model.InventoryItem
import com.layercost.app.domain.model.ItemNote
import com.layercost.app.ui.AppViewModelProvider
import com.layercost.app.ui.components.CostResultCard

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ItemDetailScreen(
    itemId: String,
    viewModel: ItemDetailViewModel = viewModel(factory = AppViewModelProvider.Factory),
    onNavigateBack: () -> Unit = {}
) {
    val state by viewModel.uiState.collectAsState()
    val item = state.item

    if (item == null) {
        // Handle loading or error state
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Text("Cargando o item no encontrado...")
        }
        return
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(item.name) },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Atrás")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.onPrimaryContainer
                ),
                actions = {
                    IconButton(onClick = { viewModel.toggleEditMode() }) {
                        Icon(
                            imageVector = if (state.isEditing) Icons.Default.Save else Icons.Default.Edit,
                            contentDescription = if (state.isEditing) "Guardar" else "Editar"
                        )
                    }
                }
            )
        },
        floatingActionButton = {
            if (!state.isAddingNote) {
                FloatingActionButton(onClick = { viewModel.toggleAddNote() }) {
                    Icon(Icons.Default.Add, contentDescription = "Agregar Nota")
                }
            }
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp)
        ) {
            // Main Content Scrollable
            LazyColumn(
                modifier = Modifier.weight(1f)
            ) {
                // Header Image
                item {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(200.dp)
                            .padding(bottom = 16.dp)
                    ) {
                         if (item.imageUri != null) {
                            AsyncImage(
                                model = android.net.Uri.parse(item.imageUri),
                                contentDescription = null,
                                modifier = Modifier.fillMaxSize(),
                                contentScale = ContentScale.Crop
                            )
                        } else {
                             Box(modifier = Modifier.fillMaxSize().background(MaterialTheme.colorScheme.surfaceVariant), contentAlignment = Alignment.Center) {
                                 Icon(painterResource(id = R.drawable.ic_3d_piece), contentDescription = null, modifier = Modifier.size(64.dp))
                             }
                        }
                    }
                }

                // General Info Section
                item {
                    Card(
                        modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Text("Información General", style = MaterialTheme.typography.titleMedium)
                            Spacer(modifier = Modifier.height(8.dp))
                            
                            if (state.isEditing) {
                                OutlinedTextField(
                                    value = state.nameInput,
                                    onValueChange = { viewModel.updateNameInput(it) },
                                    label = { Text("Nombre") },
                                    modifier = Modifier.fillMaxWidth().padding(bottom = 8.dp)
                                )
                                OutlinedTextField(
                                    value = state.brandInput,
                                    onValueChange = { viewModel.updateBrandInput(it) },
                                    label = { Text("Marca") },
                                    modifier = Modifier.fillMaxWidth().padding(bottom = 8.dp)
                                )
                                OutlinedTextField(
                                    value = state.materialInput,
                                    onValueChange = { viewModel.updateMaterialInput(it) },
                                    label = { Text("Material") },
                                    modifier = Modifier.fillMaxWidth()
                                )
                            } else {
                                Row(modifier = Modifier.padding(vertical = 4.dp)) {
                                    Text("Nombre:", fontWeight = FontWeight.Bold, modifier = Modifier.width(100.dp))
                                    Text(item.name)
                                }
                                Row(modifier = Modifier.padding(vertical = 4.dp)) {
                                    Text("Marca:", fontWeight = FontWeight.Bold, modifier = Modifier.width(100.dp))
                                    Text(item.brand)
                                }
                                Row(modifier = Modifier.padding(vertical = 4.dp)) {
                                    Text("Material:", fontWeight = FontWeight.Bold, modifier = Modifier.width(100.dp))
                                    Text(item.material)
                                }
                            }
                        }
                    }
                }

                // Economics Section
                item {
                    Card(
                        modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Text("Economía", style = MaterialTheme.typography.titleMedium)
                            Spacer(modifier = Modifier.height(8.dp))
                            
                            if (state.isEditing) {
                                OutlinedTextField(
                                    value = state.filamentCostInput,
                                    onValueChange = { viewModel.updateFilamentCostInput(it) },
                                    label = { Text("Costo Filamento ($)") },
                                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                                    modifier = Modifier.fillMaxWidth().padding(bottom = 8.dp)
                                )
                                OutlinedTextField(
                                    value = state.salePriceInput,
                                    onValueChange = { viewModel.updateSalePriceInput(it) },
                                    label = { Text("Precio Venta ($)") },
                                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                                    modifier = Modifier.fillMaxWidth()
                                )
                            } else {
                                Row(modifier = Modifier.padding(vertical = 4.dp)) {
                                    Text("Costo Fil.:", fontWeight = FontWeight.Bold, modifier = Modifier.width(100.dp))
                                    Text("$ ${"%.2f".format(item.costBreakdown?.materialCost ?: 0f)}")
                                }
                                Row(modifier = Modifier.padding(vertical = 4.dp)) {
                                    Text("Precio Venta:", fontWeight = FontWeight.Bold, modifier = Modifier.width(100.dp))
                                    Text("$ ${"%.2f".format(item.salePrice)}")
                                }
                            }
                        }
                    }
                }

                // Stock Section
                item {
                    Card(
                        modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = if (item.stock == 0) MaterialTheme.colorScheme.errorContainer else MaterialTheme.colorScheme.surfaceVariant
                        )
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Text("Inventario", style = MaterialTheme.typography.titleMedium)
                            Spacer(modifier = Modifier.height(8.dp))
                            
                            if (state.isEditing) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.Center,
                                    modifier = Modifier.fillMaxWidth()
                                ) {
                                    IconButton(
                                        onClick = { viewModel.decrementStock() },
                                        // Disable if 0 to prevent negative
                                        enabled = (state.stockInput.toIntOrNull() ?: 0) > 0
                                    ) {
                                        Icon(
                                            imageVector = Icons.Default.Remove, 
                                            contentDescription = "Disminuir Stock",
                                            tint = if ((state.stockInput.toIntOrNull() ?: 0) > 0) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.3f)
                                        )
                                    }
                                    
                                    OutlinedTextField(
                                        value = state.stockInput,
                                        onValueChange = { viewModel.updateStockInput(it) },
                                        label = null, // No label for cleaner look in this compact row
                                        modifier = Modifier
                                            .width(100.dp)
                                            .padding(horizontal = 8.dp),
                                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                                        singleLine = true,
                                        textStyle = LocalTextStyle.current.copy(textAlign = TextAlign.Center)
                                    )

                                    IconButton(onClick = { viewModel.incrementStock() }) {
                                        Icon(
                                            imageVector = Icons.Default.Add, 
                                            contentDescription = "Aumentar Stock",
                                            tint = MaterialTheme.colorScheme.primary
                                        )
                                    }
                                }
                            } else {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Text(
                                        text = "Stock: ${item.stock}",
                                        style = MaterialTheme.typography.headlineMedium,
                                        fontWeight = FontWeight.Bold
                                    )
                                    if (item.stock == 0) {
                                        Spacer(modifier = Modifier.weight(1f))
                                        Text(
                                            text = "SIN STOCK",
                                            color = MaterialTheme.colorScheme.error,
                                            fontWeight = FontWeight.Bold,
                                            style = MaterialTheme.typography.labelLarge
                                        )
                                    }
                                }
                            }
                        }
                    }
                }

                // Cost Breakdown
                item {
                    item.costBreakdown?.let { breakdown ->
                        CostResultCard(breakdown, item.salePrice)
                    }
                    Spacer(modifier = Modifier.height(16.dp))
                }

                // Notes Header
                item {
                    Text(
                        text = "Notas y Bitácora",
                        style = MaterialTheme.typography.titleLarge,
                        modifier = Modifier.padding(vertical = 8.dp)
                    )
                }

                // Add Note Input Area
                if (state.isAddingNote) {
                    item {
                        Card(
                            modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp),
                            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
                        ) {
                            Column(modifier = Modifier.padding(16.dp)) {
                                Text("Nueva Nota", style = MaterialTheme.typography.titleMedium)
                                Spacer(modifier = Modifier.height(8.dp))
                                OutlinedTextField(
                                    value = state.noteTitleInput,
                                    onValueChange = { viewModel.updateNoteTitle(it) },
                                    label = { Text("Título") },
                                    modifier = Modifier.fillMaxWidth()
                                )
                                Spacer(modifier = Modifier.height(8.dp))
                                OutlinedTextField(
                                    value = state.noteContentInput,
                                    onValueChange = { viewModel.updateNoteContent(it) },
                                    label = { Text("Contenido") },
                                    modifier = Modifier.fillMaxWidth(),
                                    minLines = 3
                                )
                                Spacer(modifier = Modifier.height(16.dp))
                                Row(horizontalArrangement = androidx.compose.foundation.layout.Arrangement.End, modifier = Modifier.fillMaxWidth()) {
                                    androidx.compose.material3.TextButton(onClick = { viewModel.toggleAddNote() }) {
                                        Text("Cancelar")
                                    }
                                    androidx.compose.material3.Button(onClick = { viewModel.saveNote() }) {
                                        Text("Guardar Nota")
                                    }
                                }
                            }
                        }
                    }
                }

                // Notes List
                items(state.notes) { note ->
                    NoteItem(note, onDelete = { viewModel.deleteNote(note) }, dateParams = viewModel::formatDate)
                    Spacer(modifier = Modifier.height(8.dp))
                }
            }
        }
    }
}

@Composable
fun NoteItem(note: ItemNote, onDelete: () -> Unit, dateParams: (Long) -> String) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = note.title,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.weight(1f)
                )
                IconButton(onClick = onDelete) {
                    Icon(Icons.Default.Delete, contentDescription = "Borrar nota", tint = MaterialTheme.colorScheme.error)
                }
            }
            Text(
                text = dateParams(note.timestamp),
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.secondary
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = note.content,
                style = MaterialTheme.typography.bodyMedium
            )
        }
    }
}
