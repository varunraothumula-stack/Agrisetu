package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
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
import com.example.model.AppLanguage
import com.example.model.AppStrings
import com.example.model.CommunityPost
import com.example.ui.theme.*

@Composable
fun CommunityScreen(
    currentLanguage: AppLanguage,
    posts: List<CommunityPost>,
    onAddQuestion: (title: String, question: String, cropTag: String) -> Unit,
    onAddReply: (postId: String, text: String) -> Unit,
    onUpvote: (postId: String) -> Unit
) {
    val isHindi = currentLanguage == AppLanguage.HINDI

    var showAskDialog by remember { mutableStateOf(false) }
    var replyingToPostId by remember { mutableStateOf<String?>(null) }
    var replyText by remember { mutableStateOf("") }

    // Ask Question Form State
    var newTitle by remember { mutableStateOf("") }
    var newQuestion by remember { mutableStateOf("") }
    var newCropTag by remember { mutableStateOf("Wheat") }

    Scaffold(
        floatingActionButton = {
            ExtendedFloatingActionButton(
                onClick = { showAskDialog = true },
                containerColor = AgriGreenPrimary,
                contentColor = Color.White,
                icon = { Icon(Icons.Filled.EditNote, contentDescription = null) },
                text = {
                    Text(
                        text = AppStrings.t("ask_question_btn", currentLanguage),
                        fontWeight = FontWeight.Bold,
                        fontSize = 13.sp
                    )
                },
                modifier = Modifier.testTag("fab_ask_community")
            )
        }
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .background(AgriBackground),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Header
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
                                    text = AppStrings.t("community_title", currentLanguage),
                                    fontSize = 18.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = AgriTextPrimary
                                )
                                Text(
                                    text = if (isHindi) "देशभर के साथी किसानों व कृषि वैज्ञानिकों से चर्चा करें" else "Peer-to-peer farmer knowledge exchange & agronomist replies",
                                    fontSize = 12.sp,
                                    color = AgriTextSecondary
                                )
                            }
                            Icon(
                                imageVector = Icons.Filled.Forum,
                                contentDescription = null,
                                tint = AgriGreenPrimary,
                                modifier = Modifier.size(28.dp)
                            )
                        }
                    }
                }
            }

            // Posts Feed
            items(posts) { post ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("post_card_${post.id}"),
                    shape = RoundedCornerShape(14.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                ) {
                    Column(modifier = Modifier.padding(14.dp)) {
                        // Author & Crop Tag Row
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text(
                                    text = "👨‍🌾 ${post.authorName}",
                                    fontSize = 13.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = AgriTextPrimary
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(
                                    text = "(${post.district})",
                                    fontSize = 11.sp,
                                    color = AgriTextMuted
                                )
                            }

                            Surface(
                                color = AgriGreenContainer,
                                shape = RoundedCornerShape(4.dp)
                            ) {
                                Text(
                                    text = "🌾 ${post.cropTag}",
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = AgriGreenPrimary,
                                    modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = post.title,
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Bold,
                            color = AgriTextPrimary
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = post.question,
                            fontSize = 13.sp,
                            color = AgriTextSecondary,
                            lineHeight = 18.sp
                        )

                        Spacer(modifier = Modifier.height(10.dp))

                        // Upvote & Reply bar
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                TextButton(
                                    onClick = { onUpvote(post.id) },
                                    contentPadding = PaddingValues(horizontal = 8.dp, vertical = 4.dp),
                                    modifier = Modifier.testTag("upvote_btn_${post.id}")
                                ) {
                                    Icon(
                                        imageVector = Icons.Filled.ThumbUp,
                                        contentDescription = "Upvote",
                                        tint = AgriGreenPrimary,
                                        modifier = Modifier.size(16.dp)
                                    )
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Text(
                                        text = "${post.upvotes}",
                                        fontSize = 12.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = AgriGreenPrimary
                                    )
                                }

                                Spacer(modifier = Modifier.width(12.dp))
                                Text(
                                    text = "💬 ${post.replies.size} ${AppStrings.t("answers_count", currentLanguage)}",
                                    fontSize = 12.sp,
                                    color = AgriTextMuted
                                )
                            }

                            TextButton(
                                onClick = { replyingToPostId = post.id },
                                modifier = Modifier.testTag("reply_btn_${post.id}")
                            ) {
                                Text(
                                    text = AppStrings.t("reply_btn", currentLanguage),
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = AgriAmberSecondary
                                )
                            }
                        }

                        // Replies section
                        if (post.replies.isNotEmpty()) {
                            Spacer(modifier = Modifier.height(8.dp))
                            Divider(color = AgriOutlineVariant)
                            Spacer(modifier = Modifier.height(8.dp))

                            post.replies.forEach { reply ->
                                Surface(
                                    color = if (reply.isExpert) AgriGreenContainer.copy(alpha = 0.5f) else AgriSurfaceVariant,
                                    shape = RoundedCornerShape(8.dp),
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(bottom = 6.dp)
                                ) {
                                    Column(modifier = Modifier.padding(10.dp)) {
                                        Row(verticalAlignment = Alignment.CenterVertically) {
                                            Text(
                                                text = reply.authorName,
                                                fontSize = 12.sp,
                                                fontWeight = FontWeight.Bold,
                                                color = if (reply.isExpert) AgriGreenPrimary else AgriTextPrimary
                                            )
                                            if (reply.isExpert) {
                                                Spacer(modifier = Modifier.width(6.dp))
                                                Surface(
                                                    color = AgriGreenPrimary,
                                                    shape = RoundedCornerShape(4.dp)
                                                ) {
                                                    Text(
                                                        text = if (isHindi) "कृषि विशेषज्ञ" else "Agronomist",
                                                        fontSize = 9.sp,
                                                        color = Color.White,
                                                        fontWeight = FontWeight.Bold,
                                                        modifier = Modifier.padding(horizontal = 4.dp, vertical = 1.dp)
                                                    )
                                                }
                                            }
                                            Spacer(modifier = Modifier.width(6.dp))
                                            Text(
                                                text = "• ${reply.timestamp}",
                                                fontSize = 10.sp,
                                                color = AgriTextMuted
                                            )
                                        }
                                        Spacer(modifier = Modifier.height(4.dp))
                                        Text(
                                            text = reply.content,
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
            }
        }
    }

    // Ask Question Dialog
    if (showAskDialog) {
        AlertDialog(
            onDismissRequest = { showAskDialog = false },
            title = {
                Text(
                    text = if (isHindi) "किसान चौपाल में सवाल पूछें" else "Post a Question to Community",
                    fontWeight = FontWeight.Bold
                )
            },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    OutlinedTextField(
                        value = newCropTag,
                        onValueChange = { newCropTag = it },
                        label = { Text("Crop / Topic (e.g. Wheat, Tomato)") },
                        modifier = Modifier.fillMaxWidth()
                    )
                    OutlinedTextField(
                        value = newTitle,
                        onValueChange = { newTitle = it },
                        label = { Text("Question Headline") },
                        modifier = Modifier.fillMaxWidth()
                    )
                    OutlinedTextField(
                        value = newQuestion,
                        onValueChange = { newQuestion = it },
                        label = { Text("Detailed Explanation / Symptoms") },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(100.dp)
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        if (newTitle.isNotBlank()) {
                            onAddQuestion(newTitle, newQuestion, newCropTag)
                            newTitle = ""
                            newQuestion = ""
                            showAskDialog = false
                        }
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = AgriGreenPrimary)
                ) {
                    Text(if (isHindi) "पोस्ट करें" else "Post Question")
                }
            },
            dismissButton = {
                TextButton(onClick = { showAskDialog = false }) {
                    Text(AppStrings.t("cancel", currentLanguage))
                }
            }
        )
    }

    // Reply Dialog
    replyingToPostId?.let { postId ->
        AlertDialog(
            onDismissRequest = { replyingToPostId = null },
            title = {
                Text(
                    text = if (isHindi) "अपना जवाब दर्ज करें" else "Write Your Reply",
                    fontWeight = FontWeight.Bold
                )
            },
            text = {
                OutlinedTextField(
                    value = replyText,
                    onValueChange = { replyText = it },
                    placeholder = { Text(if (isHindi) "सलाह या अनुभव लिखें..." else "Share your experience or advice...") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(120.dp)
                )
            },
            confirmButton = {
                Button(
                    onClick = {
                        if (replyText.isNotBlank()) {
                            onAddReply(postId, replyText)
                            replyText = ""
                            replyingToPostId = null
                        }
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = AgriGreenPrimary)
                ) {
                    Text(if (isHindi) "जवाब भेजें" else "Submit Reply")
                }
            },
            dismissButton = {
                TextButton(onClick = { replyingToPostId = null }) {
                    Text(AppStrings.t("cancel", currentLanguage))
                }
            }
        )
    }
}
