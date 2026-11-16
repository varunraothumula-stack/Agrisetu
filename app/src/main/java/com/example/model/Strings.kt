package com.example.model

object AppStrings {
    fun t(key: String, lang: AppLanguage): String {
        val entry = translations[key] ?: return key
        return if (lang == AppLanguage.HINDI) entry.second else entry.first
    }

    private val translations = mapOf(
        "app_title" to Pair("AgriSetu", "एग्रीसेतु"),
        "app_subtitle" to Pair("Farm to Market Bridge", "खेत से बाज़ार तक सीधा सेतु"),
        "nav_home" to Pair("Home", "होम"),
        "nav_marketplace" to Pair("Market", "मंडी बाज़ार"),
        "nav_mandi" to Pair("Prices", "मंडी भाव"),
        "nav_advisory" to Pair("Advisory", "मौसम व सलाह"),
        "nav_scanner" to Pair("Disease AI", "रोग पहचान"),
        "nav_store" to Pair("Agri Store", "कृषि स्टोर"),
        "nav_community" to Pair("Community", "किसान चौपाल"),
        "nav_dashboard" to Pair("Dashboard", "डैशबोर्ड"),
        "nav_business_model" to Pair("Business", "व्यापार मॉडल"),

        // Home
        "greeting_farmer" to Pair("Namaste, Farmer!", "नमस्ते, किसान भाई!"),
        "greeting_buyer" to Pair("Welcome, Trader!", "स्वागत है, व्यापारी जी!"),
        "greeting_admin" to Pair("AgriSetu Operations", "एग्रीसेतु ऑपरेशन्स"),
        "selected_district" to Pair("District", "जिला"),
        "change_district" to Pair("Change", "बदलें"),
        "weather_today" to Pair("Today's Weather", "आज का मौसम"),
        "top_mandi_rates" to Pair("Live APMC Mandi Rates", "ताज़ा मंडी भाव"),
        "view_all" to Pair("View All", "सभी देखें"),
        "quick_actions" to Pair("Quick Actions", "त्वरित सेवाएँ"),
        "action_sell" to Pair("Sell Crop", "फसल बेचें"),
        "action_prices" to Pair("Mandi Rates", "भाव देखें"),
        "action_scan" to Pair("Scan Leaf", "रोग जांचें"),
        "action_ai_advisor" to Pair("Ask Krishi AI", "कृषि सलाह"),
        "action_store" to Pair("Buy Inputs", "खाद-बीज"),
        "action_community" to Pair("Ask Community", "चौपाल चर्चा"),

        // Marketplace
        "marketplace_title" to Pair("Direct Crop Marketplace", "प्रत्यक्ष फसल बाज़ार"),
        "filter_all" to Pair("All Crops", "सभी फसलें"),
        "filter_organic" to Pair("100% Organic", "जैविक खेती"),
        "search_crop_hint" to Pair("Search crop, variety or district...", "फसल, किस्म या जिला खोजें..."),
        "available_listings" to Pair("Available Lots", "उपलब्ध फसल लॉट"),
        "expected_price" to Pair("Expected Price", "अपेक्षित मूल्य"),
        "quantity_available" to Pair("Quantity", "मात्रा"),
        "quintals" to Pair("Quintals", "क्विंटल"),
        "farmer" to Pair("Farmer", "किसान"),
        "harvested_on" to Pair("Harvest Date", "कटाई तिथि"),
        "send_buy_request" to Pair("Send Buy Offer", "खरीद प्रस्ताव भेजें"),
        "create_listing" to Pair("List My Crop", "अपनी फसल जोड़ें"),
        "my_listings_tab" to Pair("My Crop Listings", "मेरी फसलें"),
        "incoming_requests_tab" to Pair("Trade Requests", "खरीद प्रस्ताव"),

        // Request Dialog
        "buy_request_title" to Pair("Send Purchase Offer", "खरीद प्रस्ताव भेजें"),
        "your_name" to Pair("Your Name / Trading Agency", "आपका नाम / एजेंसी"),
        "your_phone" to Pair("Contact Phone Number", "फोन नंबर"),
        "offered_price_label" to Pair("Offered Price (₹ / Quintal)", "प्रस्तावित मूल्य (₹ / क्विंटल)"),
        "offered_quantity_label" to Pair("Required Quantity (Quintals)", "आवश्यक मात्रा (क्विंटल)"),
        "pickup_date_label" to Pair("Target Pickup Date", "उठाव की तारीख"),
        "offer_message" to Pair("Message to Farmer (Logistics/Packing)", "किसान को संदेश"),
        "submit_request" to Pair("Submit Offer", "प्रस्ताव भेजें"),
        "cancel" to Pair("Cancel", "रद्द करें"),

        // Mandi Prices
        "mandi_tracker_title" to Pair("APMC Mandi Price Tracker", "दैनिक मंडी भाव ट्रैकर"),
        "seven_day_trend" to Pair("7-Day Modal Price Trend (₹/Quintal)", "7-दिवसीय भाव का रुझान (₹/क्विंटल)"),
        "highest_modal" to Pair("Modal Price", "औसत भाव"),
        "min_max" to Pair("Min - Max Range", "न्यूनतम - अधिकतम"),
        "arrival_date" to Pair("Arrival Date", "आवक दिनांक"),

        // Disease Scanner
        "disease_scanner_title" to Pair("AI Crop Disease Doctor", "एआई फसल रोग डॉक्टर"),
        "scanner_subtitle" to Pair("Scan leaves with AI to detect pests & diseases instantly", "पत्ते की फोटो लें और तुरंत रोग व सटीक उपचार जानें"),
        "upload_or_select" to Pair("Choose a Disease Case or Upload Photo", "रोग नमूना चुनें या फोटो अपलोड करें"),
        "analyzing_leaf" to Pair("Analyzing leaf pathology with AI...", "एआई द्वारा पत्ते की जांच जारी है..."),
        "likely_diagnosis" to Pair("Diagnosis Result", "रोग की पहचान"),
        "confidence" to Pair("Confidence", "सटीकता"),
        "symptoms" to Pair("Key Symptoms", "मुख्य लक्षण"),
        "organic_treatment" to Pair("Organic Remedy (Jaivik)", "जैविक उपचार"),
        "chemical_treatment" to Pair("Chemical Treatment (Safe dosage)", "रासायनिक उपचार (सुरक्षित मात्रा)"),
        "prevention_tips" to Pair("Prevention for Next Season", "भविष्य में रोकथाम के उपाय"),

        // Advisory
        "advisory_title" to Pair("Weather & Agronomy Advisory", "मौसम पूर्वानुमान व कृषि परामर्श"),
        "five_day_forecast" to Pair("5-Day Weather Forecast", "5 दिनों का मौसम"),
        "rain_probability" to Pair("Rain Chance", "बारिश की संभावना"),
        "humidity" to Pair("Humidity", "नमी"),
        "active_advisories" to Pair("Active Crop Advisories", "आज की कृषि सलाह"),
        "ai_consultant_title" to Pair("Instant AI Crop Advisor", "तत्काल एआई फसल सलाहकार"),
        "select_crop" to Pair("Select Crop", "फसल चुनें"),
        "select_stage" to Pair("Growth Stage", "विकास की अवस्था"),
        "select_problem" to Pair("Issue Faced", "समस्या का प्रकार"),
        "get_ai_advice" to Pair("Get Expert Advice", "विशेषज्ञ सलाह प्राप्त करें"),

        // Store
        "input_store_title" to Pair("Agri-Input Marketplace", "कृषि इनपुट स्टोर"),
        "store_subtitle" to Pair("Certified seeds, bio-fertilizers & farm equipment", "प्रमाणित बीज, जैविक खाद व कृषि उपकरण"),
        "add_to_cart" to Pair("Add to Cart", "कार्ट में जोड़ें"),
        "in_cart" to Pair("In Cart", "कार्ट में है"),
        "cart_title" to Pair("Your Agri Cart", "आपकी कार्ट"),
        "total_amount" to Pair("Total Amount", "कुल राशि"),
        "checkout_btn" to Pair("Confirm Order (Simulated)", "ऑर्डर की पुष्टि करें"),
        "order_success" to Pair("Order Placed Successfully!", "ऑर्डर सफलतापूर्वक दर्ज हुआ!"),
        "sponsored" to Pair("Sponsored", "प्रायोजित"),

        // Community
        "community_title" to Pair("Kisan Chaupal - Community", "किसान चौपाल - परिचर्चा"),
        "ask_question_btn" to Pair("Ask a Question", "प्रश्न पूछें"),
        "answers_count" to Pair("Replies", "उत्तर"),
        "reply_btn" to Pair("Reply", "जवाब दें"),

        // Dashboard
        "dashboard_title" to Pair("Business Analytics", "व्यावसायिक एनालिटिक्स"),
        "switch_role" to Pair("Role Perspective", "भूमिका बदलें")
    )
}
