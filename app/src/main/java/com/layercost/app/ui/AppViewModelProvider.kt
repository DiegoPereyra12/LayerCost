package com.layercost.app.ui

import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.CreationExtras
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.layercost.app.LayerCostApplication
import com.layercost.app.ui.screens.createitem.CreateItemViewModel
import com.layercost.app.ui.screens.home.HomeViewModel
import com.layercost.app.ui.screens.filaments.FilamentsViewModel
import com.layercost.app.ui.screens.filaments.AddFilamentViewModel
import com.layercost.app.ui.screens.printers.PrintersViewModel

object AppViewModelProvider {
    val Factory = viewModelFactory {
        initializer {
            HomeViewModel(layerCostApplication().container.warehouseRepository)
        }
        initializer {
            CreateItemViewModel(layerCostApplication().container.warehouseRepository)
        }
        initializer {
            FilamentsViewModel(layerCostApplication().container.warehouseRepository)
        }
        initializer {
            AddFilamentViewModel(layerCostApplication().container.warehouseRepository)
        }
        initializer {
            PrintersViewModel(layerCostApplication().container.warehouseRepository)
        }
    }
}

fun CreationExtras.layerCostApplication(): LayerCostApplication =
    (this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY] as LayerCostApplication)
