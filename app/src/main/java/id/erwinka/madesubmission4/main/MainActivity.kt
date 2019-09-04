package id.erwinka.madesubmission4.main

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import id.erwinka.madesubmission4.R
import id.erwinka.madesubmission4.main.favorite.FavoriteFragment
import id.erwinka.madesubmission4.main.movie.MovieFragment
import id.erwinka.madesubmission4.main.tvshow.TVShowFragment
import id.erwinka.madesubmission4.setting.SettingActivity
import kotlinx.android.synthetic.main.activity_main.*
import org.jetbrains.anko.startActivity


class MainActivity : AppCompatActivity() {

    companion object {
        const val TYPE = "type"
        const val DATA_EXTRA = "data"
        const val MOVIE = "movie"
        const val TV = "tv"
        const val INSTANCE = "instance"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        bottom_navigation.setOnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.movie -> {
                    loadMovieFragment()
                }
                R.id.tvshow -> {
                    loadTVShowFragment()
                }
                R.id.favorite -> {
                    loadFavoriteFragment()
                }
            }
            true
        }

        if (savedInstanceState == null) {
            bottom_navigation.selectedItemId = R.id.movie
        } else {
            when (savedInstanceState.getString(INSTANCE)) {
                MovieFragment::class.java.simpleName -> {
                    bottom_navigation.selectedItemId = R.id.movie
                }
                TVShowFragment::class.java.simpleName -> {
                    bottom_navigation.selectedItemId = R.id.tvshow
                }
                FavoriteFragment::class.java.simpleName -> {
                    bottom_navigation.selectedItemId = R.id.favorite
                }
            }
        }
    }

    private fun loadMovieFragment() {
        supportFragmentManager
            .beginTransaction()
            .replace(R.id.mainFrame, MovieFragment(), MovieFragment::class.java.simpleName)
            .commit()
    }

    private fun loadTVShowFragment() {
        supportFragmentManager
            .beginTransaction()
            .replace(R.id.mainFrame, TVShowFragment(), TVShowFragment::class.java.simpleName)
            .commit()
    }

    private fun loadFavoriteFragment() {
        supportFragmentManager
            .beginTransaction()
            .replace(R.id.mainFrame, FavoriteFragment(), FavoriteFragment::class.java.simpleName)
            .commit()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.m_setting -> {
                startActivity<SettingActivity>()
                true
            }

            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onBackPressed() {
        moveTaskToBack(true)
        super.onBackPressed()
    }
}
