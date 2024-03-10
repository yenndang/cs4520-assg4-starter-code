package com.cs4520.assignment4.repository

import androidx.room.Database
import androidx.room.RoomDatabase
import com.cs4520.assignment4.models.ProductEntity

@Database(entities = [ProductEntity::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun productDao(): ProductDao
}