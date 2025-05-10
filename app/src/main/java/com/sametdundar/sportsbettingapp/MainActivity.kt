package com.sametdundar.sportsbettingapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.Receipt
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.tooling.preview.Preview
import com.sametdundar.sportsbettingapp.presentation.bulten.BultenScreen
import com.sametdundar.sportsbettingapp.presentation.mac.MacScreen
import com.sametdundar.sportsbettingapp.presentation.kupon.KuponScreen
import com.sametdundar.sportsbettingapp.ui.theme.SportsBettingAppTheme
import dagger.hilt.android.AndroidEntryPoint
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.sametdundar.sportsbettingapp.presentation.eventdetail.EventDetailScreen

sealed class BottomNavItem(val title: String, val icon: ImageVector) {
    object Bulten : BottomNavItem("Bülten", Icons.Filled.List)
    object Mac : BottomNavItem("Maç", Icons.Filled.Star)
    object Kupon : BottomNavItem("Kupon", Icons.Filled.Receipt)
}

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            SportsBettingAppTheme {
                val navController = rememberNavController()
                NavHost(navController = navController, startDestination = "main") {
                    composable("main") {
                        MainScreen(
                            onNavigateToEventDetail = { sportKey, eventId ->
                                navController.navigate("eventDetail/$sportKey/$eventId")
                            }
                        )
                    }
                    composable(
                        "eventDetail/{sportKey}/{eventId}",
                        arguments = listOf(
                            navArgument("sportKey") { type = NavType.StringType },
                            navArgument("eventId") { type = NavType.StringType }
                        )
                    ) { backStackEntry ->
                        val sportKey = backStackEntry.arguments?.getString("sportKey") ?: ""
                        val eventId = backStackEntry.arguments?.getString("eventId") ?: ""
                        EventDetailScreen(sportKey = sportKey, eventId = eventId)
                    }
                }
            }
        }
    }
}

@Composable
fun MainScreen(onNavigateToEventDetail: (String, String) -> Unit) {
    val items = listOf(
        BottomNavItem.Bulten,
        BottomNavItem.Mac,
        BottomNavItem.Kupon
    )
    var selectedIndex by remember { mutableStateOf(0) }
    val Yellow = Color(0xFFFFEB3B)

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        bottomBar = {
            NavigationBar(
                containerColor = Color.White
            ) {
                items.forEachIndexed { index, item ->
                    if (index == 1) {
                        Spacer(modifier = Modifier.width(72.dp)) // Ortada boşluk bırak
                    } else {
                        NavigationBarItem(
                            icon = { Icon(item.icon, contentDescription = item.title) },
                            label = { Text(item.title) },
                            selected = selectedIndex == index,
                            onClick = { selectedIndex = index },
                            colors = NavigationBarItemDefaults.colors(
                                indicatorColor = Yellow,
                                selectedIconColor = Color(0xFF388E3C),
                                selectedTextColor =  Color(0xFF388E3C)
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
                    .clip(CircleShape)
                    .background(if (selectedIndex == 1) Yellow else Color.White)
                    .border(4.dp, Color(0xFF388E3C), CircleShape)
                    .clickable { selectedIndex = 1 },
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text("0 Maç", color = Color(0xFF388E3C))
                    Text("0,00", color = Color(0xFF388E3C), fontWeight = FontWeight.Bold)
                }
            }
        },
        floatingActionButtonPosition = FabPosition.Center
    ) { innerPadding ->
        when (selectedIndex) {
            0 -> BultenScreen(onNavigateToEventDetail = onNavigateToEventDetail)
            1 -> MacScreen()
            2 -> KuponScreen()
        }
    }
}

@Preview(showBackground = true)
@Composable
fun MainScreenPreview() {
    SportsBettingAppTheme {
        MainScreen(onNavigateToEventDetail = { _, _ -> })
    }
}