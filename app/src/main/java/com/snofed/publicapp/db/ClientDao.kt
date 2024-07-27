package com.snofed.publicapp.db

import androidx.lifecycle.LiveData
import androidx.room.*
import com.snofed.publicapp.models.Client

@Dao
interface ClientDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsert(client: Client): Long

    @Query("SELECT * FROM client")
    fun getAllClient(): LiveData<List<Client>>

    @Delete
    suspend fun deleteArticle(client: Client)
}