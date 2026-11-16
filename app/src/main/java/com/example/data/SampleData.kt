package com.example.data

import com.example.model.*

object SampleData {
    val districts = listOf("Nashik, MH", "Indore, MP", "Guntur, AP", "Ludhiana, PB", "Varanasi, UP", "Pune, MH")

    val cropListings = mutableListOf(
        CropListing(
            id = "lst_01",
            cropName = "Wheat",
            cropNameHi = "गेहूं",
            variety = "Sharbati Gold MP",
            quantityQuintals = 120.0,
            expectedPricePerQuintal = 2480.0,
            harvestDate = "15 Oct 2026",
            qualityGrade = "A+ Premium",
            isOrganic = false,
            village = "Pimpalgaon",
            district = "Nashik, MH",
            farmerName = "Rameshwar Patil",
            farmerContact = "+91 98220 14820",
            farmerRating = 4.9,
            notes = "Sun-dried to 11% moisture, machine cleaned, stored in sealed bags."
        ),
        CropListing(
            id = "lst_02",
            cropName = "Onion",
            cropNameHi = "प्याज",
            variety = "Nashik Red Garwa",
            quantityQuintals = 200.0,
            expectedPricePerQuintal = 1950.0,
            harvestDate = "20 Oct 2026",
            qualityGrade = "Grade-1 (55mm+)",
            isOrganic = true,
            village = "Lasalgaon",
            district = "Nashik, MH",
            farmerName = "Kishore Jadhav",
            farmerContact = "+91 94231 88722",
            farmerRating = 4.8,
            notes = "Certified organic onion. Excellent shelf life, ideal for direct export."
        ),
        CropListing(
            id = "lst_03",
            cropName = "Tomato",
            cropNameHi = "टमाटर",
            variety = "Abhinav Hybrid Red",
            quantityQuintals = 85.0,
            expectedPricePerQuintal = 2200.0,
            harvestDate = "25 Oct 2026",
            qualityGrade = "A-Grade Firm",
            isOrganic = false,
            village = "Dindori",
            district = "Nashik, MH",
            farmerName = "Suresh Shinde",
            farmerContact = "+91 97654 32109",
            farmerRating = 4.7,
            status = ListingStatus.NEGOTIATING,
            notes = "Plucked 2 days ago, firm skin, uniform color, ready for transport."
        ),
        CropListing(
            id = "lst_04",
            cropName = "Rice",
            cropNameHi = "चावल",
            variety = "Basmati 1121 Extra Long",
            quantityQuintals = 350.0,
            expectedPricePerQuintal = 3850.0,
            harvestDate = "18 Oct 2026",
            qualityGrade = "Export Quality",
            isOrganic = true,
            village = "Samrala",
            district = "Ludhiana, PB",
            farmerName = "Gurpreet Singh",
            farmerContact = "+91 98140 76543",
            farmerRating = 5.0,
            notes = "Aged grain, minimal broken grain percentage (<2%). Certified organic."
        ),
        CropListing(
            id = "lst_05",
            cropName = "Cotton",
            cropNameHi = "कपास",
            variety = "Bt Cotton RCH-2",
            quantityQuintals = 150.0,
            expectedPricePerQuintal = 7450.0,
            harvestDate = "12 Oct 2026",
            qualityGrade = "Shankar-6 Equivalent",
            isOrganic = false,
            village = "Tenali Rural",
            district = "Guntur, AP",
            farmerName = "Venkat Rao",
            farmerContact = "+91 86322 99881",
            farmerRating = 4.9,
            notes = "High staple length, staple strength >29 g/tex, trash content below 3%."
        ),
        CropListing(
            id = "lst_06",
            cropName = "Potato",
            cropNameHi = "आलू",
            variety = "Kufri Jyoti",
            quantityQuintals = 180.0,
            expectedPricePerQuintal = 1450.0,
            harvestDate = "22 Oct 2026",
            qualityGrade = "Table Grade (45-60mm)",
            isOrganic = false,
            village = "Depalpur",
            district = "Indore, MP",
            farmerName = "Babulal Meena",
            farmerContact = "+91 73124 55667",
            farmerRating = 4.6,
            status = ListingStatus.SOLD,
            notes = "Smooth skin, no greening, cold storage loaded lot."
        ),
        CropListing(
            id = "lst_07",
            cropName = "Maize",
            cropNameHi = "मक्का",
            variety = "Pioneer Hybrid 900M",
            quantityQuintals = 140.0,
            expectedPricePerQuintal = 2180.0,
            harvestDate = "10 Oct 2026",
            qualityGrade = "Feed Grade",
            isOrganic = false,
            village = "Sanwer",
            district = "Indore, MP",
            farmerName = "Devendra Parmar",
            farmerContact = "+91 94250 11223",
            farmerRating = 4.7,
            notes = "Yellow bold grains, moisture 12.5%, ideal for poultry feed or mills."
        )
    )

