package id.erwinka.madesubmission4.sqllite

import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.net.Uri
import id.erwinka.madesubmission4.main.MainActivity
import org.jetbrains.anko.db.*
import java.sql.SQLException

class MyDatabaseOpenHelper(ctx: Context, dbName: String = "favorites.db") :
    ManagedSQLiteOpenHelper(ctx, dbName, null, 1) {

    private lateinit var database: SQLiteDatabase

    companion object {

        const val AUTHORITY = "id.erwinka.madesubmission4"
        private const val SCHEME = "content"

        private var instance: MyDatabaseOpenHelper? = null

        @Synchronized
        fun getInstance(ctx: Context): MyDatabaseOpenHelper {
            if (instance == null) {
                instance = MyDatabaseOpenHelper(ctx.applicationContext)
            }
            return instance as MyDatabaseOpenHelper
        }

        val CONTENT_URI = Uri.Builder().scheme(SCHEME)
            .authority(AUTHORITY).appendPath(FavoriteDatabase.TABLE_FAVORITE).build()
    }

    @Throws(SQLException::class)
    fun open() {
        database = instance!!.writableDatabase
    }

    fun queryProviderMovies(): Cursor {
        return database.query(
                FavoriteDatabase.TABLE_FAVORITE,
                null, "${FavoriteDatabase.FILM_TYPE} = ?", arrayOf(MainActivity.MOVIE),
                null, null, null)
    }

    fun queryProviderTVShow(): Cursor {
        return database.query(
                FavoriteDatabase.TABLE_FAVORITE,
                null, "${FavoriteDatabase.FILM_TYPE} = ?", arrayOf(MainActivity.TV),
                null, null, null
            )
    }

    override fun onCreate(db: SQLiteDatabase) {
        db.createTable(
            FavoriteDatabase.TABLE_FAVORITE, true,
            FavoriteDatabase.ID to INTEGER + PRIMARY_KEY + AUTOINCREMENT,
            FavoriteDatabase.FILM_ID to TEXT,
            FavoriteDatabase.FILM_TYPE to TEXT,
            FavoriteDatabase.FILM_TITLE to TEXT,
            FavoriteDatabase.POSTER_PATH to TEXT,
            FavoriteDatabase.OVERVIEW to TEXT,
            FavoriteDatabase.RELEASE_DATE to TEXT
        )
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.dropTable(FavoriteDatabase.TABLE_FAVORITE, true)
    }
}

// Access property for Context
val Context.database: MyDatabaseOpenHelper
    get() = MyDatabaseOpenHelper.getInstance(applicationContext)