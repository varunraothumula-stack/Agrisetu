package com.example.ui.screens

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.SampleData
import com.example.model.AppLanguage
import com.example.model.AppStrings
import com.example.model.MandiPrice
import com.example.ui.theme.*

@Composable
fun MandiPricesScreen(
    currentLanguage: AppLanguage,
    selectedDistrict: String
) {
    val isHindi = currentLanguage == AppLanguage.HINDI
    val allPrices = SampleData.mandiPrices

    var selectedCropFilter by remember { mutableStateOf("All") }
    var selectedMandiItem by remember { mutableStateOf(allPrices.first()) }

    val cropFilters = listOf("All", "Wheat", "Onion", "Tomato", "Rice", "Cotton", "Potato", "Maize")

    val filteredList = remember(selectedCropFilter) {
        if (selectedCropFilter == "All") allPrices
        else allPrices.filter { it.crop.equals(selectedCropFilter, ignoreCase = true) }
    }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(AgriBackground),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Title & Header Summary
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
                        Column {
                            Text(
                                text = AppStrings.t("mandi_tracker_title", currentLanguage),
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Bold,
                                color = AgriTextPrimary
                            )
                            Text(
                                text = if (isHindi) "सरकार-मान्यता प्राप्त APMC मंडियों के सीधे लाइव भाव" else "Direct live APMC rates from AGMARKNET network",
                                fontSize = 12.sp,
                                color = AgriTextSecondary
                            )
                        }
                        Icon(
                            imageVector = Icons.Filled.Verified,
                            contentDescription = "Verified APMC",
                            tint = AgriGreenPrimary,
                            modifier = Modifier.size(24.dp)
                        )
                    }
                }
            }
        }

        // 7-Day Interactive Canvas Trend Chart
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
                        Column {
                            Text(
                                text = "${if (isHindi) selectedMandiItem.cropHi else selectedMandiItem.crop} (${selectedMandiItem.mandiName})",
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold,
                                color = AgriTextPrimary
                            )
                            Text(
                                text = AppStrings.t("seven_day_trend", currentLanguage),
                                fontSize = 12.sp,
                                color = AgriTextMuted
                            )
                        }

                        Column(horizontalAlignment = Alignment.End) {
                            Text(
                                text = "₹${selectedMandiItem.modalPrice}",
                                fontSize = 20.sp,
                                fontWeight = FontWeight.ExtraBold,
                                color = AgriGreenPrimary
                            )
                            val isPos = selectedMandiItem.changePercent >= 0
                            Text(
                                text = "${if (isPos) "+" else ""}${selectedMandiItem.changePercent}% (24h)",
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                color = if (isPos) AgriSuccess else AgriError
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    // Compose Canvas 7-Day Line Chart
                    val trend = selectedMandiItem.weeklyTrend
                    if (trend.isNotEmpty()) {
                        val minVal = (trend.minOrNull() ?: 1000) - 50
                        val maxVal = (trend.maxOrNull() ?: 3000) + 50
                        val valRange = (maxVal - minVal).toFloat().coerceAtLeast(1f)

                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(140.dp)
                                .background(AgriSurfaceVariant.copy(alpha = 0.5f), RoundedCornerShape(8.dp))
                                .padding(8.dp)
                        ) {
                            Canvas(modifier = Modifier.fillMaxSize()) {
                                val width = size.width
                                val height = size.height
                                val points = trend.mapIndexed { index, price ->
                                    val x = (index / (trend.size - 1).toFloat()) * width
                                    val y = height - ((price - minVal) / valRange) * height
                                    Offset(x, y)
                                }

                                // Fill path under line
                                val fillPath = Path().apply {
                                    moveTo(points.first().x, points.first().y)
                                    points.drop(1).forEach { lineTo(it.x, it.y) }
                                    lineTo(width, height)
                                    lineTo(0f, height)
                                    close()
                                }
                                drawPath(
                                    path = fillPath,
                                    brush = Brush.verticalGradient(
                                        colors = listOf(
                                            AgriGreenLight.copy(alpha = 0.35f),
                                            AgriGreenLight.copy(alpha = 0.05f)
                                        )
                                    )
                                )

                                // Stroke line
                                val strokePath = Path().apply {
                                    moveTo(points.first().x, points.first().y)
                                    points.drop(1).forEach { lineTo(it.x, it.y) }
                                }
                                drawPath(
                                    path = strokePath,
                                    color = AgriGreenPrimary,
                                    style = Stroke(width = 5f)
                                )

                                // Data points dots
                                points.forEach { pt ->
                                    drawCircle(
                                        color = Color.White,
                                        radius = 6f,
                                        center = pt
                                    )
                                    drawCircle(
                                        color = AgriGreenPrimary,
                                        radius = 4f,
                                        center = pt
                                    )
                                }
                            }
                        }

                        // Day markers below chart
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(top = 6.dp),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            listOf("D-6", "D-5", "D-4", "D-3", "D-2", "Yesterday", "Today").forEach { day ->
                                Text(text = day, fontSize = 10.sp, color = AgriTextMuted)
                            }
                        }
                    }
                }
            }
        }

        // Filter chips row
        item {
            Column {
                Text(
                    text = if (isHindi) "फसल अनुसार फ़िल्टर करें" else "Filter Mandi by Crop",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    color = AgriTextPrimary
                )
                Spacer(modifier = Modifier.height(8.dp))
                LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    items(cropFilters) { crop ->
                        val isSelected = selectedCropFilter == crop
                        FilterChip(
                            selected = isSelected,
                            onClick = { selectedCropFilter = crop },
                            label = {
                                Text(
                                    text = when (crop) {
                                        "All" -> AppStrings.t("filter_all", currentLanguage)
                                        "Wheat" -> if (isHindi) "गेहूं" else "Wheat"
                                        "Onion" -> if (isHindi) "प्याज" else "Onion"
                                        "Tomato" -> if (isHindi) "टमाटर" else "Tomato"
                                        "Rice" -> if (isHindi) "चावल" else "Rice"
                                        "Cotton" -> if (isHindi) "कपास" else "Cotton"
                                        "Potato" -> if (isHindi) "आलू" else "Potato"
                                        "Maize" -> if (isHindi) "मक्का" else "Maize"
                                        else -> crop
                                    },
                                    fontSize = 12.sp
                                )
                            },
                            colors = FilterChipDefaults.filterChipColors(
                                selectedContainerColor = AgriGreenPrimary,
                                selectedLabelColor = Color.White
                            ),
                            modifier = Modifier.testTag("filter_mandi_$crop")
                        )
                    }
                }
            }
        }

        // Mandi Price Cards List
        items(filteredList) { item ->
            val isSelectedForChart = selectedMandiItem.id == item.id
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { selectedMandiItem = item }
                    .testTag("mandi_card_${item.id}"),
                shape = RoundedCornerShape(14.dp),
                colors = CardDefaults.cardColors(
                    containerColor = if (isSelectedForChart) AgriGreenContainer.copy(alpha = 0.6f) else Color.White
                ),
                border = if (isSelectedForChart) CardDefaults.outlinedCardBorder().copy(brush = Brush.linearGradient(listOf(AgriGreenPrimary, AgriGreenLight))) else null,
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Column(modifier = Modifier.padding(14.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text(
                                    text = if (isHindi) item.cropHi else item.crop,
                                    fontSize = 17.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = AgriTextPrimary
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Surface(
                                    color = Color(0xFFF1F5EB),
                                    shape = RoundedCornerShape(4.dp)
                                ) {
                                    Text(
                                        text = item.arrivalDate,
                                        fontSize = 10.sp,
                                        color = AgriTextSecondary,
                                        modifier = Modifier.padding(horizontal = 4.dp, vertical = 2.dp)
                                    )
                                }
                            }
                            Text(
                                text = "📍 ${item.mandiName} (${item.district}, ${item.state})",
                                fontSize = 12.sp,
                                color = AgriTextMuted
                            )
                        }

                        Column(horizontalAlignment = Alignment.End) {
                            Text(
                                text = "₹${item.modalPrice}",
                                fontSize = 20.sp,
                                fontWeight = FontWeight.ExtraBold,
                                color = AgriGreenPrimary
                            )
                            Text(
                                text = AppStrings.t("highest_modal", currentLanguage),
                                fontSize = 10.sp,
                                color = AgriTextMuted
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))
                    Divider(color = AgriOutlineVariant)
                    Spacer(modifier = Modifier.height(8.dp))

                    // Min and Max prices & 24h change
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                            Column {
                                Text(
                                    text = if (isHindi) "न्यूनतम भाव" else "Min Price",
                                    fontSize = 11.sp,
                                    color = AgriTextMuted
                                )
                                Text(
                                    text = "₹${item.minPrice}",
                                    fontSize = 13.sp,
                                    fontWeight = FontWeight.SemiBold,
                                    color = AgriTextPrimary
                                )
                            }
                            Column {
                                Text(
                                    text = if (isHindi) "अधिकतम भाव" else "Max Price",
                                    fontSize = 11.sp,
                                    color = AgriTextMuted
                                )
                                Text(
                                    text = "₹${item.maxPrice}",
                                    fontSize = 13.sp,
                                    fontWeight = FontWeight.SemiBold,
                                    color = AgriTextPrimary
                                )
                            }
                        }

                        val isPositive = item.changePercent >= 0
                        Surface(
                            color = (if (isPositive) AgriSuccess else AgriError).copy(alpha = 0.12f),
                            shape = RoundedCornerShape(6.dp)
                        ) {
                            Text(
                                text = "${if (isPositive) "▲ +" else "▼ "}${item.changePercent}% 24h",
                                color = if (isPositive) AgriSuccess else AgriError,
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                            )
                        }
                    }
                }
            }
        }
    }
}
