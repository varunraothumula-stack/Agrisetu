package com.example.ui.screens

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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.*
import com.example.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MarketplaceScreen(
    currentRole: UserRole,
    currentLanguage: AppLanguage,
    listings: List<CropListing>,
    buyRequests: List<BuyRequest>,
    onRequestBuy: (CropListing) -> Unit,
    onCreateListing: () -> Unit,
    onAcceptRequest: (String) -> Unit,
    onRejectRequest: (String) -> Unit,
    onNegotiateRequest: (String) -> Unit
) {
    val isHindi = currentLanguage == AppLanguage.HINDI

    var selectedTab by remember { mutableStateOf(0) } // 0: Browse Produce, 1: Trade Requests
    var searchQuery by remember { mutableStateOf("") }
    var selectedCropFilter by remember { mutableStateOf("All") }
    var onlyOrganic by remember { mutableStateOf(false) }

    val cropFilters = listOf("All", "Wheat", "Onion", "Tomato", "Rice", "Cotton", "Potato", "Maize")

    val filteredListings = remember(listings, searchQuery, selectedCropFilter, onlyOrganic) {
        listings.filter { listing ->
            val matchesCrop = if (selectedCropFilter == "All") true else listing.cropName.equals(selectedCropFilter, ignoreCase = true)
            val matchesOrganic = if (onlyOrganic) listing.isOrganic else true
            val matchesQuery = if (searchQuery.isBlank()) true else {
                listing.cropName.contains(searchQuery, ignoreCase = true) ||
                listing.cropNameHi.contains(searchQuery, ignoreCase = true) ||
                listing.variety.contains(searchQuery, ignoreCase = true) ||
                listing.district.contains(searchQuery, ignoreCase = true) ||
                listing.village.contains(searchQuery, ignoreCase = true)
            }
            matchesCrop && matchesOrganic && matchesQuery
        }
    }

    Scaffold(
        floatingActionButton = {
            ExtendedFloatingActionButton(
                onClick = onCreateListing,
                containerColor = AgriGreenPrimary,
                contentColor = Color.White,
                icon = { Icon(Icons.Filled.Add, contentDescription = null) },
                text = {
                    Text(
                        text = AppStrings.t("create_listing", currentLanguage),
                        fontWeight = FontWeight.Bold,
                        fontSize = 14.sp
                    )
                },
                modifier = Modifier.testTag("fab_create_listing")
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .background(AgriBackground)
        ) {
            // Tab Selector: All Lots vs Trade Requests
            TabRow(
                selectedTabIndex = selectedTab,
                containerColor = Color.White,
                contentColor = AgriGreenPrimary
            ) {
                Tab(
                    selected = selectedTab == 0,
                    onClick = { selectedTab = 0 },
                    text = {
                        Text(
                            text = if (isHindi) "फसल लॉट (${listings.size})" else "Produce Lots (${listings.size})",
                            fontWeight = if (selectedTab == 0) FontWeight.Bold else FontWeight.Normal
                        )
                    },
                    modifier = Modifier.testTag("tab_produce_lots")
                )
                Tab(
                    selected = selectedTab == 1,
                    onClick = { selectedTab = 1 },
                    text = {
                        Text(
                            text = "${AppStrings.t("incoming_requests_tab", currentLanguage)} (${buyRequests.size})",
                            fontWeight = if (selectedTab == 1) FontWeight.Bold else FontWeight.Normal
                        )
                    },
                    modifier = Modifier.testTag("tab_trade_requests")
                )
            }

            if (selectedTab == 0) {
                // Browse Listings Tab
                Column(modifier = Modifier.fillMaxSize()) {
                    // Search Bar
                    OutlinedTextField(
                        value = searchQuery,
                        onValueChange = { searchQuery = it },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp, vertical = 10.dp)
                            .testTag("search_crops_input"),
                        placeholder = {
                            Text(
                                AppStrings.t("search_crop_hint", currentLanguage),
                                fontSize = 13.sp
                            )
                        },
                        leadingIcon = {
                            Icon(Icons.Filled.Search, contentDescription = null, tint = AgriGreenPrimary)
                        },
                        trailingIcon = {
                            if (searchQuery.isNotEmpty()) {
                                IconButton(onClick = { searchQuery = "" }) {
                                    Icon(Icons.Filled.Clear, contentDescription = "Clear")
                                }
                            }
                        },
                        singleLine = true,
                        shape = RoundedCornerShape(12.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedContainerColor = Color.White,
                            unfocusedContainerColor = Color.White,
                            focusedBorderColor = AgriGreenPrimary,
                            unfocusedBorderColor = AgriOutline
                        )
                    )

                    // Crop Category Filters & Organic Toggle
                    LazyRow(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp, vertical = 4.dp),
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        // Organic filter toggle chip
                        item {
                            FilterChip(
                                selected = onlyOrganic,
                                onClick = { onlyOrganic = !onlyOrganic },
                                label = {
                                    Text(
                                        text = if (isHindi) "🌱 जैविक" else "🌱 Organic",
                                        fontWeight = FontWeight.SemiBold,
                                        fontSize = 12.sp
                                    )
                                },
                                colors = FilterChipDefaults.filterChipColors(
                                    selectedContainerColor = AgriGreenContainer,
                                    selectedLabelColor = AgriGreenPrimary
                                ),
                                modifier = Modifier.testTag("chip_filter_organic")
                            )
                        }

                        items(cropFilters) { crop ->
                            val isSelected = selectedCropFilter == crop
                            val label = when (crop) {
                                "All" -> AppStrings.t("filter_all", currentLanguage)
                                "Wheat" -> if (isHindi) "गेहूं" else "Wheat"
                                "Onion" -> if (isHindi) "प्याज" else "Onion"
                                "Tomato" -> if (isHindi) "टमाटर" else "Tomato"
                                "Rice" -> if (isHindi) "चावल" else "Rice"
                                "Cotton" -> if (isHindi) "कपास" else "Cotton"
                                "Potato" -> if (isHindi) "आलू" else "Potato"
                                "Maize" -> if (isHindi) "मक्का" else "Maize"
                                else -> crop
                            }
                            FilterChip(
                                selected = isSelected,
                                onClick = { selectedCropFilter = crop },
                                label = { Text(label, fontSize = 12.sp) },
                                colors = FilterChipDefaults.filterChipColors(
                                    selectedContainerColor = AgriGreenPrimary,
                                    selectedLabelColor = Color.White
                                ),
                                modifier = Modifier.testTag("chip_crop_$crop")
                            )
                        }
                    }

                    // Listings List
                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        contentPadding = PaddingValues(16.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        if (filteredListings.isEmpty()) {
                            item {
                                Box(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(32.dp),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(
                                        text = if (isHindi) "कोई फसल नहीं मिली। नया लॉट जोड़ने के लिए '+' दबाएं।" else "No listings found matching filter. Tap '+' to list produce.",
                                        color = AgriTextMuted,
                                        fontSize = 14.sp
                                    )
                                }
                            }
                        }

                        items(filteredListings) { listing ->
                            ProduceListingCard(
                                listing = listing,
                                currentLanguage = currentLanguage,
                                onRequestBuy = { onRequestBuy(listing) }
                            )
                        }
                    }
                }
            } else {
                // Trade Requests Tab
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    item {
                        Surface(
                            color = AgriAmberContainer,
                            shape = RoundedCornerShape(12.dp),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Row(
                                modifier = Modifier.padding(12.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(
                                    imageVector = Icons.Filled.Handshake,
                                    contentDescription = null,
                                    tint = AgriAmberSecondary,
                                    modifier = Modifier.size(24.dp)
                                )
                                Spacer(modifier = Modifier.width(10.dp))
                                Text(
                                    text = if (isHindi) "5% एग्रीसेतु सुरक्षा गारंटी: सौदे की राशि सुरक्षित एस्क्रो में रहती है।" else "5% AgriSetu Fair Trade: Payments secured in escrow until weighbridge delivery.",
                                    fontSize = 12.sp,
                                    color = AgriOnAmberContainer,
                                    fontWeight = FontWeight.Medium
                                )
                            }
                        }
                    }

                    items(buyRequests) { request ->
                        TradeRequestCard(
                            request = request,
                            currentRole = currentRole,
                            currentLanguage = currentLanguage,
                            onAccept = { onAcceptRequest(request.id) },
                            onReject = { onRejectRequest(request.id) },
                            onNegotiate = { onNegotiateRequest(request.id) }
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun ProduceListingCard(
    listing: CropListing,
    currentLanguage: AppLanguage,
    onRequestBuy: () -> Unit
) {
    val isHindi = currentLanguage == AppLanguage.HINDI

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            // Status and Organic Badge Row
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    val statusColor = when (listing.status) {
                        ListingStatus.ACTIVE -> AgriSuccess
                        ListingStatus.NEGOTIATING -> AgriWarning
                        ListingStatus.SOLD -> Color(0xFF757575)
                        ListingStatus.EXPIRED -> AgriError
                    }
                    val statusText = if (isHindi) listing.status.labelHi else listing.status.labelEn
                    Surface(
                        color = statusColor.copy(alpha = 0.12f),
                        shape = RoundedCornerShape(6.dp)
                    ) {
                        Text(
                            text = "● $statusText",
                            color = statusColor,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
                        )
                    }

                    if (listing.isOrganic) {
                        Spacer(modifier = Modifier.width(6.dp))
                        Surface(
                            color = Color(0xFFE8F5E9),
                            shape = RoundedCornerShape(6.dp)
                        ) {
                            Text(
                                text = "🌱 " + (if (isHindi) "100% जैविक" else "Organic"),
                                color = AgriGreenPrimary,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
                            )
                        }
                    }
                }

                Surface(
                    color = Color(0xFFF1F5EB),
                    shape = RoundedCornerShape(6.dp)
                ) {
                    Text(
                        text = listing.qualityGrade,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = AgriTextSecondary,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Crop Name & Expected Price
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Top
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = if (isHindi) listing.cropNameHi else listing.cropName,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = AgriTextPrimary
                    )
                    Text(
                        text = listing.variety,
                        fontSize = 13.sp,
                        color = AgriTextSecondary
                    )
                }

                Column(horizontalAlignment = Alignment.End) {
                    Text(
                        text = "₹${listing.expectedPricePerQuintal.toInt()}",
                        fontSize = 22.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = AgriGreenPrimary
                    )
                    Text(
                        text = "per ${AppStrings.t("quintals", currentLanguage)}",
                        fontSize = 11.sp,
                        color = AgriTextMuted
                    )
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Details grid
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column {
                    Text(
                        text = AppStrings.t("quantity_available", currentLanguage),
                        fontSize = 11.sp,
                        color = AgriTextMuted
                    )
                    Text(
                        text = "${listing.quantityQuintals.toInt()} ${AppStrings.t("quintals", currentLanguage)}",
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold,
                        color = AgriTextPrimary
                    )
                }
                Column {
                    Text(
                        text = AppStrings.t("harvested_on", currentLanguage),
                        fontSize = 11.sp,
                        color = AgriTextMuted
                    )
                    Text(
                        text = listing.harvestDate,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Medium,
                        color = AgriTextPrimary
                    )
                }
                Column(horizontalAlignment = Alignment.End) {
                    Text(
                        text = "Village / Mandi",
                        fontSize = 11.sp,
                        color = AgriTextMuted
                    )
                    Text(
                        text = "${listing.village}, ${listing.district.split(",").first()}",
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Medium,
                        color = AgriTextPrimary
                    )
                }
            }

            if (listing.notes.isNotBlank()) {
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "📝 ${listing.notes}",
                    fontSize = 12.sp,
                    color = AgriTextSecondary,
                    lineHeight = 16.sp
                )
            }

            Spacer(modifier = Modifier.height(12.dp))
            Divider(color = AgriOutlineVariant)
            Spacer(modifier = Modifier.height(10.dp))

            // Farmer Profile & Action Button
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = listing.farmerName,
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Bold,
                            color = AgriTextPrimary
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Icon(
                            imageVector = Icons.Filled.Verified,
                            contentDescription = "Verified Farmer",
                            tint = Color(0xFF1976D2),
                            modifier = Modifier.size(15.dp)
                        )
                    }
                    Text(
                        text = "📞 ${listing.farmerContact}",
                        fontSize = 11.sp,
                        color = AgriTextMuted
                    )
                }

                Button(
                    onClick = onRequestBuy,
                    colors = ButtonDefaults.buttonColors(containerColor = AgriGreenPrimary),
                    shape = RoundedCornerShape(10.dp),
                    modifier = Modifier
                        .height(42.dp)
                        .testTag("buy_request_btn_${listing.id}")
                ) {
                    Icon(
                        imageVector = Icons.Filled.LocalOffer,
                        contentDescription = null,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = AppStrings.t("send_buy_request", currentLanguage),
                        fontWeight = FontWeight.Bold,
                        fontSize = 13.sp
                    )
                }
            }
        }
    }
}

