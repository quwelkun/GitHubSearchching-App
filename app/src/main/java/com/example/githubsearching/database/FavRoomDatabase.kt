package com.example.githubsearching.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [Favorite::class], version = 1)
abstract class FavRoomDatabase: RoomDatabase() {
    abstract fun favDao(): FavoriteDao

    companion object {
        @Volatile
        private var INSTANCE: FavRoomDatabase? = null

        @JvmStatic
        fun getDatabase(context: Context): FavRoomDatabase {
            if (INSTANCE == null) {
                synchronized(FavRoomDatabase::class.java) {
                    if (INSTANCE == null) {
                        INSTANCE = Room.databaseBuilder(context.applicationContext,
                            FavRoomDatabase::class.java, "note_database")
                            .build()
                    }
                }
            }
            return INSTANCE as FavRoomDatabase
        }
    }
}