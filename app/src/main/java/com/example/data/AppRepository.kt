package com.example.data

import com.example.model.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.util.UUID

class AppRepository private constructor() {

    private val _currentRole = MutableStateFlow(UserRole.FARMER)
    val currentRole: StateFlow<UserRole> = _currentRole.asStateFlow()

    private val _currentLanguage = MutableStateFlow(AppLanguage.ENGLISH)
    val currentLanguage: StateFlow<AppLanguage> = _currentLanguage.asStateFlow()

    private val _selectedDistrict = MutableStateFlow("Nashik, MH")
    val selectedDistrict: StateFlow<String> = _selectedDistrict.asStateFlow()

    private val _listings = MutableStateFlow<List<CropListing>>(SampleData.cropListings.toList())
    val listings: StateFlow<List<CropListing>> = _listings.asStateFlow()

    private val _buyRequests = MutableStateFlow<List<BuyRequest>>(SampleData.buyRequests.toList())
    val buyRequests: StateFlow<List<BuyRequest>> = _buyRequests.asStateFlow()

    private val _cartItems = MutableStateFlow<List<CartItem>>(emptyList())
    val cartItems: StateFlow<List<CartItem>> = _cartItems.asStateFlow()

    private val _communityPosts = MutableStateFlow<List<CommunityPost>>(SampleData.communityPosts.toList())
    val communityPosts: StateFlow<List<CommunityPost>> = _communityPosts.asStateFlow()

    private val _ordersCount = MutableStateFlow(14)
    val ordersCount: StateFlow<Int> = _ordersCount.asStateFlow()

    private val _totalCommissionEarned = MutableStateFlow(38450.0) // ₹ 5% on completed marketplace sales
    val totalCommissionEarned: StateFlow<Double> = _totalCommissionEarned.asStateFlow()

    fun setRole(role: UserRole) {
        _currentRole.value = role
    }

    fun toggleLanguage() {
        _currentLanguage.value = if (_currentLanguage.value == AppLanguage.ENGLISH) AppLanguage.HINDI else AppLanguage.ENGLISH
    }

    fun setLanguage(lang: AppLanguage) {
        _currentLanguage.value = lang
    }

    fun setDistrict(district: String) {
        _selectedDistrict.value = district
    }

    fun addListing(
        cropName: String,
        variety: String,
        quantity: Double,
        price: Double,
        harvestDate: String,
        qualityGrade: String,
        isOrganic: Boolean,
        village: String,
        district: String,
        contact: String,
        notes: String
    ) {
        val cropHi = when (cropName.lowercase()) {
            "wheat" -> "गेहूं"
            "rice" -> "चावल"
            "tomato" -> "टमाटर"
            "onion" -> "प्याज"
            "potato" -> "आलू"
            "cotton" -> "कपास"
            "maize" -> "मक्का"
            else -> cropName
        }
        val newListing = CropListing(
            id = "lst_${System.currentTimeMillis() % 10000}",
            cropName = cropName,
            cropNameHi = cropHi,
            variety = variety.ifBlank { "Standard Variety" },
            quantityQuintals = quantity,
            expectedPricePerQuintal = price,
            harvestDate = harvestDate.ifBlank { "Recently Harvested" },
            qualityGrade = qualityGrade,
            isOrganic = isOrganic,
            village = village.ifBlank { "Kisan Village" },
            district = district,
            farmerName = if (_currentRole.value == UserRole.FARMER) "Rameshwar Patil (You)" else "Verified Farmer",
            farmerContact = contact.ifBlank { "+91 98765 43210" },
            farmerRating = 4.9,
            status = ListingStatus.ACTIVE,
            notes = notes
        )
        _listings.value = listOf(newListing) + _listings.value
    }

    fun updateListingStatus(listingId: String, newStatus: ListingStatus) {
        _listings.value = _listings.value.map {
            if (it.id == listingId) it.copy(status = newStatus) else it
        }
    }

    fun sendBuyRequest(
        listingId: String,
        cropName: String,
        buyerName: String,
        buyerContact: String,
        quantity: Double,
        offeredPrice: Double,
        pickupDate: String,
        message: String
    ) {
        val req = BuyRequest(
            id = "req_${System.currentTimeMillis() % 10000}",
            listingId = listingId,
            cropName = cropName,
            buyerName = buyerName.ifBlank { "Agro Trader Buyer" },
            buyerContact = buyerContact.ifBlank { "+91 98200 11223" },
            requestedQuantity = quantity,
            offeredPrice = offeredPrice,
            pickupDate = pickupDate.ifBlank { "Within 3 days" },
            message = message,
            status = RequestStatus.PENDING,
            createdAt = "Just now"
        )
        _buyRequests.value = listOf(req) + _buyRequests.value
        // Put listing into negotiating status
        updateListingStatus(listingId, ListingStatus.NEGOTIATING)
    }

    fun updateRequestStatus(requestId: String, status: RequestStatus) {
        _buyRequests.value = _buyRequests.value.map {
            if (it.id == requestId) {
                if (status == RequestStatus.ACCEPTED) {
                    // 5% marketplace commission
                    val dealValue = it.requestedQuantity * it.offeredPrice
                    val comm = dealValue * 0.05
                    _totalCommissionEarned.value += comm
                    _ordersCount.value += 1
                }
                it.copy(status = status)
            } else it
        }
    }

    fun addToCart(product: InputProduct) {
        val existing = _cartItems.value.find { it.product.id == product.id }
        if (existing != null) {
            _cartItems.value = _cartItems.value.map {
                if (it.product.id == product.id) it.copy(quantity = it.quantity + 1) else it
            }
        } else {
            _cartItems.value = _cartItems.value + CartItem(product, 1)
        }
    }

    fun updateCartQuantity(productId: String, delta: Int) {
        val list = _cartItems.value.mapNotNull {
            if (it.product.id == productId) {
                val newQty = it.quantity + delta
                if (newQty > 0) it.copy(quantity = newQty) else null
            } else it
        }
        _cartItems.value = list
    }

    fun clearCart() {
        _cartItems.value = emptyList()
    }

    fun checkoutCart(): Double {
        val total = _cartItems.value.sumOf { it.product.price * it.quantity }
        _ordersCount.value += 1
        _cartItems.value = emptyList()
        return total
    }

    fun addCommunityPost(title: String, question: String, cropTag: String, authorName: String) {
        val post = CommunityPost(
            id = "post_${System.currentTimeMillis() % 10000}",
            authorName = authorName.ifBlank { "Kisan Member" },
            district = _selectedDistrict.value.split(",").firstOrNull() ?: "Nashik",
            cropTag = cropTag.ifBlank { "General" },
            title = title,
            question = question,
            upvotes = 1,
            timestamp = "Just now",
            replies = emptyList()
        )
        _communityPosts.value = listOf(post) + _communityPosts.value
    }

    fun addReply(postId: String, authorName: String, content: String, isExpert: Boolean = false) {
        _communityPosts.value = _communityPosts.value.map { post ->
            if (post.id == postId) {
                val newReply = CommunityReply(
                    id = "rep_${System.currentTimeMillis() % 10000}",
                    authorName = authorName.ifBlank { "AgriSetu Advisor" },
                    isExpert = isExpert,
                    content = content,
                    timestamp = "Just now",
                    upvotes = 0
                )
                post.copy(replies = post.replies + newReply)
            } else post
        }
    }

    fun upvotePost(postId: String) {
        _communityPosts.value = _communityPosts.value.map {
            if (it.id == postId) it.copy(upvotes = it.upvotes + 1) else it
        }
    }

    companion object {
        val instance by lazy { AppRepository() }
    }
}
