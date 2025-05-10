package com.sametdundar.sportsbettingapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.Receipt
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.tooling.preview.Preview
import com.sametdundar.sportsbettingapp.presentation.canli.CanliScreen
import com.sametdundar.sportsbettingapp.presentation.bulten.BultenScreen
import com.sametdundar.sportsbettingapp.presentation.mac.MacScreen
import com.sametdundar.sportsbettingapp.presentation.favori.FavoriScreen
import com.sametdundar.sportsbettingapp.presentation.kupon.KuponScreen
import com.sametdundar.sportsbettingapp.ui.theme.SportsBettingAppTheme
import dagger.hilt.android.AndroidEntryPoint

sealed class BottomNavItem(val title: String, val icon: ImageVector) {
    object Canli : BottomNavItem("Canlı", Icons.Filled.Home)
    object Bulten : BottomNavItem("Bülten", Icons.Filled.List)
    object Mac : BottomNavItem("Maç", Icons.Filled.Star)
    object Favori : BottomNavItem("Favori", Icons.Filled.Favorite)
    object Kupon : BottomNavItem("Kupon", Icons.Filled.Receipt)
}

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            SportsBettingAppTheme {
                MainScreen()
            }
        }
    }
}

@Composable
fun MainScreen() {
    val items = listOf(
        BottomNavItem.Bulten,
        BottomNavItem.Mac,
        BottomNavItem.Kupon
    )
    var selectedIndex by remember { mutableStateOf(0) }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        bottomBar = {
            NavigationBar(
                containerColor = Color.White
            ) {
                items.forEachIndexed { index, item ->
                    NavigationBarItem(
                        icon = { Icon(item.icon, contentDescription = item.title) },
                        label = { Text(item.title) },
                        selected = selectedIndex == index,
                        onClick = { selectedIndex = index }
                    )
                }
            }
        }
    ) { innerPadding ->
        when (selectedIndex) {
            0 -> BultenScreen()
            1 -> MacScreen()
            2 -> KuponScreen()
        }
    }
}

@Preview(showBackground = true)
@Composable
fun MainScreenPreview() {
    SportsBettingAppTheme {
        MainScreen()
    }
}