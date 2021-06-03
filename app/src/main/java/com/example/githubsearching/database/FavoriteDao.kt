package com.example.githubsearching.database

import android.database.Cursor
import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface FavoriteDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(fav: Favorite)

    @Delete
    fun delete(fav: Favorite)

    @Query("SELECT * FROM favorite")
    fun cursorGetAll(): Cursor

    @Query("SELECT * from favorite ORDER BY id ASC")
    fun getAllFav(): LiveData<List<Favorite>>
}