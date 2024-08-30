/*
package com.snofed.publicapp.db

import android.content.Context
import androidx.databinding.adapters.Converters
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.snofed.publicapp.models.Client


@Database(entities = [Client::class], version = 1, exportSchema = false)
@TypeConverters(Converters::class) // Add this annotation
abstract class ClientDatabase : RoomDatabase() {

    abstract fun clientDao(): RoomDao

    companion object {
        @Volatile
        private var INSTANCE: ClientDatabase? = null

        fun getDatabase(context: Context): ClientDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    ClientDatabase::class.java,
                    "clientDB"
                ).build()
                INSTANCE = instance
                instance
            }
        }

    }
}
*/
