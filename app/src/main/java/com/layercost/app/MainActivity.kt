package com.layercost.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Build
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Print
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.layercost.app.ui.screens.createitem.CreateItemScreen
import com.layercost.app.ui.screens.filaments.AddFilamentScreen
import com.layercost.app.ui.screens.filaments.FilamentsScreen
import com.layercost.app.ui.screens.home.HomeScreen
import com.layercost.app.ui.screens.printers.AddPrinterScreen
import com.layercost.app.ui.screens.printers.PrintersScreen
import com.layercost.app.ui.theme.LayerCostTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            LayerCostTheme {
                MainScreen()
            }
        }
    }
}

@Composable
fun MainScreen() {
    val navController = rememberNavController()
    // Current route to determine selected tab
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    Scaffold(
        bottomBar = {
            // Only show bottom bar for main tabs
            if (currentRoute in listOf("home", "filaments", "printers")) {
                NavigationBar {
                    NavigationBarItem(
                        icon = { Icon(Icons.Default.Home, contentDescription = "Home") },
                        label = { Text("Home") },
                        selected = currentRoute == "home",
                        onClick = {
                            if (currentRoute != "home") {
                                navController.navigate("home") {
                                    popUpTo("home") { inclusive = true }
                                }
                            }
                        }
                    )
                    NavigationBarItem(
                        icon = { Icon(Icons.Default.Build, contentDescription = "Filamentos") },
                        label = { Text("Filamentos") },
                        selected = currentRoute == "filaments",
                        onClick = {
                            if (currentRoute != "filaments") {
                                navController.navigate("filaments") {
                                    popUpTo("home")
                                    launchSingleTop = true
                                }
                            }
                        }
                    )
                    NavigationBarItem(
                        icon = { Icon(Icons.Default.Print, contentDescription = "Impresoras") },
                        label = { Text("Impresoras") },
                        selected = currentRoute == "printers",
                        onClick = {
                            if (currentRoute != "printers") {
                                navController.navigate("printers") {
                                    popUpTo("home")
                                    launchSingleTop = true
                                }
                            }
                        }
                    )
                }
            }
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = "home",
            modifier = Modifier.padding(innerPadding)
        ) {
                composable("home") {
                    HomeScreen(
                        onAddClick = { navController.navigate("create_item") }
                    )
                }
                composable("filaments") {
                    FilamentsScreen(
                        onAddClick = { navController.navigate("add_filament") }
                    )
                }
                composable("add_filament") {
                    AddFilamentScreen(
                        onNavigateBack = { navController.popBackStack() }
                    )
                }
                composable("printers") {
                    PrintersScreen(
                        onAddClick = { navController.navigate("add_printer") }
                    )
                }
                composable("add_printer") {
                    AddPrinterScreen(
                        onNavigateBack = { navController.popBackStack() }
                    )
                }
                composable("create_item") {
                    CreateItemScreen(
                        onNavigateBack = { navController.popBackStack() }
                    )
                }
            }
    }
}