@Composable
fun TradeRequestCard(
    request: BuyRequest,
    currentRole: UserRole,
    currentLanguage: AppLanguage,
    onAccept: () -> Unit,
    onReject: () -> Unit,
    onNegotiate: () -> Unit
) {
    val isHindi = currentLanguage == AppLanguage.HINDI

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
                        text = "Offer for: ${request.cropName}",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = AgriTextPrimary
                    )
                    Text(
                        text = "From: ${request.buyerName}",
                        fontSize = 12.sp,
                        color = AgriTextSecondary
                    )
                }

                val statusColor = when (request.status) {
                    RequestStatus.PENDING -> AgriWarning
                    RequestStatus.ACCEPTED -> AgriSuccess
                    RequestStatus.REJECTED -> AgriError
                    RequestStatus.COUNTER_OFFER -> Color(0xFF1976D2)
                }
                Surface(
                    color = statusColor.copy(alpha = 0.12f),
                    shape = RoundedCornerShape(6.dp)
                ) {
                    Text(
                        text = if (isHindi) request.status.labelHi else request.status.labelEn,
                        color = statusColor,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Pricing summary
            Surface(
                color = AgriSurfaceVariant,
                shape = RoundedCornerShape(10.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier.padding(12.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Column {
                        Text(
                            text = if (isHindi) "प्रस्तावित भाव" else "Offered Price",
                            fontSize = 11.sp,
                            color = AgriTextMuted
                        )
                        Text(
                            text = "₹${request.offeredPrice.toInt()}/Qtl",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            color = AgriGreenPrimary
                        )
                    }
                    Column {
                        Text(
                            text = if (isHindi) "मांगी गई मात्रा" else "Required Qty",
                            fontSize = 11.sp,
                            color = AgriTextMuted
                        )
                        Text(
                            text = "${request.requestedQuantity.toInt()} Qtl",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            color = AgriTextPrimary
                        )
                    }
                    Column(horizontalAlignment = Alignment.End) {
                        Text(
                            text = if (isHindi) "कुल सौदा मूल्य" else "Deal Value",
                            fontSize = 11.sp,
                            color = AgriTextMuted
                        )
                        val total = (request.offeredPrice * request.requestedQuantity).toInt()
                        Text(
                            text = "₹$total",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.ExtraBold,
                            color = AgriAmberSecondary
                        )
                    }
                }
            }

            if (request.message.isNotBlank()) {
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "💬 \"${request.message}\"",
                    fontSize = 12.sp,
                    color = AgriTextSecondary
                )
            }

            Spacer(modifier = Modifier.height(6.dp))
            Text(
                text = "📅 Pickup Target: ${request.pickupDate} • ${request.createdAt}",
                fontSize = 11.sp,
                color = AgriTextMuted
            )

            // Farmer Actions (Accept, Negotiate, Reject)
            if (request.status == RequestStatus.PENDING || request.status == RequestStatus.COUNTER_OFFER) {
                Spacer(modifier = Modifier.height(12.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    OutlinedButton(
                        onClick = onReject,
                        colors = ButtonDefaults.outlinedButtonColors(contentColor = AgriError),
                        modifier = Modifier
                            .weight(1f)
                            .height(40.dp)
                            .testTag("reject_request_btn_${request.id}")
                    ) {
                        Text(if (isHindi) "अस्वीकार" else "Decline", fontSize = 12.sp)
                    }

                    OutlinedButton(
                        onClick = onNegotiate,
                        colors = ButtonDefaults.outlinedButtonColors(contentColor = AgriAmberSecondary),
                        modifier = Modifier
                            .weight(1.2f)
                            .height(40.dp)
                            .testTag("negotiate_request_btn_${request.id}")
                    ) {
                        Text(if (isHindi) "बातचीत करें" else "Counter Offer", fontSize = 12.sp)
                    }

                    Button(
                        onClick = onAccept,
                        colors = ButtonDefaults.buttonColors(containerColor = AgriGreenPrimary),
                        modifier = Modifier
                            .weight(1.2f)
                            .height(40.dp)
                            .testTag("accept_request_btn_${request.id}")
                    ) {
                        Text(if (isHindi) "स्वीकार करें" else "Accept Deal", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                    }
                }
            }
        }
    }
}
