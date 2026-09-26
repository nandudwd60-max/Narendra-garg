package com.example.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "user_profile")
data class UserProfile(
    @PrimaryKey val userId: String = "me_user_1",
    val name: String = "Alex Sharma",
    val bio: String = "Here for good vibes, music & late night talks! 🎸✨",
    val avatar: String = "avatar_alex",
    val gender: String = "Male",
    val age: Int = 22,
    val coins: Int = 180,
    val dailyFreeMatchesRemaining: Int = 4,
    val isVip: Boolean = false,
    val vipPlan: String = "None", // "None", "Weekly", "Monthly", "Annual"
    val vipExpiryTimestamp: Long = 0L,
    val preferredGenderFilter: String = "All", // "All", "Female", "Male"
    val preferredRegion: String = "India", // "Global", "India", "USA", "Europe"
    val hdVideoEnabled: Boolean = false
)

@Entity(tableName = "friends")
data class Friend(
    @PrimaryKey val id: String,
    val name: String,
    val avatarUrl: String,
    val status: String, // "Online", "In Call", "Away", "Offline"
    val bio: String,
    val interests: String,
    val gender: String,
    val age: Int,
    val isFavorite: Boolean = false,
    val lastSeenOrCall: String = "Just now"
)

@Entity(tableName = "call_history")
data class CallHistory(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val peerName: String,
    val peerAvatar: String,
    val callType: String, // "Random Match", "Friend Call", "Lounge"
    val durationSeconds: Int,
    val timestamp: Long = System.currentTimeMillis(),
    val coinsSpent: Int = 0,
    val wasAddedAsFriend: Boolean = false
)

data class HangoutLounge(
    val id: String,
    val title: String,
    val category: String,
    val hostName: String,
    val hostAvatar: String,
    val participantsCount: Int,
    val isVipOnly: Boolean = false,
    val currentTopic: String,
    val emojiTag: String
)

data class PeerCandidate(
    val id: String,
    val name: String,
    val age: Int,
    val gender: String,
    val country: String,
    val city: String,
    val avatarBgColorHex: Long,
    val bio: String,
    val interests: List<String>,
    val moodStatus: String,
    val simulatedPhrases: List<String>,
    val videoTone: String // "chill", "funny", "music", "party"
)

enum class Screen {
    MATCH,
    FRIENDS,
    LOUNGES,
    VIP_STORE,
    PROFILE,
    ACTIVE_CALL
}

data class VirtualGift(
    val id: String,
    val name: String,
    val emoji: String,
    val coinCost: Int,
    val description: String
)

val AVAILABLE_GIFTS = listOf(
    VirtualGift("gift_rose", "Red Rose", "🌹", 10, "Sweet & friendly gesture"),
    VirtualGift("gift_chai", "Hot Chai", "☕", 20, "Vibe over a warm cup"),
    VirtualGift("gift_sunglasses", "Cool Shades", "🕶️", 50, "You look stunning!"),
    VirtualGift("gift_fireworks", "Fireworks", "🎆", 100, "Party celebration!"),
    VirtualGift("gift_rocket", "Rocket Boost", "🚀", 250, "To the moon vibes"),
    VirtualGift("gift_crown", "VIP Crown", "👑", 500, "Ultimate royal appreciation")
)
