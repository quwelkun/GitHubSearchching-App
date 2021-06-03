package com.example.githubsearching.repository

import android.app.Application
import androidx.lifecycle.LiveData
import com.example.githubsearching.database.FavRoomDatabase
import com.example.githubsearching.database.Favorite
import com.example.githubsearching.database.FavoriteDao
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

class FavRepository(application: Application) {
    private val mFavDao: FavoriteDao
    private val executorService: ExecutorService = Executors.newSingleThreadExecutor()
    init {
        val db = FavRoomDatabase.getDatabase(application)
        mFavDao = db.favDao()
    }
    fun getAllFavs(): LiveData<List<Favorite>> = mFavDao.getAllFav()
    fun insert(fav: Favorite) {
        executorService.execute { mFavDao.insert(fav) }
    }
    fun delete(fav: Favorite) {
        executorService.execute { mFavDao.delete(fav) }
    }
}