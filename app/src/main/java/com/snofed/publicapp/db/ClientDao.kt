package com.snofed.publicapp.db

import androidx.lifecycle.LiveData
import androidx.room.*
import com.snofed.publicapp.models.Client
import kotlinx.coroutines.flow.Flow

@Dao
interface ClientDao {
    @Insert
    suspend fun insertWishlistItem(item: Client)

    @Update
    suspend fun updateWishlistItem(item: Client)

    @Query("DELETE FROM wishlist WHERE id = :id")
    suspend fun deleteWishlistItem(id: Long)

    @Query("SELECT * FROM wishlist")
    fun getAllWishlistItems(): LiveData<List<Client>>

    @Query("SELECT * FROM wishlist WHERE id = :id")
    fun getWishlistItemById(id: Long): LiveData<Client>
}