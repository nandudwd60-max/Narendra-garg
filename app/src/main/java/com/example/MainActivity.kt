package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Forum
import androidx.compose.material.icons.filled.People
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Videocam
import androidx.compose.material.icons.filled.WorkspacePremium
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.data.model.Screen
import com.example.ui.AdRewardType
import com.example.ui.VibeViewModel
import com.example.ui.VibeViewModelFactory
import com.example.ui.components.RewardedAdDialog
import com.example.ui.screens.FriendsScreen
import com.example.ui.screens.LoungesScreen
import com.example.ui.screens.MatchScreen
import com.example.ui.screens.ProfileScreen
import com.example.ui.screens.VideoCallScreen
import com.example.ui.screens.VipStoreScreen
import com.example.ui.theme.DarkSurface
import com.example.ui.theme.DarkSurfaceCard
import com.example.ui.theme.DarkSurfaceElevated
import com.example.ui.theme.DeepIndigoBg
import com.example.ui.theme.ElectricPurple
import com.example.ui.theme.ElectricPurpleBright
import com.example.ui.theme.EmeraldOnline
import com.example.ui.theme.GoldenVip
import com.example.ui.theme.HotPink
import com.example.ui.theme.MyApplicationTheme
import com.example.ui.theme.NeonCoral

class MainActivity : ComponentActivity() {

