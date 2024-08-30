package com.snofed.publicapp.db


import androidx.room.*
import com.snofed.publicapp.models.Client
import com.snofed.publicapp.models.NewClubData
import com.snofed.publicapp.models.PublicData

@Dao
interface RoomDao {
    /*@Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertClient(client: Client)

    @Query("SELECT * FROM clients WHERE id = :clientId")
    suspend fun getClientById(clientId: String): Client?

    @Query("SELECT * FROM clients WHERE isInWishlist = 1")
    suspend fun getWishlistClients(): List<Client>*/


    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertClients(clients: NewClubData)

    @Query("SELECT * FROM clients WHERE id = :id")
    suspend fun getClientById(id: String): Client?

    @Query("SELECT * FROM clients")
    suspend fun getAllClients(): List<Client>

    @Query("SELECT * FROM clients")
    suspend fun getAllPublicData(): List<PublicData>

    @Query("SELECT * FROM clients WHERE visibility = 0")
    suspend fun getVisibleClients(): List<Client>

    @Query("UPDATE clients SET isInWishlist = :status WHERE id = :id")
    suspend fun updateWishlistStatus(id: String, status: Boolean)
}