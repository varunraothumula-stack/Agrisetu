package com.example.ui.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.AppLanguage
import com.example.model.AppStrings
import com.example.ui.theme.AgriGreenContainer
import com.example.ui.theme.AgriGreenPrimary

enum class AppDestination(val route: String, val icon: ImageVector, val stringKey: String) {
    HOME("home", Icons.Filled.Home, "nav_home"),
    MARKETPLACE("marketplace", Icons.Filled.Storefront, "nav_marketplace"),
    MANDI("mandi", Icons.Filled.TrendingUp, "nav_mandi"),
    ADVISORY("advisory", Icons.Filled.WbSunny, "nav_advisory"),
    SCANNER("scanner", Icons.Filled.Biotech, "nav_scanner"),
    STORE("store", Icons.Filled.ShoppingBag, "nav_store"),
    COMMUNITY("community", Icons.Filled.Forum, "nav_community"),
    DASHBOARD("dashboard", Icons.Filled.Analytics, "nav_dashboard")
}

@Composable
fun AppBottomBar(
    currentDestination: AppDestination,
    currentLanguage: AppLanguage,
    onDestinationSelected: (AppDestination) -> Unit
) {
    // Primary tabs for mobile navigation bar
    val mainTabs = listOf(
        AppDestination.HOME,
        AppDestination.MARKETPLACE,
        AppDestination.MANDI,
        AppDestination.ADVISORY,
        AppDestination.SCANNER,
        AppDestination.STORE,
        AppDestination.DASHBOARD
    )

    NavigationBar(
        containerColor = Color.White,
        tonalElevation = 8.dp
    ) {
        mainTabs.forEach { destination ->
            val selected = currentDestination == destination
            NavigationBarItem(
                selected = selected,
                onClick = { onDestinationSelected(destination) },
                icon = {
                    Icon(
                        imageVector = destination.icon,
                        contentDescription = AppStrings.t(destination.stringKey, currentLanguage)
                    )
                },
                label = {
                    Text(
                        text = AppStrings.t(destination.stringKey, currentLanguage),
                        fontSize = 10.sp,
                        fontWeight = if (selected) FontWeight.Bold else FontWeight.Normal,
                        maxLines = 1
                    )
                },
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = AgriGreenPrimary,
                    selectedTextColor = AgriGreenPrimary,
                    indicatorColor = AgriGreenContainer,
                    unselectedIconColor = Color(0xFF687364),
                    unselectedTextColor = Color(0xFF687364)
                ),
                modifier = Modifier.testTag("nav_item_${destination.route}")
            )
        }
    }
}
