package id.erwinka.madesubmission4.main.favorite.favoritetvshow

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.widget.SwipeRefreshLayout
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
import id.erwinka.madesubmission4.main.tvshow.TVShowAdapter
import id.erwinka.madesubmission4.main.tvshow.TVShowModel
import id.erwinka.madesubmission4.util.invisible
import id.erwinka.madesubmission4.util.visible
import kotlinx.android.synthetic.main.fragment_favorite_movie.*
import org.jetbrains.anko.support.v4.startActivity

class FavoriteTVShowFragment : Fragment(), FavoriteTVShowView {

    private lateinit var presenter: FavoriteTVShowPresenter
    private lateinit var adapterMovies: TVShowAdapter
    private lateinit var recyclerView: RecyclerView
    private lateinit var progressBar: ProgressBar
    private var dataTVShows = mutableListOf<TVShowModel>()
    private lateinit var viewModel: FavoriteTVShowViewModel
    private lateinit var swipeRefreshLayout: SwipeRefreshLayout

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_favorite_tvshow, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        recyclerView = view.findViewById(R.id.recyclerview)
        progressBar = view.findViewById(R.id.progress_circular)
        swipeRefreshLayout = view.findViewById(R.id.swipeRefreshLayout)
        super.onViewCreated(view, savedInstanceState)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        viewModel = ViewModelProviders.of(this).get(FavoriteTVShowViewModel::class.java)
        viewModel.getMovies().observe(this, getFavTVShows)

        presenter = FavoriteTVShowPresenter(this)

        recyclerView.addItemDecoration(DividerItemDecoration(recyclerView.context, DividerItemDecoration.VERTICAL))
        recyclerView.layoutManager = LinearLayoutManager(context)
        adapterMovies = TVShowAdapter(requireContext(), dataTVShows) {
            startActivity<DetailActivity>(MainActivity.DATA_EXTRA to it.id, MainActivity.TYPE to MainActivity.TV)
        }
        recyclerView.adapter = adapterMovies

        if (savedInstanceState == null) {
            presenter.getFavoriteTVShow(requireContext())
        } else {
            progressBar.invisible()
        }

        swipeRefreshLayout.setOnRefreshListener {
            presenter.getFavoriteTVShow(requireContext())
        }

        super.onActivityCreated(savedInstanceState)
    }

    override fun showLoading() {
        progressBar.visible()
    }

    override fun hideLoading() {
        progressBar.invisible()
        swipeRefreshLayout.isRefreshing = false
    }

    override fun processFavTVShowData(data: List<TVShowModel>) {
        viewModel.setMovies(data)
        if (data.isEmpty()) {
            tv_data_not_exist.visible()
        }
    }

    private val getFavTVShows = Observer<List<TVShowModel>> {
        if (it != null) {
            adapterMovies.setData(it)
        }
    }
}
