package com.example.githubsearching.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.githubsearching.R
import com.example.githubsearching.UserSource
import com.example.githubsearching.adapter.FavoriteAdapter
import com.example.githubsearching.database.Favorite
import com.example.githubsearching.databinding.ActivityDisplayFavoriteBinding
import com.example.githubsearching.helper.ViewModelFactory
import com.example.githubsearching.insert.MainViewModel
import com.google.android.material.snackbar.Snackbar

class DisplayFavoriteActivity : AppCompatActivity() {
    private var _activityDisplayFavBinding: ActivityDisplayFavoriteBinding? = null
    private val binding get() = _activityDisplayFavBinding

    private lateinit var adapter: FavoriteAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _activityDisplayFavBinding = ActivityDisplayFavoriteBinding.inflate(layoutInflater)
        setContentView(binding?.root)
        supportActionBar?.title = "FavoriteList"

        val favViewModel = obtainViewModel(this@DisplayFavoriteActivity)
        favViewModel.getAllFavs().observe(this, favObserver)

        adapter = FavoriteAdapter(this@DisplayFavoriteActivity)

        binding?.rvFavorite?.layoutManager = LinearLayoutManager(this)
        binding?.rvFavorite?.setHasFixedSize(true)
        binding?.rvFavorite?.adapter = adapter

    }

    override fun onBackPressed() {
        startActivity(Intent(this@DisplayFavoriteActivity, MainActivity::class.java))
        super.onBackPressed()
    }

    override fun onDestroy() {
        super.onDestroy()
        _activityDisplayFavBinding = null
    }

    private fun obtainViewModel(activity: AppCompatActivity): MainViewModel {
        val factory = ViewModelFactory.getInstance(activity.application)
        return ViewModelProvider(activity, factory).get(MainViewModel::class.java)
    }

    private val favObserver = Observer<List<Favorite>> { noteList ->
        if (noteList != null) {
            adapter.setListFav(noteList)
            adapter.setOnItemClickCallback(object : FavoriteAdapter.OnItemClickCallback {
                override fun onItemClicked(userSource: Favorite) {
                    Toast.makeText(this@DisplayFavoriteActivity, "${userSource.login}", Toast.LENGTH_SHORT).show()
                    showSelectedUser(userSource, userSource.login as String)
                }
            })
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (data != null) {
            if (requestCode == FavoriteAddActivity.REQUEST_ADD) {
                if (resultCode == FavoriteAddActivity.RESULT_ADD) {
                    showSnackbarMessage(getString(R.string.added))
                }
            } else if (requestCode == FavoriteAddActivity.REQUEST_UPDATE) {
                if (resultCode == FavoriteAddActivity.RESULT_UPDATE) {
                    showSnackbarMessage(getString(R.string.changed))
                } else if (resultCode == FavoriteAddActivity.RESULT_DELETE) {
                    showSnackbarMessage(getString(R.string.deleted))
                }
            }
        }
    }

    private fun showSnackbarMessage(message: String) {
        Snackbar.make(binding?.root as View, message, Snackbar.LENGTH_SHORT).show()
    }

    private fun showSelectedUser(userFav: Favorite, username: String) {
        var intent = Intent(this@DisplayFavoriteActivity, DetailUserActivity::class.java)
        intent.putExtra(DetailUserActivity.USER_NAME, username)
        intent.putExtra(DetailUserActivity.FAVORITE, userFav)
        intent.putExtra(DetailUserActivity.OPTION_MENU, 1)
        startActivity(intent)
    }


}