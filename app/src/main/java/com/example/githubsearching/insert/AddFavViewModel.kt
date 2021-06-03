package com.example.githubsearching.insert

import android.app.Application
import androidx.lifecycle.ViewModel
import com.example.githubsearching.database.Favorite
import com.example.githubsearching.repository.FavRepository

class AddFavViewModel(application: Application): ViewModel() {
    private val mFavRepository: FavRepository = FavRepository(application)

    fun insert(fav: Favorite) {
        mFavRepository.insert(fav)
    }

    fun delete(fav: Favorite) {
        mFavRepository.delete(fav)
    }

}