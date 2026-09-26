package com.example.ui.screens

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
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.LockOpen
import androidx.compose.material.icons.filled.MonetizationOn
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.WorkspacePremium
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
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
import com.example.data.model.UserProfile
import com.example.ui.AdRewardType
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

data class SubscriptionPlan(
    val id: String,
    val name: String,
    val days: Int,
    val price: String,
    val perPeriod: String,
    val bonusCoins: Int,
    val isPopular: Boolean = false,
    val tag: String? = null
)

data class CoinPackage(
    val id: String,
    val coins: Int,
    val bonus: Int,
    val price: String,
    val popularTag: String? = null
)

@Composable
fun VipStoreScreen(
    userProfile: UserProfile,
    onSubscribe: (planName: String, days: Int, priceLabel: String) -> Unit,
    onBuyCoins: (coins: Int, bonus: Int, priceLabel: String) -> Unit,
    onWatchAd: (AdRewardType) -> Unit,
    modifier: Modifier = Modifier
) {
    val subscriptionPlans = listOf(
        SubscriptionPlan(
            id = "plan_weekly",
            name = "Weekly VIP Pass",
            days = 7,
            price = "₹199 ($2.99)",
            perPeriod = "per week",
            bonusCoins = 300,
            tag = "Flexible"
        ),
        SubscriptionPlan(
            id = "plan_monthly",
            name = "Monthly Pro VIP",
            days = 30,
            price = "₹499 ($7.99)",
            perPeriod = "per month",
            bonusCoins = 1500,
            isPopular = true,
            tag = "MOST POPULAR 🔥"
        ),
        SubscriptionPlan(
            id = "plan_annual",
            name = "Annual Gold Pass",
            days = 365,
            price = "₹2,499 ($39.99)",
            perPeriod = "per year",
            bonusCoins = 6000,
            tag = "SAVE 65% 👑"
        )
    )

    val coinPackages = listOf(
        CoinPackage("coin_100", 100, 0, "₹49 ($0.99)"),
        CoinPackage("coin_500", 500, 50, "₹199 ($2.99)", "BEST STARTER"),
        CoinPackage("coin_1500", 1500, 200, "₹499 ($5.99)", "POPULAR"),
        CoinPackage("coin_5000", 5000, 1000, "₹1,499 ($17.99)", "MEGA PACK")
    )

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(DeepIndigoBg)
            .testTag("vip_store_screen")
    ) {
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(bottom = 96.dp)
        ) {
            // VIP Pass Hero Graphic Banner
            item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(190.dp)
                        .padding(16.dp)
                        .clip(RoundedCornerShape(20.dp))
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.vip_pass_banner),
                        contentDescription = "VIP Pass",
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.fillMaxSize()
                    )

                    // Gradient overlay
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(
                                Brush.verticalGradient(
                                    listOf(Color.Transparent, DeepIndigoBg.copy(alpha = 0.9f))
                                )
                            )
                    )

                    // Badge & Current status
                    Column(
                        modifier = Modifier
                            .align(Alignment.BottomStart)
                            .padding(16.dp)
                    ) {
                        Surface(
                            shape = RoundedCornerShape(12.dp),
                            color = GoldenVip.copy(alpha = 0.25f),
                            border = androidx.compose.foundation.BorderStroke(1.dp, GoldenVip)
                        ) {
                            Text(
                                text = if (userProfile.isVip) "👑 VIP MEMBERSHIP ACTIVE" else "⭐ VIP MEMBERSHIP CLUB",
                                color = GoldenVip,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
                            )
                        }

                        Spacer(modifier = Modifier.height(6.dp))

                        Text(
                            text = if (userProfile.isVip) "You have unlimited hangout perks!" else "Upgrade to Unlimited Hangouts",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.ExtraBold,
                            color = Color.White
                        )
                    }
                }
            }

            // Wallet & Coin balance bar
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
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Box(
                                modifier = Modifier
                                    .size(40.dp)
                                    .clip(CircleShape)
                                    .background(GoldenVip.copy(alpha = 0.2f)),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(text = "🪙", fontSize = 22.sp)
                            }
                            Spacer(modifier = Modifier.width(12.dp))
                            Column {
                                Text(
                                    text = "COIN BALANCE",
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = GoldenVip,
                                    letterSpacing = 1.sp
                                )
                                Text(
                                    text = "${userProfile.coins} Coins",
                                    fontSize = 16.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color.White
                                )
                            }
                        }

                        Surface(
                            shape = RoundedCornerShape(14.dp),
                            color = ElectricPurple.copy(alpha = 0.3f)
                        ) {
                            Text(
                                text = "Use for in-call gifts",
                                color = ElectricPurpleBright,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.SemiBold,
                                modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp)
                            )
                        }
                    }
                }
            }

            // SECTION 1: AD-SUPPORTED FREE REWARDS ("Watch & Earn")
            item {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 12.dp)
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(text = "🎁", fontSize = 18.sp)
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "AD-SUPPORTED REWARDS (100% FREE)",
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Bold,
                            color = NeonCoral,
                            letterSpacing = 0.5.sp
                        )
                    }
                    Text(
                        text = "Watch short 5-second video ads to earn free coins and match passes anytime!",
                        fontSize = 11.sp,
                        color = Color.White.copy(alpha = 0.65f)
                    )
                }
            }

            item {
                Column(
                    modifier = Modifier.padding(horizontal = 16.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    // Ad Option 1: Coins
                    AdRewardActionCard(
                        title = "+30 Free Wallet Coins",
                        subtitle = "Send in-call roses & chai gifts to your hangout buddies",
                        icon = "🪙",
                        buttonText = "Watch Ad",
                        onClick = { onWatchAd(AdRewardType.COINS_30) },
                        testTag = "watch_ad_coins_button"
                    )

                    // Ad Option 2: Match Quota
                    AdRewardActionCard(
                        title = "+3 Extra Video Matches",
                        subtitle = "Instantly replenish your daily match quota",
                        icon = "🔥",
                        buttonText = "Refill",
                        onClick = { onWatchAd(AdRewardType.MATCH_PASS_3) },
                        testTag = "watch_ad_matches_button"
                    )

                    // Ad Option 3: HD Filter Trial
                    AdRewardActionCard(
                        title = "24-Hour VIP Filter Trial",
                        subtitle = "Unlock Beauty Glow, Cyberpunk & HD camera filter",
                        icon = "✨",
                        buttonText = "Unlock",
                        onClick = { onWatchAd(AdRewardType.VIP_HD_PASS) },
                        testTag = "watch_ad_vip_trial_button"
                    )
                }
            }

            // SECTION 2: VIP SUBSCRIPTION PLANS
            item {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 16.dp, end = 16.dp, top = 20.dp, bottom = 12.dp)
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(text = "👑", fontSize = 18.sp)
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "VIP CLUB SUBSCRIPTION PLANS",
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Bold,
                            color = GoldenVip,
                            letterSpacing = 0.5.sp
                        )
                    }
                    Text(
                        text = "Zero ads, unlimited matching time, gender filter & VIP badge",
                        fontSize = 11.sp,
                        color = Color.White.copy(alpha = 0.65f)
                    )
                }
            }

            // Subscription Cards
            items(subscriptionPlans) { plan ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 6.dp)
                        .testTag("plan_card_${plan.id}"),
                    shape = RoundedCornerShape(20.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = if (plan.isPopular) DarkSurfaceCard else DarkSurfaceElevated
                    ),
                    border = if (plan.isPopular) androidx.compose.foundation.BorderStroke(1.5.dp, GoldenVip)
                    else androidx.compose.foundation.BorderStroke(1.dp, Color.White.copy(alpha = 0.1f))
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column {
                                plan.tag?.let { tag ->
                                    Text(
                                        text = tag,
                                        fontSize = 10.sp,
                                        fontWeight = FontWeight.Black,
                                        color = if (plan.isPopular) GoldenVip else CyberTeal,
                                        letterSpacing = 0.5.sp
                                    )
                                }
                                Text(
                                    text = plan.name,
                                    fontSize = 16.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color.White
                                )
                            }

                            Column(horizontalAlignment = Alignment.End) {
                                Text(
                                    text = plan.price,
                                    fontSize = 16.sp,
                                    fontWeight = FontWeight.ExtraBold,
                                    color = if (plan.isPopular) GoldenVip else Color.White
                                )
                                Text(
                                    text = plan.perPeriod,
                                    fontSize = 11.sp,
                                    color = Color.White.copy(alpha = 0.6f)
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(10.dp))

                        // Perks Included
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.Check, contentDescription = null, tint = EmeraldOnline, modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(6.dp))
                            Text("100% Ad-Free Video Calls & Lounges", fontSize = 12.sp, color = Color.White.copy(alpha = 0.9f))
                        }
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.Check, contentDescription = null, tint = EmeraldOnline, modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(6.dp))
                            Text("Unlimited matches (No daily quota limit)", fontSize = 12.sp, color = Color.White.copy(alpha = 0.9f))
                        }
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.Check, contentDescription = null, tint = EmeraldOnline, modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(6.dp))
                            Text("Unlock Gender & Country Radar Filters", fontSize = 12.sp, color = Color.White.copy(alpha = 0.9f))
                        }
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.Check, contentDescription = null, tint = GoldenVip, modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(6.dp))
                            Text("🎁 Includes +${plan.bonusCoins} Bonus Coins", fontSize = 12.sp, color = GoldenVip, fontWeight = FontWeight.Bold)
                        }

                        Spacer(modifier = Modifier.height(14.dp))

                        Button(
                            onClick = { onSubscribe(plan.name, plan.days, plan.price) },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(46.dp)
                                .testTag("subscribe_button_${plan.id}"),
                            shape = RoundedCornerShape(14.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = if (plan.isPopular) GoldenVip else ElectricPurple,
                                contentColor = if (plan.isPopular) Color.Black else Color.White
                            )
                        ) {
                            Icon(Icons.Default.WorkspacePremium, contentDescription = null, modifier = Modifier.size(18.dp))
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = "Subscribe to ${plan.name}",
                                fontWeight = FontWeight.Bold,
                                fontSize = 13.sp
                            )
                        }
                    }
                }
            }

            // SECTION 3: COIN SHOP
            item {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 16.dp, end = 16.dp, top = 20.dp, bottom = 12.dp)
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(text = "💎", fontSize = 18.sp)
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "COIN PACKS (VIRTUAL GIFTS)",
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Bold,
                            color = CyberTeal,
                            letterSpacing = 0.5.sp
                        )
                    }
                }
            }

            items(coinPackages) { pack ->
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
                            .padding(12.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Box(
                                modifier = Modifier
                                    .size(42.dp)
                                    .clip(CircleShape)
                                    .background(ElectricPurple.copy(alpha = 0.25f)),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(text = "🪙", fontSize = 20.sp)
                            }
                            Spacer(modifier = Modifier.width(12.dp))
                            Column {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Text(
                                        text = "${pack.coins} Coins",
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 14.sp,
                                        color = Color.White
                                    )
                                    if (pack.bonus > 0) {
                                        Spacer(modifier = Modifier.width(6.dp))
                                        Text(
                                            text = "+${pack.bonus} FREE",
                                            fontSize = 10.sp,
                                            fontWeight = FontWeight.Black,
                                            color = EmeraldOnline
                                        )
                                    }
                                }
                                pack.popularTag?.let {
                                    Text(text = it, fontSize = 10.sp, color = GoldenVip, fontWeight = FontWeight.Bold)
                                }
                            }
                        }

                        Button(
                            onClick = { onBuyCoins(pack.coins, pack.bonus, pack.price) },
                            colors = ButtonDefaults.buttonColors(containerColor = DarkSurfaceCard),
                            border = androidx.compose.foundation.BorderStroke(1.dp, ElectricPurple),
                            shape = RoundedCornerShape(16.dp),
                            contentPadding = PaddingValues(horizontal = 14.dp, vertical = 4.dp),
                            modifier = Modifier.testTag("buy_coin_${pack.id}")
                        ) {
                            Text(pack.price, fontSize = 12.sp, fontWeight = FontWeight.Bold, color = ElectricPurpleBright)
                        }
                    }
                }
            }

            // SECTION 4: PERKS COMPARISON TABLE
            item {
                Spacer(modifier = Modifier.height(16.dp))
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    shape = RoundedCornerShape(20.dp),
                    colors = CardDefaults.cardColors(containerColor = DarkSurfaceElevated)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(
                            text = "Free Tier vs VIP Club Comparison",
                            fontWeight = FontWeight.Bold,
                            fontSize = 14.sp,
                            color = Color.White
                        )
                        Spacer(modifier = Modifier.height(12.dp))

                        ComparisonRow(feature = "Ads", free = "Ad-supported", vip = "100% Ad-Free")
                        ComparisonRow(feature = "Daily Matches", free = "5 free/day", vip = "Unlimited 24/7")
                        ComparisonRow(feature = "Match Refill", free = "Via 5s Video Ad", vip = "No refill needed")
                        ComparisonRow(feature = "Gender Filter", free = "Locked", vip = "Unlocked")
                        ComparisonRow(feature = "Video Quality", free = "Standard", vip = "Full HD + Filters")
                        ComparisonRow(feature = "VIP Lounges", free = "Public Only", vip = "All Rooms Access")
                    }
                }
            }
        }
    }
}

