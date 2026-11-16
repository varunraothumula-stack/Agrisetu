package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.data.AppRepository
import com.example.model.*
import com.example.ui.components.*
import com.example.ui.screens.*
import com.example.ui.theme.AgriGreenPrimary
import com.example.ui.theme.MyApplicationTheme
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MyApplicationTheme {
                AgriSetuApp()
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AgriSetuApp() {
    val repository = remember { AppRepository.instance }
    val scope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }

    val currentRole by repository.currentRole.collectAsStateWithLifecycle()
    val currentLanguage by repository.currentLanguage.collectAsStateWithLifecycle()
    val selectedDistrict by repository.selectedDistrict.collectAsStateWithLifecycle()
    val listings by repository.listings.collectAsStateWithLifecycle()
    val buyRequests by repository.buyRequests.collectAsStateWithLifecycle()
    val cartItems by repository.cartItems.collectAsStateWithLifecycle()
    val communityPosts by repository.communityPosts.collectAsStateWithLifecycle()
    val ordersCount by repository.ordersCount.collectAsStateWithLifecycle()
    val totalCommission by repository.totalCommissionEarned.collectAsStateWithLifecycle()

    var currentDestination by remember { mutableStateOf(AppDestination.HOME) }
    var isBusinessModelOpen by remember { mutableStateOf(false) }

    // Dialog & Sheet States
    var showCreateListingDialog by remember { mutableStateOf(false) }
    var selectedListingForBuy by remember { mutableStateOf<CropListing?>(null) }
    var showCartSheet by remember { mutableStateOf(false) }
    var confirmedOrderTotal by remember { mutableStateOf<Double?>(null) }

    // BackHandler for secondary screens
    if (isBusinessModelOpen) {
        BackHandler { isBusinessModelOpen = false }
    } else if (currentDestination != AppDestination.HOME) {
        BackHandler { currentDestination = AppDestination.HOME }
    }

    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .testTag("agrisetu_root_scaffold"),
        snackbarHost = { SnackbarHost(snackbarHostState) },
        topBar = {
            if (!isBusinessModelOpen) {
                AppHeader(
                    currentRole = currentRole,
                    currentLanguage = currentLanguage,
                    selectedDistrict = selectedDistrict,
                    cartItemCount = cartItems.sumOf { it.quantity },
                    onRoleSelected = { repository.setRole(it) },
                    onLanguageToggle = { repository.toggleLanguage() },
                    onDistrictSelected = { repository.setDistrict(it) },
                    onCartClicked = { showCartSheet = true },
                    onBusinessModelClicked = { isBusinessModelOpen = true }
                )
            }
        },
        bottomBar = {
            if (!isBusinessModelOpen) {
                AppBottomBar(
                    currentDestination = currentDestination,
                    currentLanguage = currentLanguage,
                    onDestinationSelected = { currentDestination = it }
                )
            }
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            if (isBusinessModelOpen) {
                BusinessModelScreen(
                    currentLanguage = currentLanguage,
                    onBack = { isBusinessModelOpen = false }
                )
            } else {
                when (currentDestination) {
                    AppDestination.HOME -> HomeScreen(
                        currentRole = currentRole,
                        currentLanguage = currentLanguage,
                        selectedDistrict = selectedDistrict,
                        listings = listings,
                        onNavigate = { currentDestination = it },
                        onRequestBuy = { selectedListingForBuy = it },
                        onCreateListing = { showCreateListingDialog = true }
                    )
                    AppDestination.MARKETPLACE -> MarketplaceScreen(
                        currentRole = currentRole,
                        currentLanguage = currentLanguage,
                        listings = listings,
                        buyRequests = buyRequests,
                        onRequestBuy = { selectedListingForBuy = it },
                        onCreateListing = { showCreateListingDialog = true },
                        onAcceptRequest = { id ->
                            repository.updateRequestStatus(id, RequestStatus.ACCEPTED)
                            scope.launch {
                                snackbarHostState.showSnackbar("Trade offer accepted! 5% platform fee recorded.")
                            }
                        },
                        onRejectRequest = { id ->
                            repository.updateRequestStatus(id, RequestStatus.REJECTED)
                            scope.launch {
                                snackbarHostState.showSnackbar("Trade offer declined.")
                            }
                        },
                        onNegotiateRequest = { id ->
                            repository.updateRequestStatus(id, RequestStatus.COUNTER_OFFER)
                            scope.launch {
                                snackbarHostState.showSnackbar("Counter-offer notification sent to buyer.")
                            }
                        }
                    )
                    AppDestination.MANDI -> MandiPricesScreen(
                        currentLanguage = currentLanguage,
                        selectedDistrict = selectedDistrict
                    )
                    AppDestination.ADVISORY -> AdvisoryScreen(
                        currentLanguage = currentLanguage,
                        selectedDistrict = selectedDistrict
                    )
                    AppDestination.SCANNER -> DiseaseScannerScreen(
                        currentLanguage = currentLanguage
                    )
                    AppDestination.STORE -> InputStoreScreen(
                        currentLanguage = currentLanguage,
                        cartItems = cartItems,
                        onAddToCart = { prod ->
                            repository.addToCart(prod)
                            scope.launch {
                                snackbarHostState.showSnackbar("${prod.name} added to cart!")
                            }
                        },
                        onOpenCart = { showCartSheet = true }
                    )
                    AppDestination.COMMUNITY -> CommunityScreen(
                        currentLanguage = currentLanguage,
                        posts = communityPosts,
                        onAddQuestion = { title, question, cropTag ->
                            repository.addCommunityPost(title, question, cropTag, if (currentRole == UserRole.FARMER) "Rameshwar Patil (You)" else "Member")
                            scope.launch {
                                snackbarHostState.showSnackbar("Question posted to Kisan Chaupal!")
                            }
                        },
                        onAddReply = { postId, text ->
                            repository.addReply(postId, if (currentRole == UserRole.ADMIN) "AgriSetu Agronomist" else "Experienced Farmer", text, currentRole == UserRole.ADMIN)
                            scope.launch {
                                snackbarHostState.showSnackbar("Reply posted successfully!")
                            }
                        },
                        onUpvote = { postId ->
                            repository.upvotePost(postId)
                        }
                    )
                    AppDestination.DASHBOARD -> DashboardScreen(
                        currentRole = currentRole,
                        currentLanguage = currentLanguage,
                        listings = listings,
                        buyRequests = buyRequests,
                        ordersCount = ordersCount,
                        totalCommission = totalCommission,
                        onRoleSelected = { repository.setRole(it) },
                        onOpenBusinessModel = { isBusinessModelOpen = true }
                    )
                }
            }
        }
    }

    // Modal: Create Produce Listing
    if (showCreateListingDialog) {
        CreateListingDialog(
            currentLanguage = currentLanguage,
            currentDistrict = selectedDistrict,
            onDismiss = { showCreateListingDialog = false },
            onSubmit = { crop, variety, qty, price, harvestDate, grade, isOrganic, village, district, contact, notes ->
                repository.addListing(
                    cropName = crop,
                    variety = variety,
                    quantity = qty,
                    price = price,
                    harvestDate = harvestDate,
                    qualityGrade = grade,
                    isOrganic = isOrganic,
                    village = village,
                    district = district,
                    contact = contact,
                    notes = notes
                )
                showCreateListingDialog = false
                scope.launch {
                    snackbarHostState.showSnackbar("Crop listed successfully on AgriSetu Marketplace!")
                }
            }
        )
    }

    // Modal: Send Buy Request Offer
    selectedListingForBuy?.let { listing ->
        SendBuyRequestDialog(
            listing = listing,
            currentLanguage = currentLanguage,
            onDismiss = { selectedListingForBuy = null },
            onSubmit = { buyerName, buyerContact, qty, price, pickupDate, message ->
                repository.sendBuyRequest(
                    listingId = listing.id,
                    cropName = listing.cropName,
                    buyerName = buyerName,
                    buyerContact = buyerContact,
                    quantity = qty,
                    offeredPrice = price,
                    pickupDate = pickupDate,
                    message = message
                )
                selectedListingForBuy = null
                scope.launch {
                    snackbarHostState.showSnackbar("Buy Offer sent directly to ${listing.farmerName}!")
                }
            }
        )
    }

    // Modal: Cart Bottom Sheet
    if (showCartSheet) {
        CartBottomSheet(
            cartItems = cartItems,
            currentLanguage = currentLanguage,
            onDismiss = { showCartSheet = false },
            onUpdateQuantity = { id, delta -> repository.updateCartQuantity(id, delta) },
            onCheckout = {
                val total = repository.checkoutCart()
                showCartSheet = false
                confirmedOrderTotal = total
            }
        )
    }

    // Modal: Simulated Order Confirmation
    confirmedOrderTotal?.let { total ->
        AlertDialog(
            onDismissRequest = { confirmedOrderTotal = null },
            icon = {
                Icon(
                    Icons.Filled.Verified,
                    contentDescription = null,
                    tint = AgriGreenPrimary,
                    modifier = Modifier.size(40.dp)
                )
            },
            title = {
                Text(
                    text = if (currentLanguage == AppLanguage.HINDI) "ऑर्डर सफलतापूर्वक दर्ज हुआ!" else "Agri-Input Order Confirmed!",
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp
                )
            },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text(
                        text = if (currentLanguage == AppLanguage.HINDI) {
                            "ऑर्डर संख्या: #AGRI-${System.currentTimeMillis() % 100000}\nराशि: ₹${total.toInt()}\n\nप्रमाणित सामग्री 48 घंटे के भीतर आपके पंजीकृत पते ($selectedDistrict) पर निःशुल्क डिलीवर कर दी जाएगी।"
                        } else {
                            "Order Reference: #AGRI-${System.currentTimeMillis() % 100000}\nTotal Paid: ₹${total.toInt()} (Simulated)\n\nCertified agri-inputs will be dispatched within 48 hours to your registered location in $selectedDistrict."
                        },
                        fontSize = 13.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        lineHeight = 18.sp
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = { confirmedOrderTotal = null },
                    colors = ButtonDefaults.buttonColors(containerColor = AgriGreenPrimary)
                ) {
                    Text(if (currentLanguage == AppLanguage.HINDI) "ठीक है" else "Done")
                }
            }
        )
    }
}
