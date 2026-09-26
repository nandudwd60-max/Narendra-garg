package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Group
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Mic
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.HangoutLounge
import com.example.ui.components.AdBannerCard
import com.example.ui.theme.CyberTeal
import com.example.ui.theme.DarkSurfaceElevated
import com.example.ui.theme.DeepIndigoBg
import com.example.ui.theme.ElectricPurple
import com.example.ui.theme.ElectricPurpleBright
import com.example.ui.theme.EmeraldOnline
import com.example.ui.theme.GoldenVip
import com.example.ui.theme.HotPink
import com.example.ui.theme.NeonCoral

@Composable
fun LoungesScreen(
    lounges: List<HangoutLounge>,
    isVip: Boolean,
    onJoinLounge: (HangoutLounge) -> Unit,
    onWatchAd: () -> Unit,
    onOpenVipStore: () -> Unit,
    modifier: Modifier = Modifier
) {
    var selectedCategory by remember { mutableStateOf("All") }
    val categories = listOf("All", "Chill Hangout", "Music & Singing", "Gaming & Esports", "VIP Exclusive")

    val filteredLounges = remember(lounges, selectedCategory) {
        if (selectedCategory == "All") lounges
        else lounges.filter { it.category.equals(selectedCategory, ignoreCase = true) }
    }

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(DeepIndigoBg)
            .testTag("lounges_screen")
    ) {
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(bottom = 96.dp)
        ) {
            // Header
            item {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 16.dp, end = 16.dp, top = 20.dp, bottom = 8.dp)
                ) {
                    Text(
                        text = "Hangout Lounges",
                        style = MaterialTheme.typography.headlineSmall,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                    Text(
                        text = "Drop in to open video stages, group voice talks & music jams",
                        fontSize = 12.sp,
                        color = CyberTeal
                    )
                }
            }

            // Categories Filter Row
            item {
                LazyRow(
                    contentPadding = PaddingValues(horizontal = 16.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier.padding(bottom = 12.dp)
                ) {
                    items(categories) { category ->
                        FilterChip(
                            selected = selectedCategory == category,
                            onClick = { selectedCategory = category },
                            label = { Text(category, fontSize = 12.sp) },
                            colors = FilterChipDefaults.filterChipColors(
                                selectedContainerColor = ElectricPurple,
                                selectedLabelColor = Color.White
                            )
                        )
                    }
                }
            }

            // Lounges List
            items(filteredLounges) { lounge ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 6.dp)
                        .clickable { onJoinLounge(lounge) }
                        .testTag("lounge_card_${lounge.id}"),
                    shape = RoundedCornerShape(20.dp),
                    colors = CardDefaults.cardColors(containerColor = DarkSurfaceElevated)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            // Category & Emoji Tag
                            Surface(
                                shape = RoundedCornerShape(12.dp),
                                color = if (lounge.isVipOnly) GoldenVip.copy(alpha = 0.2f) else ElectricPurple.copy(alpha = 0.2f),
                                border = if (lounge.isVipOnly) androidx.compose.foundation.BorderStroke(1.dp, GoldenVip) else null
                            ) {
                                Row(
                                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text(text = lounge.emojiTag, fontSize = 12.sp)
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Text(
                                        text = lounge.category,
                                        fontSize = 11.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = if (lounge.isVipOnly) GoldenVip else ElectricPurpleBright
                                    )
                                }
                            }

                            // Active count
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Box(
                                    modifier = Modifier
                                        .size(8.dp)
                                        .clip(CircleShape)
                                        .background(EmeraldOnline)
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(
                                    text = "${lounge.participantsCount} online",
                                    fontSize = 11.sp,
                                    color = EmeraldOnline,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(10.dp))

                        Text(
                            text = lounge.title,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )

                        Spacer(modifier = Modifier.height(4.dp))

                        Text(
                            text = "Topic: ${lounge.currentTopic}",
                            fontSize = 12.sp,
                            color = Color.White.copy(alpha = 0.7f)
                        )

                        Spacer(modifier = Modifier.height(14.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Box(
                                    modifier = Modifier
                                        .size(28.dp)
                                        .clip(CircleShape)
                                        .background(NeonCoral),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(
                                        text = lounge.hostName.take(1),
                                        fontSize = 12.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = Color.White
                                    )
                                }
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    text = "Host: ${lounge.hostName}",
                                    fontSize = 12.sp,
                                    color = Color.White.copy(alpha = 0.8f)
                                )
                            }

                            Button(
                                onClick = { onJoinLounge(lounge) },
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = if (lounge.isVipOnly) GoldenVip else ElectricPurple,
                                    contentColor = if (lounge.isVipOnly) Color.Black else Color.White
                                ),
                                shape = RoundedCornerShape(16.dp),
                                contentPadding = PaddingValues(horizontal = 14.dp, vertical = 6.dp),
                                modifier = Modifier.testTag("join_lounge_${lounge.id}")
                            ) {
                                if (lounge.isVipOnly) {
                                    Icon(Icons.Default.Lock, contentDescription = null, modifier = Modifier.size(14.dp))
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Text("VIP Entry", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                                } else {
                                    Icon(Icons.Default.Mic, contentDescription = null, modifier = Modifier.size(14.dp))
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Text("Join Stage", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                                }
                            }
                        }
                    }
                }
            }

            // Ad Banner for free tier
            item {
                Spacer(modifier = Modifier.height(8.dp))
                AdBannerCard(
                    isVip = isVip,
                    onWatchAdClick = onWatchAd,
                    onUpgradeVipClick = onOpenVipStore
                )
            }
        }
    }
}
