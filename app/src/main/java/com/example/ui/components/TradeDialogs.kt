package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.*
import com.example.ui.theme.*

@Composable
fun CreateListingDialog(
    currentLanguage: AppLanguage,
    currentDistrict: String,
    onDismiss: () -> Unit,
    onSubmit: (
        cropName: String,
        variety: String,
        quantity: Double,
        price: Double,
        harvestDate: String,
        grade: String,
        isOrganic: Boolean,
        village: String,
        district: String,
        contact: String,
        notes: String
    ) -> Unit
) {
    val isHindi = currentLanguage == AppLanguage.HINDI

    var cropName by remember { mutableStateOf("Wheat") }
    var variety by remember { mutableStateOf("Sharbati Gold") }
    var quantityText by remember { mutableStateOf("100") }
    var priceText by remember { mutableStateOf("2400") }
    var harvestDate by remember { mutableStateOf("26 Oct 2026") }
    var qualityGrade by remember { mutableStateOf("A+ Premium") }
    var isOrganic by remember { mutableStateOf(false) }
    var village by remember { mutableStateOf("Kisanpur") }
    var contact by remember { mutableStateOf("+91 98765 43210") }
    var notes by remember { mutableStateOf("Sun dried, clean and bagged in 50kg bags.") }
    var errorMessage by remember { mutableStateOf<String?>(null) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(
                text = if (isHindi) "अपनी नई फसल की बिक्री सूची बनाएं" else "List Produce for Sale",
                fontWeight = FontWeight.Bold,
                fontSize = 17.sp
            )
        },
        text = {
            LazyColumn(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                item {
                    OutlinedTextField(
                        value = cropName,
                        onValueChange = { cropName = it },
                        label = { Text("Crop Name (e.g. Wheat, Tomato, Onion)") },
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true
                    )
                }
                item {
                    OutlinedTextField(
                        value = variety,
                        onValueChange = { variety = it },
                        label = { Text("Variety / Breed (किस्म)") },
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true
                    )
                }
                item {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        OutlinedTextField(
                            value = quantityText,
                            onValueChange = { quantityText = it },
                            label = { Text("Qty (Quintals)") },
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                            modifier = Modifier.weight(1f),
                            singleLine = true
                        )
                        OutlinedTextField(
                            value = priceText,
                            onValueChange = { priceText = it },
                            label = { Text("Price (₹/Qtl)") },
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                            modifier = Modifier.weight(1f),
                            singleLine = true
                        )
                    }
                }
                item {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = if (isHindi) "100% जैविक प्रमाणीकरण" else "Certified 100% Organic",
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Medium
                        )
                        Switch(
                            checked = isOrganic,
                            onCheckedChange = { isOrganic = it },
                            colors = SwitchDefaults.colors(checkedThumbColor = AgriGreenPrimary)
                        )
                    }
                }
                item {
                    OutlinedTextField(
                        value = harvestDate,
                        onValueChange = { harvestDate = it },
                        label = { Text("Harvest Date") },
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true
                    )
                }
                item {
                    OutlinedTextField(
                        value = village,
                        onValueChange = { village = it },
                        label = { Text("Village / Tehsil (गाँव/तहसील)") },
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true
                    )
                }
                item {
                    OutlinedTextField(
                        value = contact,
                        onValueChange = { contact = it },
                        label = { Text("Farmer Phone (संपर्क नंबर)") },
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true
                    )
                }
                item {
                    OutlinedTextField(
                        value = notes,
                        onValueChange = { notes = it },
                        label = { Text("Notes / Storage / Packaging (विवरण)") },
                        modifier = Modifier.fillMaxWidth(),
                        maxLines = 2
                    )
                }
                errorMessage?.let { err ->
                    item {
                        Text(err, color = AgriError, fontSize = 12.sp)
                    }
                }
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    val qty = quantityText.toDoubleOrNull()
                    val prc = priceText.toDoubleOrNull()
                    if (cropName.isBlank() || qty == null || prc == null || qty <= 0 || prc <= 0) {
                        errorMessage = "Please enter valid crop name, quantity and price."
                    } else {
                        onSubmit(
                            cropName,
                            variety,
                            qty,
                            prc,
                            harvestDate,
                            qualityGrade,
                            isOrganic,
                            village,
                            currentDistrict,
                            contact,
                            notes
                        )
                    }
                },
                colors = ButtonDefaults.buttonColors(containerColor = AgriGreenPrimary),
                modifier = Modifier.testTag("submit_listing_btn")
            ) {
                Text(if (isHindi) "सूचीबद्ध करें" else "Publish Listing", fontWeight = FontWeight.Bold)
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text(AppStrings.t("cancel", currentLanguage))
            }
        }
    )
}

