package com.example.githubsearching.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import androidx.annotation.StringRes
import androidx.viewpager2.widget.ViewPager2
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.githubsearching.R
import com.example.githubsearching.UserSource
import com.example.githubsearching.adapter.SectionsPagerAdapter
import com.example.githubsearching.database.Favorite
import com.example.githubsearching.databinding.ActivityDetailUserBinding
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.loopj.android.http.AsyncHttpClient
import com.loopj.android.http.AsyncHttpResponseHandler
import cz.msebera.android.httpclient.Header
import org.json.JSONObject

class DetailUserActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailUserBinding
    private lateinit var userSource: UserSource
    private  var option_menu: Int = 0
    private lateinit var userFav: Favorite

    companion object{
        const val USER_NAME = "user_name"
        const val OPTION_MENU = "option_menu"
        const val FAVORITE = "favorite"

        @StringRes
        private val TAB_TITLES = intArrayOf(
                R.string.tab_following,
                R.string.tab_follower
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailUserBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val actionBar = supportActionBar
        actionBar?.title = "Profile"

        val username = intent.getStringExtra(USER_NAME)
        option_menu = intent.getIntExtra(OPTION_MENU, 0)

        getDetailData(username)
        setPagerAdapter(username!!)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        if(option_menu == 0) {
            menuInflater.inflate(R.menu.add_menu, menu)
        } else if(option_menu == 1) {
            menuInflater.inflate(R.menu.del_menu, menu)
        }
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId) {
            R.id.add_item -> {
                val intent = Intent(this@DetailUserActivity, FavoriteAddActivity::class.java)
                intent.putExtra(FavoriteAddActivity.USER_FAV_SOURCE, userSource)
                startActivityForResult(intent, FavoriteAddActivity.REQUEST_ADD)
            }
            R.id.del_item -> {
                userFav = intent.getParcelableExtra<Favorite>(FAVORITE) as Favorite
                var intent = Intent(this@DetailUserActivity, FavDelActivity::class.java)
                intent.putExtra(FavDelActivity.USER_NAME, userFav)
                startActivity(intent)
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun getDetailData(username: String?) {
        val url = "https://api.github.com/users/$username"
        val asyncClient = AsyncHttpClient()
        asyncClient.addHeader("Authorization", "token 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")
        asyncClient.addHeader("User-Agent", "request")
        asyncClient.get(url, object: AsyncHttpResponseHandler() {

            override fun onSuccess(statusCode: Int, headers: Array<out Header>?, responseBody: ByteArray?) {
                try {
                    val result = String(responseBody!!)
                    val objectData = JSONObject(result)
                    Log.d("Object", objectData.toString())

                    binding.tvDetailUserName.text = objectData.getString("login")
                    binding.tvDetailName.text = objectData.getString("type")
                    binding.followerCount.text = objectData.getString("followers")
                    binding.followingCount.text = objectData.getString("following")
                    binding.repositoryCount.text = objectData.getString("public_repos")
                    Glide.with(this@DetailUserActivity)
                            .load(objectData.getString("avatar_url"))
                            .apply(RequestOptions().override(350,550))
                            .into(binding.imgAvatar)

                    userSource = UserSource(
                        objectData.getString("login"),
                        objectData.getString("type"),
                        objectData.getString("avatar_url")
                    )

                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }

            override fun onFailure(statusCode: Int, headers: Array<out Header>?, responseBody: ByteArray?, error: Throwable?) {

            }
        })
    }

    private fun setPagerAdapter(username: String) {
        val sectionsPagerAdapter = SectionsPagerAdapter(this)
        sectionsPagerAdapter.username = username
        val viewPager: ViewPager2 = findViewById(R.id.view_pager)
        viewPager.adapter = sectionsPagerAdapter
        val tabs: TabLayout = findViewById(R.id.tabs)
        TabLayoutMediator(tabs, viewPager) { tab, position ->
            tab.text = resources.getString(TAB_TITLES[position])
        }.attach()
        supportActionBar?.elevation = 0f
    }


}