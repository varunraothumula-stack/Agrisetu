package com.example.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
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
import com.example.data.SampleData
import com.example.model.AppLanguage
import com.example.model.AppStrings
import com.example.model.DiseaseCase
import com.example.ui.theme.*
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun DiseaseScannerScreen(
    currentLanguage: AppLanguage
) {
    val isHindi = currentLanguage == AppLanguage.HINDI
    val cases = SampleData.diseaseCases
    val coroutineScope = rememberCoroutineScope()

    var activeCase by remember { mutableStateOf<DiseaseCase?>(cases.first()) }
    var isScanning by remember { mutableStateOf(false) }
    var userSimulatedPhotoSelected by remember { mutableStateOf(false) }

    fun triggerScan(selectedCase: DiseaseCase) {
        coroutineScope.launch {
            isScanning = true
            delay(1200) // realistic AI inference delay
            activeCase = selectedCase
            isScanning = false
            userSimulatedPhotoSelected = true
        }
    }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(AgriBackground),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Scanner Header
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
                                text = AppStrings.t("disease_scanner_title", currentLanguage),
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Bold,
                                color = AgriTextPrimary
                            )
                            Spacer(modifier = Modifier.height(2.dp))
                            Text(
                                text = AppStrings.t("scanner_subtitle", currentLanguage),
                                fontSize = 12.sp,
                                color = AgriTextSecondary
                            )
                        }
                        Box(
                            modifier = Modifier
                                .size(44.dp)
                                .clip(CircleShape)
                                .background(Color(0xFFEDE7F6)),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Filled.Biotech,
                                contentDescription = null,
                                tint = Color(0xFF6A1B9A),
                                modifier = Modifier.size(24.dp)
                            )
                        }
                    }
                }
            }
        }

        // Camera / Upload Frame
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(180.dp)
                            .clip(RoundedCornerShape(12.dp))
                            .background(AgriSurfaceVariant)
                            .border(2.dp, AgriGreenLight.copy(alpha = 0.4f), RoundedCornerShape(12.dp)),
                        contentAlignment = Alignment.Center
                    ) {
                        if (isScanning) {
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                CircularProgressIndicator(color = AgriGreenPrimary)
                                Spacer(modifier = Modifier.height(10.dp))
                                Text(
                                    text = AppStrings.t("analyzing_leaf", currentLanguage),
                                    fontSize = 13.sp,
                                    fontWeight = FontWeight.SemiBold,
                                    color = AgriGreenPrimary
                                )
                            }
                        } else {
                            Column(
                                horizontalAlignment = Alignment.CenterHorizontally,
                                modifier = Modifier.padding(16.dp)
                            ) {
                                Icon(
                                    imageVector = if (userSimulatedPhotoSelected) Icons.Filled.Spa else Icons.Filled.AddAPhoto,
                                    contentDescription = null,
                                    tint = if (userSimulatedPhotoSelected) AgriGreenPrimary else AgriTextMuted,
                                    modifier = Modifier.size(46.dp)
                                )
                                Spacer(modifier = Modifier.height(8.dp))
                                Text(
                                    text = if (userSimulatedPhotoSelected) {
                                        "${activeCase?.crop ?: "Leaf"} Specimen Inspected"
                                    } else {
                                        if (isHindi) "खेत में रोगी पत्ते की फोटो लें" else "Take or upload photo of infected leaf"
                                    },
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = AgriTextPrimary
                                )
                                Text(
                                    text = if (isHindi) "एआई रोगजनक फफूंद, वायरस व कीटों की पहचान करेगा" else "Powered by Gemini Vision Model for agricultural pathology",
                                    fontSize = 11.sp,
                                    color = AgriTextMuted
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Button(
                            onClick = { triggerScan(cases.random()) },
                            colors = ButtonDefaults.buttonColors(containerColor = AgriGreenPrimary),
                            shape = RoundedCornerShape(10.dp),
                            modifier = Modifier
                                .weight(1f)
                                .height(44.dp)
                                .testTag("camera_capture_btn")
                        ) {
                            Icon(Icons.Filled.CameraAlt, contentDescription = null, modifier = Modifier.size(18.dp))
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(if (isHindi) "कैमरा स्कैन" else "Scan Leaf", fontSize = 13.sp, fontWeight = FontWeight.Bold)
                        }

                        OutlinedButton(
                            onClick = { triggerScan(cases.first()) },
                            shape = RoundedCornerShape(10.dp),
                            colors = ButtonDefaults.outlinedButtonColors(contentColor = AgriGreenPrimary),
                            modifier = Modifier
                                .weight(1f)
                                .height(44.dp)
                                .testTag("upload_gallery_btn")
                        ) {
                            Icon(Icons.Filled.UploadFile, contentDescription = null, modifier = Modifier.size(18.dp))
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(if (isHindi) "फोटो चुनें" else "Upload Photo", fontSize = 13.sp)
                        }
                    }
                }
            }
        }

        // Disease Case Selection Strip
        item {
            Column {
                Text(
                    text = AppStrings.t("upload_or_select", currentLanguage),
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    color = AgriTextPrimary
                )
                Spacer(modifier = Modifier.height(8.dp))

                LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    items(cases) { disCase ->
                        val isSelected = activeCase?.id == disCase.id
                        Card(
                            modifier = Modifier
                                .clickable { triggerScan(disCase) }
                                .testTag("disease_chip_${disCase.id}"),
                            shape = RoundedCornerShape(10.dp),
                            colors = CardDefaults.cardColors(
                                containerColor = if (isSelected) AgriGreenContainer else Color.White
                            ),
                            border = if (isSelected) CardDefaults.outlinedCardBorder().copy(brush = androidx.compose.ui.graphics.SolidColor(AgriGreenPrimary)) else null,
                            elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
                        ) {
                            Row(
                                modifier = Modifier.padding(horizontal = 10.dp, vertical = 8.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = if (isHindi) disCase.diseaseNameHi else disCase.diseaseName,
                                    fontSize = 12.sp,
                                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                                    color = if (isSelected) AgriGreenPrimary else AgriTextPrimary
                                )
                            }
                        }
                    }
                }
            }
        }

        // AI Diagnostic Result Card
        activeCase?.let { diagnosis ->
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    elevation = CardDefaults.cardElevation(defaultElevation = 3.dp)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column {
                                Text(
                                    text = AppStrings.t("likely_diagnosis", currentLanguage),
                                    fontSize = 12.sp,
                                    color = AgriTextMuted
                                )
                                Text(
                                    text = if (isHindi) diagnosis.diseaseNameHi else diagnosis.diseaseName,
                                    fontSize = 18.sp,
                                    fontWeight = FontWeight.ExtraBold,
                                    color = AgriError
                                )
                                Text(
                                    text = "Pathogen: ${diagnosis.scientificName}",
                                    fontSize = 11.sp,
                                    color = AgriTextSecondary
                                )
                            }

                            Surface(
                                color = Color(0xFFE8F5E9),
                                shape = RoundedCornerShape(8.dp)
                            ) {
                                Column(
                                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
                                    horizontalAlignment = Alignment.CenterHorizontally
                                ) {
                                    Text(
                                        text = "${diagnosis.confidencePercent}%",
                                        fontSize = 16.sp,
                                        fontWeight = FontWeight.ExtraBold,
                                        color = AgriGreenPrimary
                                    )
                                    Text(
                                        text = AppStrings.t("confidence", currentLanguage),
                                        fontSize = 9.sp,
                                        color = AgriTextMuted
                                    )
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(14.dp))
                        Divider(color = AgriOutlineVariant)
                        Spacer(modifier = Modifier.height(12.dp))

                        // Key Symptoms
                        Text(
                            text = "🔍 ${AppStrings.t("symptoms", currentLanguage)}:",
                            fontWeight = FontWeight.Bold,
                            fontSize = 13.sp,
                            color = AgriTextPrimary
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = if (isHindi) diagnosis.symptomsHi else diagnosis.symptoms,
                            fontSize = 12.sp,
                            color = AgriTextSecondary,
                            lineHeight = 17.sp
                        )

                        Spacer(modifier = Modifier.height(14.dp))

                        // Organic Treatment (Jaivik Upchar)
                        Surface(
                            color = Color(0xFFE8F5E9),
                            shape = RoundedCornerShape(10.dp),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Column(modifier = Modifier.padding(12.dp)) {
                                Text(
                                    text = "🌿 ${AppStrings.t("organic_treatment", currentLanguage)}:",
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 13.sp,
                                    color = AgriGreenPrimary
                                )
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(
                                    text = if (isHindi) diagnosis.organicTreatmentHi else diagnosis.organicTreatment,
                                    fontSize = 12.sp,
                                    color = AgriOnGreenContainer,
                                    lineHeight = 17.sp
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(10.dp))

                        // Chemical Treatment
                        Surface(
                            color = Color(0xFFFFF3E0),
                            shape = RoundedCornerShape(10.dp),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Column(modifier = Modifier.padding(12.dp)) {
                                Text(
                                    text = "🧪 ${AppStrings.t("chemical_treatment", currentLanguage)}:",
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 13.sp,
                                    color = Color(0xFFE65100)
                                )
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(
                                    text = if (isHindi) diagnosis.chemicalTreatmentHi else diagnosis.chemicalTreatment,
                                    fontSize = 12.sp,
                                    color = Color(0xFF4E2600),
                                    lineHeight = 17.sp
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(10.dp))

                        // Prevention Tips
                        Surface(
                            color = AgriSurfaceVariant,
                            shape = RoundedCornerShape(10.dp),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Column(modifier = Modifier.padding(12.dp)) {
                                Text(
                                    text = "🛡️ ${AppStrings.t("prevention_tips", currentLanguage)}:",
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 13.sp,
                                    color = AgriTextPrimary
                                )
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(
                                    text = if (isHindi) diagnosis.preventionTipsHi else diagnosis.preventionTips,
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
