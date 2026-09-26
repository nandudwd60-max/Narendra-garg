package com.example.ui.screens

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
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Videocam
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
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
import com.example.data.model.Friend
import com.example.ui.components.AdBannerCard
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

@Composable
fun FriendsScreen(
    friends: List<Friend>,
    isVip: Boolean,
    onDirectVideoCall: (Friend) -> Unit,
    onToggleFavorite: (Friend) -> Unit,
    onRemoveFriend: (String) -> Unit,
    onAddNewFriend: (name: String, bio: String, interests: String) -> Unit,
    onWatchAd: () -> Unit,
    onOpenVipStore: () -> Unit,
    modifier: Modifier = Modifier
) {
    var searchQuery by remember { mutableStateOf("") }
    var showAddDialog by remember { mutableStateOf(false) }

    val filteredFriends = remember(friends, searchQuery) {
        if (searchQuery.isBlank()) friends
        else friends.filter {
            it.name.contains(searchQuery, ignoreCase = true) ||
                    it.interests.contains(searchQuery, ignoreCase = true)
        }
    }

    val onlineFriends = remember(friends) {
        friends.filter { it.status == "Online" }
    }

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(DeepIndigoBg)
            .testTag("friends_screen")
    ) {
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(bottom = 96.dp)
        ) {
            // Header
            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 16.dp, end = 16.dp, top = 20.dp, bottom = 8.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            text = "Hangout Friends",
                            style = MaterialTheme.typography.headlineSmall,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                        Text(
                            text = "${friends.size} friends • ${onlineFriends.size} online",
                            fontSize = 12.sp,
                            color = CyberTeal
                        )
                    }

                    Button(
                        onClick = { showAddDialog = true },
                        colors = ButtonDefaults.buttonColors(containerColor = ElectricPurple),
                        shape = RoundedCornerShape(20.dp),
                        contentPadding = PaddingValues(horizontal = 14.dp, vertical = 6.dp),
                        modifier = Modifier.testTag("add_friend_button")
                    ) {
                        Icon(Icons.Default.Add, contentDescription = null, modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("Add", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                    }
                }
            }

            // Search Bar
            item {
                OutlinedTextField(
                    value = searchQuery,
                    onValueChange = { searchQuery = it },
                    placeholder = { Text("Search friends by name or interest...", fontSize = 13.sp) },
                    leadingIcon = {
                        Icon(Icons.Default.Search, contentDescription = "Search", tint = Color.White.copy(alpha = 0.6f))
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 8.dp)
                        .testTag("search_friends_input"),
                    shape = RoundedCornerShape(16.dp),
                    singleLine = true
                )
            }

            // Online Friends Horizontal Story Row
            if (onlineFriends.isNotEmpty()) {
                item {
                    Text(
                        text = "🟢 ONLINE NOW (1-Tap Video Call)",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = EmeraldOnline,
                        letterSpacing = 0.5.sp,
                        modifier = Modifier.padding(horizontal = 16.dp, vertical = 6.dp)
                    )
                }

                item {
                    LazyRow(
                        contentPadding = PaddingValues(horizontal = 16.dp),
                        horizontalArrangement = Arrangement.spacedBy(14.dp),
                        modifier = Modifier.padding(bottom = 12.dp)
                    ) {
                        items(onlineFriends) { friend ->
                            Column(
                                horizontalAlignment = Alignment.CenterHorizontally,
                                modifier = Modifier
                                    .clickable { onDirectVideoCall(friend) }
                                    .testTag("online_friend_avatar_${friend.id}")
                            ) {
                                Box(
                                    modifier = Modifier
                                        .size(62.dp)
                                        .clip(CircleShape)
                                        .background(
                                            androidx.compose.ui.graphics.Brush.linearGradient(
                                                listOf(ElectricPurple, NeonCoral)
                                            )
                                        )
                                        .padding(2.5.dp)
                                ) {
                                    Box(
                                        modifier = Modifier
                                            .fillMaxSize()
                                            .clip(CircleShape)
                                            .background(DarkSurfaceElevated),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Text(
                                            text = friend.name.take(1),
                                            fontSize = 22.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = Color.White
                                        )
                                    }
                                    // Green indicator
                                    Box(
                                        modifier = Modifier
                                            .size(14.dp)
                                            .align(Alignment.BottomEnd)
                                            .clip(CircleShape)
                                            .background(EmeraldOnline)
                                            .border(2.dp, DeepIndigoBg, CircleShape)
                                    )
                                }
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(
                                    text = friend.name.split(" ").firstOrNull() ?: friend.name,
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Medium,
                                    color = Color.White
                                )
                            }
                        }
                    }
                }
            }

            // Friends List Header
            item {
                Text(
                    text = "ALL FRIENDS",
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White.copy(alpha = 0.6f),
                    letterSpacing = 1.sp,
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
                )
            }

            // Friends List Items
            items(filteredFriends) { friend ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 4.dp)
                        .testTag("friend_card_${friend.id}"),
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
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.weight(1f)
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(48.dp)
                                    .clip(CircleShape)
                                    .background(ElectricPurple.copy(alpha = 0.3f)),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = friend.name.take(1),
                                    fontSize = 20.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color.White
                                )
                            }

                            Spacer(modifier = Modifier.width(12.dp))

                            Column {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Text(
                                        text = friend.name,
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 14.sp,
                                        color = Color.White
                                    )
                                    if (friend.isFavorite) {
                                        Spacer(modifier = Modifier.width(4.dp))
                                        Icon(
                                            Icons.Default.Favorite,
                                            contentDescription = "Favorite",
                                            tint = HotPink,
                                            modifier = Modifier.size(14.dp)
                                        )
                                    }
                                }

                                Text(
                                    text = friend.bio,
                                    fontSize = 11.sp,
                                    color = Color.White.copy(alpha = 0.65f),
                                    maxLines = 1
                                )

                                Text(
                                    text = "Interests: ${friend.interests}",
                                    fontSize = 10.sp,
                                    color = CyberTeal
                                )
                            }
                        }

                        Row(verticalAlignment = Alignment.CenterVertically) {
                            IconButton(
                                onClick = { onToggleFavorite(friend) },
                                modifier = Modifier.size(36.dp)
                            ) {
                                Icon(
                                    imageVector = if (friend.isFavorite) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                                    contentDescription = "Favorite",
                                    tint = if (friend.isFavorite) HotPink else Color.White.copy(alpha = 0.4f),
                                    modifier = Modifier.size(18.dp)
                                )
                            }

                            IconButton(
                                onClick = { onDirectVideoCall(friend) },
                                modifier = Modifier
                                    .size(40.dp)
                                    .clip(CircleShape)
                                    .background(ElectricPurple)
                                    .testTag("call_friend_${friend.id}")
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Videocam,
                                    contentDescription = "Video Call",
                                    tint = Color.White,
                                    modifier = Modifier.size(20.dp)
                                )
                            }
                        }
                    }
                }
            }

            // Ad banner
            item {
                Spacer(modifier = Modifier.height(8.dp))
                AdBannerCard(
                    isVip = isVip,
                    onWatchAdClick = onWatchAd,
                    onUpgradeVipClick = onOpenVipStore
                )
            }
        }

        // Add Friend Dialog
        if (showAddDialog) {
            var newName by remember { mutableStateOf("") }
            var newBio by remember { mutableStateOf("") }
            var newInterests by remember { mutableStateOf("") }

            AlertDialog(
                onDismissRequest = { showAddDialog = false },
                title = { Text("Add Hangout Friend", color = Color.White) },
                text = {
                    Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                        OutlinedTextField(
                            value = newName,
                            onValueChange = { newName = it },
                            label = { Text("Friend's Name") },
                            singleLine = true
                        )
                        OutlinedTextField(
                            value = newBio,
                            onValueChange = { newBio = it },
                            label = { Text("Bio or status") }
                        )
                        OutlinedTextField(
                            value = newInterests,
                            onValueChange = { newInterests = it },
                            label = { Text("Interests (e.g. Music, Gaming)") }
                        )
                    }
                },
                confirmButton = {
                    Button(
                        onClick = {
                            if (newName.isNotBlank()) {
                                onAddNewFriend(
                                    newName.trim(),
                                    newBio.ifBlank { "Loves casual video hangouts & chats ✨" },
                                    newInterests.ifBlank { "Music, Chai, Movies" }
                                )
                                showAddDialog = false
                            }
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = ElectricPurple)
                    ) {
                        Text("Add to List")
                    }
                },
                dismissButton = {
                    TextButton(onClick = { showAddDialog = false }) {
                        Text("Cancel", color = Color.White.copy(alpha = 0.7f))
                    }
                },
                containerColor = DarkSurfaceElevated
            )
        }
    }
}
