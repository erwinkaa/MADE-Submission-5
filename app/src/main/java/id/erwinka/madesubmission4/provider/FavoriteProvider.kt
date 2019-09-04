package id.erwinka.madesubmission4.provider

import android.content.ContentProvider
import android.content.ContentValues
import android.content.UriMatcher
import android.database.Cursor
import android.net.Uri
import id.erwinka.madesubmission4.sqllite.MyDatabaseOpenHelper.Companion.AUTHORITY
import id.erwinka.madesubmission4.sqllite.FavoriteDatabase.Companion.TABLE_FAVORITE
import id.erwinka.madesubmission4.sqllite.MyDatabaseOpenHelper
import id.erwinka.madesubmission4.sqllite.database

class FavoriteProvider : ContentProvider() {

    private lateinit var databaseOpenHelper: MyDatabaseOpenHelper
    private val movieId = 1
    private val tvShowId = 2

    private val sUriMatcher = UriMatcher(UriMatcher.NO_MATCH).apply {
        this.addURI(AUTHORITY, TABLE_FAVORITE, movieId)
        this.addURI(AUTHORITY, TABLE_FAVORITE, tvShowId)
    }

    override fun insert(uri: Uri, values: ContentValues?): Uri? {
        return null
    }

    override fun query(
        uri: Uri,
        projection: Array<String>?,
        selection: String?,
        selectionArgs: Array<String>?,
        sortOrder: String?
    ): Cursor? {
        databaseOpenHelper.open()
        return when (sUriMatcher.match(uri)) {
            movieId -> databaseOpenHelper.queryProviderMovies()
            tvShowId -> databaseOpenHelper.queryProviderTVShow()
            else -> databaseOpenHelper.queryProviderMovies()
        }
    }

    override fun onCreate(): Boolean {
        databaseOpenHelper = context!!.database
        return true
    }

    override fun update(uri: Uri, values: ContentValues?, selection: String?, selectionArgs: Array<String>?): Int {
        return 0
    }

    override fun delete(uri: Uri, selection: String?, selectionArgs: Array<String>?): Int {
        return 0

    }

    override fun getType(uri: Uri): String? {
        return null
    }

}