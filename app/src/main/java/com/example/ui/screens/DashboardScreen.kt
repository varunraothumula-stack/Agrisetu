package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.*
import com.example.ui.theme.*

@Composable
fun DashboardScreen(
    currentRole: UserRole,
    currentLanguage: AppLanguage,
    listings: List<CropListing>,
    buyRequests: List<BuyRequest>,
    ordersCount: Int,
    totalCommission: Double,
    onRoleSelected: (UserRole) -> Unit,
    onOpenBusinessModel: () -> Unit
) {
    val isHindi = currentLanguage == AppLanguage.HINDI

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(AgriBackground),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Title & Role switcher
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
                                text = AppStrings.t("dashboard_title", currentLanguage),
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Bold,
                                color = AgriTextPrimary
                            )
                            Text(
                                text = "${AppStrings.t("switch_role", currentLanguage)}: ${if (isHindi) currentRole.labelHi else currentRole.labelEn}",
                                fontSize = 12.sp,
                                color = AgriTextSecondary
                            )
                        }

                        // Toggle role buttons for presentation demo
                        Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                            UserRole.values().forEach { role ->
                                val isSelected = currentRole == role
                                Surface(
                                    color = if (isSelected) AgriGreenPrimary else AgriSurfaceVariant,
                                    shape = RoundedCornerShape(8.dp),
                                    modifier = Modifier.testTag("dash_role_${role.name}")
                                ) {
                                    TextButton(
                                        onClick = { onRoleSelected(role) },
                                        contentPadding = PaddingValues(horizontal = 8.dp, vertical = 2.dp)
                                    ) {
                                        Text(
                                            text = when (role) {
                                                UserRole.FARMER -> "Farmer"
                                                UserRole.BUYER -> "Buyer"
                                                UserRole.ADMIN -> "Admin"
                                            },
                                            fontSize = 11.sp,
                                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                                            color = if (isSelected) Color.White else AgriTextPrimary
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }

        // Role-Specific Metrics
        when (currentRole) {
            UserRole.FARMER -> {
                // Farmer Metrics
                item {
                    Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                        Text(
                            text = if (isHindi) "किसान व्यापार आँकड़े" else "Farmer Performance Metrics",
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Bold,
                            color = AgriTextPrimary
                        )

                        val activeListingsCount = listings.count { it.status == ListingStatus.ACTIVE || it.status == ListingStatus.NEGOTIATING }
                        val activeRequestsCount = buyRequests.count { it.status == RequestStatus.PENDING || it.status == RequestStatus.COUNTER_OFFER }
                        val estimatedRevenue = listings.sumOf { it.expectedPricePerQuintal * it.quantityQuintals }

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            MetricCard(
                                title = if (isHindi) "सक्रिय फसल लॉट" else "Active Listings",
                                value = "$activeListingsCount",
                                subtitle = "Lots on market",
                                icon = Icons.Filled.Inventory,
                                color = AgriGreenPrimary,
                                modifier = Modifier.weight(1f)
                            )
                            MetricCard(
                                title = if (isHindi) "आवक खरीद प्रस्ताव" else "Buy Inquiries",
                                value = "$activeRequestsCount",
                                subtitle = "Ready for negotiation",
                                icon = Icons.Filled.LocalOffer,
                                color = AgriAmberSecondary,
                                modifier = Modifier.weight(1f)
                            )
                        }

                        MetricCard(
                            title = if (isHindi) "अपेक्षित फसल राजस्व" else "Estimated Inventory Revenue",
                            value = "₹${estimatedRevenue.toInt()}",
                            subtitle = if (isHindi) "पारंपरिक बिचौलियों की तुलना में 18% अधिक मूल्य" else "+18% direct farmer realization vs local middlemen",
                            icon = Icons.Filled.AccountBalanceWallet,
                            color = Color(0xFF2E7D32),
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                }

                // Farmer Listings Breakdown
                item {
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(14.dp),
                        colors = CardDefaults.cardColors(containerColor = Color.White),
                        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                    ) {
                        Column(modifier = Modifier.padding(14.dp)) {
                            Text(
                                text = if (isHindi) "फसल मूल्य संवर्धन (Direct vs Middleman)" else "Value Realization: Direct vs Middlemen",
                                fontSize = 15.sp,
                                fontWeight = FontWeight.Bold,
                                color = AgriTextPrimary
                            )
                            Spacer(modifier = Modifier.height(10.dp))
                            listOf(
                                Triple("Onion (Nashik Red)", "₹1,950 direct", "₹1,600 middleman (+21%)"),
                                Triple("Wheat (Sharbati)", "₹2,480 direct", "₹2,150 middleman (+15%)"),
                                Triple("Tomato (Hybrid)", "₹2,200 direct", "₹1,800 middleman (+22%)")
                            ).forEach { item ->
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(vertical = 4.dp),
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Text(item.first, fontSize = 13.sp, fontWeight = FontWeight.SemiBold)
                                    Column(horizontalAlignment = Alignment.End) {
                                        Text(item.second, fontSize = 12.sp, fontWeight = FontWeight.Bold, color = AgriGreenPrimary)
                                        Text(item.third, fontSize = 11.sp, color = AgriTextMuted)
                                    }
                                }
                                Divider(color = AgriOutlineVariant)
                            }
                        }
                    }
                }
            }

            UserRole.BUYER -> {
                // Buyer / Trader Metrics
                item {
                    Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                        Text(
                            text = if (isHindi) "खरीदार खरीद डैशबोर्ड" else "Procurement & Sourcing Dashboard",
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Bold,
                            color = AgriTextPrimary
                        )

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            MetricCard(
                                title = if (isHindi) "भेजे गए प्रस्ताव" else "Purchase Offers",
                                value = "${buyRequests.size}",
                                subtitle = "Across 3 mandis",
                                icon = Icons.Filled.Send,
                                color = Color(0xFF1976D2),
                                modifier = Modifier.weight(1f)
                            )
                            MetricCard(
                                title = if (isHindi) "स्वीकृत सौदे" else "Deals Locked",
                                value = "${buyRequests.count { it.status == RequestStatus.ACCEPTED }}",
                                subtitle = "Ready for dispatch",
                                icon = Icons.Filled.CheckCircle,
                                color = AgriGreenPrimary,
                                modifier = Modifier.weight(1f)
                            )
                        }

                        MetricCard(
                            title = if (isHindi) "अनुमानित खरीद बचत" else "Procurement Cost Savings",
                            value = "₹1,42,800",
                            subtitle = if (isHindi) "सीधे खेत से खरीद कर बिचौलियों का 12% कमीशन बचाया" else "Disintermediated 12% secondary mandi brokerage fees",
                            icon = Icons.Filled.Savings,
                            color = Color(0xFFE65100),
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                }
            }

            UserRole.ADMIN -> {
                // Admin Platform Metrics
                item {
                    Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                        Text(
                            text = if (isHindi) "एग्रीसेतु प्लेटफ़ॉर्म ऑपरेशन्स" else "Platform Operations & Monetization",
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Bold,
                            color = AgriTextPrimary
                        )

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            MetricCard(
                                title = if (isHindi) "सत्यापित किसान" else "Total Farmers",
                                value = "1,280",
                                subtitle = "KYC verified",
                                icon = Icons.Filled.People,
                                color = AgriGreenPrimary,
                                modifier = Modifier.weight(1f)
                            )
                            MetricCard(
                                title = if (isHindi) "व्यापारी / खरीदार" else "Verified Buyers",
                                value = "340",
                                subtitle = "Mandi licensed",
                                icon = Icons.Filled.Store,
                                color = Color(0xFF1976D2),
                                modifier = Modifier.weight(1f)
                            )
                        }

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            MetricCard(
                                title = if (isHindi) "5% कमीशन राजस्व" else "5% Trade Comm.",
                                value = "₹${totalCommission.toInt()}",
                                subtitle = "From closed trade deals",
                                icon = Icons.Filled.MonetizationOn,
                                color = AgriAmberSecondary,
                                modifier = Modifier.weight(1f)
                            )
                            MetricCard(
                                title = if (isHindi) "कुल ऑर्डर / सौदे" else "Orders Fulfilled",
                                value = "$ordersCount",
                                subtitle = "With escrow lock",
                                icon = Icons.Filled.ReceiptLong,
                                color = Color(0xFF6A1B9A),
                                modifier = Modifier.weight(1f)
                            )
                        }
                    }
                }

                // Top Selling Crops Breakdown
                item {
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(14.dp),
                        colors = CardDefaults.cardColors(containerColor = Color.White),
                        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                    ) {
                        Column(modifier = Modifier.padding(14.dp)) {
                            Text(
                                text = if (isHindi) "शीर्ष व्यापार की जाने वाली फसलें (Marketplace Volume)" else "Top Traded Commodities (By Volume)",
                                fontSize = 15.sp,
                                fontWeight = FontWeight.Bold,
                                color = AgriTextPrimary
                            )
                            Spacer(modifier = Modifier.height(10.dp))
                            listOf(
                                Pair("Wheat (Sharbati & Lokwan)", "4,200 Quintals • ₹1.02 Cr"),
                                Pair("Onion (Nashik Red Garwa)", "3,800 Quintals • ₹72 Lakhs"),
                                Pair("Basmati Rice (Pusa 1121)", "2,400 Quintals • ₹91 Lakhs"),
                                Pair("Cotton (Bt Shankar-6)", "1,900 Quintals • ₹1.41 Cr")
                            ).forEach { commodity ->
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(vertical = 4.dp),
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Text(commodity.first, fontSize = 13.sp, fontWeight = FontWeight.SemiBold)
                                    Text(commodity.second, fontSize = 12.sp, color = AgriGreenPrimary, fontWeight = FontWeight.Bold)
                                }
                                Divider(color = AgriOutlineVariant)
                            }
                        }
                    }
                }
            }
        }

        // Link to Course Business Model Presentation
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = AgriGreenContainer),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = "🎓 B.Tech Business Model & Case Study",
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold,
                                color = AgriOnGreenContainer
                            )
                            Spacer(modifier = Modifier.height(2.dp))
                            Text(
                                text = if (isHindi) "समस्या विश्लेषण, 4 राजस्व स्रोत व किसान प्रशंसापत्र देखें" else "Problem breakdown, 4 revenue streams & farmer/buyer testimonials",
                                fontSize = 12.sp,
                                color = AgriTextSecondary
                            )
                        }

                        Button(
                            onClick = onOpenBusinessModel,
                            colors = ButtonDefaults.buttonColors(containerColor = AgriGreenPrimary),
                            shape = RoundedCornerShape(10.dp),
                            modifier = Modifier.testTag("open_business_model_btn")
                        ) {
                            Text(if (isHindi) "देखें" else "View", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun MetricCard(
    title: String,
    value: String,
    subtitle: String,
    icon: ImageVector,
    color: Color,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = title,
                    fontSize = 12.sp,
                    color = AgriTextMuted,
                    fontWeight = FontWeight.Medium
                )
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = color,
                    modifier = Modifier.size(18.dp)
                )
            }
            Spacer(modifier = Modifier.height(6.dp))
            Text(
                text = value,
                fontSize = 20.sp,
                fontWeight = FontWeight.ExtraBold,
                color = color
            )
            Spacer(modifier = Modifier.height(2.dp))
            Text(
                text = subtitle,
                fontSize = 11.sp,
                color = AgriTextSecondary
            )
        }
    }
}
