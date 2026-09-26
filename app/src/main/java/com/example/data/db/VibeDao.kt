package com.example.data.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.data.model.CallHistory
import com.example.data.model.Friend
import com.example.data.model.UserProfile
import kotlinx.coroutines.flow.Flow

@Dao
interface VibeDao {

    @Query("SELECT * FROM user_profile WHERE userId = 'me_user_1' LIMIT 1")
    fun getUserProfile(): Flow<UserProfile?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrUpdateProfile(profile: UserProfile)

    @Query("UPDATE user_profile SET coins = :coins WHERE userId = 'me_user_1'")
    suspend fun updateCoins(coins: Int)

    @Query("UPDATE user_profile SET dailyFreeMatchesRemaining = :matches WHERE userId = 'me_user_1'")
    suspend fun updateDailyFreeMatches(matches: Int)

    @Query("UPDATE user_profile SET isVip = :isVip, vipPlan = :plan, vipExpiryTimestamp = :expiry WHERE userId = 'me_user_1'")
    suspend fun updateVipStatus(isVip: Boolean, plan: String, expiry: Long)

    @Query("UPDATE user_profile SET preferredGenderFilter = :gender, preferredRegion = :region WHERE userId = 'me_user_1'")
    suspend fun updateFilters(gender: String, region: String)

    @Query("SELECT * FROM friends ORDER BY isFavorite DESC, name ASC")
    fun getAllFriends(): Flow<List<Friend>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFriend(friend: Friend)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFriends(friends: List<Friend>)

    @Query("DELETE FROM friends WHERE id = :friendId")
    suspend fun deleteFriend(friendId: String)

    @Query("UPDATE friends SET isFavorite = :isFavorite WHERE id = :friendId")
    suspend fun setFriendFavorite(friendId: String, isFavorite: Boolean)

    @Query("SELECT * FROM call_history ORDER BY timestamp DESC LIMIT 50")
    fun getCallHistory(): Flow<List<CallHistory>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCallHistory(call: CallHistory)
}
