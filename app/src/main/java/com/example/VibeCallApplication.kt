package com.example

import android.app.Application
import com.example.data.db.VibeDatabase
import com.example.data.repository.VibeRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob

class VibeCallApplication : Application() {
    private val applicationScope = CoroutineScope(SupervisorJob())

    val database by lazy { VibeDatabase.getDatabase(this, applicationScope) }
    val repository by lazy { VibeRepository(database.vibeDao()) }
}
