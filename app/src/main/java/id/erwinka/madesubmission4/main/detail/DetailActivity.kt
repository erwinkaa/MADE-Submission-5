package id.erwinka.madesubmission4.main.detail

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.view.Menu
import android.view.MenuItem
import com.bumptech.glide.Glide
import id.erwinka.madesubmission4.R
import id.erwinka.madesubmission4.api.ApiRepository
import id.erwinka.madesubmission4.main.MainActivity
import id.erwinka.madesubmission4.util.invisible
import id.erwinka.madesubmission4.util.visible
import kotlinx.android.synthetic.main.activity_detail.*
import org.jetbrains.anko.toast

class DetailActivity : AppCompatActivity(), DetailFilmView {

    private var menuItem: Menu? = null
    private lateinit var presenter: DetailFilmPresenter
    private var isFavorite: Boolean = false
    private var type: String = "type"
    private var id: String = "0"
    private var posterPath: String = "url"
    private var title: String = "title"

    companion object {
        const val INTENT_RESULT_CODE = 200
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        supportActionBar?.title = resources.getString(R.string.detailfilm)

        type = intent.getStringExtra(MainActivity.TYPE)
        id = intent.getStringExtra(MainActivity.DATA_EXTRA)

        val service = ApiRepository.create()
        presenter = DetailFilmPresenter(this, service)

        if (type == MainActivity.MOVIE) {
            presenter.loadMovieDetail(id)
        } else if (type == MainActivity.TV) {
            presenter.loadTVShowDetail(id)
        }

        isFavorite = presenter.favoriteState(this, id)
    }

    override fun showLoading() {
        progress_circular.visible()
        layout_detail.invisible()
    }

    override fun hideLoading() {
        progress_circular.invisible()
        layout_detail.visible()
    }

    override fun processMovieData(data: DetailMovieModel) {
        tv_film_title.text = data.title
        tv_film_releasedate.text = data.release_date
        tv_film_runningtime.text = data.runtime

        var companies = ""
        for (i in 0 until data.production_companies.size) {
            var comma = ", "
            if (i == data.production_companies.size - 1) {
                comma = ""
            }
            companies += (data.production_companies[i].name + comma)
        }

        tv_film_productioncompanies.text = companies
        tv_film_overview.text = data.overview
        Glide.with(this).load(ApiRepository.BASE_IMAGE_URL + data.poster_path).into(iv_film_posterdetail)

        title = data.title
        posterPath = data.poster_path
    }

    override fun processTVShowData(data: DetailTVShowModel) {
        tv_film_title.text = data.name
        tv_film_releasedate.text = data.first_air_date
        tv_film_runningtime.text = data.episode_run_time[0].toString()

        var companies = ""
        for (i in 0 until data.production_companies.size) {
            var comma = ", "
            if (i == data.production_companies.size - 1) {
                comma = ""
            }
            companies += (data.production_companies[i].name + comma)
        }

        tv_film_productioncompanies.text = companies
        tv_film_overview.text = data.overview
        Glide.with(this).load(ApiRepository.BASE_IMAGE_URL + data.poster_path).into(iv_film_posterdetail)

        title = data.name
        posterPath = data.poster_path
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.detail_menu, menu)
        menuItem = menu
        setFavorite()
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                onBackPressed()
                finish()
                true
            }

            R.id.add_to_favorite -> {
                if (isFavorite) {
                    presenter.removeFromFavorite(this, id)
                    isFavorite = !isFavorite
                    setFavorite()
                } else {
                    if (id != "0" && title != "title" && type != "type" && posterPath != "url") {
                        presenter.addToFavorite(this, id, type, title, posterPath)
                        isFavorite = !isFavorite
                        setFavorite()
                    } else {
                        toast(R.string.notavail)
                    }
                }
                true
            }

            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun setFavorite() {
        if (isFavorite)
            menuItem?.getItem(0)?.icon = ContextCompat.getDrawable(this, R.drawable.ic_favorite_white_24dp)
        else
            menuItem?.getItem(0)?.icon = ContextCompat.getDrawable(this, R.drawable.ic_favorite_border_white_24dp)
    }

    override fun onBackPressed() {
        setResult(INTENT_RESULT_CODE, Intent())
        super.onBackPressed()
    }
}
