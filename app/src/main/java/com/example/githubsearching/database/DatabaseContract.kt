package com.example.githubsearching.database

import android.net.Uri

object DatabaseContract {
    val TABLE_NAME = "favorite"
    val AUTHORITY = "com.example.githubsearching"
    const val SCHEME = "content"
    val CONTENT_URI: Uri = Uri.Builder().scheme(SCHEME)
            .authority(AUTHORITY)
            .appendPath(TABLE_NAME)
            .build()
}