    private val viewModel: VibeViewModel by viewModels {
        val app = application as VibeCallApplication
        VibeViewModelFactory(app.repository)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            MyApplicationTheme {
                val currentScreen by viewModel.currentScreen.collectAsStateWithLifecycle()
                val userProfile by viewModel.userProfile.collectAsStateWithLifecycle()
                val friends by viewModel.friends.collectAsStateWithLifecycle()
                val callHistory by viewModel.callHistory.collectAsStateWithLifecycle()
                val lounges by viewModel.hangoutLounges.collectAsStateWithLifecycle()
                val activePeer by viewModel.activePeer.collectAsStateWithLifecycle()
                val callType by viewModel.callType.collectAsStateWithLifecycle()
                val callDuration by viewModel.callDurationSeconds.collectAsStateWithLifecycle()
                val isMicMuted by viewModel.isMicMuted.collectAsStateWithLifecycle()
                val isVideoMuted by viewModel.isVideoMuted.collectAsStateWithLifecycle()
                val isFrontCamera by viewModel.isFrontCamera.collectAsStateWithLifecycle()
                val activeFilter by viewModel.activeFilter.collectAsStateWithLifecycle()
                val incomingSpeech by viewModel.incomingSpeechText.collectAsStateWithLifecycle()
                val floatingReactions by viewModel.floatingReactions.collectAsStateWithLifecycle()
                val activeGamePrompt by viewModel.activeGamePrompt.collectAsStateWithLifecycle()
                val lastGiftSent by viewModel.lastGiftSent.collectAsStateWithLifecycle()
                val isMatchSearching by viewModel.isMatchSearching.collectAsStateWithLifecycle()
                val searchStatusText by viewModel.searchStatusText.collectAsStateWithLifecycle()

                val isAdPlaying by viewModel.isAdPlaying.collectAsStateWithLifecycle()
                val adCountdown by viewModel.adCountdown.collectAsStateWithLifecycle()
                val activeAdRewardType by viewModel.activeAdRewardType.collectAsStateWithLifecycle()
                val snackbarMessage by viewModel.snackbarMessage.collectAsStateWithLifecycle()

                val snackbarHostState = remember { SnackbarHostState() }

                LaunchedEffect(snackbarMessage) {
                    snackbarMessage?.let { msg ->
                        snackbarHostState.showSnackbar(msg)
                        viewModel.clearSnackbar()
                    }
                }

                // Handle back press
                BackHandler(enabled = currentScreen != Screen.MATCH) {
                    if (currentScreen == Screen.ACTIVE_CALL) {
                        viewModel.endCall()
                    } else {
                        viewModel.navigateTo(Screen.MATCH)
                    }
                }

                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                    containerColor = DeepIndigoBg,
                    snackbarHost = { SnackbarHost(snackbarHostState) },
                    topBar = {
                        if (currentScreen != Screen.ACTIVE_CALL) {
                            VibeTopBar(
                                coins = userProfile.coins,
                                isVip = userProfile.isVip,
                                onCoinClick = { viewModel.navigateTo(Screen.VIP_STORE) },
                                onWatchAdForCoins = { viewModel.watchRewardedAd(AdRewardType.COINS_30) },
                                onVipClick = { viewModel.navigateTo(Screen.VIP_STORE) }
                            )
                        }
                    },
                    bottomBar = {
                        if (currentScreen != Screen.ACTIVE_CALL) {
                            VibeBottomNavigationBar(
                                currentScreen = currentScreen,
                                onSelectScreen = { viewModel.navigateTo(it) }
                            )
                        }
                    }
                ) { innerPadding ->
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(
                                top = if (currentScreen != Screen.ACTIVE_CALL) innerPadding.calculateTopPadding() else 0.dp,
                                bottom = if (currentScreen != Screen.ACTIVE_CALL) innerPadding.calculateBottomPadding() else 0.dp
                            )
                    ) {
                        when (currentScreen) {
                            Screen.MATCH -> {
                                MatchScreen(
                                    userProfile = userProfile,
                                    isSearching = isMatchSearching,
                                    searchStatusText = searchStatusText,
                                    candidates = remember {
                                        (application as VibeCallApplication).repository.getSampleCandidates()
                                    },
                                    onStartMatch = { viewModel.startRandomMatch() },
                                    onDirectCallPeer = { peer ->
                                        viewModel.startFriendCall(
                                            com.example.data.model.Friend(
                                                id = peer.id,
                                                name = peer.name,
                                                avatarUrl = "avatar_${peer.name.lowercase()}",
                                                status = "Online",
                                                bio = peer.bio,
                                                interests = peer.interests.joinToString(", "),
                                                gender = peer.gender,
                                                age = peer.age
                                            )
                                        )
                                    },
                                    onWatchAdForMatches = { viewModel.watchRewardedAd(AdRewardType.MATCH_PASS_3) },
                                    onOpenVipStore = { viewModel.navigateTo(Screen.VIP_STORE) },
                                    onFilterChange = { gender, region ->
                                        viewModel.updateFilters(gender, region)
                                    }
                                )
                            }

                            Screen.ACTIVE_CALL -> {
                                activePeer?.let { peer ->
                                    VideoCallScreen(
                                        peer = peer,
                                        userProfile = userProfile,
                                        callType = callType,
                                        callDurationSeconds = callDuration,
                                        isMicMuted = isMicMuted,
                                        isVideoMuted = isVideoMuted,
                                        isFrontCamera = isFrontCamera,
                                        activeFilter = activeFilter,
                                        incomingSpeechText = incomingSpeech,
                                        floatingReactions = floatingReactions,
                                        activeGamePrompt = activeGamePrompt,
                                        lastGiftSent = lastGiftSent,
                                        onToggleMic = { viewModel.toggleMic() },
                                        onToggleVideo = { viewModel.toggleVideo() },
                                        onFlipCamera = { viewModel.flipCamera() },
                                        onSetFilter = { viewModel.setFilter(it) },
                                        onSendReaction = { viewModel.sendReaction(it) },
                                        onSendGift = { viewModel.sendGift(it) },
                                        onAddFriend = { viewModel.addCurrentPeerAsFriend() },
                                        onTriggerGame = { viewModel.triggerGamePrompt(it) },
                                        onClearGame = { viewModel.clearGamePrompt() },
                                        onNextMatch = { viewModel.nextMatch() },
                                        onEndCall = { viewModel.endCall() }
                                    )
                                }
                            }

                            Screen.FRIENDS -> {
                                FriendsScreen(
                                    friends = friends,
                                    isVip = userProfile.isVip,
                                    onDirectVideoCall = { friend -> viewModel.startFriendCall(friend) },
                                    onToggleFavorite = { friend -> viewModel.toggleFavoriteFriend(friend) },
                                    onRemoveFriend = { id -> viewModel.removeFriend(id) },
                                    onAddNewFriend = { name, bio, interests ->
                                        val newFriend = com.example.data.model.Friend(
                                            id = "f_${System.currentTimeMillis()}",
                                            name = name,
                                            avatarUrl = "avatar_${name.lowercase()}",
                                            status = "Online",
                                            bio = bio,
                                            interests = interests,
                                            gender = "Any",
                                            age = 22,
                                            isFavorite = false
                                        )
                                        viewModel.addCurrentPeerAsFriend()
                                    },
                                    onWatchAd = { viewModel.watchRewardedAd(AdRewardType.COINS_30) },
                                    onOpenVipStore = { viewModel.navigateTo(Screen.VIP_STORE) }
                                )
                            }

                            Screen.LOUNGES -> {
                                LoungesScreen(
                                    lounges = lounges,
                                    isVip = userProfile.isVip,
                                    onJoinLounge = { lounge -> viewModel.joinLounge(lounge) },
                                    onWatchAd = { viewModel.watchRewardedAd(AdRewardType.COINS_30) },
                                    onOpenVipStore = { viewModel.navigateTo(Screen.VIP_STORE) }
                                )
                            }

                            Screen.VIP_STORE -> {
                                VipStoreScreen(
                                    userProfile = userProfile,
                                    onSubscribe = { planName, days, price ->
                                        viewModel.subscribeVip(planName, days, price)
                                    },
                                    onBuyCoins = { coins, bonus, price ->
                                        viewModel.buyCoinPack(coins, bonus, price)
                                    },
                                    onWatchAd = { rewardType ->
                                        viewModel.watchRewardedAd(rewardType)
                                    }
                                )
                            }

                            Screen.PROFILE -> {
                                ProfileScreen(
                                    userProfile = userProfile,
                                    callHistory = callHistory,
                                    onUpdateProfile = { name, bio ->
                                        // Update profile
                                    },
                                    onOpenVipStore = { viewModel.navigateTo(Screen.VIP_STORE) }
                                )
                            }
                        }

                        // Rewarded Ad Simulation Overlay
                        RewardedAdDialog(
                            isOpen = isAdPlaying,
                            countdown = adCountdown,
                            rewardType = activeAdRewardType,
                            onClaimReward = { viewModel.claimAdReward() },
                            onDismiss = { viewModel.dismissAd() }
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun VibeTopBar(
    coins: Int,
    isVip: Boolean,
    onCoinClick: () -> Unit,
    onWatchAdForCoins: () -> Unit,
    onVipClick: () -> Unit
) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .windowInsetsPadding(WindowInsets.statusBars),
        color = DarkSurface
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 10.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            // App Branding
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(34.dp)
                        .clip(RoundedCornerShape(10.dp))
                        .background(
                            Brush.linearGradient(listOf(ElectricPurple, HotPink))
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Videocam,
                        contentDescription = "VibeCall",
                        tint = Color.White,
                        modifier = Modifier.size(20.dp)
                    )
                }

                Spacer(modifier = Modifier.width(8.dp))

                Column {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = "VibeCall",
                            fontWeight = FontWeight.ExtraBold,
                            fontSize = 18.sp,
                            color = Color.White
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Box(
                            modifier = Modifier
                                .size(6.dp)
                                .clip(CircleShape)
                                .background(EmeraldOnline)
                        )
                    }
                    Text(
                        text = "Live Video Hangout",
                        fontSize = 10.sp,
                        color = Color.White.copy(alpha = 0.6f)
                    )
                }
            }

