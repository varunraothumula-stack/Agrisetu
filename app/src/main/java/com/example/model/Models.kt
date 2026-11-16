package com.example.model

enum class UserRole(val labelEn: String, val labelHi: String) {
    FARMER("Farmer", "किसान"),
    BUYER("Buyer / Trader", "खरीदार / व्यापारी"),
    ADMIN("Admin / Operations", "एडमिन")
}

enum class AppLanguage(val code: String, val label: String) {
    ENGLISH("en", "English"),
    HINDI("hi", "हिन्दी")
}

enum class ListingStatus(val labelEn: String, val labelHi: String) {
    ACTIVE("Active", "सक्रिय"),
    NEGOTIATING("Negotiation", "बातचीत जारी"),
    SOLD("Sold", "बिक गया"),
    EXPIRED("Expired", "समाप्त")
}

data class CropListing(
    val id: String,
    val cropName: String,
    val cropNameHi: String,
    val variety: String,
    val quantityQuintals: Double,
    val expectedPricePerQuintal: Double,
    val harvestDate: String,
    val qualityGrade: String = "A-Grade",
    val isOrganic: Boolean = false,
    val village: String,
    val district: String,
    val state: String = "Maharashtra",
    val farmerName: String,
    val farmerContact: String,
    val farmerRating: Double = 4.8,
    val status: ListingStatus = ListingStatus.ACTIVE,
    val notes: String = ""
)

data class BuyRequest(
    val id: String,
    val listingId: String,
    val cropName: String,
    val buyerName: String,
    val buyerContact: String,
    val requestedQuantity: Double,
    val offeredPrice: Double,
    val pickupDate: String,
    val message: String,
    val status: RequestStatus = RequestStatus.PENDING,
    val createdAt: String = "Today, 10:30 AM"
)

enum class RequestStatus(val labelEn: String, val labelHi: String) {
    PENDING("Pending", "लंबित"),
    ACCEPTED("Accepted", "स्वीकृत"),
    REJECTED("Declined", "अस्वीकृत"),
    COUNTER_OFFER("Counter Offer", "नया प्रस्ताव")
}

data class MandiPrice(
    val id: String,
    val crop: String,
    val cropHi: String,
    val mandiName: String,
    val district: String,
    val state: String,
    val minPrice: Int,
    val maxPrice: Int,
    val modalPrice: Int,
    val arrivalDate: String,
    val changePercent: Double,
    val weeklyTrend: List<Int> = emptyList()
)

data class WeatherDay(
    val dayName: String,
    val dayNameHi: String,
    val date: String,
    val tempMax: Int,
    val tempMin: Int,
    val condition: String,
    val conditionHi: String,
    val rainChance: Int,
    val humidity: Int,
    val advisoryNote: String,
    val advisoryNoteHi: String
)

data class CropAdvisory(
    val id: String,
    val crop: String,
    val cropHi: String,
    val stage: String,
    val stageHi: String,
    val category: AdvisoryCategory,
    val title: String,
    val titleHi: String,
    val advice: String,
    val adviceHi: String,
    val urgency: AdvisoryUrgency = AdvisoryUrgency.NORMAL
)

enum class AdvisoryCategory(val labelEn: String, val labelHi: String) {
    IRRIGATION("Irrigation", "सिंचाई"),
    FERTILIZER("Fertilizer", "उर्वरक"),
    PEST("Pest & Disease", "कीट व रोग"),
    HARVEST("Harvesting", "कटाई")
}

enum class AdvisoryUrgency {
    NORMAL, WARNING, CRITICAL
}

data class DiseaseCase(
    val id: String,
    val crop: String,
    val cropHi: String,
    val diseaseName: String,
    val diseaseNameHi: String,
    val scientificName: String,
    val confidencePercent: Int,
    val severity: String,
    val symptoms: String,
    val symptomsHi: String,
    val organicTreatment: String,
    val organicTreatmentHi: String,
    val chemicalTreatment: String,
    val chemicalTreatmentHi: String,
    val preventionTips: String,
    val preventionTipsHi: String
)

enum class ProductCategory(val labelEn: String, val labelHi: String) {
    ALL("All", "सभी"),
    SEEDS("Seeds", "बीज"),
    FERTILIZERS("Fertilizers", "उर्वरक"),
    PESTICIDES("Crop Protection", "कीटनाशक"),
    TOOLS("Tools & Equipment", "कृषि उपकरण")
}

data class InputProduct(
    val id: String,
    val name: String,
    val nameHi: String,
    val category: ProductCategory,
    val brand: String,
    val price: Double,
    val mrp: Double,
    val rating: Double,
    val reviewsCount: Int,
    val packSize: String,
    val inStock: Boolean = true,
    val description: String,
    val descriptionHi: String,
    val isSponsored: Boolean = false
)

data class CartItem(
    val product: InputProduct,
    val quantity: Int
)

data class CommunityReply(
    val id: String,
    val authorName: String,
    val isExpert: Boolean = false,
    val content: String,
    val timestamp: String,
    val upvotes: Int = 0
)

data class CommunityPost(
    val id: String,
    val authorName: String,
    val district: String,
    val cropTag: String,
    val title: String,
    val question: String,
    val upvotes: Int,
    val timestamp: String,
    val replies: List<CommunityReply> = emptyList()
)
