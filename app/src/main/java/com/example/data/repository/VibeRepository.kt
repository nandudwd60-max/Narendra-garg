package com.example.data.repository

import com.example.data.db.VibeDao
import com.example.data.model.CallHistory
import com.example.data.model.Friend
import com.example.data.model.HangoutLounge
import com.example.data.model.PeerCandidate
import com.example.data.model.UserProfile
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class VibeRepository(private val dao: VibeDao) {

    val userProfile: Flow<UserProfile> = dao.getUserProfile().map { profile ->
        profile ?: UserProfile()
    }

    val friends: Flow<List<Friend>> = dao.getAllFriends()

    val callHistory: Flow<List<CallHistory>> = dao.getCallHistory()

    suspend fun updateProfile(profile: UserProfile) {
        dao.insertOrUpdateProfile(profile)
    }

    suspend fun addCoins(amount: Int, currentCoins: Int) {
        dao.updateCoins(currentCoins + amount)
    }

    suspend fun spendCoins(amount: Int, currentCoins: Int): Boolean {
        if (currentCoins >= amount) {
            dao.updateCoins(currentCoins - amount)
            return true
        }
        return false
    }

    suspend fun decrementMatchQuota(currentQuota: Int) {
        if (currentQuota > 0) {
            dao.updateDailyFreeMatches(currentQuota - 1)
        }
    }

    suspend fun refillMatchQuota(amount: Int, currentQuota: Int) {
        dao.updateDailyFreeMatches(currentQuota + amount)
    }

    suspend fun activateVip(plan: String, days: Int) {
        val expiry = System.currentTimeMillis() + (days * 24L * 60L * 60L * 1000L)
        dao.updateVipStatus(isVip = true, plan = plan, expiry = expiry)
    }

    suspend fun updateFilterPreferences(gender: String, region: String) {
        dao.updateFilters(gender = gender, region = region)
    }

    suspend fun addFriend(friend: Friend) {
        dao.insertFriend(friend)
    }

    suspend fun removeFriend(friendId: String) {
        dao.deleteFriend(friendId)
    }

    suspend fun toggleFavorite(friendId: String, currentFavorite: Boolean) {
        dao.setFriendFavorite(friendId, !currentFavorite)
    }

    suspend fun recordCall(call: CallHistory) {
        dao.insertCallHistory(call)
    }

    // Static / Simulated sample candidate pool for dynamic video matching
    fun getSampleCandidates(): List<PeerCandidate> {
        return listOf(
            PeerCandidate(
                id = "cand_1",
                name = "Aanya",
                age = 21,
                gender = "Female",
                country = "India",
                city = "Bangalore",
                avatarBgColorHex = 0xFFFF4081,
                bio = "Designing by day, singing acoustic Bollywood by night! 🎶✨",
                interests = listOf("Music", "Bollywood", "Coffee", "Travel"),
                moodStatus = "Listening to Lo-Fi Chai Beats",
                simulatedPhrases = listOf(
                    "Hey there! Where are you connecting from? 😊",
                    "Haha nice to meet you! How's your day going?",
                    "Do you love Bollywood songs too or English pop?",
                    "That's so awesome! Let's play Truth or Dare next!"
                ),
                videoTone = "music"
            ),
            PeerCandidate(
                id = "cand_2",
                name = "Dev",
                age = 23,
                gender = "Male",
                country = "India",
                city = "Delhi",
                avatarBgColorHex = 0xFF3F51B5,
                bio = "Cricket enthusiast & late night coder. Timepass only! 🏏💻",
                interests = listOf("Gaming", "Cricket", "Tech", "Chai"),
                moodStatus = "Looking for someone to talk gaming & cricket!",
                simulatedPhrases = listOf(
                    "Yo bro! Watched yesterday's match?",
                    "Finally someone who matches on gaming vibes!",
                    "What games do you play? Mobile or PC?",
                    "Bro send a gift and let's roll the challenge dice!"
                ),
                videoTone = "funny"
            ),
            PeerCandidate(
                id = "cand_3",
                name = "Natasha",
                age = 22,
                gender = "Female",
                country = "India",
                city = "Mumbai",
                avatarBgColorHex = 0xFF9C27B0,
                bio = "Marine Drive sunsets, photography & spontaneous conversations 🌊📸",
                interests = listOf("Photography", "Movies", "Beaches", "Street Food"),
                moodStatus = "Chilling on my balcony enjoying the breeze 🌆",
                simulatedPhrases = listOf(
                    "Hi! Your lighting looks so cozy!",
                    "I just made a hot cup of tea, perfect time to talk!",
                    "Have you ever been to Mumbai Marine Drive at night?",
                    "Aww thanks so much for the virtual rose, you're sweet! 🌹"
                ),
                videoTone = "chill"
            ),
            PeerCandidate(
                id = "cand_4",
                name = "Zaid",
                age = 24,
                gender = "Male",
                country = "India",
                city = "Hyderabad",
                avatarBgColorHex = 0xFF009688,
                bio = "Biryani fan, beatboxer & meme curator. Let's laugh! 😂🍔",
                interests = listOf("Food", "Beatbox", "Comedy", "Travel"),
                moodStatus = "In the mood for rapid fire Q&A!",
                simulatedPhrases = listOf(
                    "What's up! Ready for some hilarious rapid fire questions?",
                    "If you had to eat only one food for rest of life, what is it?",
                    "No way haha, great choice!",
                    "You've got great energy, adding you to friends!"
                ),
                videoTone = "party"
            ),
            PeerCandidate(
                id = "cand_5",
                name = "Kritika",
                age = 20,
                gender = "Female",
                country = "India",
                city = "Jaipur",
                avatarBgColorHex = 0xFFFF9800,
                bio = "Dancer, student & anime binge watcher. Kon'nichiwa! 🌸✨",
                interests = listOf("Anime", "Dance", "Art", "Books"),
                moodStatus = "Binge watching Demon Slayer between calls 🍿",
                simulatedPhrases = listOf(
                    "Hii! Wow nice camera quality!",
                    "Do you watch anime or K-dramas?",
                    "That filter you have on is super cool!",
                    "Let's play 2 Truths and a Lie!"
                ),
                videoTone = "chill"
            )
        )
    }

    fun getHangoutLounges(): List<HangoutLounge> {
        return listOf(
            HangoutLounge(
                id = "lounge_1",
                title = "Late Night Chai & Deep Talks ☕🌙",
                category = "Chill Hangout",
                hostName = "Aryan Malhotra",
                hostAvatar = "host_aryan",
                participantsCount = 142,
                isVipOnly = false,
                currentTopic = "What is the best memory of college life?",
                emojiTag = "☕"
            ),
            HangoutLounge(
                id = "lounge_2",
                title = "Bollywood Antakshari & Guitar Jam 🎸🎤",
                category = "Music & Singing",
                hostName = "Meera Saxena",
                hostAvatar = "host_meera",
                participantsCount = 89,
                isVipOnly = false,
                currentTopic = "Singing 90s & 2000s golden era songs",
                emojiTag = "🎵"
            ),
            HangoutLounge(
                id = "lounge_3",
                title = "Gamers Lounge: Valorant & BGMI Squad 🎮🔥",
                category = "Gaming & Esports",
                hostName = "TriggerHappy",
                hostAvatar = "host_gamer",
                participantsCount = 210,
                isVipOnly = false,
                currentTopic = "Finding teammates for ranked push tonight",
                emojiTag = "🎮"
            ),
            HangoutLounge(
                id = "lounge_4",
                title = "VIP Secret Suite: High Roller Hangout 👑✨",
                category = "VIP Exclusive",
                hostName = "VibeQueen Maya",
                hostAvatar = "host_maya",
                participantsCount = 56,
                isVipOnly = true,
                currentTopic = "Celebrity gossip, luxury travel & exclusive rewards",
                emojiTag = "👑"
            ),
            HangoutLounge(
                id = "lounge_5",
                title = "English Practice & Global Exchange 🌍🗣️",
                category = "Learning & Social",
                hostName = "Chloe Bennett",
                hostAvatar = "host_chloe",
                participantsCount = 74,
                isVipOnly = false,
                currentTopic = "Casual fluency practice & travel stories",
                emojiTag = "💬"
            )
        )
    }
}
