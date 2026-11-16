package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.SampleData
import com.example.model.*
import com.example.ui.components.AppDestination
import com.example.ui.theme.*

@Composable
fun HomeScreen(
    currentRole: UserRole,
    currentLanguage: AppLanguage,
    selectedDistrict: String,
    listings: List<CropListing>,
    onNavigate: (AppDestination) -> Unit,
    onRequestBuy: (CropListing) -> Unit,
    onCreateListing: () -> Unit
) {
    val isHindi = currentLanguage == AppLanguage.HINDI
    val todayWeather = SampleData.weatherForecast.first()

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(AgriBackground),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Welcome Card with Role Badge
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = AgriGreenContainer),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        val greeting = when (currentRole) {
                            UserRole.FARMER -> AppStrings.t("greeting_farmer", currentLanguage)
                            UserRole.BUYER -> AppStrings.t("greeting_buyer", currentLanguage)
                            UserRole.ADMIN -> AppStrings.t("greeting_admin", currentLanguage)
                        }
                        val userSubtitle = when (currentRole) {
                            UserRole.FARMER -> if (isHindi) "आज का मंडी भाव उत्तम है। अपनी उपज सीधे बेचें!" else "Mandi demand is high today. List produce directly!"
                            UserRole.BUYER -> if (isHindi) "सत्यापित किसानों से उत्तम गुणवत्ता की सीधी खरीद करें।" else "Direct sourcing from verified farmers at fair prices."
                            UserRole.ADMIN -> if (isHindi) "मंच पर 1,280 किसान व 340 खरीदार सक्रिय हैं।" else "1,280 active farmers & 340 verified trade buyers."
                        }
                        Text(
                            text = greeting,
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            color = AgriOnGreenContainer
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = userSubtitle,
                            fontSize = 13.sp,
                            color = AgriTextSecondary
                        )
                    }
                    Box(
                        modifier = Modifier
                            .size(46.dp)
                            .clip(CircleShape)
                            .background(AgriGreenPrimary),
                        contentAlignment = Alignment.Center
                    ) {
                        val icon = when (currentRole) {
                            UserRole.FARMER -> Icons.Filled.Agriculture
                            UserRole.BUYER -> Icons.Filled.BusinessCenter
                            UserRole.ADMIN -> Icons.Filled.Security
                        }
                        Icon(icon, contentDescription = null, tint = Color.White)
                    }
                }
            }
        }

        // Today's Live Weather Card
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Filled.WbSunny,
                                contentDescription = null,
                                tint = Color(0xFFF57F17),
                                modifier = Modifier.size(24.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "${AppStrings.t("weather_today", currentLanguage)} ($selectedDistrict)",
                                fontWeight = FontWeight.Bold,
                                fontSize = 15.sp,
                                color = AgriTextPrimary
                            )
                        }
                        TextButton(
                            onClick = { onNavigate(AppDestination.ADVISORY) },
                            contentPadding = PaddingValues(0.dp)
                        ) {
                            Text(
                                text = if (isHindi) "5-दिवसीय" else "5-Day >",
                                color = AgriGreenPrimary,
                                fontSize = 13.sp,
                                fontWeight = FontWeight.SemiBold
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Row(verticalAlignment = Alignment.Bottom) {
                            Text(
                                text = "${todayWeather.tempMax}°C",
                                fontSize = 28.sp,
                                fontWeight = FontWeight.ExtraBold,
                                color = AgriTextPrimary
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = "/ ${todayWeather.tempMin}°C",
                                fontSize = 16.sp,
                                color = AgriTextMuted,
                                modifier = Modifier.padding(bottom = 3.dp)
                            )
                        }

                        Surface(
                            shape = RoundedCornerShape(20.dp),
                            color = Color(0xFFE3F2FD)
                        ) {
                            Row(
                                modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(
                                    Icons.Filled.WaterDrop,
                                    contentDescription = null,
                                    tint = Color(0xFF1976D2),
                                    modifier = Modifier.size(14.dp)
                                )
                                Spacer(modifier = Modifier.width(4.dp))
                                Text(
                                    text = "${todayWeather.rainChance}% ${if (isHindi) "बारिश" else "Rain"}",
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Medium,
                                    color = Color(0xFF1976D2)
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = if (isHindi) "💡 सलाह: ${todayWeather.advisoryNoteHi}" else "💡 Advisory: ${todayWeather.advisoryNote}",
                        fontSize = 12.sp,
                        color = Color(0xFF2E7D32),
                        fontWeight = FontWeight.Medium
                    )
                }
            }
        }

        // Live APMC Mandi Ticker
        item {
            Column {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = AppStrings.t("top_mandi_rates", currentLanguage),
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp,
                        color = AgriTextPrimary
                    )
                    TextButton(onClick = { onNavigate(AppDestination.MANDI) }) {
                        Text(
                            text = AppStrings.t("view_all", currentLanguage),
                            color = AgriGreenPrimary,
                            fontWeight = FontWeight.Bold,
                            fontSize = 13.sp
                        )
                    }
                }

                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    items(SampleData.mandiPrices) { mandi ->
                        Card(
                            modifier = Modifier
                                .width(155.dp)
                                .clickable { onNavigate(AppDestination.MANDI) },
                            shape = RoundedCornerShape(12.dp),
                            colors = CardDefaults.cardColors(containerColor = Color.White),
                            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                        ) {
                            Column(modifier = Modifier.padding(12.dp)) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text(
                                        text = if (isHindi) mandi.cropHi else mandi.crop,
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 14.sp
                                    )
                                    val isPositive = mandi.changePercent >= 0
                                    Text(
                                        text = "${if (isPositive) "+" else ""}${mandi.changePercent}%",
                                        fontSize = 11.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = if (isPositive) AgriSuccess else AgriError
                                    )
                                }
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(
                                    text = "₹${mandi.modalPrice}",
                                    fontSize = 18.sp,
                                    fontWeight = FontWeight.ExtraBold,
                                    color = AgriGreenPrimary
                                )
                                Text(
                                    text = "/ ${AppStrings.t("quintals", currentLanguage)}",
                                    fontSize = 10.sp,
                                    color = AgriTextMuted
                                )
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(
                                    text = mandi.mandiName,
                                    fontSize = 11.sp,
                                    color = AgriTextSecondary,
                                    maxLines = 1
                                )
                            }
                        }
                    }
                }
            }
        }

        // Quick Actions 6-Grid
        item {
            Column {
                Text(
                    text = AppStrings.t("quick_actions", currentLanguage),
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp,
                    color = AgriTextPrimary
                )
                Spacer(modifier = Modifier.height(10.dp))

                val actions = listOf(
                    QuickActionItem(
                        title = AppStrings.t("action_sell", currentLanguage),
                        icon = Icons.Filled.AddBusiness,
                        color = Color(0xFF2E7D32),
                        tag = "quick_sell",
                        onClick = {
                            if (currentRole == UserRole.FARMER) onCreateListing()
                            else onNavigate(AppDestination.MARKETPLACE)
                        }
                    ),
                    QuickActionItem(
                        title = AppStrings.t("action_prices", currentLanguage),
                        icon = Icons.Filled.TrendingUp,
                        color = Color(0xFF1565C0),
                        tag = "quick_prices",
                        onClick = { onNavigate(AppDestination.MANDI) }
                    ),
                    QuickActionItem(
                        title = AppStrings.t("action_scan", currentLanguage),
                        icon = Icons.Filled.Biotech,
                        color = Color(0xFF6A1B9A),
                        tag = "quick_scan",
                        onClick = { onNavigate(AppDestination.SCANNER) }
                    ),
                    QuickActionItem(
                        title = AppStrings.t("action_ai_advisor", currentLanguage),
                        icon = Icons.Filled.Psychology,
                        color = Color(0xFFD84315),
                        tag = "quick_advisory",
                        onClick = { onNavigate(AppDestination.ADVISORY) }
                    ),
                    QuickActionItem(
                        title = AppStrings.t("action_store", currentLanguage),
                        icon = Icons.Filled.ShoppingBag,
                        color = Color(0xFF00838F),
                        tag = "quick_store",
                        onClick = { onNavigate(AppDestination.STORE) }
                    ),
                    QuickActionItem(
                        title = AppStrings.t("action_community", currentLanguage),
                        icon = Icons.Filled.Forum,
                        color = Color(0xFF4527A0),
                        tag = "quick_community",
                        onClick = { onNavigate(AppDestination.COMMUNITY) }
                    )
                )

                // 2 rows of 3 buttons
                for (row in actions.chunked(3)) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 8.dp),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        for (item in row) {
                            Card(
                                modifier = Modifier
                                    .weight(1f)
                                    .clickable { item.onClick() }
                                    .testTag(item.tag),
                                shape = RoundedCornerShape(12.dp),
                                colors = CardDefaults.cardColors(containerColor = Color.White),
                                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                            ) {
                                Column(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(vertical = 12.dp, horizontal = 4.dp),
                                    horizontalAlignment = Alignment.CenterHorizontally
                                ) {
                                    Box(
                                        modifier = Modifier
                                            .size(40.dp)
                                            .clip(CircleShape)
                                            .background(item.color.copy(alpha = 0.12f)),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Icon(
                                            imageVector = item.icon,
                                            contentDescription = item.title,
                                            tint = item.color,
                                            modifier = Modifier.size(22.dp)
                                        )
                                    }
                                    Spacer(modifier = Modifier.height(6.dp))
                                    Text(
                                        text = item.title,
                                        fontSize = 11.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = AgriTextPrimary,
                                        maxLines = 1
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }

        // Available Produce Lots
        item {
            Column {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = if (isHindi) "उपलब्ध ताज़ा फसल लॉट" else "Available Fresh Crop Lots",
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp,
                        color = AgriTextPrimary
                    )
                    TextButton(onClick = { onNavigate(AppDestination.MARKETPLACE) }) {
                        Text(
                            text = AppStrings.t("view_all", currentLanguage),
                            color = AgriGreenPrimary,
                            fontWeight = FontWeight.Bold,
                            fontSize = 13.sp
                        )
                    }
                }

                listings.take(3).forEach { listing ->
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 10.dp),
                        shape = RoundedCornerShape(14.dp),
                        colors = CardDefaults.cardColors(containerColor = Color.White),
                        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                    ) {
                        Column(modifier = Modifier.padding(14.dp)) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.Top
                            ) {
                                Column(modifier = Modifier.weight(1f)) {
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        Text(
                                            text = if (isHindi) listing.cropNameHi else listing.cropName,
                                            fontSize = 17.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = AgriTextPrimary
                                        )
                                        if (listing.isOrganic) {
                                            Spacer(modifier = Modifier.width(6.dp))
                                            Surface(
                                                color = Color(0xFFE8F5E9),
                                                shape = RoundedCornerShape(4.dp)
                                            ) {
                                                Text(
                                                    text = if (isHindi) "जैविक" else "Organic",
                                                    fontSize = 10.sp,
                                                    fontWeight = FontWeight.Bold,
                                                    color = AgriGreenPrimary,
                                                    modifier = Modifier.padding(horizontal = 4.dp, vertical = 2.dp)
                                                )
                                            }
                                        }
                                    }
                                    Text(
                                        text = "${listing.variety} • ${listing.qualityGrade}",
                                        fontSize = 12.sp,
                                        color = AgriTextSecondary
                                    )
                                    Text(
                                        text = "📍 ${listing.village}, ${listing.district}",
                                        fontSize = 12.sp,
                                        color = AgriTextMuted
                                    )
                                }

                                Column(horizontalAlignment = Alignment.End) {
                                    Text(
                                        text = "₹${listing.expectedPricePerQuintal.toInt()}",
                                        fontSize = 18.sp,
                                        fontWeight = FontWeight.ExtraBold,
                                        color = AgriGreenPrimary
                                    )
                                    Text(
                                        text = "/ ${AppStrings.t("quintals", currentLanguage)}",
                                        fontSize = 10.sp,
                                        color = AgriTextMuted
                                    )
                                    Surface(
                                        color = Color(0xFFF1F5EB),
                                        shape = RoundedCornerShape(4.dp),
                                        modifier = Modifier.padding(top = 4.dp)
                                    ) {
                                        Text(
                                            text = "${listing.quantityQuintals.toInt()} Qtl",
                                            fontSize = 11.sp,
                                            fontWeight = FontWeight.SemiBold,
                                            color = AgriTextPrimary,
                                            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                        )
                                    }
                                }
                            }

                            Spacer(modifier = Modifier.height(10.dp))
                            Divider(color = AgriOutlineVariant)
                            Spacer(modifier = Modifier.height(8.dp))

                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Text(
                                        text = "👤 ${listing.farmerName}",
                                        fontSize = 12.sp,
                                        color = AgriTextSecondary
                                    )
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Text(
                                        text = "★ ${listing.farmerRating}",
                                        fontSize = 11.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = Color(0xFFF57F17)
                                    )
                                }

                                Button(
                                    onClick = { onRequestBuy(listing) },
                                    colors = ButtonDefaults.buttonColors(containerColor = AgriGreenPrimary),
                                    shape = RoundedCornerShape(8.dp),
                                    contentPadding = PaddingValues(horizontal = 14.dp, vertical = 6.dp),
                                    modifier = Modifier
                                        .height(34.dp)
                                        .testTag("home_buy_btn_${listing.id}")
                                ) {
                                    Text(
                                        text = AppStrings.t("send_buy_request", currentLanguage),
                                        fontSize = 12.sp,
                                        fontWeight = FontWeight.Bold
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

data class QuickActionItem(
    val title: String,
    val icon: ImageVector,
    val color: Color,
    val tag: String,
    val onClick: () -> Unit
)
