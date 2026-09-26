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
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.Videocam
import androidx.compose.material.icons.filled.WorkspacePremium
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
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
import com.example.data.model.CallHistory
import com.example.data.model.UserProfile
import com.example.ui.theme.CyberTeal
import com.example.ui.theme.DarkSurfaceCard
import com.example.ui.theme.DarkSurfaceElevated
import com.example.ui.theme.DeepIndigoBg
import com.example.ui.theme.ElectricPurple
import com.example.ui.theme.ElectricPurpleBright
import com.example.ui.theme.EmeraldOnline
import com.example.ui.theme.GoldenVip
import com.example.ui.theme.HotPink
import com.example.ui.theme.NeonCoral
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun ProfileScreen(
    userProfile: UserProfile,
    callHistory: List<CallHistory>,
    onUpdateProfile: (name: String, bio: String) -> Unit,
    onOpenVipStore: () -> Unit,
    modifier: Modifier = Modifier
) {
    var showEditDialog by remember { mutableStateOf(false) }

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(DeepIndigoBg)
            .testTag("profile_screen")
    ) {
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(bottom = 96.dp)
        ) {
            // Profile Card Header
            item {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    shape = RoundedCornerShape(24.dp),
                    colors = CardDefaults.cardColors(containerColor = DarkSurfaceElevated)
                ) {
                    Column(
                        modifier = Modifier.padding(20.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Box(
                            modifier = Modifier
                                .size(88.dp)
                                .clip(CircleShape)
                                .background(ElectricPurple),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = userProfile.name.take(1),
                                fontSize = 38.sp,
                                fontWeight = FontWeight.Black,
                                color = Color.White
                            )

                            if (userProfile.isVip) {
                                Box(
                                    modifier = Modifier
                                        .size(26.dp)
                                        .align(Alignment.BottomEnd)
                                        .clip(CircleShape)
                                        .background(GoldenVip),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Icon(
                                        Icons.Default.WorkspacePremium,
                                        contentDescription = "VIP",
                                        tint = Color.Black,
                                        modifier = Modifier.size(16.dp)
                                    )
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(12.dp))

                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = "${userProfile.name}, ${userProfile.age}",
                                style = MaterialTheme.typography.titleLarge,
                                fontWeight = FontWeight.Bold,
                                color = Color.White
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Surface(
                                shape = RoundedCornerShape(12.dp),
                                color = if (userProfile.isVip) GoldenVip.copy(alpha = 0.2f) else DarkSurfaceCard,
                                border = if (userProfile.isVip) androidx.compose.foundation.BorderStroke(1.dp, GoldenVip) else null
                            ) {
                                Text(
                                    text = if (userProfile.isVip) "👑 VIP" else "Free Tier",
                                    fontSize = 11.sp,
                                    color = if (userProfile.isVip) GoldenVip else Color.White.copy(alpha = 0.7f),
                                    fontWeight = FontWeight.Bold,
                                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp)
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(6.dp))

                        Text(
                            text = userProfile.bio,
                            fontSize = 13.sp,
                            color = Color.White.copy(alpha = 0.75f),
                            textAlign = androidx.compose.ui.text.style.TextAlign.Center
                        )

                        Spacer(modifier = Modifier.height(16.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceEvenly
                        ) {
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Text(
                                    text = "${userProfile.coins} 🪙",
                                    fontWeight = FontWeight.Black,
                                    fontSize = 16.sp,
                                    color = GoldenVip
                                )
                                Text("Coins", fontSize = 11.sp, color = Color.White.copy(alpha = 0.6f))
                            }
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Text(
                                    text = if (userProfile.isVip) "Unlimited" else "${userProfile.dailyFreeMatchesRemaining}",
                                    fontWeight = FontWeight.Black,
                                    fontSize = 16.sp,
                                    color = NeonCoral
                                )
                                Text("Free Matches", fontSize = 11.sp, color = Color.White.copy(alpha = 0.6f))
                            }
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Text(
                                    text = "${callHistory.size}",
                                    fontWeight = FontWeight.Black,
                                    fontSize = 16.sp,
                                    color = CyberTeal
                                )
                                Text("Calls Done", fontSize = 11.sp, color = Color.White.copy(alpha = 0.6f))
                            }
                        }

                        Spacer(modifier = Modifier.height(16.dp))

                        Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                            Button(
                                onClick = { showEditDialog = true },
                                colors = ButtonDefaults.buttonColors(containerColor = DarkSurfaceCard),
                                shape = RoundedCornerShape(14.dp),
                                modifier = Modifier.testTag("edit_profile_button")
                            ) {
                                Icon(Icons.Default.Edit, contentDescription = null, modifier = Modifier.size(16.dp))
                                Spacer(modifier = Modifier.width(6.dp))
                                Text("Edit Profile", fontSize = 12.sp)
                            }

                            if (!userProfile.isVip) {
                                Button(
                                    onClick = onOpenVipStore,
                                    colors = ButtonDefaults.buttonColors(containerColor = GoldenVip, contentColor = Color.Black),
                                    shape = RoundedCornerShape(14.dp),
                                    modifier = Modifier.testTag("upgrade_vip_profile_button")
                                ) {
                                    Icon(Icons.Default.WorkspacePremium, contentDescription = null, modifier = Modifier.size(16.dp))
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Text("Upgrade VIP", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                                }
                            }
                        }
                    }
                }
            }

            // Call History Section Header
            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 8.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.History, contentDescription = null, tint = ElectricPurpleBright, modifier = Modifier.size(18.dp))
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "RECENT HANGOUT CALLS",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White,
                            letterSpacing = 0.5.sp
                        )
                    }
                    Text(
                        text = "${callHistory.size} calls",
                        fontSize = 11.sp,
                        color = Color.White.copy(alpha = 0.5f)
                    )
                }
            }

            // Call History Items
            if (callHistory.isEmpty()) {
                item {
                    Text(
                        text = "No calls made yet. Tap Match to start your first video call!",
                        fontSize = 13.sp,
                        color = Color.White.copy(alpha = 0.6f),
                        modifier = Modifier.padding(16.dp)
                    )
                }
            } else {
                items(callHistory) { call ->
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp, vertical = 4.dp),
                        shape = RoundedCornerShape(14.dp),
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
                                        .size(38.dp)
                                        .clip(CircleShape)
                                        .background(ElectricPurple.copy(alpha = 0.25f)),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Icon(
                                        Icons.Default.Videocam,
                                        contentDescription = null,
                                        tint = ElectricPurpleBright,
                                        modifier = Modifier.size(18.dp)
                                    )
                                }
                                Spacer(modifier = Modifier.width(10.dp))
                                Column {
                                    Text(
                                        text = call.peerName,
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 13.sp,
                                        color = Color.White
                                    )
                                    Text(
                                        text = call.callType,
                                        fontSize = 11.sp,
                                        color = CyberTeal
                                    )
                                }
                            }

                            Column(horizontalAlignment = Alignment.End) {
                                val min = call.durationSeconds / 60
                                val sec = call.durationSeconds % 60
                                Text(
                                    text = String.format("%02d:%02d", min, sec),
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 12.sp,
                                    color = EmeraldOnline
                                )
                                val dateFormat = SimpleDateFormat("dd MMM, hh:mm a", Locale.getDefault())
                                Text(
                                    text = dateFormat.format(Date(call.timestamp)),
                                    fontSize = 10.sp,
                                    color = Color.White.copy(alpha = 0.5f)
                                )
                            }
                        }
                    }
                }
            }

            // Safety & Community Policy
            item {
                Spacer(modifier = Modifier.height(16.dp))
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = DarkSurfaceCard)
                ) {
                    Column(modifier = Modifier.padding(14.dp)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.Security, contentDescription = null, tint = EmeraldOnline, modifier = Modifier.size(18.dp))
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "Safe & Friendly Community",
                                fontWeight = FontWeight.Bold,
                                fontSize = 13.sp,
                                color = Color.White
                            )
                        }
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "VibeCall is designed for friendly timepass and genuine hangout conversations. Inappropriate behavior is strictly prohibited. You can report or block users at any time.",
                            fontSize = 11.sp,
                            color = Color.White.copy(alpha = 0.65f)
                        )
                    }
                }
            }
        }

        // Edit Profile Dialog
        if (showEditDialog) {
            var tempName by remember { mutableStateOf(userProfile.name) }
            var tempBio by remember { mutableStateOf(userProfile.bio) }

            AlertDialog(
                onDismissRequest = { showEditDialog = false },
                title = { Text("Edit Your Profile", color = Color.White) },
                text = {
                    Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                        OutlinedTextField(
                            value = tempName,
                            onValueChange = { tempName = it },
                            label = { Text("Your Name") },
                            singleLine = true
                        )
                        OutlinedTextField(
                            value = tempBio,
                            onValueChange = { tempBio = it },
                            label = { Text("Bio / Status") }
                        )
                    }
                },
                confirmButton = {
                    Button(
                        onClick = {
                            if (tempName.isNotBlank()) {
                                onUpdateProfile(tempName.trim(), tempBio.trim())
                                showEditDialog = false
                            }
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = ElectricPurple)
                    ) {
                        Text("Save Changes")
                    }
                },
                dismissButton = {
                    TextButton(onClick = { showEditDialog = false }) {
                        Text("Cancel", color = Color.White.copy(alpha = 0.7f))
                    }
                },
                containerColor = DarkSurfaceElevated
            )
        }
    }
}
