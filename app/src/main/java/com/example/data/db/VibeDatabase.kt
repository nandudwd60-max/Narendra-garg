package com.example.data.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.data.model.CallHistory
import com.example.data.model.Friend
import com.example.data.model.UserProfile
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Database(
    entities = [UserProfile::class, Friend::class, CallHistory::class],
    version = 1,
    exportSchema = false
)
abstract class VibeDatabase : RoomDatabase() {

    abstract fun vibeDao(): VibeDao

    companion object {
        @Volatile
        private var INSTANCE: VibeDatabase? = null

        fun getDatabase(context: Context, scope: CoroutineScope): VibeDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    VibeDatabase::class.java,
                    "vibecall_database"
                )
                    .fallbackToDestructiveMigration()
                    .addCallback(DatabaseCallback(scope))
                    .build()
                INSTANCE = instance
                instance
            }
        }

        private class DatabaseCallback(
            private val scope: CoroutineScope
        ) : RoomDatabase.Callback() {
            override fun onCreate(db: SupportSQLiteDatabase) {
                super.onCreate(db)
                INSTANCE?.let { database ->
                    scope.launch(Dispatchers.IO) {
                        populateInitialData(database.vibeDao())
                    }
                }
            }

            suspend fun populateInitialData(dao: VibeDao) {
                dao.insertOrUpdateProfile(
                    UserProfile(
                        userId = "me_user_1",
                        name = "Alex Sharma",
                        bio = "Music lover, weekend gamer & late night timepass caller 🎧✨",
                        avatar = "avatar_alex",
                        gender = "Male",
                        age = 22,
                        coins = 150,
                        dailyFreeMatchesRemaining = 5,
                        isVip = false,
                        vipPlan = "None",
                        preferredGenderFilter = "All",
                        preferredRegion = "India"
                    )
                )

                val initialFriends = listOf(
                    Friend(
                        id = "f1",
                        name = "Priya Kapoor",
                        avatarUrl = "avatar_priya",
                        status = "Online",
                        bio = "Coffee lover & amateur guitarist 🎸",
                        interests = "Music, Anime, Travel",
                        gender = "Female",
                        age = 21,
                        isFavorite = true,
                        lastSeenOrCall = "2m ago"
                    ),
                    Friend(
                        id = "f2",
                        name = "Rohan Verma",
                        avatarUrl = "avatar_rohan",
                        status = "In Call",
                        bio = "Valorant grinder & movie buff 🎮🎬",
                        interests = "Gaming, Tech, Marvel",
                        gender = "Male",
                        age = 23,
                        isFavorite = true,
                        lastSeenOrCall = "15m ago"
                    ),
                    Friend(
                        id = "f3",
                        name = "Ananya Sen",
                        avatarUrl = "avatar_ananya",
                        status = "Online",
                        bio = "Always ready for deep 3am conversations 🌙",
                        interests = "Poetry, Chai, Late Talks",
                        gender = "Female",
                        age = 22,
                        isFavorite = false,
                        lastSeenOrCall = "1h ago"
                    ),
                    Friend(
                        id = "f4",
                        name = "Kabir Singh",
                        avatarUrl = "avatar_kabir",
                        status = "Away",
                        bio = "Fitness, EDM & Road trips 🏍️",
                        interests = "Fitness, Music, Photography",
                        gender = "Male",
                        age = 24,
                        isFavorite = false,
                        lastSeenOrCall = "3h ago"
                    ),
                    Friend(
                        id = "f5",
                        name = "Sara Khan",
                        avatarUrl = "avatar_sara",
                        status = "Online",
                        bio = "Fashion enthusiast & foodie explorer 🍕👗",
                        interests = "Food, Fashion, Travel",
                        gender = "Female",
                        age = 20,
                        isFavorite = false,
                        lastSeenOrCall = "5m ago"
                    )
                )
                dao.insertFriends(initialFriends)

                val initialHistory = listOf(
                    CallHistory(
                        peerName = "Priya Kapoor",
                        peerAvatar = "avatar_priya",
                        callType = "Friend Call",
                        durationSeconds = 480,
                        timestamp = System.currentTimeMillis() - 7200000L,
                        coinsSpent = 0,
                        wasAddedAsFriend = true
                    ),
                    CallHistory(
                        peerName = "Riya from Mumbai",
                        peerAvatar = "avatar_riya",
                        callType = "Random Match",
                        durationSeconds = 240,
                        timestamp = System.currentTimeMillis() - 28800000L,
                        coinsSpent = 10,
                        wasAddedAsFriend = false
                    )
                )
                for (call in initialHistory) {
                    dao.insertCallHistory(call)
                }
            }
        }
    }
}
