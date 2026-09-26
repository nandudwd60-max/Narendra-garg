package com.example.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.data.model.AVAILABLE_GIFTS
import com.example.data.model.CallHistory
import com.example.data.model.Friend
import com.example.data.model.HangoutLounge
import com.example.data.model.PeerCandidate
import com.example.data.model.Screen
import com.example.data.model.UserProfile
import com.example.data.model.VirtualGift
import com.example.data.repository.VibeRepository
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlin.random.Random

data class ReactionBubble(
    val id: Long,
    val emoji: String,
    val horizontalBias: Float
)

data class GamePrompt(
    val type: String, // "Truth", "Dare", "Rapid Fire", "Would You Rather"
    val question: String
)

enum class AdRewardType {
    COINS_30,
    MATCH_PASS_3,
    VIP_HD_PASS
}

class VibeViewModel(
    private val repository: VibeRepository
) : ViewModel() {

    val userProfile: StateFlow<UserProfile> = repository.userProfile
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = UserProfile()
        )

    val friends: StateFlow<List<Friend>> = repository.friends
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    val callHistory: StateFlow<List<CallHistory>> = repository.callHistory
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    private val _currentScreen = MutableStateFlow(Screen.MATCH)
    val currentScreen: StateFlow<Screen> = _currentScreen.asStateFlow()

    private val _hangoutLounges = MutableStateFlow<List<HangoutLounge>>(repository.getHangoutLounges())
    val hangoutLounges: StateFlow<List<HangoutLounge>> = _hangoutLounges.asStateFlow()

    // Active Call State
    private val _activePeer = MutableStateFlow<PeerCandidate?>(null)
    val activePeer: StateFlow<PeerCandidate?> = _activePeer.asStateFlow()

    private val _callType = MutableStateFlow("Random Match")
    val callType: StateFlow<String> = _callType.asStateFlow()

    private val _callDurationSeconds = MutableStateFlow(0)
    val callDurationSeconds: StateFlow<Int> = _callDurationSeconds.asStateFlow()

    private val _isMicMuted = MutableStateFlow(false)
    val isMicMuted: StateFlow<Boolean> = _isMicMuted.asStateFlow()

    private val _isVideoMuted = MutableStateFlow(false)
    val isVideoMuted: StateFlow<Boolean> = _isVideoMuted.asStateFlow()

    private val _isFrontCamera = MutableStateFlow(true)
    val isFrontCamera: StateFlow<Boolean> = _isFrontCamera.asStateFlow()

    private val _activeFilter = MutableStateFlow("Normal")
    val activeFilter: StateFlow<String> = _activeFilter.asStateFlow()

    private val _incomingSpeechText = MutableStateFlow<String?>("Connecting audio stream...")
    val incomingSpeechText: StateFlow<String?> = _incomingSpeechText.asStateFlow()

    private val _floatingReactions = MutableStateFlow<List<ReactionBubble>>(emptyList())
    val floatingReactions: StateFlow<List<ReactionBubble>> = _floatingReactions.asStateFlow()

    private val _activeGamePrompt = MutableStateFlow<GamePrompt?>(null)
    val activeGamePrompt: StateFlow<GamePrompt?> = _activeGamePrompt.asStateFlow()

    private val _lastGiftSent = MutableStateFlow<VirtualGift?>(null)
    val lastGiftSent: StateFlow<VirtualGift?> = _lastGiftSent.asStateFlow()

    private val _isMatchSearching = MutableStateFlow(false)
    val isMatchSearching: StateFlow<Boolean> = _isMatchSearching.asStateFlow()

    private val _searchStatusText = MutableStateFlow("Looking for vibrant people...")
    val searchStatusText: StateFlow<String> = _searchStatusText.asStateFlow()

    // Ad Watch Simulation
    private val _isAdPlaying = MutableStateFlow(false)
    val isAdPlaying: StateFlow<Boolean> = _isAdPlaying.asStateFlow()

    private val _adCountdown = MutableStateFlow(5)
    val adCountdown: StateFlow<Int> = _adCountdown.asStateFlow()

    private val _activeAdRewardType = MutableStateFlow<AdRewardType?>(null)
    val activeAdRewardType: StateFlow<AdRewardType?> = _activeAdRewardType.asStateFlow()

    // Dialogs & Feedback
    private val _snackbarMessage = MutableStateFlow<String?>(null)
    val snackbarMessage: StateFlow<String?> = _snackbarMessage.asStateFlow()

    private val _selectedLounge = MutableStateFlow<HangoutLounge?>(null)
    val selectedLounge: StateFlow<HangoutLounge?> = _selectedLounge.asStateFlow()

    private var callTimerJob: Job? = null
    private var peerSimulationJob: Job? = null
    private var adSimulationJob: Job? = null

    fun navigateTo(screen: Screen) {
        if (_currentScreen.value == Screen.ACTIVE_CALL && screen != Screen.ACTIVE_CALL) {
            endCall()
        }
        _currentScreen.value = screen
    }

    fun startRandomMatch() {
        val profile = userProfile.value
        if (!profile.isVip && profile.dailyFreeMatchesRemaining <= 0) {
            _snackbarMessage.value = "Daily free matches limit reached! Watch an Ad or get VIP to continue matching."
            return
        }

        _isMatchSearching.value = true
        _searchStatusText.value = "Scanning hangout radar..."

        viewModelScope.launch {
            delay(800)
            _searchStatusText.value = "Filtering by ${profile.preferredRegion} & ${profile.preferredGenderFilter}..."
            delay(900)
            _searchStatusText.value = "Matching video audio channels..."
            delay(700)

            val candidates = repository.getSampleCandidates()
            val filtered = candidates.filter { candidate ->
                if (profile.preferredGenderFilter == "All") true
                else candidate.gender.equals(profile.preferredGenderFilter, ignoreCase = true)
            }.ifEmpty { candidates }

            val chosenPeer = filtered.random()
            _activePeer.value = chosenPeer
            _callType.value = "Random Match"
            _isMatchSearching.value = false

            // Decrement match quota if not VIP
            if (!profile.isVip) {
                repository.decrementMatchQuota(profile.dailyFreeMatchesRemaining)
            }

            // Launch active call screen
            launchActiveCallSession(chosenPeer)
        }
    }

    fun startFriendCall(friend: Friend) {
        val peer = PeerCandidate(
            id = friend.id,
            name = friend.name,
            age = friend.age,
            gender = friend.gender,
            country = "India",
            city = "Online",
            avatarBgColorHex = 0xFF7E57C2,
            bio = friend.bio,
            interests = friend.interests.split(",").map { it.trim() },
            moodStatus = "Talking with friend",
            simulatedPhrases = listOf(
                "Hey! So glad you called! What's new? ☕",
                "I was just thinking about that funny story!",
                "Show me what filter you are using haha!",
                "Always good vibes talking with you!"
            ),
            videoTone = "chill"
        )
        _activePeer.value = peer
        _callType.value = "Friend Call: ${friend.name}"
        launchActiveCallSession(peer)
    }

    fun joinLounge(lounge: HangoutLounge) {
        val profile = userProfile.value
        if (lounge.isVipOnly && !profile.isVip) {
            _snackbarMessage.value = "👑 VIP Exclusive Lounge! Subscribe to VIP Pass to enter."
            return
        }
        val peer = PeerCandidate(
            id = lounge.id,
            name = "${lounge.title} (Host: ${lounge.hostName})",
            age = 24,
            gender = "Group",
            country = "India",
            city = "${lounge.participantsCount} Hanging out",
            avatarBgColorHex = 0xFFFF7043,
            bio = lounge.currentTopic,
            interests = listOf(lounge.category, "Hangout", "Stage"),
            moodStatus = "Live stage with ${lounge.participantsCount} participants",
            simulatedPhrases = listOf(
                "Welcome to the hangout room! Grab a chai and chill ☕",
                "Next topic: ${lounge.currentTopic}!",
                "Drop your reactions and virtual claps! 👏🎉",
                "Who wants to take the virtual mic next?"
            ),
            videoTone = "party"
        )
        _activePeer.value = peer
        _callType.value = "Hangout Lounge"
        launchActiveCallSession(peer)
    }

    private fun launchActiveCallSession(peer: PeerCandidate) {
        _currentScreen.value = Screen.ACTIVE_CALL
        _callDurationSeconds.value = 0
        _isMicMuted.value = false
        _isVideoMuted.value = false
        _floatingReactions.value = emptyList()
        _activeGamePrompt.value = null
        _lastGiftSent.value = null

        // Start Call Timer
        callTimerJob?.cancel()
        callTimerJob = viewModelScope.launch {
            while (true) {
                delay(1000)
                _callDurationSeconds.value += 1
            }
        }

        // Simulate Peer dynamic interactions
        peerSimulationJob?.cancel()
        peerSimulationJob = viewModelScope.launch {
            delay(1500)
            _incomingSpeechText.value = peer.simulatedPhrases.firstOrNull() ?: "Hey there!"
            var phraseIndex = 1

            while (true) {
                delay(6000)
                if (phraseIndex < peer.simulatedPhrases.size) {
                    _incomingSpeechText.value = peer.simulatedPhrases[phraseIndex]
                    phraseIndex++
                } else {
                    val genericResponses = listOf(
                        "Haha that's hilarious! 😂",
                        "Tell me more about your hobbies!",
                        "You have awesome vibes! ✨",
                        "Let's stay connected on VibeCall!"
                    )
                    _incomingSpeechText.value = genericResponses.random()
                }

                // Random peer reaction occasionally
                if (Random.nextBoolean()) {
                    val emojis = listOf("❤️", "🔥", "🎉", "👏", "✨", "😂")
                    sendReaction(emojis.random())
                }
            }
        }
    }

    fun endCall() {
        callTimerJob?.cancel()
        peerSimulationJob?.cancel()

        val peer = _activePeer.value
        val duration = _callDurationSeconds.value
        if (peer != null && duration > 2) {
            viewModelScope.launch {
                repository.recordCall(
                    CallHistory(
                        peerName = peer.name,
                        peerAvatar = peer.id,
                        callType = _callType.value,
                        durationSeconds = duration,
                        timestamp = System.currentTimeMillis(),
                        coinsSpent = 0,
                        wasAddedAsFriend = false
                    )
                )
            }
        }

        _activePeer.value = null
        _currentScreen.value = Screen.MATCH
    }

    fun nextMatch() {
        endCall()
        startRandomMatch()
    }

    fun toggleMic() {
        _isMicMuted.value = !_isMicMuted.value
    }

    fun toggleVideo() {
        _isVideoMuted.value = !_isVideoMuted.value
    }

    fun flipCamera() {
        _isFrontCamera.value = !_isFrontCamera.value
    }

    fun setFilter(filter: String) {
        val profile = userProfile.value
        if (filter != "Normal" && !profile.isVip && !profile.hdVideoEnabled) {
            _snackbarMessage.value = "✨ Beauty & HD Filters are a VIP feature! Watch Ad or upgrade to VIP."
        }
        _activeFilter.value = filter
    }

    fun sendReaction(emoji: String) {
        val bubble = ReactionBubble(
            id = System.currentTimeMillis() + Random.nextInt(1000),
            emoji = emoji,
            horizontalBias = Random.nextFloat()
        )
        _floatingReactions.value = _floatingReactions.value + bubble

        viewModelScope.launch {
            delay(2800)
            _floatingReactions.value = _floatingReactions.value.filter { it.id != bubble.id }
        }
    }

    fun sendGift(gift: VirtualGift) {
        val profile = userProfile.value
        if (profile.coins < gift.coinCost) {
            _snackbarMessage.value = "Need ${gift.coinCost - profile.coins} more coins! Tap store or watch ad."
            return
        }

        viewModelScope.launch {
            val success = repository.spendCoins(gift.coinCost, profile.coins)
            if (success) {
                _lastGiftSent.value = gift
                sendReaction(gift.emoji)
                _snackbarMessage.value = "Sent ${gift.name} ${gift.emoji} to ${_activePeer.value?.name ?: "partner"}!"
                delay(3000)
                _lastGiftSent.value = null
            }
        }
    }

    fun addCurrentPeerAsFriend() {
        val peer = _activePeer.value ?: return
        viewModelScope.launch {
            val newFriend = Friend(
                id = "f_${peer.id}_${System.currentTimeMillis()}",
                name = peer.name,
                avatarUrl = "avatar_${peer.name.lowercase()}",
                status = "Online",
                bio = peer.bio,
                interests = peer.interests.joinToString(", "),
                gender = peer.gender,
                age = peer.age,
                isFavorite = false,
                lastSeenOrCall = "Just now"
            )
            repository.addFriend(newFriend)
            _snackbarMessage.value = "🎉 ${peer.name} added to your Friends list!"
        }
    }

    fun triggerGamePrompt(category: String) {
        val prompts = when (category) {
            "Truth" -> listOf(
                "What is the most embarrassing thing you've done on a call?",
                "What is your biggest secret guilty pleasure song?",
                "Have you ever ghosted someone you liked?",
                "What was your first impression of me right now?"
            )
            "Dare" -> listOf(
                "Sing the chorus of your favorite song right now out loud!",
                "Do your funniest celebrity impression for 10 seconds!",
                "Show the most random object within your arm's reach!",
                "Speak in a dramatic movie villain voice for the next 30 seconds!"
            )
            else -> listOf(
                "Tea or Coffee? Rapid answer!",
                "Mountains or Beaches for vacation?",
                "Late night chatterbox or early morning bird?",
                "Instagram Reels or YouTube Shorts?"
            )
        }
        _activeGamePrompt.value = GamePrompt(category, prompts.random())
    }

    fun clearGamePrompt() {
        _activeGamePrompt.value = null
    }

    // Ad Watch Simulation
    fun watchRewardedAd(rewardType: AdRewardType) {
        _activeAdRewardType.value = rewardType
        _isAdPlaying.value = true
        _adCountdown.value = 5

        adSimulationJob?.cancel()
        adSimulationJob = viewModelScope.launch {
            while (_adCountdown.value > 0) {
                delay(1000)
                _adCountdown.value -= 1
            }
        }
    }

    fun claimAdReward() {
        val rewardType = _activeAdRewardType.value ?: return
        val profile = userProfile.value
        viewModelScope.launch {
            when (rewardType) {
                AdRewardType.COINS_30 -> {
                    repository.addCoins(30, profile.coins)
                    _snackbarMessage.value = "🎁 Ad Reward: +30 Coins credited to your wallet!"
                }
                AdRewardType.MATCH_PASS_3 -> {
                    repository.refillMatchQuota(3, profile.dailyFreeMatchesRemaining)
                    _snackbarMessage.value = "🎁 Ad Reward: +3 Free Video Matches unlocked!"
                }
                AdRewardType.VIP_HD_PASS -> {
                    repository.activateVip("1-Day Ad Pass", 1)
                    _snackbarMessage.value = "🎁 Ad Reward: 24-Hour VIP Trial Activated!"
                }
            }
            _isAdPlaying.value = false
            _activeAdRewardType.value = null
        }
    }

    fun dismissAd() {
        _isAdPlaying.value = false
        _activeAdRewardType.value = null
    }

    // Purchase & Subscriptions
    fun subscribeVip(planName: String, days: Int, priceLabel: String) {
        viewModelScope.launch {
            repository.activateVip(planName, days)
            repository.addCoins(500, userProfile.value.coins) // VIP bonus coins!
            _snackbarMessage.value = "👑 Welcome to VIP Club ($planName)! +500 Bonus coins credited!"
        }
    }

    fun buyCoinPack(coins: Int, bonus: Int, priceLabel: String) {
        viewModelScope.launch {
            val total = coins + bonus
            repository.addCoins(total, userProfile.value.coins)
            _snackbarMessage.value = "💎 Success! $total Coins added to your balance for $priceLabel."
        }
    }

    fun updateFilters(gender: String, region: String) {
        val profile = userProfile.value
        if (gender != "All" && !profile.isVip) {
            _snackbarMessage.value = "👑 Gender filter is unlocked with VIP subscription!"
            return
        }
        viewModelScope.launch {
            repository.updateFilterPreferences(gender, region)
            _snackbarMessage.value = "Filters updated: $region | $gender"
        }
    }

    fun clearSnackbar() {
        _snackbarMessage.value = null
    }

    fun removeFriend(friendId: String) {
        viewModelScope.launch {
            repository.removeFriend(friendId)
            _snackbarMessage.value = "Friend removed."
        }
    }

    fun toggleFavoriteFriend(friend: Friend) {
        viewModelScope.launch {
            repository.toggleFavorite(friend.id, friend.isFavorite)
        }
    }

    fun setSelectedLounge(lounge: HangoutLounge?) {
        _selectedLounge.value = lounge
    }
}

class VibeViewModelFactory(
    private val repository: VibeRepository
) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(VibeViewModel::class.java)) {
            return VibeViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