    val buyRequests = mutableListOf(
        BuyRequest(
            id = "req_101",
            listingId = "lst_03",
            cropName = "Tomato",
            buyerName = "FreshDirect Retail (Rajiv)",
            buyerContact = "+91 98200 44321",
            requestedQuantity = 50.0,
            offeredPrice = 2150.0,
            pickupDate = "28 Oct 2026",
            message = "We arrange refrigerated logistics from farm gate. Fast payment.",
            status = RequestStatus.PENDING,
            createdAt = "Today, 09:15 AM"
        ),
        BuyRequest(
            id = "req_102",
            listingId = "lst_01",
            cropName = "Wheat",
            buyerName = "Annapurna Flour Mills",
            buyerContact = "+91 93110 55443",
            requestedQuantity = 100.0,
            offeredPrice = 2450.0,
            pickupDate = "30 Oct 2026",
            message = "Regular buyer looking for clean Sharbati lots.",
            status = RequestStatus.ACCEPTED,
            createdAt = "Yesterday, 04:30 PM"
        )
    )

    val mandiPrices = listOf(
        MandiPrice("m1", "Wheat", "गेहूं", "APMC Lasalgaon", "Nashik", "Maharashtra", 2300, 2550, 2440, "27 Oct", 2.4, listOf(2360, 2380, 2400, 2390, 2420, 2435, 2440)),
        MandiPrice("m2", "Onion", "प्याज", "APMC Lasalgaon", "Nashik", "Maharashtra", 1600, 2100, 1880, "27 Oct", 4.1, listOf(1720, 1750, 1790, 1820, 1840, 1860, 1880)),
        MandiPrice("m3", "Tomato", "टमाटर", "APMC Vashi", "Mumbai", "Maharashtra", 1800, 2400, 2200, "27 Oct", -1.8, listOf(2350, 2300, 2280, 2250, 2220, 2210, 2200)),
        MandiPrice("m4", "Rice", "चावल", "APMC Khanna", "Ludhiana", "Punjab", 3500, 4100, 3850, "27 Oct", 1.2, listOf(3780, 3800, 3810, 3820, 3830, 3840, 3850)),
        MandiPrice("m5", "Cotton", "कपास", "APMC Guntur", "Guntur", "Andhra Pradesh", 7100, 7650, 7450, "27 Oct", 0.8, listOf(7320, 7350, 7380, 7400, 7420, 7430, 7450)),
        MandiPrice("m6", "Potato", "आलू", "APMC Choithram", "Indore", "Madhya Pradesh", 1250, 1580, 1420, "27 Oct", -0.5, listOf(1450, 1440, 1430, 1435, 1425, 1420, 1420)),
        MandiPrice("m7", "Maize", "मक्का", "APMC Indore", "Indore", "Madhya Pradesh", 2050, 2280, 2160, "27 Oct", 1.5, listOf(2100, 2110, 2125, 2140, 2145, 2155, 2160))
    )

    val weatherForecast = listOf(
        WeatherDay("Today", "आज", "27 Oct", 31, 19, "Partly Sunny", "आंशिक धूप", 10, 58, "Good for pesticide spraying or field harvesting.", "कीटनाशक छिड़काव या फसल कटाई के लिए अनुकूल।"),
        WeatherDay("Wed", "बुध", "28 Oct", 32, 20, "Sunny & Warm", "साफ धूप", 5, 52, "Plan light irrigation for vegetable plots in late evening.", "शाम के समय सब्जी फसलों में हल्की सिंचाई करें।"),
        WeatherDay("Thu", "गुरु", "29 Oct", 30, 18, "Cloudy Intervals", "बादल छाए रहेंगे", 25, 64, "Watch out for aphid infestation on tender tomato shoots.", "टमाटर की नई कोंपलों पर माहू कीट की निगरानी रखें।"),
        WeatherDay("Fri", "शुक्र", "30 Oct", 28, 17, "Light Rain", "हल्की बारिश", 45, 72, "Postpone fertilizer broadcast; ensure drain outlets are clear.", "उर्वरक का छिड़काव टालें; जल निकासी नाली साफ रखें।"),
        WeatherDay("Sat", "शनि", "31 Oct", 29, 18, "Clear Sky", "साफ आसमान", 10, 55, "Optimal conditions for drying harvested grain.", "खलिहान में कटी फसल सुखाने के लिए सर्वोत्तम दिन।")
    )