            // Quick Actions: Coin balance & Watch Ad / VIP badge
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                // Free Ad reward shortcut
                Surface(
                    shape = RoundedCornerShape(14.dp),
                    color = ElectricPurple.copy(alpha = 0.25f),
                    modifier = Modifier
                        .clickable { onWatchAdForCoins() }
                        .testTag("topbar_watch_ad_pill")
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            Icons.Default.PlayArrow,
                            contentDescription = null,
                            tint = GoldenVip,
                            modifier = Modifier.size(12.dp)
                        )
                        Spacer(modifier = Modifier.width(3.dp))
                        Text(
                            text = "+30 🪙",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = GoldenVip
                        )
                    }
                }

                // Coins pill
                Surface(
                    shape = RoundedCornerShape(16.dp),
                    color = DarkSurfaceCard,
                    border = androidx.compose.foundation.BorderStroke(1.dp, GoldenVip.copy(alpha = 0.4f)),
                    modifier = Modifier
                        .clickable { onCoinClick() }
                        .testTag("topbar_coins_chip")
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(text = "🪙", fontSize = 12.sp)
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = "$coins",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                    }
                }

                // VIP Badge
                Surface(
                    shape = RoundedCornerShape(16.dp),
                    color = if (isVip) GoldenVip else ElectricPurple,
                    modifier = Modifier
                        .clickable { onVipClick() }
                        .testTag("topbar_vip_button")
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.WorkspacePremium,
                            contentDescription = "VIP",
                            tint = if (isVip) Color.Black else Color.White,
                            modifier = Modifier.size(14.dp)
                        )
                        Spacer(modifier = Modifier.width(3.dp))
                        Text(
                            text = if (isVip) "VIP PRO" else "VIP",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = if (isVip) Color.Black else Color.White
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun VibeBottomNavigationBar(
    currentScreen: Screen,
    onSelectScreen: (Screen) -> Unit
) {
    NavigationBar(
        modifier = Modifier
            .fillMaxWidth()
            .windowInsetsPadding(WindowInsets.navigationBars)
            .testTag("bottom_nav_bar"),
        containerColor = DarkSurface,
        tonalElevation = 8.dp
    ) {
        val navItems = listOf(
            Triple(Screen.MATCH, "Match", Icons.Default.Videocam),
            Triple(Screen.FRIENDS, "Friends", Icons.Default.People),
            Triple(Screen.LOUNGES, "Lounges", Icons.Default.Forum),
            Triple(Screen.VIP_STORE, "VIP Store", Icons.Default.WorkspacePremium),
            Triple(Screen.PROFILE, "Profile", Icons.Default.AccountCircle)
        )

        navItems.forEach { (screen, label, icon) ->
            val isSelected = currentScreen == screen
            NavigationBarItem(
                selected = isSelected,
                onClick = { onSelectScreen(screen) },
                icon = {
                    Icon(
                        imageVector = icon,
                        contentDescription = label,
                        modifier = Modifier.size(24.dp)
                    )
                },
                label = {
                    Text(
                        text = label,
                        fontSize = 11.sp,
                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                    )
                },
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = Color.White,
                    selectedTextColor = ElectricPurpleBright,
                    indicatorColor = ElectricPurple,
                    unselectedIconColor = Color.White.copy(alpha = 0.5f),
                    unselectedTextColor = Color.White.copy(alpha = 0.5f)
                ),
                modifier = Modifier.testTag("nav_item_${screen.name.lowercase()}")
            )
        }
    }
}
