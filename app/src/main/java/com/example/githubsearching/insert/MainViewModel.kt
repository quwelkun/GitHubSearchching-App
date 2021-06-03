package com.example.githubsearching.insert

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.githubsearching.database.Favorite
import com.example.githubsearching.repository.FavRepository

class MainViewModel(application: Application): ViewModel() {
    private val mFavRepository: FavRepository = FavRepository(application)

    fun getAllFavs(): LiveData<List<Favorite>> = mFavRepository.getAllFavs()

}