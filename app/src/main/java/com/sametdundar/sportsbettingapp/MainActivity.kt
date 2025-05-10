@file:OptIn(ExperimentalMaterial3Api::class)

package com.sametdundar.sportsbettingapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Receipt
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.sametdundar.sportsbettingapp.presentation.bulten.BultenScreen
import com.sametdundar.sportsbettingapp.presentation.kupon.KuponScreen
import com.sametdundar.sportsbettingapp.presentation.eventdetail.EventDetailScreen
import com.sametdundar.sportsbettingapp.presentation.mac.MacScreen
import com.sametdundar.sportsbettingapp.ui.theme.SportsBettingAppTheme
import dagger.hilt.android.AndroidEntryPoint
import com.sametdundar.sportsbettingapp.MainViewModel
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.compose.runtime.collectAsState
import com.sametdundar.sportsbettingapp.presentation.bulten.BultenViewModel
import com.sametdundar.sportsbettingapp.presentation.bulten.BultenEvent

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            SportsBettingAppTheme {
                MainNavigation()
            }
        }
    }
}

sealed class BottomNavItem(val route: String, val label: String, val icon: ImageVector) {
    object Bulletin : BottomNavItem("bulletin", "Bülten", Icons.Default.List)
    object Matches : BottomNavItem("matches", "Maç", Icons.Default.Star)
    object Coupons : BottomNavItem("coupons", "Kupon", Icons.Default.Receipt)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainNavigation() {
    val navController = rememberNavController()
    val items = listOf(
        BottomNavItem.Bulletin,
        BottomNavItem.Matches,
        BottomNavItem.Coupons
    )
    val Yellow = Color(0xFFFFEB3B)
    val navBackStackEntry = navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry.value?.destination?.route
    val selectedIndex = items.indexOfFirst { it.route == currentRoute }.takeIf { it >= 0 } ?: 0

    // BasketManager'ı Hilt ile inject etme, onun yerine MainViewModel'i kullan
    val mainViewModel: MainViewModel = androidx.hilt.navigation.compose.hiltViewModel()
    val selectedBets by mainViewModel.selectedBets.collectAsState()
    val selectedMatchCount = selectedBets.size

    val bultenViewModel: BultenViewModel = androidx.hilt.navigation.compose.hiltViewModel()

    Scaffold(
        bottomBar = {
            NavigationBar(
                containerColor = Color.White
            ) {
                items.forEachIndexed { index, item ->
                    if (index == 1) {
                        Spacer(modifier = Modifier.width(72.dp)) // Ortada FAB için boşluk bırak
                    } else {
                        NavigationBarItem(
                            selected = selectedIndex == index,
                            onClick = {
                                if (currentRoute != item.route) {
                                    navController.navigate(item.route) {
                                        popUpTo(navController.graph.startDestinationId) {
                                            saveState = true
                                        }
                                        launchSingleTop = true
                                        restoreState = true
                                    }
                                }
                            },
                            icon = { Icon(item.icon, contentDescription = item.label) },
                            label = { Text(item.label) },
                            colors = NavigationBarItemDefaults.colors(
                                indicatorColor = Yellow,
                                selectedIconColor = Color(0xFF388E3C),
                                selectedTextColor = Color(0xFF388E3C)
                            )
                        )
                    }
                }
            }
        },
        floatingActionButton = {
            Box(
                modifier = Modifier
                    .offset(y = 60.dp)
                    .size(72.dp)
                    .clip(androidx.compose.foundation.shape.CircleShape)
                    .background(if (selectedIndex == 1) Yellow else Color.White)
                    .border(4.dp, Color(0xFF388E3C), androidx.compose.foundation.shape.CircleShape)
                    .clickable {
                        if (currentRoute != BottomNavItem.Matches.route) {
                            navController.navigate(BottomNavItem.Matches.route) {
                                popUpTo(navController.graph.startDestinationId) { saveState = true }
                                launchSingleTop = true
                                restoreState = true
                            }
                        }
                    },
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Icon(
                        BottomNavItem.Matches.icon,
                        contentDescription = "Oyun",
                        tint = Color(0xFF388E3C)
                    )
                    Text("${selectedMatchCount} Maç", color = Color(0xFF388E3C))
                }
            }
        },
        floatingActionButtonPosition = androidx.compose.material3.FabPosition.Center
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = BottomNavItem.Bulletin.route,
            modifier = Modifier.padding(innerPadding)
        ) {
            composable(BottomNavItem.Bulletin.route) {
                BultenScreen(
                    onNavigateToEventDetail = { sportKey, eventId ->
                        navController.navigate("eventDetail/$sportKey/$eventId")
                    },
                    viewModel = bultenViewModel
                )
            }
            composable(BottomNavItem.Matches.route) {
                MacScreen(onAllBetsCleared = {
                    bultenViewModel.onEvent(BultenEvent.ClearAllSelectedOdds)
                })
            }
            composable(BottomNavItem.Coupons.route) {
                KuponScreen()
            }
            composable("eventDetail/{sportKey}/{eventId}") { backStackEntry ->
                val sportKey = backStackEntry.arguments?.getString("sportKey") ?: ""
                val eventId = backStackEntry.arguments?.getString("eventId") ?: ""
                EventDetailScreen(sportKey, eventId)
            }
        }
    }
}