    val cropAdvisories = listOf(
        CropAdvisory("adv_1", "Wheat", "गेहूं", "Crown Root (20-25 DAS)", "शीर्ष जड़ अवस्था", AdvisoryCategory.IRRIGATION, "First Critical Irrigation Stage", "पहली महत्वपूर्ण सिंचाई का समय", "Give light irrigation to ensure deep root penetration. Apply 1st split dose of Urea (30 kg/acre).", "हल्की सिंचाई करें ताकि जड़ें गहराई तक जाएं। प्रति एकड़ 30 किलो यूरिया दें।", AdvisoryUrgency.WARNING),
        CropAdvisory("adv_2", "Tomato", "टमाटर", "Flowering & Fruit Setting", "फूल व फल अवस्था", AdvisoryCategory.PEST, "Prevent Early Blight & Fruit Borer", "अगेती झुलसा व फल छेदक से बचाव", "Spray Mancozeb 75% WP @ 2.5g/L water. Hang yellow sticky traps for whiteflies.", "मैंकोजेब 75% WP (2.5 ग्राम/लीटर) का छिड़काव करें। पीले ट्रैप लगाएं।", AdvisoryUrgency.CRITICAL),
        CropAdvisory("adv_3", "Onion", "प्याज", "Bulb Enlargement Stage", "कंद विकास अवस्था", AdvisoryCategory.FERTILIZER, "Potash & Micronutrient Boost", "पोटाश व सूक्ष्म पोषक तत्व", "Apply 00:00:50 (SOP) @ 5g/L to improve onion firmness, shiny skin and storage duration.", "कंद को चमकदार व टिकाऊ बनाने हेतु 00:00:50 (SOP) 5 ग्राम/लीटर छिड़कें।"),
        CropAdvisory("adv_4", "Cotton", "कपास", "Boll Maturation Stage", "टिंडे पकने की अवस्था", AdvisoryCategory.HARVEST, "Clean Cotton Picking Guide", "स्वच्छ कपास चुनाई निर्देश", "Pick cotton only after morning dew dries (after 10 AM) to prevent lint discoloration.", "सुबह की ओस सूखने के बाद (सुबह 10 बजे के बाद) ही चुनाई करें।")
    )

