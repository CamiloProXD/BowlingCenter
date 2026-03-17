package com.example.bowlingcenter.data.db

import android.content.Context
import androidx.room.*
import androidx.room.Room
import com.example.bowlingcenter.data.model.Reserva

@Database(entities = [Reserva::class], version = 2, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {

    abstract fun reservaDao(): ReservaDao

    companion object {
        @Volatile private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase =
            INSTANCE ?: synchronized(this) {
                Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "bowling_db"
                )
                    .fallbackToDestructiveMigration()
                    .build()
                    .also { INSTANCE = it }
            }
    }
}