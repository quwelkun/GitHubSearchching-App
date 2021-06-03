package com.example.githubsearching.activity

import android.app.SearchManager
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.githubsearching.adapter.ListViewAdapter
import com.example.githubsearching.R
import com.example.githubsearching.UserSource
import com.example.githubsearching.databinding.ActivityMainBinding
import com.loopj.android.http.AsyncHttpClient
import com.loopj.android.http.AsyncHttpResponseHandler
import cz.msebera.android.httpclient.Header
import org.json.JSONObject

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.rvGitAccount.setHasFixedSize(true)

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.option_menu, menu)

        val searchManager = getSystemService(Context.SEARCH_SERVICE) as SearchManager
        val searchView = menu?.findItem(R.id.search)?.actionView as SearchView

        searchView.setSearchableInfo(searchManager.getSearchableInfo(componentName))
        searchView.queryHint = resources.getString(R.string.search_hint)
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {

            override fun onQueryTextSubmit(query: String): Boolean {
                binding.layMain.visibility = View.VISIBLE
                getDataUserFromApi(query)
                return true
            }

            override fun onQueryTextChange(newText: String): Boolean {
                if(newText != null) {
                    binding.progressBar.visibility = View.VISIBLE
                    getDataUserFromApi(newText)
                }
                return true
            }


        })

        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId) {
            R.id.setelan -> {
                startActivity(Intent(this@MainActivity, SettingActivity::class.java))
            }
            R.id.favorite_menu -> {
                startActivity(Intent(this@MainActivity, DisplayFavoriteActivity::class.java))
            }
        }
        return super.onOptionsItemSelected(item)

    }

    private fun getDataUserFromApi(username: String) {
        binding.layMain.visibility = View.INVISIBLE
        val asyncClient = AsyncHttpClient()
        val url = "https://api.github.com/search/users?q=$username"
        asyncClient.addHeader("Authorization", "token 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")
        asyncClient.addHeader("User-Agent", "request")
        asyncClient.get(url, object: AsyncHttpResponseHandler() {

            override fun onSuccess(
                statusCode: Int,
                headers: Array<out Header>?,
                responseBody: ByteArray?
            ) {

                binding.progressBar.visibility = View.INVISIBLE
                var mArrayAccount= arrayListOf<UserSource>()

                Log.d(MainActivity::class.java.simpleName, String(responseBody!!))
                try {
                    val result = String(responseBody)
                    val responseObject = JSONObject(result)
                    val items = responseObject.getJSONArray("items")
                    var userList = ArrayList<UserSource>()

                    for(i in 0 until items.length()) {
                        val jsonObject = items.getJSONObject(i)
                        val name = jsonObject.getString("login")
                        val type = jsonObject.getString("type")
                        val avatar = jsonObject.getString("avatar_url")

                        val userSource = UserSource(
                            name, type, avatar
                        )

                        userList.add(userSource)
                    }

                    mArrayAccount.addAll(userList)
                    Log.d("ArrayAccount", mArrayAccount.toString())
                    showList(mArrayAccount)

                } catch (e: Exception) {
                    Toast.makeText(this@MainActivity, e.message, Toast.LENGTH_SHORT).show()
                    e.printStackTrace()
                }

            }

            override fun onFailure(
                statusCode: Int,
                headers: Array<out Header>?,
                responseBody: ByteArray?,
                error: Throwable?
            ) {
                binding.progressBar.visibility = View.INVISIBLE
            }
        })
    }

    private fun showList(mArrayAccount: ArrayList<UserSource>) {
        Log.d("ShowListGet", mArrayAccount.toString())
        binding.rvGitAccount.layoutManager = LinearLayoutManager(this)
        val gitUserAdapter = ListViewAdapter()
        gitUserAdapter.setData(mArrayAccount)
        binding.rvGitAccount.adapter = gitUserAdapter

        if(gitUserAdapter.itemCount == 0) {
            binding.layMain.visibility = View.VISIBLE
        } else {
            binding.layMain.visibility = View.INVISIBLE
        }

        gitUserAdapter.setOnItemClickCallback(object : ListViewAdapter.OnItemClickCallback {
            override fun onItemClicked(userSource: UserSource) {
                Toast.makeText(this@MainActivity, "${userSource.name}", Toast.LENGTH_SHORT).show()
                showSelectedUser(userSource.name)
            }
        })
    }

    private fun showSelectedUser(username: String) {
        var intent = Intent(this@MainActivity, DetailUserActivity::class.java)
        intent.putExtra(DetailUserActivity.USER_NAME, username)
        intent.putExtra(DetailUserActivity.OPTION_MENU, 0)
        startActivity(intent)
    }
}