package id.erwinka.madesubmission4.main.favorite.favoritemovie

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar

import id.erwinka.madesubmission4.R
import id.erwinka.madesubmission4.main.MainActivity
import id.erwinka.madesubmission4.main.detail.DetailActivity
import id.erwinka.madesubmission4.main.detail.DetailActivity.Companion.INTENT_RESULT_CODE
import id.erwinka.madesubmission4.main.favorite.FavoriteFragment.Companion.INTENT_REQUEST_CODE
import id.erwinka.madesubmission4.main.movie.*
import id.erwinka.madesubmission4.util.invisible
import id.erwinka.madesubmission4.util.visible
import kotlinx.android.synthetic.main.fragment_favorite_movie.*
import org.jetbrains.anko.support.v4.startActivityForResult

class FavoriteMovieFragment : Fragment(), FavoriteMovieView {

    private lateinit var presenter: FavoriteMoviePresenter
    private lateinit var adapterMovies: MovieAdapter
    private lateinit var recyclerView: RecyclerView
    private lateinit var progressBar: ProgressBar
    private var dataMovies = mutableListOf<MovieModel>()
    private lateinit var viewModel: FavoriteMovieViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_favorite_movie, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        recyclerView = view.findViewById(R.id.recyclerview)
        progressBar = view.findViewById(R.id.progress_circular)
        super.onViewCreated(view, savedInstanceState)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        viewModel = ViewModelProviders.of(this).get(FavoriteMovieViewModel::class.java)
        viewModel.getMovies().observe(this, getFavMovies)

        presenter = FavoriteMoviePresenter(this)

        recyclerView.addItemDecoration(DividerItemDecoration(recyclerView.context, DividerItemDecoration.VERTICAL))
        recyclerView.layoutManager = LinearLayoutManager(context)
        adapterMovies = MovieAdapter(requireContext(), dataMovies) {
            startActivityForResult<DetailActivity>(
                INTENT_REQUEST_CODE,
                MainActivity.DATA_EXTRA to it.id,
                MainActivity.TYPE to MainActivity.MOVIE
            )
        }
        recyclerView.adapter = adapterMovies

        if (savedInstanceState == null) {
            presenter.getFavoriteMovie(requireContext())
        } else {
            progressBar.invisible()
        }

        super.onActivityCreated(savedInstanceState)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == INTENT_REQUEST_CODE) {
            if (resultCode == INTENT_RESULT_CODE) {
                presenter.getFavoriteMovie(requireContext())
            }
        }
        super.onActivityResult(requestCode, resultCode, data)
    }

    override fun showLoading() {
        progressBar.visible()
    }

    override fun hideLoading() {
        progressBar.invisible()
    }

    override fun processFavMovieData(data: List<MovieModel>) {
        viewModel.setMovies(data)
        if (data.isEmpty()) {
            tv_data_not_exist.visible()
        }
    }

    private val getFavMovies = Observer<List<MovieModel>> {
        if (it != null) {
            adapterMovies.setData(it)
        }
    }
}
