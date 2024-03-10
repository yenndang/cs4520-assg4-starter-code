package com.cs4520.assignment4

import android.content.Context
import androidx.room.Room
import com.cs4520.assignment4.repository.AppDatabase

object AppDatabaseSingleton {
    private var INSTANCE: AppDatabase? = null

    fun getDatabase(context: Context): AppDatabase {
        return INSTANCE ?: synchronized(this) {
            val instance = Room.databaseBuilder(
                context.applicationContext,
                AppDatabase::class.java,
                "app_database"
            ).build()
            INSTANCE = instance
            instance
        }
    }
}