    val diseaseCases = listOf(
        DiseaseCase(
            id = "dis_01",
            crop = "Tomato",
            cropHi = "टमाटर",
            diseaseName = "Tomato Late Blight",
            diseaseNameHi = "टमाटर का पछेती झुलसा",
            scientificName = "Phytophthora infestans",
            confidencePercent = 96,
            severity = "High Alert",
            symptoms = "Dark water-soaked lesions on leaf tips; white fungal growth on undersides in humid conditions.",
            symptomsHi = "पत्तियों के किनारों पर गहरे जल-युक्त धब्बे, अधिक नमी में पत्तों के नीचे सफेद फफूंद।",
            organicTreatment = "Spray Trichoderma viride @ 5g/L mixed with cow urine (Gau-mutra) decoction and neem leaf extract.",
            organicTreatmentHi = "ट्राइकोडर्मा विरिडी (5 ग्राम/लीटर) गौमूत्र व नीम काढ़े के साथ 7 दिन के अंतराल पर छिड़कें।",
            chemicalTreatment = "Apply Dimethomorph 50% WP (1g/L) OR Metalaxyl 8% + Mancozeb 64% WP (2.5g/L water).",
            chemicalTreatmentHi = "डाइमेथोमॉर्फ 50% WP (1 ग्राम/लीटर) या मेटालैक्सिल 8% + मैंकोजेब 64% WP का छिड़काव करें।",
            preventionTips = "Avoid overhead irrigation; space plants 60cm apart for aeration; use blight-resistant seeds.",
            preventionTipsHi = "फव्वारा सिंचाई से बचें; पौधों में 60 सेमी दूरी रखें; रोगरोधी किस्मों का चयन करें।"
        ),
        DiseaseCase(
            id = "dis_02",
            crop = "Wheat",
            cropHi = "गेहूं",
            diseaseName = "Wheat Yellow Rust",
            diseaseNameHi = "गेहूं का पीला रतुआ",
            scientificName = "Puccinia striiformis",
            confidencePercent = 94,
            severity = "Moderate",
            symptoms = "Bright yellow pustules forming linear stripes along leaf veins; leaves yellow powder on touch.",
            symptomsHi = "पत्तियों पर पीली धारियां; छूने पर हाथ में हल्दी जैसा पीला पाउडर लगना।",
            organicTreatment = "Spray fermented buttermilk (Khatta Chhachh) mixed with copper vessel extract (20ml/L).",
            organicTreatmentHi = "तांबे के बर्तन में रखा 5 दिन पुराना खट्टा छाछ (20 मिली/लीटर पानी) मिलाकर छिड़कें।",
            chemicalTreatment = "Spray Propiconazole 25% EC (Tilt) @ 1ml/L or Tebuconazole 25.9% EC @ 1ml/L immediately.",
            chemicalTreatmentHi = "प्रोपिकोनाज़ोल 25% EC (टिल्ट) 1 मिली/लीटर या टेबुकोनाज़ोल का तत्काल छिड़काव करें।",
            preventionTips = "Plant resistant cultivars like HD-2967, DBW-187; monitor northern borders in cold fog.",
            preventionTipsHi = "एचडी-2967 जैसी रोगरोधी किस्में लगाएं; शीतलहर के दौरान नियमित खेत की निगरानी करें।"
        ),
        DiseaseCase(
            id = "dis_03",
            crop = "Cotton",
            cropHi = "कपास",
            diseaseName = "Cotton Leaf Curl Virus",
            diseaseNameHi = "कपास का पत्ता मरोड़ रोग",
            scientificName = "Begomovirus (Whitefly)",
            confidencePercent = 91,
            severity = "High Alert",
            symptoms = "Upward/downward curling of leaf margins, vein thickening, leaf-like outgrowths below.",
            symptomsHi = "पत्तियों का मुड़ना, नसों का मोटा होना और पत्ते के नीचे छोटे पत्तीनुमा उभार बनना।",
            organicTreatment = "Neem Oil 10,000 PPM (3ml/L) + Soap solution to repel whitefly vectors organically.",
            organicTreatmentHi = "नीम तेल 10,000 पीपीएम (3 मिली/लीटर) शैम्पू/साबुन घोल के साथ छिड़कें।",
            chemicalTreatment = "Control whitefly: Spray Diafenthiuron 50% WP (1.2g/L) or Pyriproxyfen + Bifenthrin.",
            chemicalTreatmentHi = "सफेद मक्खी नियंत्रण हेतु डायफेंथियूरॉन 50% WP (1.2 ग्राम/लीटर) का छिड़काव करें।",
            preventionTips = "Eradicate alternate weed hosts around field bunds; install 20 yellow sticky cards/acre.",
            preventionTipsHi = "मेड़ों से खरपतवार नष्ट करें; प्रति एकड़ 20 पीले चिपचिपे कार्ड लगाएं।"
        ),
        DiseaseCase(
            id = "dis_04",
            crop = "Potato",
            cropHi = "आलू",
            diseaseName = "Potato Early Blight",
            diseaseNameHi = "आलू का अगेती झुलसा",
            scientificName = "Alternaria solani",
            confidencePercent = 95,
            severity = "Moderate",
            symptoms = "Concentric dark brown rings resembling target boards on older leaves; defoliation.",
            symptomsHi = "पुरानी पत्तियों पर गोल छल्लेदार गहरे कत्थई धब्बे (टारगेट बोर्ड जैसी आकृति)।",
            organicTreatment = "Apply Pseudomonas fluorescens bio-fungicide @ 10g/L water during early phase.",
            organicTreatmentHi = "स्यूडोमोनास फ्लोरेसेंस जैव फफूंदनाशी (10 ग्राम/लीटर) का छिड़काव करें।",
            chemicalTreatment = "Spray Chlorothalonil 75% WP @ 2g/L or Azoxystrobin + Difenoconazole @ 1ml/L.",
            chemicalTreatmentHi = "क्लोरोथालोनिल 75% WP (2 ग्राम/लीटर) या एजोक्सीस्ट्रोबिन + डाइफेनोकोनाज़ोल छिड़कें।",
            preventionTips = "Practice crop rotation; maintain balanced nitrogen:potash fertilization.",
            preventionTipsHi = "फसल चक्र अपनाएं; नाइट्रोजन व पोटाश का संतुलित प्रयोग करें।"
        )
    )

