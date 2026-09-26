package com.example.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.material.icons.filled.ElectricBolt
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Videocam
import androidx.compose.material.icons.filled.WorkspacePremium
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
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
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.data.model.PeerCandidate
import com.example.data.model.UserProfile
import com.example.ui.AdRewardType
import com.example.ui.components.AdBannerCard
import com.example.ui.theme.CyberTeal
import com.example.ui.theme.DarkSurface
import com.example.ui.theme.DarkSurfaceCard
import com.example.ui.theme.DarkSurfaceElevated
import com.example.ui.theme.DeepIndigoBg
import com.example.ui.theme.ElectricPurple
import com.example.ui.theme.ElectricPurpleBright
import com.example.ui.theme.EmeraldOnline
import com.example.ui.theme.GoldenVip
import com.example.ui.theme.HotPink
import com.example.ui.theme.NeonCoral

@Composable
fun MatchScreen(
    userProfile: UserProfile,
    isSearching: Boolean,
    searchStatusText: String,
    candidates: List<PeerCandidate>,
    onStartMatch: () -> Unit,
    onDirectCallPeer: (PeerCandidate) -> Unit,
    onWatchAdForMatches: () -> Unit,
    onOpenVipStore: () -> Unit,
    onFilterChange: (gender: String, region: String) -> Unit,
    modifier: Modifier = Modifier
) {
    var showFilterDialog by remember { mutableStateOf(false) }

    // Pulsing animation for radar
    val infiniteTransition = rememberInfiniteTransition(label = "radar_pulse")
    val pulseScale by infiniteTransition.animateFloat(
        initialValue = 1f,
        targetValue = 1.25f,
        animationSpec = infiniteRepeatable(
            animation = tween(1200, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "pulse_scale"
    )

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(DeepIndigoBg)
    ) {
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(bottom = 96.dp)
        ) {
            // Hero Visual Banner
            item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(180.dp)
                        .padding(16.dp)
                        .clip(RoundedCornerShape(20.dp))
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.hero_video_hangout),
                        contentDescription = "Hangout Banner",
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.fillMaxSize()
                    )

                    // Gradient overlay
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(
                                Brush.verticalGradient(
                                    listOf(Color.Transparent, DeepIndigoBg.copy(alpha = 0.85f))
                                )
                            )
                    )

                    // Overlay Content
                    Column(
                        modifier = Modifier
                            .align(Alignment.BottomStart)
                            .padding(16.dp)
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Box(
                                modifier = Modifier
                                    .size(8.dp)
                                    .clip(CircleShape)
                                    .background(EmeraldOnline)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = "24,850+ People Hanging Out Now",
                                color = EmeraldOnline,
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                        Text(
                            text = "Meet New Friends on Video",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.ExtraBold,
                            color = Color.White
                        )
                    }
                }
            }

            // Quota & Match Status Bar
            item {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 4.dp),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = DarkSurfaceElevated)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(14.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text(
                                text = if (userProfile.isVip) "VIP MEMBERSHIP ACTIVE" else "DAILY FREE MATCHES",
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                color = if (userProfile.isVip) GoldenVip else NeonCoral,
                                letterSpacing = 0.5.sp
                            )
                            Text(
                                text = if (userProfile.isVip) "Unlimited Video Matches 👑"
                                else "${userProfile.dailyFreeMatchesRemaining} matches left today",
                                fontSize = 14.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = Color.White
                            )
                        }

                        if (!userProfile.isVip) {
                            Button(
                                onClick = onWatchAdForMatches,
                                colors = ButtonDefaults.buttonColors(containerColor = ElectricPurple),
                                shape = RoundedCornerShape(20.dp),
                                contentPadding = PaddingValues(horizontal = 12.dp, vertical = 4.dp),
                                modifier = Modifier.testTag("refill_matches_button")
                            ) {
                                Icon(Icons.Default.PlayArrow, contentDescription = null, modifier = Modifier.size(16.dp))
                                Spacer(modifier = Modifier.width(4.dp))
                                Text("+3 via Ad", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                            }
                        } else {
                            Surface(
                                shape = RoundedCornerShape(16.dp),
                                color = GoldenVip.copy(alpha = 0.2f),
                                border = androidx.compose.foundation.BorderStroke(1.dp, GoldenVip)
                            ) {
                                Text(
                                    text = "VIP PRO",
                                    color = GoldenVip,
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold,
                                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
                                )
                            }
                        }
                    }
                }
            }

            // Filter Chips
            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 8.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = "Match Filters",
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White.copy(alpha = 0.8f)
                    )

                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        FilterChip(
                            selected = userProfile.preferredRegion != "Global",
                            onClick = {
                                val nextRegion = if (userProfile.preferredRegion == "India") "Global" else "India"
                                onFilterChange(userProfile.preferredGenderFilter, nextRegion)
                            },
                            label = { Text("📍 ${userProfile.preferredRegion}", fontSize = 12.sp) },
                            colors = FilterChipDefaults.filterChipColors(
                                selectedContainerColor = ElectricPurple,
                                selectedLabelColor = Color.White
                            )
                        )

                        FilterChip(
                            selected = userProfile.preferredGenderFilter != "All",
                            onClick = {
                                if (!userProfile.isVip) {
                                    onOpenVipStore()
                                } else {
                                    val nextGender = when (userProfile.preferredGenderFilter) {
                                        "All" -> "Female"
                                        "Female" -> "Male"
                                        else -> "All"
                                    }
                                    onFilterChange(nextGender, userProfile.preferredRegion)
                                }
                            },
                            label = {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    if (!userProfile.isVip) {
                                        Icon(
                                            Icons.Default.Lock,
                                            contentDescription = "VIP Only",
                                            tint = GoldenVip,
                                            modifier = Modifier.size(12.dp)
                                        )
                                        Spacer(modifier = Modifier.width(4.dp))
                                    }
                                    Text(
                                        text = if (userProfile.isVip) "Gender: ${userProfile.preferredGenderFilter}" else "Gender (VIP)",
                                        fontSize = 12.sp,
                                        color = if (!userProfile.isVip) GoldenVip else Color.White
                                    )
                                }
                            }
                        )
                    }
                }
            }

            // Radar Match Main Action Button
            item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 24.dp),
                    contentAlignment = Alignment.Center
                ) {
                    // Outer pulse rings
                    Box(
                        modifier = Modifier
                            .size(190.dp)
                            .scale(pulseScale)
                            .clip(CircleShape)
                            .background(ElectricPurple.copy(alpha = 0.12f))
                    )
                    Box(
                        modifier = Modifier
                            .size(160.dp)
                            .scale(pulseScale * 0.95f)
                            .clip(CircleShape)
                            .background(NeonCoral.copy(alpha = 0.18f))
                    )

                    // Big Round Match Action
                    Box(
                        modifier = Modifier
                            .size(130.dp)
                            .clip(CircleShape)
                            .background(
                                Brush.linearGradient(listOf(NeonCoral, HotPink, ElectricPurple))
                            )
                            .clickable(enabled = !isSearching) { onStartMatch() }
                            .testTag("start_video_match_button"),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.Videocam,
                                contentDescription = "Start Video Call",
                                tint = Color.White,
                                modifier = Modifier.size(42.dp)
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = "MATCH NOW",
                                fontSize = 13.sp,
                                fontWeight = FontWeight.Black,
                                color = Color.White,
                                letterSpacing = 1.sp
                            )
                        }
                    }
                }

                Text(
                    text = "Tap to connect randomly with 1-on-1 video call",
                    fontSize = 12.sp,
                    color = Color.White.copy(alpha = 0.65f),
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 12.dp)
                )
            }

            // Ad Banner for Free Tier
            item {
                AdBannerCard(
                    isVip = userProfile.isVip,
                    onWatchAdClick = onWatchAdForMatches,
                    onUpgradeVipClick = onOpenVipStore
                )
            }

            // Active Hangout Recommendations
            item {
                Text(
                    text = "🔥 Active Hangout Stars",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White,
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 10.dp)
                )
            }

            item {
                LazyRow(
                    contentPadding = PaddingValues(horizontal = 16.dp),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(candidates) { candidate ->
                        Card(
                            modifier = Modifier
                                .width(150.dp)
                                .clip(RoundedCornerShape(16.dp))
                                .clickable { onDirectCallPeer(candidate) }
                                .testTag("peer_card_${candidate.name}"),
                            colors = CardDefaults.cardColors(containerColor = DarkSurfaceElevated)
                        ) {
                            Column(
                                modifier = Modifier.padding(12.dp),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Box(
                                    modifier = Modifier
                                        .size(60.dp)
                                        .clip(CircleShape)
                                        .background(Color(candidate.avatarBgColorHex)),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(
                                        text = candidate.name.take(1),
                                        fontSize = 26.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = Color.White
                                    )
                                    // Online dot
                                    Box(
                                        modifier = Modifier
                                            .size(12.dp)
                                            .align(Alignment.BottomEnd)
                                            .clip(CircleShape)
                                            .background(EmeraldOnline)
                                            .border(2.dp, DarkSurfaceElevated, CircleShape)
                                    )
                                }

                                Spacer(modifier = Modifier.height(8.dp))

                                Text(
                                    text = "${candidate.name}, ${candidate.age}",
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 13.sp,
                                    color = Color.White
                                )

                                Text(
                                    text = candidate.city,
                                    fontSize = 11.sp,
                                    color = CyberTeal
                                )

                                Spacer(modifier = Modifier.height(8.dp))

                                Surface(
                                    shape = RoundedCornerShape(12.dp),
                                    color = ElectricPurple.copy(alpha = 0.25f)
                                ) {
                                    Row(
                                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Icon(
                                            Icons.Default.Videocam,
                                            contentDescription = null,
                                            tint = ElectricPurpleBright,
                                            modifier = Modifier.size(12.dp)
                                        )
                                        Spacer(modifier = Modifier.width(4.dp))
                                        Text(
                                            text = "Hangout",
                                            fontSize = 11.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = ElectricPurpleBright
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }

        // Radar Scanning Overlay
        AnimatedVisibility(
            visible = isSearching,
            modifier = Modifier.fillMaxSize()
        ) {
            Surface(
                modifier = Modifier.fillMaxSize(),
                color = DeepIndigoBg.copy(alpha = 0.95f)
            ) {
                Column(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Box(
                        modifier = Modifier
                            .size(140.dp)
                            .clip(CircleShape)
                            .background(ElectricPurple.copy(alpha = 0.2f)),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(100.dp),
                            color = NeonCoral,
                            strokeWidth = 4.dp
                        )
                        Icon(
                            imageVector = Icons.Default.Videocam,
                            contentDescription = "Searching",
                            tint = Color.White,
                            modifier = Modifier.size(40.dp)
                        )
                    }

                    Spacer(modifier = Modifier.height(24.dp))

                    Text(
                        text = "Finding Your Hangout Buddy...",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = searchStatusText,
                        fontSize = 14.sp,
                        color = CyberTeal
                    )
                }
            }
        }
    }
}