@Composable
private fun AdRewardActionCard(
    title: String,
    subtitle: String,
    icon: String,
    buttonText: String,
    onClick: () -> Unit,
    testTag: String
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = DarkSurfaceElevated),
        border = androidx.compose.foundation.BorderStroke(1.dp, ElectricPurple.copy(alpha = 0.3f))
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
                        .size(42.dp)
                        .clip(CircleShape)
                        .background(NeonCoral.copy(alpha = 0.18f)),
                    contentAlignment = Alignment.Center
                ) {
                    Text(text = icon, fontSize = 20.sp)
                }
                Spacer(modifier = Modifier.width(12.dp))
                Column {
                    Text(
                        text = title,
                        fontWeight = FontWeight.Bold,
                        fontSize = 13.sp,
                        color = Color.White
                    )
                    Text(
                        text = subtitle,
                        fontSize = 11.sp,
                        color = Color.White.copy(alpha = 0.65f),
                        maxLines = 1
                    )
                }
            }

            Button(
                onClick = onClick,
                colors = ButtonDefaults.buttonColors(containerColor = ElectricPurple),
                shape = RoundedCornerShape(16.dp),
                contentPadding = PaddingValues(horizontal = 12.dp, vertical = 6.dp),
                modifier = Modifier.testTag(testTag)
            ) {
                Icon(Icons.Default.PlayArrow, contentDescription = null, modifier = Modifier.size(16.dp))
                Spacer(modifier = Modifier.width(4.dp))
                Text(buttonText, fontSize = 12.sp, fontWeight = FontWeight.Bold)
            }
        }
    }
}

@Composable
private fun ComparisonRow(feature: String, free: String, vip: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(feature, fontSize = 12.sp, color = Color.White.copy(alpha = 0.7f), modifier = Modifier.weight(1f))
        Text(free, fontSize = 11.sp, color = Color.White.copy(alpha = 0.6f), modifier = Modifier.weight(1f), textAlign = TextAlign.Center)
        Text(vip, fontSize = 11.sp, color = GoldenVip, fontWeight = FontWeight.Bold, modifier = Modifier.weight(1f), textAlign = TextAlign.End)
    }
}
