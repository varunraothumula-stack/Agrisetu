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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.SampleData
import com.example.model.*
import com.example.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AdvisoryScreen(
    currentLanguage: AppLanguage,
    selectedDistrict: String
) {
    val isHindi = currentLanguage == AppLanguage.HINDI
    val forecast = SampleData.weatherForecast
    val advisories = SampleData.cropAdvisories

    // AI Consultant State
    var selectedCrop by remember { mutableStateOf("Wheat") }
    var selectedStage by remember { mutableStateOf("Vegetative Growth") }
    var selectedProblem by remember { mutableStateOf("Yellow leaves / Stunted growth") }
    var aiAdviceResult by remember { mutableStateOf<String?>(null) }
    var isGeneratingAdvice by remember { mutableStateOf(false) }

    val cropOptions = listOf("Wheat", "Tomato", "Onion", "Rice", "Cotton", "Potato", "Maize")
    val stageOptions = listOf("Germination / Sowing", "Vegetative Growth", "Flowering & Tillering", "Fruit / Grain Development", "Maturity & Harvest")
    val problemOptions = listOf(
        "Yellow leaves / Stunted growth",
        "Pest infestation (aphids / borers)",
        "Water stress / Curling leaves",
        "White or dark spots on foliage",
        "Fruit drop / Low flowering",
        "Nutrient deficiency guidance"
    )

    fun generateExpertAdvice() {
        isGeneratingAdvice = true
        // Realistic Agronomic recommendation generator
        aiAdviceResult = when (selectedCrop) {
            "Wheat" -> if (isHindi) {
                "【कृषि विशेषज्ञ एआई परामर्श】\n• अवस्था: $selectedStage\n• समस्या: $selectedProblem\n\n📌 तात्कालिक उपचार:\n1. 19:19:19 (NPK घुलनशील) @ 1 किग्रा/एकड़ 150 लीटर पानी में मिलाकर पर्णीय छिड़काव करें।\n2. यदि मिट्टी में नमी कम है, तो हल्की सिंचाई अवश्य करें।\n3. जिंक सल्फेट 33% (500 ग्राम/एकड़) का प्रयोग पीलापन दूर करने में अत्यधिक प्रभावी रहेगा।"
            } else {
                "【AgriSetu AI Agronomy Diagnosis】\n• Stage: $selectedStage\n• Issue: $selectedProblem\n\n📌 Recommended Action Plan:\n1. Spray 19:19:19 (Water-Soluble NPK) @ 1 kg/acre in 150L water for rapid chlorosis recovery.\n2. Ensure optimum moisture in soil; avoid deep stagnation.\n3. Add Zinc Sulphate 33% (500g/acre) to correct micronutrient deficiency."
            }
            "Tomato" -> if (isHindi) {
                "【कृषि विशेषज्ञ एआई परामर्श】\n• अवस्था: $selectedStage\n• समस्या: $selectedProblem\n\n📌 तात्कालिक उपचार:\n1. फफूंद जनित रोगों से सुरक्षा हेतु मैंकोजेब 75% WP (2.5 ग्राम/लीटर) का तुरंत छिड़काव करें।\n2. फल गिरना रोकने हेतु प्लानोफिक्स (अल्फा नेफ्थाइल एसिटिक एसिड) 4 मिली प्रति 15 लीटर टंकी में मिलाएं।\n3. प्रति एकड़ 15 पीले चिपचिपे कार्ड लगाएं।"
            } else {
                "【AgriSetu AI Agronomy Diagnosis】\n• Stage: $selectedStage\n• Issue: $selectedProblem\n\n📌 Recommended Action Plan:\n1. Apply Mancozeb 75% WP @ 2.5g/L water to curb potential fungal leaf spots.\n2. Spray Planofix @ 4ml per 15L tank to prevent premature flower and fruit drop.\n3. Install 15 yellow sticky traps per acre to intercept whitefly and thrip vectors."
            }
            "Onion" -> if (isHindi) {
                "【कृषि विशेषज्ञ एआई परामर्श】\n• अवस्था: $selectedStage\n• समस्या: $selectedProblem\n\n📌 तात्कालिक उपचार:\n1. थ्रिप्स नियंत्रण हेतु फिप्रोनिल 5% SC (2 मिली/लीटर) छिड़कें।\n2. कंद के विकास हेतु सल्फेट ऑफ पोटाश (00:00:50) @ 5 ग्राम/लीटर का छिड़काव करें।"
            } else {
                "【AgriSetu AI Agronomy Diagnosis】\n• Stage: $selectedStage\n• Issue: $selectedProblem\n\n📌 Recommended Action Plan:\n1. Spray Fipronil 5% SC @ 2ml/L to control onion thrips hiding in inner leaf sheaths.\n2. Apply Sulphate of Potash (00:00:50) @ 5g/L to enhance bulb diameter and pungency."
            }
            else -> if (isHindi) {
                "【कृषि विशेषज्ञ एआई परामर्श】\n• अवस्था: $selectedStage\n• समस्या: $selectedProblem\n\n📌 तात्कालिक उपचार:\n1. नीम तेल (10,000 PPM) 3 मिली/लीटर पानी में घोलकर प्राथमिक जैविक छिड़काव करें।\n2. जैविक एनपीके तरल खाद ड्रिप या जड़ों के पास दें।"
            } else {
                "【AgriSetu AI Agronomy Diagnosis】\n• Stage: $selectedStage\n• Issue: $selectedProblem\n\n📌 Recommended Action Plan:\n1. Spray Pure Neem Oil (10,000 PPM) @ 3ml/L water as organic first response.\n2. Ensure balanced moisture and apply Bio-NPK consortium at the root zone."
            }
        }
        isGeneratingAdvice = false
    }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(AgriBackground),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Title banner
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
                                text = "${AppStrings.t("advisory_title", currentLanguage)} ($selectedDistrict)",
                                fontSize = 17.sp,
                                fontWeight = FontWeight.Bold,
                                color = AgriTextPrimary
                            )
                            Text(
                                text = if (isHindi) "स्थानीय मौसम आधारित समय पर वैज्ञानिक कृषि परामर्श" else "Meteorological crop alerts & localized agronomist guidance",
                                fontSize = 12.sp,
                                color = AgriTextSecondary
                            )
                        }
                        Icon(
                            imageVector = Icons.Filled.CloudDone,
                            contentDescription = null,
                            tint = AgriGreenPrimary,
                            modifier = Modifier.size(28.dp)
                        )
                    }
                }
            }
        }

        // 5-Day Weather Forecast Horizontal Cards
        item {
            Column {
                Text(
                    text = AppStrings.t("five_day_forecast", currentLanguage),
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = AgriTextPrimary
                )
                Spacer(modifier = Modifier.height(10.dp))

                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    items(forecast) { day ->
                        Card(
                            modifier = Modifier.width(140.dp),
                            shape = RoundedCornerShape(14.dp),
                            colors = CardDefaults.cardColors(containerColor = Color.White),
                            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                        ) {
                            Column(
                                modifier = Modifier.padding(12.dp),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Text(
                                    text = if (isHindi) day.dayNameHi else day.dayName,
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = AgriTextPrimary
                                )
                                Text(
                                    text = day.date,
                                    fontSize = 11.sp,
                                    color = AgriTextMuted
                                )
                                Spacer(modifier = Modifier.height(6.dp))
                                Icon(
                                    imageVector = if (day.rainChance > 30) Icons.Filled.WaterDrop else Icons.Filled.WbSunny,
                                    contentDescription = null,
                                    tint = if (day.rainChance > 30) Color(0xFF1976D2) else Color(0xFFF57F17),
                                    modifier = Modifier.size(28.dp)
                                )
                                Spacer(modifier = Modifier.height(6.dp))
                                Text(
                                    text = "${day.tempMax}° / ${day.tempMin}°",
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = AgriTextPrimary
                                )
                                Text(
                                    text = if (isHindi) day.conditionHi else day.condition,
                                    fontSize = 10.sp,
                                    color = AgriTextSecondary,
                                    maxLines = 1
                                )
                                Spacer(modifier = Modifier.height(6.dp))
                                Surface(
                                    color = Color(0xFFE8F5E9),
                                    shape = RoundedCornerShape(4.dp)
                                ) {
                                    Text(
                                        text = "${day.rainChance}% rain",
                                        fontSize = 10.sp,
                                        fontWeight = FontWeight.SemiBold,
                                        color = AgriGreenPrimary,
                                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }

        // Active Crop Advisories
        item {
            Column {
                Text(
                    text = AppStrings.t("active_advisories", currentLanguage),
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = AgriTextPrimary
                )
                Spacer(modifier = Modifier.height(10.dp))

                advisories.forEach { adv ->
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 10.dp),
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
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    val catIcon = when (adv.category) {
                                        AdvisoryCategory.IRRIGATION -> Icons.Filled.WaterDrop
                                        AdvisoryCategory.FERTILIZER -> Icons.Filled.Science
                                        AdvisoryCategory.PEST -> Icons.Filled.PestControl
                                        AdvisoryCategory.HARVEST -> Icons.Filled.Agriculture
                                    }
                                    Icon(
                                        imageVector = catIcon,
                                        contentDescription = null,
                                        tint = AgriGreenPrimary,
                                        modifier = Modifier.size(18.dp)
                                    )
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Text(
                                        text = "${if (isHindi) adv.cropHi else adv.crop} • ${if (isHindi) adv.category.labelHi else adv.category.labelEn}",
                                        fontSize = 12.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = AgriGreenPrimary
                                    )
                                }

                                val (urgColor, urgLabel) = when (adv.urgency) {
                                    AdvisoryUrgency.CRITICAL -> Pair(AgriError, if (isHindi) "अति आवश्यक" else "Critical")
                                    AdvisoryUrgency.WARNING -> Pair(AgriWarning, if (isHindi) "चेतावनी" else "Alert")
                                    AdvisoryUrgency.NORMAL -> Pair(AgriSuccess, if (isHindi) "सामान्य" else "Normal")
                                }
                                Surface(
                                    color = urgColor.copy(alpha = 0.12f),
                                    shape = RoundedCornerShape(4.dp)
                                ) {
                                    Text(
                                        text = urgLabel,
                                        fontSize = 10.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = urgColor,
                                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                    )
                                }
                            }

                            Spacer(modifier = Modifier.height(6.dp))
                            Text(
                                text = if (isHindi) adv.titleHi else adv.title,
                                fontSize = 15.sp,
                                fontWeight = FontWeight.Bold,
                                color = AgriTextPrimary
                            )
                            Text(
                                text = "Stage: ${if (isHindi) adv.stageHi else adv.stage}",
                                fontSize = 11.sp,
                                color = AgriTextMuted
                            )
                            Spacer(modifier = Modifier.height(6.dp))
                            Text(
                                text = if (isHindi) adv.adviceHi else adv.advice,
                                fontSize = 13.sp,
                                color = AgriTextSecondary,
                                lineHeight = 18.sp
                            )
                        }
                    }
                }
            }
        }

        // AI-Style Advisory Consultation Box
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(defaultElevation = 3.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Filled.Psychology,
                            contentDescription = null,
                            tint = Color(0xFF6A1B9A),
                            modifier = Modifier.size(24.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = AppStrings.t("ai_consultant_title", currentLanguage),
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            color = AgriTextPrimary
                        )
                    }
                    Text(
                        text = if (isHindi) "फसल, अवस्था और समस्या चुनें - एआई तत्काल सटीक उपचार बताएगा" else "Select your crop, growth stage, and observed symptoms for instant diagnosis",
                        fontSize = 12.sp,
                        color = AgriTextMuted
                    )

                    Spacer(modifier = Modifier.height(14.dp))

                    // Crop Selection dropdown/chips
                    Text(
                        text = AppStrings.t("select_crop", currentLanguage),
                        fontSize = 12.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = AgriTextPrimary
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    LazyRow(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                        items(cropOptions) { crop ->
                            FilterChip(
                                selected = selectedCrop == crop,
                                onClick = { selectedCrop = crop },
                                label = { Text(crop, fontSize = 12.sp) },
                                colors = FilterChipDefaults.filterChipColors(
                                    selectedContainerColor = AgriGreenPrimary,
                                    selectedLabelColor = Color.White
                                )
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    // Growth Stage
                    Text(
                        text = AppStrings.t("select_stage", currentLanguage),
                        fontSize = 12.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = AgriTextPrimary
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    LazyRow(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                        items(stageOptions) { stage ->
                            FilterChip(
                                selected = selectedStage == stage,
                                onClick = { selectedStage = stage },
                                label = { Text(stage, fontSize = 11.sp) },
                                colors = FilterChipDefaults.filterChipColors(
                                    selectedContainerColor = AgriGreenPrimary,
                                    selectedLabelColor = Color.White
                                )
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    // Problem
                    Text(
                        text = AppStrings.t("select_problem", currentLanguage),
                        fontSize = 12.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = AgriTextPrimary
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    problemOptions.forEach { problem ->
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 2.dp)
                        ) {
                            RadioButton(
                                selected = selectedProblem == problem,
                                onClick = { selectedProblem = problem },
                                colors = RadioButtonDefaults.colors(selectedColor = AgriGreenPrimary)
                            )
                            Text(
                                text = problem,
                                fontSize = 12.sp,
                                color = AgriTextPrimary
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    Button(
                        onClick = { generateExpertAdvice() },
                        colors = ButtonDefaults.buttonColors(containerColor = AgriGreenPrimary),
                        shape = RoundedCornerShape(10.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(48.dp)
                            .testTag("get_ai_advice_btn")
                    ) {
                        Icon(Icons.Filled.AutoAwesome, contentDescription = null)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = AppStrings.t("get_ai_advice", currentLanguage),
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }

                    // Result card
                    aiAdviceResult?.let { advice ->
                        Spacer(modifier = Modifier.height(14.dp))
                        Surface(
                            color = AgriGreenContainer,
                            shape = RoundedCornerShape(12.dp),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Column(modifier = Modifier.padding(14.dp)) {
                                Text(
                                    text = advice,
                                    fontSize = 13.sp,
                                    color = AgriOnGreenContainer,
                                    lineHeight = 18.sp
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}
