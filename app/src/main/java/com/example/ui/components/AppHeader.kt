package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.SampleData
import com.example.model.AppLanguage
import com.example.model.AppStrings
import com.example.model.UserRole
import com.example.ui.theme.AgriGreenLight
import com.example.ui.theme.AgriGreenPrimary

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppHeader(
    currentRole: UserRole,
    currentLanguage: AppLanguage,
    selectedDistrict: String,
    cartItemCount: Int,
    onRoleSelected: (UserRole) -> Unit,
    onLanguageToggle: () -> Unit,
    onDistrictSelected: (String) -> Unit,
    onCartClicked: () -> Unit,
    onBusinessModelClicked: () -> Unit
) {
    var showRoleMenu by remember { mutableStateOf(false) }
    var showDistrictDialog by remember { mutableStateOf(false) }

    Surface(
        modifier = Modifier.fillMaxWidth(),
        color = AgriGreenPrimary,
        tonalElevation = 4.dp
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .statusBarsPadding()
                .padding(horizontal = 16.dp, vertical = 10.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                // App Logo & Title
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.weight(1f)
                ) {
                    Box(
                        modifier = Modifier
                            .size(38.dp)
                            .clip(CircleShape)
                            .background(Color.White),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Filled.Spa,
                            contentDescription = "AgriSetu Logo",
                            tint = AgriGreenPrimary,
                            modifier = Modifier.size(24.dp)
                        )
                    }
                    Spacer(modifier = Modifier.width(10.dp))
                    Column {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = AppStrings.t("app_title", currentLanguage),
                                color = Color.White,
                                fontSize = 20.sp,
                                fontWeight = FontWeight.Bold
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Surface(
                                color = Color(0xFFFFD54F),
                                shape = RoundedCornerShape(4.dp)
                            ) {
                                Text(
                                    text = if (currentLanguage == AppLanguage.HINDI) "भारत" else "India",
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color(0xFF3E2723),
                                    modifier = Modifier.padding(horizontal = 4.dp, vertical = 2.dp)
                                )
                            }
                        }
                        Text(
                            text = AppStrings.t("app_subtitle", currentLanguage),
                            color = Color(0xFFC8E6C9),
                            fontSize = 11.sp
                        )
                    }
                }

                // Header Action Buttons
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    // Language Switcher Button [EN / हिन्दी]
                    OutlinedButton(
                        onClick = onLanguageToggle,
                        colors = ButtonDefaults.outlinedButtonColors(
                            containerColor = Color.White.copy(alpha = 0.15f),
                            contentColor = Color.White
                        ),
                        border = ButtonDefaults.outlinedButtonBorder.copy(
                            brush = androidx.compose.ui.graphics.SolidColor(Color.White.copy(alpha = 0.4f))
                        ),
                        shape = RoundedCornerShape(8.dp),
                        contentPadding = PaddingValues(horizontal = 8.dp, vertical = 4.dp),
                        modifier = Modifier
                            .height(34.dp)
                            .testTag("language_toggle_btn")
                    ) {
                        Text(
                            text = if (currentLanguage == AppLanguage.ENGLISH) "हिन्दी" else "EN",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.SemiBold
                        )
                    }

                    // Business Model Guide Icon
                    IconButton(
                        onClick = onBusinessModelClicked,
                        modifier = Modifier
                            .size(36.dp)
                            .clip(CircleShape)
                            .background(Color.White.copy(alpha = 0.15f))
                            .testTag("business_model_btn")
                    ) {
                        Icon(
                            imageVector = Icons.Filled.Info,
                            contentDescription = "Business Model",
                            tint = Color.White,
                            modifier = Modifier.size(20.dp)
                        )
                    }

                    // Cart Icon with Badge
                    IconButton(
                        onClick = onCartClicked,
                        modifier = Modifier
                            .size(36.dp)
                            .clip(CircleShape)
                            .background(Color.White.copy(alpha = 0.15f))
                            .testTag("cart_icon_btn")
                    ) {
                        BadgedBox(
                            badge = {
                                if (cartItemCount > 0) {
                                    Badge(
                                        containerColor = Color(0xFFE65100),
                                        contentColor = Color.White
                                    ) {
                                        Text("$cartItemCount", fontSize = 10.sp)
                                    }
                                }
                            }
                        ) {
                            Icon(
                                imageVector = Icons.Filled.ShoppingCart,
                                contentDescription = "Shopping Cart",
                                tint = Color.White,
                                modifier = Modifier.size(20.dp)
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Sub-bar: Active Role Selector & District Picker
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                // Role Picker Chip
                Box {
                    Surface(
                        color = Color.White.copy(alpha = 0.2f),
                        shape = RoundedCornerShape(16.dp),
                        modifier = Modifier
                            .clickable { showRoleMenu = true }
                            .testTag("role_selector_chip")
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp)
                        ) {
                            val roleIcon = when (currentRole) {
                                UserRole.FARMER -> Icons.Filled.Agriculture
                                UserRole.BUYER -> Icons.Filled.Business
                                UserRole.ADMIN -> Icons.Filled.AdminPanelSettings
                            }
                            Icon(
                                imageVector = roleIcon,
                                contentDescription = null,
                                tint = Color(0xFFFFE082),
                                modifier = Modifier.size(16.dp)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = if (currentLanguage == AppLanguage.HINDI) currentRole.labelHi else currentRole.labelEn,
                                color = Color.White,
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Medium
                            )
                            Icon(
                                imageVector = Icons.Filled.ArrowDropDown,
                                contentDescription = "Select Role",
                                tint = Color.White,
                                modifier = Modifier.size(18.dp)
                            )
                        }
                    }

                    DropdownMenu(
                        expanded = showRoleMenu,
                        onDismissRequest = { showRoleMenu = false }
                    ) {
                        UserRole.values().forEach { role ->
                            DropdownMenuItem(
                                text = {
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        val icon = when (role) {
                                            UserRole.FARMER -> Icons.Filled.Agriculture
                                            UserRole.BUYER -> Icons.Filled.Business
                                            UserRole.ADMIN -> Icons.Filled.AdminPanelSettings
                                        }
                                        Icon(icon, contentDescription = null, tint = AgriGreenLight)
                                        Spacer(modifier = Modifier.width(8.dp))
                                        Text(
                                            text = if (currentLanguage == AppLanguage.HINDI) role.labelHi else role.labelEn,
                                            fontWeight = if (role == currentRole) FontWeight.Bold else FontWeight.Normal
                                        )
                                    }
                                },
                                onClick = {
                                    onRoleSelected(role)
                                    showRoleMenu = false
                                }
                            )
                        }
                    }
                }

                // Selected District Badge
                Surface(
                    color = Color.White.copy(alpha = 0.15f),
                    shape = RoundedCornerShape(16.dp),
                    modifier = Modifier
                        .clickable { showDistrictDialog = true }
                        .testTag("district_picker_btn")
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Filled.LocationOn,
                            contentDescription = "Location",
                            tint = Color(0xFF81C784),
                            modifier = Modifier.size(14.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = selectedDistrict,
                            color = Color.White,
                            fontSize = 12.sp
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Icon(
                            imageVector = Icons.Filled.Edit,
                            contentDescription = "Change",
                            tint = Color.White.copy(alpha = 0.7f),
                            modifier = Modifier.size(12.dp)
                        )
                    }
                }
            }
        }
    }

    // District selection dialog
    if (showDistrictDialog) {
        AlertDialog(
            onDismissRequest = { showDistrictDialog = false },
            title = {
                Text(
                    text = if (currentLanguage == AppLanguage.HINDI) "अपना कृषि जिला चुनें" else "Select Agricultural District",
                    fontWeight = FontWeight.Bold
                )
            },
            text = {
                Column {
                    SampleData.districts.forEach { district ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable {
                                    onDistrictSelected(district)
                                    showDistrictDialog = false
                                }
                                .padding(vertical = 12.dp, horizontal = 8.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(
                                text = district,
                                fontSize = 15.sp,
                                fontWeight = if (district == selectedDistrict) FontWeight.Bold else FontWeight.Normal,
                                color = if (district == selectedDistrict) AgriGreenPrimary else Color.Unspecified
                            )
                            if (district == selectedDistrict) {
                                Icon(Icons.Filled.Check, contentDescription = null, tint = AgriGreenPrimary)
                            }
                        }
                    }
                }
            },
            confirmButton = {
                TextButton(onClick = { showDistrictDialog = false }) {
                    Text(if (currentLanguage == AppLanguage.HINDI) "रद्द करें" else "Close")
                }
            }
        )
    }
}
