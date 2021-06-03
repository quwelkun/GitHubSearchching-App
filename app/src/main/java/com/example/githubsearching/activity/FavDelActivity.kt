package com.example.githubsearching.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.githubsearching.database.Favorite
import com.example.githubsearching.databinding.ActivityFavDelBinding
import com.example.githubsearching.helper.ViewModelFactory
import com.example.githubsearching.insert.AddFavViewModel

class FavDelActivity : AppCompatActivity() {
    private lateinit var addFavViewModel: AddFavViewModel
    private lateinit var binding: ActivityFavDelBinding

    companion object{
        const val USER_NAME = "user_name"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFavDelBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.title = "HapusFavorite"

        val fav = intent.getParcelableExtra<Favorite>(USER_NAME) as Favorite
        binding.tvItemTitleFav.text = fav.login
        binding.tvItemTypeFav.text = fav.type
        Glide.with(this)
            .load(fav.avatar)
            .apply(RequestOptions().override(350,550))
            .into(binding.profileImageFav)

        binding.btnSubmit.setOnClickListener {
            addFavViewModel = obtainViewModel(this@FavDelActivity)
            addFavViewModel.delete(fav as Favorite)
            Toast.makeText(this, "Username: ${fav.login} telah dihapus", Toast.LENGTH_SHORT).show()
            startActivity(Intent(this@FavDelActivity, DisplayFavoriteActivity::class.java))
        }
    }

    private fun obtainViewModel(activity: AppCompatActivity): AddFavViewModel {
        val factory = ViewModelFactory.getInstance(activity.application)
        return ViewModelProvider(activity, factory).get(AddFavViewModel::class.java)
    }
}