@Composable
fun SendBuyRequestDialog(
    listing: CropListing,
    currentLanguage: AppLanguage,
    onDismiss: () -> Unit,
    onSubmit: (buyerName: String, buyerContact: String, quantity: Double, price: Double, pickupDate: String, message: String) -> Unit
) {
    val isHindi = currentLanguage == AppLanguage.HINDI

    var buyerName by remember { mutableStateOf("Rajiv Sharma (FreshOrganics)") }
    var buyerContact by remember { mutableStateOf("+91 98200 44321") }
    var quantityText by remember { mutableStateOf(listing.quantityQuintals.toInt().toString()) }
    var offeredPriceText by remember { mutableStateOf(listing.expectedPricePerQuintal.toInt().toString()) }
    var pickupDate by remember { mutableStateOf("Within 3 days") }
    var message by remember { mutableStateOf("We can arrange truck pickup from your farmgate. Instant payment after weighing.") }
    var errorMessage by remember { mutableStateOf<String?>(null) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Column {
                Text(
                    text = AppStrings.t("buy_request_title", currentLanguage),
                    fontWeight = FontWeight.Bold,
                    fontSize = 17.sp
                )
                Text(
                    text = "For: ${if (isHindi) listing.cropNameHi else listing.cropName} (${listing.variety})",
                    fontSize = 12.sp,
                    color = AgriGreenPrimary,
                    fontWeight = FontWeight.SemiBold
                )
            }
        },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                OutlinedTextField(
                    value = buyerName,
                    onValueChange = { buyerName = it },
                    label = { Text(AppStrings.t("your_name", currentLanguage)) },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )
                OutlinedTextField(
                    value = buyerContact,
                    onValueChange = { buyerContact = it },
                    label = { Text(AppStrings.t("your_phone", currentLanguage)) },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    OutlinedTextField(
                        value = quantityText,
                        onValueChange = { quantityText = it },
                        label = { Text(AppStrings.t("offered_quantity_label", currentLanguage)) },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        modifier = Modifier.weight(1f),
                        singleLine = true
                    )
                    OutlinedTextField(
                        value = offeredPriceText,
                        onValueChange = { offeredPriceText = it },
                        label = { Text(AppStrings.t("offered_price_label", currentLanguage)) },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        modifier = Modifier.weight(1f),
                        singleLine = true
                    )
                }
                OutlinedTextField(
                    value = pickupDate,
                    onValueChange = { pickupDate = it },
                    label = { Text(AppStrings.t("pickup_date_label", currentLanguage)) },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )
                OutlinedTextField(
                    value = message,
                    onValueChange = { message = it },
                    label = { Text(AppStrings.t("offer_message", currentLanguage)) },
                    modifier = Modifier.fillMaxWidth(),
                    maxLines = 2
                )
                errorMessage?.let { err ->
                    Text(err, color = AgriError, fontSize = 12.sp)
                }
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    val qty = quantityText.toDoubleOrNull()
                    val prc = offeredPriceText.toDoubleOrNull()
                    if (buyerName.isBlank() || qty == null || prc == null || qty <= 0 || prc <= 0) {
                        errorMessage = "Please enter valid quantity and price."
                    } else {
                        onSubmit(buyerName, buyerContact, qty, prc, pickupDate, message)
                    }
                },
                colors = ButtonDefaults.buttonColors(containerColor = AgriGreenPrimary),
                modifier = Modifier.testTag("submit_buy_offer_btn")
            ) {
                Text(AppStrings.t("submit_request", currentLanguage), fontWeight = FontWeight.Bold)
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text(AppStrings.t("cancel", currentLanguage))
            }
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CartBottomSheet(
    cartItems: List<CartItem>,
    currentLanguage: AppLanguage,
    onDismiss: () -> Unit,
    onUpdateQuantity: (productId: String, delta: Int) -> Unit,
    onCheckout: () -> Unit
) {
    val isHindi = currentLanguage == AppLanguage.HINDI
    val subtotal = cartItems.sumOf { it.product.price * it.quantity }
    val gst = subtotal * 0.05
    val total = subtotal + gst

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        containerColor = Color.White
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp, vertical = 12.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Filled.ShoppingCart, contentDescription = null, tint = AgriGreenPrimary)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = AppStrings.t("cart_title", currentLanguage),
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = AgriTextPrimary
                    )
                }
                Text(
                    text = "${cartItems.size} items",
                    fontSize = 12.sp,
                    color = AgriTextMuted
                )
            }

            Spacer(modifier = Modifier.height(14.dp))

            if (cartItems.isEmpty()) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(32.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = if (isHindi) "आपकी कार्ट खाली है" else "Your cart is empty",
                        color = AgriTextMuted,
                        fontSize = 14.sp
                    )
                }
            } else {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxWidth()
                        .heightIn(max = 240.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    items(cartItems) { item ->
                        Surface(
                            color = AgriSurfaceVariant,
                            shape = RoundedCornerShape(10.dp),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(10.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Column(modifier = Modifier.weight(1f)) {
                                    Text(
                                        text = if (isHindi) item.product.nameHi else item.product.name,
                                        fontSize = 13.sp,
                                        fontWeight = FontWeight.Bold,
                                        maxLines = 1
                                    )
                                    Text(
                                        text = "₹${item.product.price.toInt()} each",
                                        fontSize = 11.sp,
                                        color = AgriGreenPrimary,
                                        fontWeight = FontWeight.SemiBold
                                    )
                                }

                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    IconButton(
                                        onClick = { onUpdateQuantity(item.product.id, -1) },
                                        modifier = Modifier.size(28.dp)
                                    ) {
                                        Icon(Icons.Filled.Remove, contentDescription = "Decrease", modifier = Modifier.size(16.dp))
                                    }
                                    Text(
                                        text = "${item.quantity}",
                                        fontSize = 14.sp,
                                        fontWeight = FontWeight.Bold,
                                        modifier = Modifier.padding(horizontal = 6.dp)
                                    )
                                    IconButton(
                                        onClick = { onUpdateQuantity(item.product.id, 1) },
                                        modifier = Modifier.size(28.dp)
                                    ) {
                                        Icon(Icons.Filled.Add, contentDescription = "Increase", modifier = Modifier.size(16.dp))
                                    }
                                }
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))
                Divider(color = AgriOutlineVariant)
                Spacer(modifier = Modifier.height(10.dp))

                // Price Summary
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(text = "Subtotal:", fontSize = 13.sp, color = AgriTextMuted)
                    Text(text = "₹${subtotal.toInt()}", fontSize = 13.sp, fontWeight = FontWeight.SemiBold)
                }
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(text = "Agri-GST (5%):", fontSize = 13.sp, color = AgriTextMuted)
                    Text(text = "₹${gst.toInt()}", fontSize = 13.sp)
                }
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(text = "Farm Delivery:", fontSize = 13.sp, color = AgriTextMuted)
                    Text(text = "FREE", fontSize = 13.sp, fontWeight = FontWeight.Bold, color = AgriGreenPrimary)
                }

                Spacer(modifier = Modifier.height(8.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(text = AppStrings.t("total_amount", currentLanguage), fontSize = 16.sp, fontWeight = FontWeight.Bold)
                    Text(text = "₹${total.toInt()}", fontSize = 22.sp, fontWeight = FontWeight.ExtraBold, color = AgriGreenPrimary)
                }

                Spacer(modifier = Modifier.height(16.dp))

                Button(
                    onClick = onCheckout,
                    colors = ButtonDefaults.buttonColors(containerColor = AgriGreenPrimary),
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp)
                        .testTag("checkout_order_btn")
                ) {
                    Icon(Icons.Filled.CheckCircle, contentDescription = null)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = AppStrings.t("checkout_btn", currentLanguage),
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}