    val inputProducts = listOf(
        InputProduct("prod_01", "IFFCO Nano Urea Liquid (500 ml)", "इफको नैनो यूरिया तरल (500 मिली)", ProductCategory.FERTILIZERS, "IFFCO", 225.0, 250.0, 4.8, 428, "500 ml bottle (= 1 bag urea)", true, "Environment friendly nano-nitrogen. Increases crop yield by 8%.", "पर्यावरण अनुकूल नैनो नाइट्रोजन। उपज में 8% तक वृद्धि और 1 बोरी यूरिया की बचत।", true),
        InputProduct("prod_02", "Mahyco Hybrid Tomato Seeds", "माहिको संकर टमाटर बीज (अभिनव)", ProductCategory.SEEDS, "Mahyco Seeds Ltd", 850.0, 950.0, 4.9, 312, "10 grams packet", true, "High disease resistance to TYLCV and bacterial wilt. High firmness.", "वायरस व उकठा रोग के प्रति उच्च सहनशीलता। ठोस फल जो परिवहन के लिए उपयुक्त हैं।"),
        InputProduct("prod_03", "Kisan Shakti 16L Battery Sprayer", "किसान शक्ति 16 लीटर बैटरी स्प्रेयर", ProductCategory.TOOLS, "AgriEquip India", 2199.0, 2899.0, 4.7, 189, "16L Tank + 12V 8Ah Battery", true, "Heavy duty dual motor with 4 adjustable brass nozzles.", "डबल मोटर और 4 पीतल की नोजल के साथ। एक चार्ज में 25 टंकी छिड़काव।"),
        InputProduct("prod_04", "Bio-NPK Consortium Organic Liquid", "बायो-एनपीके कंसोर्टियम जैविक तरल", ProductCategory.FERTILIZERS, "National Bio-Agro", 380.0, 450.0, 4.6, 154, "1 Litre bottle", true, "Live microbial culture of Nitrogen fixers & Phosphate solubilizers.", "नाइट्रोजन, फास्फोरस व पोटाश उपलब्ध कराने वाले जीवित जीवाणु।"),
        InputProduct("prod_05", "Neem Gold Bio-Pesticide (10,000 PPM)", "नीम गोल्ड बायो-कीटनाशक (10,000 PPM)", ProductCategory.PESTICIDES, "Herbal Agro Tech", 420.0, 520.0, 4.8, 276, "1 Litre bottle", true, "Pure azadirachtin extract. Repels whiteflies, bollworms, aphids.", "शुद्ध अज़ाडिराक्टिन। सफेद मक्खी, सुंडी, माहू कीटों से प्राकृतिक सुरक्षा।", true),
        InputProduct("prod_06", "Basmati Rice Certified Seeds (1121)", "बासमती धान प्रमाणित बीज (पूसा-1121)", ProductCategory.SEEDS, "Pusa ICAR Certified", 1150.0, 1300.0, 4.9, 520, "10 kg bag", true, "Certified breeder seeds with 98% germination rate.", "98% अंकुरण क्षमता वाला प्रमाणित बीज।")
    )

    val communityPosts = mutableListOf(
        CommunityPost(
            id = "post_01",
            authorName = "Rameshwar Patil",
            district = "Nashik",
            cropTag = "Onion",
            title = "Best storage practice for Nashik red onion in monsoon?",
            question = "Brothers, my onion harvest is around 200 quintals. What is the best low-cost chawl ventilation setup to avoid rotting before market rates peak?",
            upvotes = 34,
            timestamp = "3 hours ago",
            replies = listOf(
                CommunityReply("rep_01", "Dr. Ashok Kulkarni (Agronomist)", true, "Ensure bamboo bottom slats are 1.5 ft above ground. Use 50% shade netting on outer walls to block direct heat while keeping airflow 24x7.", "2 hours ago", 19),
                CommunityReply("rep_02", "Santosh Gaikwad", false, "Dusted with light dry sulphur powder on the top layer. Zero fungus for 4 months last season!", "1 hour ago", 8)
            )
        ),
        CommunityPost(
            id = "post_02",
            authorName = "Gurpreet Singh",
            district = "Ludhiana",
            cropTag = "Wheat",
            title = "Happy Seeder vs Super Seeder for wheat sowing?",
            question = "Planning wheat sowing next week. Soil has heavy stubble moisture. Which machine gives better germination without burning stubble?",
            upvotes = 42,
            timestamp = "Yesterday",
            replies = listOf(
                CommunityReply("rep_03", "PAU Agronomy Cell", true, "Super Seeder mixes chopped stubble into soil nicely in single pass if soil moisture is optimum.", "18 hours ago", 27)
            )
        )
    )
}
