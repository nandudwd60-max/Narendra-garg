package com.example.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CardGiftcard
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.Verified
import androidx.compose.material.icons.filled.WorkspacePremium
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.example.ui.AdRewardType
import com.example.ui.theme.DarkSurface
import com.example.ui.theme.DarkSurfaceCard
import com.example.ui.theme.ElectricPurple
import com.example.ui.theme.GoldenVip
import com.example.ui.theme.HotPink
import com.example.ui.theme.NeonCoral

@Composable
fun AdBannerCard(
    isVip: Boolean,
    onWatchAdClick: () -> Unit,
    onUpgradeVipClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    if (isVip) return

    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 6.dp)
            .testTag("ad_banner_card"),
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = DarkSurfaceCard.copy(alpha = 0.85f)),
        border = CardDefaults.outlinedCardBorder().copy(
            brush = Brush.linearGradient(listOf(ElectricPurple.copy(alpha = 0.5f), NeonCoral.copy(alpha = 0.5f)))
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.weight(1f)
            ) {
                Box(
                    modifier = Modifier
                        .size(38.dp)
                        .clip(RoundedCornerShape(8.dp))
                        .background(
                            Brush.linearGradient(listOf(GoldenVip, HotPink))
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.CardGiftcard,
                        contentDescription = "Ad Reward",
                        tint = Color.Black,
                        modifier = Modifier.size(22.dp)
                    )
                }
                Spacer(modifier = Modifier.width(10.dp))
                Column {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = "AD SPONSORED",
                            fontSize = 9.sp,
                            fontWeight = FontWeight.Bold,
                            color = GoldenVip,
                            letterSpacing = 1.sp
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = "• Free Tier",
                            fontSize = 10.sp,
                            color = Color.White.copy(alpha = 0.6f)
                        )
                    }
                    Text(
                        text = "Watch 5s Video Ad & Earn +30 Free Coins!",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = Color.White
                    )
                }
            }

            Spacer(modifier = Modifier.width(8.dp))

            Button(
                onClick = onWatchAdClick,
                colors = ButtonDefaults.buttonColors(
                    containerColor = ElectricPurple
                ),
                shape = RoundedCornerShape(20.dp),
                contentPadding = androidx.compose.foundation.layout.PaddingValues(horizontal = 12.dp, vertical = 6.dp),
                modifier = Modifier.testTag("watch_ad_banner_button")
            ) {
                Icon(
                    imageVector = Icons.Default.PlayArrow,
                    contentDescription = null,
                    modifier = Modifier.size(16.dp),
                    tint = Color.White
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text("Watch", fontSize = 12.sp, fontWeight = FontWeight.Bold)
            }
        }
    }
}

@Composable
fun RewardedAdDialog(
    isOpen: Boolean,
    countdown: Int,
    rewardType: AdRewardType?,
    onClaimReward: () -> Unit,
    onDismiss: () -> Unit
) {
    if (!isOpen || rewardType == null) return

    val progress = (5 - countdown) / 5f
    val animatedProgress by animateFloatAsState(
        targetValue = progress,
        animationSpec = tween(durationMillis = 1000, easing = LinearEasing),
        label = "ad_progress"
    )

    val rewardTitle = when (rewardType) {
        AdRewardType.COINS_30 -> "+30 Free Coins"
        AdRewardType.MATCH_PASS_3 -> "+3 Free Video Matches"
        AdRewardType.VIP_HD_PASS -> "24-Hour VIP Filter Pass"
    }

    Dialog(
        onDismissRequest = {
            if (countdown == 0) onDismiss()
        },
        properties = DialogProperties(
            dismissOnBackPress = countdown == 0,
            dismissOnClickOutside = false,
            usePlatformDefaultWidth = false
        )
    ) {
        Surface(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black),
            color = Color.Black
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(20.dp)
            ) {
                // Header with countdown and close button
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .align(Alignment.TopCenter)
                        .padding(top = 16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(16.dp))
                            .background(Color.White.copy(alpha = 0.2f))
                            .padding(horizontal = 12.dp, vertical = 6.dp)
                    ) {
                        Text(
                            text = if (countdown > 0) "Reward in ${countdown}s..." else "Reward Ready! 🎉",
                            color = if (countdown == 0) GoldenVip else Color.White,
                            fontWeight = FontWeight.Bold,
                            fontSize = 13.sp
                        )
                    }

                    if (countdown == 0) {
                        IconButton(
                            onClick = onDismiss,
                            modifier = Modifier
                                .clip(CircleShape)
                                .background(Color.White.copy(alpha = 0.3f))
                                .size(36.dp)
                                .testTag("close_ad_button")
                        ) {
                            Icon(Icons.Default.Close, contentDescription = "Close Ad", tint = Color.White)
                        }
                    }
                }

                // Main Ad Simulated Content
                Column(
                    modifier = Modifier
                        .align(Alignment.Center)
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    // Ad sponsor graphic
                    Box(
                        modifier = Modifier
                            .size(120.dp)
                            .clip(CircleShape)
                            .background(
                                Brush.radialGradient(listOf(GoldenVip, HotPink, ElectricPurple))
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.WorkspacePremium,
                            contentDescription = "Ad Sponsor",
                            tint = Color.White,
                            modifier = Modifier.size(64.dp)
                        )
                    }

                    Spacer(modifier = Modifier.height(24.dp))

                    Text(
                        text = "VibeCall Gaming & Hangout Deals",
                        style = MaterialTheme.typography.headlineSmall,
                        fontWeight = FontWeight.Bold,
                        color = Color.White,
                        textAlign = TextAlign.Center
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = "Connect with genuine people around the world! Enjoy crystal clear video, instant translation & mini games.",
                        style = MaterialTheme.typography.bodyMedium,
                        color = Color.White.copy(alpha = 0.8f),
                        textAlign = TextAlign.Center,
                        modifier = Modifier.padding(horizontal = 16.dp)
                    )

                    Spacer(modifier = Modifier.height(24.dp))

                    // Progress Indicator
                    LinearProgressIndicator(
                        progress = { animatedProgress },
                        modifier = Modifier
                            .fillMaxWidth(0.85f)
                            .height(8.dp)
                            .clip(RoundedCornerShape(4.dp)),
                        color = NeonCoral,
                        trackColor = Color.White.copy(alpha = 0.2f)
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    Text(
                        text = "Rewarding you with: $rewardTitle",
                        color = GoldenVip,
                        fontWeight = FontWeight.Bold,
                        fontSize = 15.sp
                    )
                }

                // Bottom Claim Action
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .align(Alignment.BottomCenter)
                        .padding(bottom = 32.dp)
                ) {
                    Button(
                        onClick = onClaimReward,
                        enabled = countdown == 0,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(54.dp)
                            .testTag("claim_ad_reward_button"),
                        shape = RoundedCornerShape(16.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = GoldenVip,
                            contentColor = Color.Black,
                            disabledContainerColor = Color.White.copy(alpha = 0.2f),
                            disabledContentColor = Color.White.copy(alpha = 0.4f)
                        )
                    ) {
                        Icon(
                            imageVector = Icons.Default.Verified,
                            contentDescription = null,
                            modifier = Modifier.size(20.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = if (countdown == 0) "CLAIM $rewardTitle NOW!" else "Watching Ad ($countdown seconds)...",
                            fontWeight = FontWeight.Bold,
                            fontSize = 15.sp
                        )
                    }
                }
            }
        }
    }
}
