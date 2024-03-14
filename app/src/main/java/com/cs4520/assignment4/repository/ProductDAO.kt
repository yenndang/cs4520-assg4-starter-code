package com.cs4520.assignment4.repository

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.cs4520.assignment4.models.ProductEntity

@Dao
interface ProductDao {
    @Query("SELECT * FROM products")
    fun getAllProducts(): LiveData<List<ProductEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(products: List<ProductEntity>)

    @Query("DELETE FROM products")
    suspend fun deleteAllProducts()

    @Transaction // all operations inside are run atomically
    suspend fun clearAndCacheProducts(products: List<ProductEntity>) {
        deleteAllProducts()
        insertAll(products)
    }
}