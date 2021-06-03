package com.example.githubsearching

import android.content.ContentProvider
import android.content.ContentValues
import android.content.UriMatcher
import android.database.Cursor
import android.net.Uri
import com.example.githubsearching.database.DatabaseContract.AUTHORITY
import com.example.githubsearching.database.FavRoomDatabase
import com.example.githubsearching.database.FavoriteDao

class MyContentProvider : ContentProvider() {

    companion object {
        private const val USER = 1
        private const val TABLE_NAME = "favorite"

        private val  sUriMatcher = UriMatcher(UriMatcher.NO_MATCH).apply {
            addURI(AUTHORITY,
                    TABLE_NAME,
                    USER)
        }
    }

    override fun onCreate(): Boolean {
        return false
    }
    override fun insert(uri: Uri, values: ContentValues?): Uri? {
        throw UnsupportedOperationException()
    }

    private val userDaoFavorit: FavoriteDao by lazy {
        FavRoomDatabase.getDatabase(requireNotNull(context)).favDao()
    }

    override fun query(uri: Uri, projection: Array<String>?, selection: String?,
                       selectionArgs: Array<String>?, sortOrder: String?
    ): Cursor? {
        return when (sUriMatcher.match(uri)) {
            USER -> userDaoFavorit.cursorGetAll()
            else -> null
        }
    }

    override fun update(uri: Uri, values: ContentValues?, selection: String?, selectionArgs: Array<out String>?): Int {
        throw UnsupportedOperationException()
    }

    override fun delete(uri: Uri, selection: String?, selectionArgs: Array<out String>?): Int {
        throw UnsupportedOperationException()
    }

    override fun getType(uri: Uri): String? {
        throw UnsupportedOperationException()
    }
}