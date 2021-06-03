package com.example.consumerapp.ui

import android.database.Cursor
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.consumerapp.R
import com.example.consumerapp.adapter.ConsumerAdapter
import com.example.consumerapp.database.entity.Favorite

class MainActivity : AppCompatActivity() {
    lateinit var recyclerView: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        recyclerView = findViewById(R.id.rv_git_account_consumer)

        getContent()
    }

    private fun getContent() {
        val AUTHORITY = "com.example.githubsearching"
        val SCHEME = "content"
        val CONTENT_URI: Uri = Uri.Builder()
            .scheme(SCHEME)
            .authority(AUTHORITY)
            .appendPath("favorite")
            .build()

        val content = this.contentResolver
        val cursor = content.query(
            CONTENT_URI,
            null,
            null,
            null,
            null
        )
        Log.d("cek cursor","$cursor")
        Log.d("cek content uri","$CONTENT_URI")

        setRecyclerView(cursor)

    }

    private fun convertCursor(cursor: Cursor?): ArrayList<Favorite> {
        val listUser = ArrayList<Favorite>()

        cursor?.apply {
            while (moveToNext()) {
                val id = getInt(getColumnIndexOrThrow("id"))
                val avatar = getString(getColumnIndexOrThrow("avatar"))
                val login = getString(getColumnIndexOrThrow("login"))
                val type = getString(getColumnIndexOrThrow("type"))

                listUser.add(
                    Favorite(id, login, type, avatar)
                )
            }
        }
        return listUser
    }

    fun setRecyclerView(cursor: Cursor?){
        recyclerView.apply {
            layoutManager = LinearLayoutManager(this@MainActivity)
            adapter = ConsumerAdapter(convertCursor(cursor) )
        }

    }
}