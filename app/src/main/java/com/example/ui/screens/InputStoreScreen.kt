package com.example.ui.screens

import androidx.compose.foundation.background
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.SampleData
import com.example.model.*
import com.example.ui.theme.*

@Composable
fun InputStoreScreen(
    currentLanguage: AppLanguage,
    cartItems: List<CartItem>,
    onAddToCart: (InputProduct) -> Unit,
    onOpenCart: () -> Unit
) {
    val isHindi = currentLanguage == AppLanguage.HINDI
    val allProducts = SampleData.inputProducts

    var selectedCategory by remember { mutableStateOf(ProductCategory.ALL) }
    var searchQuery by remember { mutableStateOf("") }

    val filteredProducts = remember(selectedCategory, searchQuery) {
        allProducts.filter { prod ->
            val matchCat = if (selectedCategory == ProductCategory.ALL) true else prod.category == selectedCategory
            val matchQuery = if (searchQuery.isBlank()) true else {
                prod.name.contains(searchQuery, ignoreCase = true) ||
                prod.nameHi.contains(searchQuery, ignoreCase = true) ||
                prod.brand.contains(searchQuery, ignoreCase = true)
            }
            matchCat && matchQuery
        }
    }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(AgriBackground),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Store Header & Cart Floating Bar
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
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = AppStrings.t("input_store_title", currentLanguage),
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Bold,
                                color = AgriTextPrimary
                            )
                            Text(
                                text = AppStrings.t("store_subtitle", currentLanguage),
                                fontSize = 12.sp,
                                color = AgriTextSecondary
                            )
                        }

                        if (cartItems.isNotEmpty()) {
                            val totalCartAmount = cartItems.sumOf { it.product.price * it.quantity }
                            Button(
                                onClick = onOpenCart,
                                colors = ButtonDefaults.buttonColors(containerColor = AgriAmberSecondary),
                                shape = RoundedCornerShape(10.dp),
                                modifier = Modifier.testTag("floating_cart_btn")
                            ) {
                                Icon(Icons.Filled.ShoppingCart, contentDescription = null, modifier = Modifier.size(16.dp))
                                Spacer(modifier = Modifier.width(6.dp))
                                Text("₹${totalCartAmount.toInt()}", fontWeight = FontWeight.Bold, fontSize = 13.sp)
                            }
                        }
                    }
                }
            }
        }

        // Search Field
        item {
            OutlinedTextField(
                value = searchQuery,
                onValueChange = { searchQuery = it },
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("search_store_input"),
                placeholder = {
                    Text(
                        if (isHindi) "खाद, बीज, कीटनाशक या ब्रांड खोजें..." else "Search seeds, fertilizers, sprayers...",
                        fontSize = 13.sp
                    )
                },
                leadingIcon = {
                    Icon(Icons.Filled.Search, contentDescription = null, tint = AgriGreenPrimary)
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
        }

        // Category Chips
        item {
            LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                items(ProductCategory.values()) { category ->
                    val isSelected = selectedCategory == category
                    FilterChip(
                        selected = isSelected,
                        onClick = { selectedCategory = category },
                        label = {
                            Text(
                                text = if (isHindi) category.labelHi else category.labelEn,
                                fontSize = 12.sp,
                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                            )
                        },
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = AgriGreenPrimary,
                            selectedLabelColor = Color.White
                        ),
                        modifier = Modifier.testTag("chip_store_${category.name}")
                    )
                }
            }
        }

        // Product Cards List
        items(filteredProducts) { product ->
            val inCartCount = cartItems.find { it.product.id == product.id }?.quantity ?: 0

            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("product_card_${product.id}"),
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
                            if (product.isSponsored) {
                                Surface(
                                    color = Color(0xFFFFF8E1),
                                    shape = RoundedCornerShape(4.dp),
                                    modifier = Modifier.padding(bottom = 4.dp)
                                ) {
                                    Text(
                                        text = "★ " + AppStrings.t("sponsored", currentLanguage),
                                        fontSize = 10.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = Color(0xFFF57F17),
                                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                    )
                                }
                            }
                            Text(
                                text = if (isHindi) product.nameHi else product.name,
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold,
                                color = AgriTextPrimary
                            )
                            Text(
                                text = "By ${product.brand} • ${product.packSize}",
                                fontSize = 12.sp,
                                color = AgriTextSecondary
                            )
                        }

                        Surface(
                            color = if (product.inStock) Color(0xFFE8F5E9) else Color(0xFFFFEBEE),
                            shape = RoundedCornerShape(4.dp)
                        ) {
                            Text(
                                text = if (product.inStock) (if (isHindi) "उपलब्ध है" else "In Stock") else "Out of Stock",
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold,
                                color = if (product.inStock) AgriGreenPrimary else AgriError,
                                modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = if (isHindi) product.descriptionHi else product.description,
                        fontSize = 12.sp,
                        color = AgriTextMuted,
                        lineHeight = 16.sp
                    )

                    Spacer(modifier = Modifier.height(10.dp))
                    Divider(color = AgriOutlineVariant)
                    Spacer(modifier = Modifier.height(8.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.Bottom) {
                            Text(
                                text = "₹${product.price.toInt()}",
                                fontSize = 20.sp,
                                fontWeight = FontWeight.ExtraBold,
                                color = AgriGreenPrimary
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = "₹${product.mrp.toInt()}",
                                fontSize = 12.sp,
                                color = AgriTextMuted,
                                textDecoration = TextDecoration.LineThrough,
                                modifier = Modifier.padding(bottom = 2.dp)
                            )
                            val discountPercent = (((product.mrp - product.price) / product.mrp) * 100).toInt()
                            if (discountPercent > 0) {
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(
                                    text = "$discountPercent% OFF",
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = AgriAmberSecondary,
                                    modifier = Modifier.padding(bottom = 2.dp)
                                )
                            }
                        }

                        Button(
                            onClick = { onAddToCart(product) },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = if (inCartCount > 0) AgriAmberSecondary else AgriGreenPrimary
                            ),
                            shape = RoundedCornerShape(8.dp),
                            contentPadding = PaddingValues(horizontal = 14.dp, vertical = 6.dp),
                            modifier = Modifier
                                .height(38.dp)
                                .testTag("add_to_cart_btn_${product.id}")
                        ) {
                            Icon(
                                imageVector = if (inCartCount > 0) Icons.Filled.Check else Icons.Filled.AddShoppingCart,
                                contentDescription = null,
                                modifier = Modifier.size(16.dp)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = if (inCartCount > 0) "${AppStrings.t("in_cart", currentLanguage)} ($inCartCount)" else AppStrings.t("add_to_cart", currentLanguage),
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
