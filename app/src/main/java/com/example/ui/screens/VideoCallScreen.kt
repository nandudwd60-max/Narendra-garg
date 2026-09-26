package com.example.ui.screens

import android.Manifest
import android.content.pm.PackageManager
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
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
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.CallEnd
import androidx.compose.material.icons.filled.Cameraswitch
import androidx.compose.material.icons.filled.CardGiftcard
import androidx.compose.material.icons.filled.Casino
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Mic
import androidx.compose.material.icons.filled.MicOff
import androidx.compose.material.icons.filled.PersonAdd
import androidx.compose.material.icons.filled.SkipNext
import androidx.compose.material.icons.filled.Videocam
import androidx.compose.material.icons.filled.VideocamOff
import androidx.compose.material.icons.filled.VolumeUp
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import com.example.data.model.AVAILABLE_GIFTS
import com.example.data.model.PeerCandidate
import com.example.data.model.UserProfile
import com.example.data.model.VirtualGift
import com.example.ui.GamePrompt
import com.example.ui.ReactionBubble
import com.example.ui.components.CameraPreview
import com.example.ui.theme.CyberTeal
import com.example.ui.theme.DarkSurface
import com.example.ui.theme.DarkSurfaceCard
import com.example.ui.theme.DarkSurfaceElevated
import com.example.ui.theme.ElectricPurple
import com.example.ui.theme.ElectricPurpleBright
import com.example.ui.theme.EmeraldOnline
import com.example.ui.theme.GoldenVip
import com.example.ui.theme.HotPink
import com.example.ui.theme.NeonCoral

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun VideoCallScreen(
    peer: PeerCandidate,
    userProfile: UserProfile,
    callType: String,
    callDurationSeconds: Int,
    isMicMuted: Boolean,
    isVideoMuted: Boolean,
    isFrontCamera: Boolean,
    activeFilter: String,
    incomingSpeechText: String?,
    floatingReactions: List<ReactionBubble>,
    activeGamePrompt: GamePrompt?,
    lastGiftSent: VirtualGift?,
    onToggleMic: () -> Unit,
    onToggleVideo: () -> Unit,
    onFlipCamera: () -> Unit,
    onSetFilter: (String) -> Unit,
    onSendReaction: (String) -> Unit,
    onSendGift: (VirtualGift) -> Unit,
    onAddFriend: () -> Unit,
    onTriggerGame: (String) -> Unit,
    onClearGame: () -> Unit,
    onNextMatch: () -> Unit,
    onEndCall: () -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    var hasCameraPermission by remember {
        mutableStateOf(
            ContextCompat.checkSelfPermission(context, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED
        )
    }

    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        hasCameraPermission = isGranted
    }

    LaunchedEffect(Unit) {
        if (!hasCameraPermission) {
            permissionLauncher.launch(Manifest.permission.CAMERA)
        }
    }

    var showGiftsSheet by remember { mutableStateOf(false) }
    var showFiltersSheet by remember { mutableStateOf(false) }

    // Pulsing audio wave animation for peer
    val infiniteTransition = rememberInfiniteTransition(label = "peer_audio")
    val waveScale by infiniteTransition.animateFloat(
        initialValue = 1f,
        targetValue = 1.08f,
        animationSpec = infiniteRepeatable(
            animation = tween(700, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "wave_scale"
    )

    val filterOverlayColor = when (activeFilter) {
        "Beauty Glow" -> HotPink.copy(alpha = 0.08f)
        "Cyberpunk" -> CyberTeal.copy(alpha = 0.12f)
        "Golden Hour" -> GoldenVip.copy(alpha = 0.12f)
        "B&W Vintage" -> Color.Black.copy(alpha = 0.25f)
        else -> Color.Transparent
    }

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(Color.Black)
            .testTag("video_call_screen")
    ) {
        // REMOTE PEER FULLSCREEN VIDEO SIMULATION
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.verticalGradient(
                        listOf(
                            Color(peer.avatarBgColorHex).copy(alpha = 0.7f),
                            Color(0xFF140D2D),
                            Color.Black
                        )
                    )
                )
        ) {
            // Visual dynamic peer avatar & video stage
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(bottom = 120.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Box(
                    modifier = Modifier
                        .size(160.dp)
                        .scale(waveScale)
                        .clip(CircleShape)
                        .background(
                            Brush.radialGradient(
                                listOf(
                                    Color(peer.avatarBgColorHex),
                                    Color(peer.avatarBgColorHex).copy(alpha = 0.4f),
                                    Color.Transparent
                                )
                            )
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Box(
                        modifier = Modifier
                            .size(120.dp)
                            .clip(CircleShape)
                            .background(Color(peer.avatarBgColorHex))
                            .border(3.dp, CyberTeal, CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = peer.name.take(1),
                            fontSize = 52.sp,
                            fontWeight = FontWeight.Black,
                            color = Color.White
                        )
                    }
                }

                Spacer(modifier = Modifier.height(18.dp))

                Text(
                    text = "${peer.name}, ${peer.age}",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )

                Text(
                    text = "${peer.city}, ${peer.country} • ${peer.moodStatus}",
                    fontSize = 13.sp,
                    color = Color.White.copy(alpha = 0.75f),
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(horizontal = 32.dp)
                )

                Spacer(modifier = Modifier.height(14.dp))

                // Speech Bubble with animated voice indicator
                incomingSpeechText?.let { speech ->
                    Card(
                        shape = RoundedCornerShape(18.dp),
                        colors = CardDefaults.cardColors(containerColor = DarkSurfaceElevated.copy(alpha = 0.9f)),
                        border = androidx.compose.foundation.BorderStroke(1.dp, CyberTeal.copy(alpha = 0.4f)),
                        modifier = Modifier
                            .padding(horizontal = 24.dp)
                            .testTag("peer_speech_bubble")
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 14.dp, vertical = 10.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                Icons.Default.VolumeUp,
                                contentDescription = null,
                                tint = CyberTeal,
                                modifier = Modifier.size(18.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = speech,
                                fontSize = 13.sp,
                                fontWeight = FontWeight.Medium,
                                color = Color.White
                            )
                        }
                    }
                }
            }
        }

        // Active Filter Tint Overlay
        if (filterOverlayColor != Color.Transparent) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(filterOverlayColor)
            )
        }

        // TOP BAR: Call info, Timer, Connection status
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 40.dp, start = 16.dp, end = 16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Call Type & Timer Chip
            Surface(
                shape = RoundedCornerShape(20.dp),
                color = DarkSurfaceCard.copy(alpha = 0.85f),
                border = androidx.compose.foundation.BorderStroke(1.dp, Color.White.copy(alpha = 0.15f))
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(8.dp)
                            .clip(CircleShape)
                            .background(EmeraldOnline)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    val minutes = callDurationSeconds / 60
                    val seconds = callDurationSeconds % 60
                    Text(
                        text = String.format("%02d:%02d", minutes, seconds),
                        fontWeight = FontWeight.Bold,
                        fontSize = 13.sp,
                        color = Color.White
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "• HD 48ms",
                        fontSize = 11.sp,
                        color = CyberTeal
                    )
                }
            }

            // Quick Add Friend Action
            Surface(
                shape = RoundedCornerShape(20.dp),
                color = ElectricPurple.copy(alpha = 0.85f),
                modifier = Modifier
                    .clickable { onAddFriend() }
                    .testTag("in_call_add_friend_button")
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        Icons.Default.PersonAdd,
                        contentDescription = "Add Friend",
                        tint = Color.White,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = "Add Friend",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                }
            }
        }

        // LOCAL USER CAMERA (Picture-in-Picture Floating Window)
        Box(
            modifier = Modifier
                .align(Alignment.TopEnd)
                .padding(top = 90.dp, end = 16.dp)
                .size(width = 110.dp, height = 150.dp)
                .clip(RoundedCornerShape(16.dp))
                .border(2.dp, ElectricPurpleBright, RoundedCornerShape(16.dp))
                .background(Color(0xFF1B172C))
                .testTag("local_camera_preview")
        ) {
            CameraPreview(
                isFrontCamera = isFrontCamera,
                isVideoMuted = isVideoMuted,
                hasCameraPermission = hasCameraPermission,
                modifier = Modifier.fillMaxSize()
            )

            // Local user label
            Box(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .fillMaxWidth()
                    .background(Color.Black.copy(alpha = 0.5f))
                    .padding(vertical = 2.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "You",
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
            }
        }

        // Active Game Prompt Card (Truth or Dare / Rapid Fire)
        activeGamePrompt?.let { prompt ->
            Card(
                modifier = Modifier
                    .align(Alignment.Center)
                    .fillMaxWidth(0.9f)
                    .padding(16.dp)
                    .testTag("game_prompt_card"),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = DarkSurfaceElevated.copy(alpha = 0.95f)),
                border = androidx.compose.foundation.BorderStroke(2.dp, GoldenVip)
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "🎲 TIME-PASS GAME: ${prompt.type.uppercase()}",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Black,
                            color = GoldenVip,
                            letterSpacing = 1.sp
                        )
                        IconButton(onClick = onClearGame, modifier = Modifier.size(24.dp)) {
                            Icon(Icons.Default.Close, contentDescription = "Close Game", tint = Color.White)
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    Text(
                        text = prompt.question,
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White,
                        textAlign = TextAlign.Center
                    )

                    Spacer(modifier = Modifier.height(14.dp))

                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        Surface(
                            shape = RoundedCornerShape(12.dp),
                            color = ElectricPurple,
                            modifier = Modifier.clickable { onTriggerGame("Truth") }
                        ) {
                            Text(
                                text = "Next Truth",
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.White,
                                modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp)
                            )
                        }

                        Surface(
                            shape = RoundedCornerShape(12.dp),
                            color = HotPink,
                            modifier = Modifier.clickable { onTriggerGame("Dare") }
                        ) {
                            Text(
                                text = "Next Dare",
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.White,
                                modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp)
                            )
                        }

                        Surface(
                            shape = RoundedCornerShape(12.dp),
                            color = CyberTeal,
                            modifier = Modifier.clickable { onTriggerGame("Rapid") }
                        ) {
                            Text(
                                text = "Rapid Fire",
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.Black,
                                modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp)
                            )
                        }
                    }
                }
            }
        }

        // Floating reaction bubbles
        floatingReactions.forEach { bubble ->
            FloatingReactionEmoji(bubble)
        }

        // Last Gift Celebration Notice
        lastGiftSent?.let { gift ->
            Box(
                modifier = Modifier
                    .align(Alignment.Center)
                    .clip(RoundedCornerShape(24.dp))
                    .background(GoldenVip.copy(alpha = 0.95f))
                    .padding(horizontal = 20.dp, vertical = 10.dp)
            ) {
                Text(
                    text = "${gift.emoji} Sent ${gift.name}!",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Black,
                    color = Color.Black
                )
            }
        }

        // Quick Reaction Emoji Bar
        Row(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 98.dp)
                .clip(RoundedCornerShape(24.dp))
                .background(Color.Black.copy(alpha = 0.6f))
                .padding(horizontal = 12.dp, vertical = 6.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            val quickEmojis = listOf("❤️", "🔥", "😂", "👏", "🎉", "☕")
            quickEmojis.forEach { emoji ->
                Text(
                    text = emoji,
                    fontSize = 24.sp,
                    modifier = Modifier
                        .clickable { onSendReaction(emoji) }
                        .testTag("quick_emoji_$emoji")
                )
            }
        }

        // IN-CALL CONTROL ACTIONS BOTTOM BAR
        Card(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth()
                .padding(16.dp),
            shape = RoundedCornerShape(24.dp),
            colors = CardDefaults.cardColors(containerColor = DarkSurfaceElevated.copy(alpha = 0.92f)),
            border = androidx.compose.foundation.BorderStroke(1.dp, Color.White.copy(alpha = 0.1f))
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 12.dp, vertical = 10.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Mic toggle
                IconButton(
                    onClick = onToggleMic,
                    modifier = Modifier
                        .clip(CircleShape)
                        .background(if (isMicMuted) HotPink else DarkSurfaceCard)
                        .testTag("toggle_mic_button")
                ) {
                    Icon(
                        imageVector = if (isMicMuted) Icons.Default.MicOff else Icons.Default.Mic,
                        contentDescription = "Mic",
                        tint = Color.White
                    )
                }

                // Camera Toggle
                IconButton(
                    onClick = onToggleVideo,
                    modifier = Modifier
                        .clip(CircleShape)
                        .background(if (isVideoMuted) HotPink else DarkSurfaceCard)
                        .testTag("toggle_video_button")
                ) {
                    Icon(
                        imageVector = if (isVideoMuted) Icons.Default.VideocamOff else Icons.Default.Videocam,
                        contentDescription = "Video",
                        tint = Color.White
                    )
                }

                // Camera Flip
                IconButton(
                    onClick = onFlipCamera,
                    modifier = Modifier
                        .clip(CircleShape)
                        .background(DarkSurfaceCard)
                        .testTag("flip_camera_button")
                ) {
                    Icon(
                        imageVector = Icons.Default.Cameraswitch,
                        contentDescription = "Flip Camera",
                        tint = Color.White
                    )
                }

                // Beauty Filters
                IconButton(
                    onClick = { showFiltersSheet = true },
                    modifier = Modifier
                        .clip(CircleShape)
                        .background(if (activeFilter != "Normal") ElectricPurple else DarkSurfaceCard)
                        .testTag("filters_button")
                ) {
                    Icon(
                        imageVector = Icons.Default.AutoAwesome,
                        contentDescription = "Filters",
                        tint = if (activeFilter != "Normal") GoldenVip else Color.White
                    )
                }

                // Game Prompt Trigger
                IconButton(
                    onClick = { onTriggerGame("Truth") },
                    modifier = Modifier
                        .clip(CircleShape)
                        .background(DarkSurfaceCard)
                        .testTag("game_trigger_button")
                ) {
                    Icon(
                        imageVector = Icons.Default.Casino,
                        contentDescription = "Timepass Games",
                        tint = GoldenVip
                    )
                }

                // Send Gift
                IconButton(
                    onClick = { showGiftsSheet = true },
                    modifier = Modifier
                        .clip(CircleShape)
                        .background(NeonCoral)
                        .testTag("open_gifts_button")
                ) {
                    Icon(
                        imageVector = Icons.Default.CardGiftcard,
                        contentDescription = "Send Gift",
                        tint = Color.White
                    )
                }

                // Next Match (Skip)
                IconButton(
                    onClick = onNextMatch,
                    modifier = Modifier
                        .clip(CircleShape)
                        .background(ElectricPurple)
                        .testTag("next_match_button")
                ) {
                    Icon(
                        imageVector = Icons.Default.SkipNext,
                        contentDescription = "Next Match",
                        tint = Color.White
                    )
                }

                // End Call
                IconButton(
                    onClick = onEndCall,
                    modifier = Modifier
                        .clip(CircleShape)
                        .background(Color(0xFFE53935))
                        .testTag("end_call_button")
                ) {
                    Icon(
                        imageVector = Icons.Default.CallEnd,
                        contentDescription = "End Call",
                        tint = Color.White
                    )
                }
            }
        }

        // SEND GIFTS MODAL SHEET
        if (showGiftsSheet) {
            ModalBottomSheet(
                onDismissRequest = { showGiftsSheet = false },
                sheetState = rememberModalBottomSheetState(),
                containerColor = DarkSurfaceElevated
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(20.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Send Virtual Gift to ${peer.name}",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                        Text(
                            text = "Balance: ${userProfile.coins} 🪙",
                            color = GoldenVip,
                            fontWeight = FontWeight.Bold,
                            fontSize = 13.sp
                        )
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    LazyRow(
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        items(AVAILABLE_GIFTS) { gift ->
                            Card(
                                modifier = Modifier
                                    .width(110.dp)
                                    .clickable {
                                        onSendGift(gift)
                                        showGiftsSheet = false
                                    }
                                    .testTag("send_gift_${gift.id}"),
                                shape = RoundedCornerShape(16.dp),
                                colors = CardDefaults.cardColors(containerColor = DarkSurfaceCard)
                            ) {
                                Column(
                                    modifier = Modifier.padding(12.dp),
                                    horizontalAlignment = Alignment.CenterHorizontally
                                ) {
                                    Text(text = gift.emoji, fontSize = 34.sp)
                                    Spacer(modifier = Modifier.height(6.dp))
                                    Text(
                                        text = gift.name,
                                        fontSize = 12.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = Color.White
                                    )
                                    Text(
                                        text = "${gift.coinCost} 🪙",
                                        fontSize = 12.sp,
                                        color = GoldenVip,
                                        fontWeight = FontWeight.Bold
                                    )
                                }
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(24.dp))
                }
            }
        }

        // FILTERS SHEET
        if (showFiltersSheet) {
            ModalBottomSheet(
                onDismissRequest = { showFiltersSheet = false },
                sheetState = rememberModalBottomSheetState(),
                containerColor = DarkSurfaceElevated
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(20.dp)
                ) {
                    Text(
                        text = "Video & Beauty Filters",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )

                    Spacer(modifier = Modifier.height(14.dp))

                    val filters = listOf(
                        "Normal" to "Clean natural feed",
                        "Beauty Glow" to "Skin smoothing & warm light",
                        "Cyberpunk" to "Neon teal & violet contrast",
                        "Golden Hour" to "Warm sunset glow",
                        "B&W Vintage" to "Classic cinema tone"
                    )

                    filters.forEach { (filterName, desc) ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(12.dp))
                                .background(if (activeFilter == filterName) ElectricPurple.copy(alpha = 0.25f) else Color.Transparent)
                                .clickable {
                                    onSetFilter(filterName)
                                    showFiltersSheet = false
                                }
                                .padding(12.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Column {
                                Text(
                                    text = filterName,
                                    fontWeight = FontWeight.Bold,
                                    color = if (activeFilter == filterName) GoldenVip else Color.White
                                )
                                Text(
                                    text = desc,
                                    fontSize = 11.sp,
                                    color = Color.White.copy(alpha = 0.6f)
                                )
                            }
                            if (filterName != "Normal" && !userProfile.isVip) {
                                Text(
                                    text = "VIP / Ad Pass",
                                    fontSize = 10.sp,
                                    color = GoldenVip,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(24.dp))
                }
            }
        }
    }
}

@Composable
private fun FloatingReactionEmoji(bubble: ReactionBubble) {
    val animOffsetY = remember { Animatable(0f) }
    val animAlpha = remember { Animatable(1f) }

    LaunchedEffect(bubble.id) {
        animOffsetY.animateTo(
            targetValue = -350f,
            animationSpec = tween(durationMillis = 2400, easing = LinearEasing)
        )
    }

    LaunchedEffect(bubble.id) {
        animAlpha.animateTo(
            targetValue = 0f,
            animationSpec = tween(durationMillis = 2400, easing = LinearEasing)
        )
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 40.dp, vertical = 140.dp)
    ) {
        Text(
            text = bubble.emoji,
            fontSize = 32.sp,
            modifier = Modifier
                .align(Alignment.BottomStart)
                .offset {
                    IntOffset(
                        x = (bubble.horizontalBias * 240).toInt(),
                        y = animOffsetY.value.toInt()
                    )
                }
        )
    }
}
