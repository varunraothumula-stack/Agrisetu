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
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.AppLanguage
import com.example.ui.theme.*

@Composable
fun BusinessModelScreen(
    currentLanguage: AppLanguage,
    onBack: () -> Unit
) {
    val isHindi = currentLanguage == AppLanguage.HINDI

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(AgriBackground),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Top Back & Header
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(
                    onClick = onBack,
                    modifier = Modifier.testTag("back_from_business_model")
                ) {
                    Icon(Icons.Filled.ArrowBack, contentDescription = "Back", tint = AgriGreenPrimary)
                }
                Spacer(modifier = Modifier.width(6.dp))
                Column {
                    Text(
                        text = if (isHindi) "एग्रीसेतु: व्यापार मॉडल व परियोजना विवरण" else "AgriSetu: B.Tech Business Case Study",
                        fontSize = 17.sp,
                        fontWeight = FontWeight.Bold,
                        color = AgriTextPrimary
                    )
                    Text(
                        text = "Farm to Market Agritech Monetization & Impact",
                        fontSize = 11.sp,
                        color = AgriTextMuted
                    )
                }
            }
        }

        // Executive Summary
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "🎯 Executive Summary",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = AgriGreenPrimary
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = if (isHindi) {
                            "एग्रीसेतु भारतीय किसानों को सीधे सत्यापित खरीदारों से जोड़कर बिचौलियों के चंगुल से मुक्त करता है। यह एक व्यवहार्य 'प्लेटफ़ॉर्म-एज़-ए-सर्विस' (PaaS) मॉडल पर संचालित है जो निष्पक्ष मूल्य, एआई रोग पहचान और इनपुट बाज़ार को एक सूत्र में पिरोता है।"
                        } else {
                            "AgriSetu is an integrated digital farm-to-fork ecosystem empowering 140M+ Indian smallholder farmers. By eliminating 4-5 layers of exploitative middlemen, AgriSetu improves farmer net price realization by 15-22% while providing bulk institutional buyers traceable, graded agricultural produce."
                        },
                        fontSize = 13.sp,
                        color = AgriTextSecondary,
                        lineHeight = 18.sp
                    )
                }
            }
        }

        // Section 1: The Problem We Solve
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Filled.ReportProblem, contentDescription = null, tint = AgriError)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = if (isHindi) "1. हम किन समस्याओं का समाधान करते हैं?" else "1. The Problems We Solve",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            color = AgriTextPrimary
                        )
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    val problems = listOf(
                        Pair(
                            "📉 Low Farmer Price Realization",
                            "Farmers traditionally receive only 30% to 40% of the consumer retail rupee due to excessive markups by village aggregators and commission agents (arhtiyas)."
                        ),
                        Pair(
                            "⛓️ Middlemen Dependency & Cartelization",
                            "Farmers are trapped in credit-linked sales where local lenders force them to sell produce at distressed rates during harvest gluts."
                        ),
                        Pair(
                            "🔬 Lack of Timely Agronomic Advice",
                            "Over 65% of crop losses occur because farmers misidentify pests and spend heavily on ineffective or fake chemicals."
                        ),
                        Pair(
                            "🌦️ Weather & Post-Harvest Losses",
                            "Over ₹92,000 Crore worth of agricultural produce is lost annually in India due to lack of local storage and unpredictable rain."
                        ),
                        Pair(
                            "🔍 Buyer Sourcing Friction",
                            "Food processors and modern retailers struggle to find consistent, quality-graded bulk produce without paying heavy APMC broker fees."
                        )
                    )

                    problems.forEach { item ->
                        Surface(
                            color = AgriSurfaceVariant,
                            shape = RoundedCornerShape(10.dp),
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(bottom = 8.dp)
                        ) {
                            Column(modifier = Modifier.padding(10.dp)) {
                                Text(
                                    text = item.first,
                                    fontSize = 13.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = AgriTextPrimary
                                )
                                Spacer(modifier = Modifier.height(3.dp))
                                Text(
                                    text = item.second,
                                    fontSize = 12.sp,
                                    color = AgriTextSecondary,
                                    lineHeight = 16.sp
                                )
                            }
                        }
                    }
                }
            }
        }

        // Section 2: Viable Business & Monetization Model
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Filled.AccountBalance, contentDescription = null, tint = AgriGreenPrimary)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = if (isHindi) "2. एग्रीसेतु के 4 राजस्व स्रोत" else "2. AgriSetu 4-Pillar Revenue Model",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            color = AgriTextPrimary
                        )
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    val streams = listOf(
                        Triple(
                            "1. 5% Marketplace Transaction Commission",
                            "Charged on closed produce transactions between farmers and buyers. Both parties save money (traditional mandi takes 8-12% broker fee).",
                            "₹38,450 earned in current demo"
                        ),
                        Triple(
                            "2. AgriSetu Krishi Pro Advisory (₹499/Year)",
                            "Annual subscription offering priority agronomist consultation, 10-day precision hyper-local weather alerts, and satellite soil health tracking.",
                            "High retention SaaS recurring revenue"
                        ),
                        Triple(
                            "3. Logistics & Quality Assay Fee (₹50/Quintal)",
                            "End-to-end farmgate pickup, digital moisture testing, weighing bridge certification, and delivery orchestration.",
                            "Direct operational margin"
                        ),
                        Triple(
                            "4. Sponsored Input Store Listings & Ads",
                            "Certified seed, bio-fertilizer, and equipment manufacturers pay to promote certified products directly to active farmers.",
                            "B2B advertising revenue"
                        )
                    )

                    streams.forEach { stream ->
                        Surface(
                            color = AgriGreenContainer,
                            shape = RoundedCornerShape(10.dp),
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(bottom = 8.dp)
                        ) {
                            Column(modifier = Modifier.padding(12.dp)) {
                                Text(
                                    text = stream.first,
                                    fontSize = 13.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = AgriOnGreenContainer
                                )
                                Spacer(modifier = Modifier.height(3.dp))
                                Text(
                                    text = stream.second,
                                    fontSize = 12.sp,
                                    color = AgriTextSecondary,
                                    lineHeight = 16.sp
                                )
                                Spacer(modifier = Modifier.height(4.dp))
                                Surface(
                                    color = Color.White,
                                    shape = RoundedCornerShape(4.dp)
                                ) {
                                    Text(
                                        text = "⚡ " + stream.third,
                                        fontSize = 10.sp,
                                        fontWeight = FontWeight.Bold,
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

        // Section 3: Testimonials
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Filled.FormatQuote, contentDescription = null, tint = AgriAmberSecondary)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = if (isHindi) "3. किसान व खरीदार अनुभव (प्रशंसापत्र)" else "3. Stakeholder Testimonials",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            color = AgriTextPrimary
                        )
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    val testimonials = listOf(
                        Triple(
                            "Rameshwar Patil (Nashik, Maharashtra)",
                            "Onion Farmer • 200 Quintals Sold",
                            "\"I used to travel 25 km to Lasalgaon Mandi, waiting two days in line only for brokers to quote distressed prices. Through AgriSetu, a verified food processor in Mumbai inspected my lot photos and paid ₹1,950/quintal—getting me ₹350 extra per quintal!\""
                        ),
                        Triple(
                            "Gurpreet Singh (Ludhiana, Punjab)",
                            "Wheat Farmer • 150 Quintals Listed",
                            "\"The AI disease doctor saved my crop! When yellow powder appeared on my wheat leaves, the scanner identified Yellow Rust within 5 seconds and prescribed Propiconazole. Yield was completely protected.\""
                        ),
                        Triple(
                            "Amit Shah (Pune, Maharashtra)",
                            "Head of Sourcing, FreshOrganics Retail",
                            "\"Sourcing directly through AgriSetu removed 3 layers of mandi middlemen. We get farmgate-traceable produce with reliable moisture checks and 12% lower procurement costs.\""
                        )
                    )

                    testimonials.forEach { t ->
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(bottom = 10.dp),
                            shape = RoundedCornerShape(12.dp),
                            colors = CardDefaults.cardColors(containerColor = AgriSurfaceVariant)
                        ) {
                            Column(modifier = Modifier.padding(12.dp)) {
                                Text(
                                    text = t.first,
                                    fontSize = 13.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = AgriTextPrimary
                                )
                                Text(
                                    text = t.second,
                                    fontSize = 11.sp,
                                    color = AgriGreenPrimary,
                                    fontWeight = FontWeight.SemiBold
                                )
                                Spacer(modifier = Modifier.height(6.dp))
                                Text(
                                    text = t.third,
                                    fontSize = 12.sp,
                                    color = AgriTextSecondary,
                                    lineHeight = 17.sp
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}
