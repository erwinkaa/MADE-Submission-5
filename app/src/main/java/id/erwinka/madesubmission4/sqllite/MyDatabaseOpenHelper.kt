package id.erwinka.madesubmission4.sqllite

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import org.jetbrains.anko.db.*

class MyDatabaseOpenHelper(ctx: Context, dbName: String = "favorites.db") :
    ManagedSQLiteOpenHelper(ctx, dbName, null, 1) {


    companion object {
        private var instance: MyDatabaseOpenHelper? = null

        @Synchronized
        fun getInstance(ctx: Context): MyDatabaseOpenHelper {
            if (instance == null) {
                instance = MyDatabaseOpenHelper(ctx.applicationContext)
            }
            return instance as MyDatabaseOpenHelper
        }
    }

    override fun onCreate(db: SQLiteDatabase) {
        db.createTable(
            FavoriteDatabase.TABLE_FAVORITE, true,
            FavoriteDatabase.ID to INTEGER + PRIMARY_KEY + AUTOINCREMENT,
            FavoriteDatabase.FILM_ID to TEXT,
            FavoriteDatabase.FILM_TYPE to TEXT,
            FavoriteDatabase.FILM_TITLE to TEXT,
            FavoriteDatabase.POSTER_PATH to TEXT
        )
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        // Here you can upgrade tables, as usual
        db.dropTable(FavoriteDatabase.TABLE_FAVORITE, true)
    }
}

// Access property for Context
val Context.database: MyDatabaseOpenHelper
    get() = MyDatabaseOpenHelper.getInstance(applicationContext)