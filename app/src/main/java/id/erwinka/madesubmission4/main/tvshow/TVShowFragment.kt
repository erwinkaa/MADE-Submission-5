package id.erwinka.madesubmission4.main.tvshow

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.*
import android.view.*
import android.widget.ProgressBar

import id.erwinka.madesubmission4.R
import id.erwinka.madesubmission4.util.invisible
import id.erwinka.madesubmission4.util.visible
import id.erwinka.madesubmission4.api.ApiRepository
import id.erwinka.madesubmission4.main.MainActivity
import id.erwinka.madesubmission4.main.detail.DetailActivity
import org.jetbrains.anko.support.v4.startActivity

class TVShowFragment : Fragment(), TVShowView {

    private lateinit var presenter: TVShowPresenter
    private lateinit var adapterTVShows: TVShowAdapter
    private lateinit var recyclerView: RecyclerView
    private lateinit var progressBar: ProgressBar
    private var dataTVShows = mutableListOf<TVShowModel>()
    private lateinit var viewModel: TVShowViewModel
    private lateinit var toolbar: Toolbar

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_tvshow, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        toolbar = view.findViewById(R.id.toolbar)
        recyclerView = view.findViewById(R.id.recyclerview)
        progressBar = view.findViewById(R.id.progress_circular)
        super.onViewCreated(view, savedInstanceState)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {

        (activity as AppCompatActivity).setSupportActionBar(toolbar)
        toolbar.setTitleTextColor(resources.getColor(R.color.white))
        (activity as AppCompatActivity).supportActionBar?.setTitle(R.string.bottom_menu_tvshow)

        setHasOptionsMenu(true)

        viewModel = ViewModelProviders.of(this).get(TVShowViewModel::class.java)
        viewModel.getTVShows().observe(this, getTVShows)

        val service = ApiRepository.create()
        presenter = TVShowPresenter(this, service)

        recyclerView.addItemDecoration(DividerItemDecoration(recyclerView.context, DividerItemDecoration.VERTICAL))
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        adapterTVShows = TVShowAdapter(requireContext(), dataTVShows) {
            startActivity<DetailActivity>(MainActivity.DATA_EXTRA to it.id, MainActivity.TYPE to MainActivity.TV)
        }
        recyclerView.adapter = adapterTVShows
        if (savedInstanceState == null) {
            presenter.loadTVShow()
        } else {
            progressBar.invisible()
        }

        super.onActivityCreated(savedInstanceState)
    }

    override fun onSaveInstanceState(outState: Bundle) {
        outState.putString(MainActivity.INSTANCE, TVShowFragment::class.java.simpleName)
        super.onSaveInstanceState(outState)
    }

    override fun showLoading() {
        recyclerView.invisible()
        progressBar.visible()
    }

    override fun hideLoading() {
        recyclerView.visible()
        progressBar.invisible()
    }

    override fun processTVShowData(data: TVShowResponseModel) {
        viewModel.setTVShows(data)
    }

    private val getTVShows = Observer<TVShowResponseModel> {
        if (it != null) {
            adapterTVShows.setData(it.results)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        val menuItem = menu.findItem(R.id.m_search)
        if (menuItem != null) {
            val searchView: SearchView = menuItem.actionView as SearchView
            searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
                override fun onQueryTextSubmit(query: String): Boolean {
                    presenter.searchTVShow(query)
                    return false
                }

                override fun onQueryTextChange(query: String): Boolean {
                    return false
                }
            })
        }
        super.onCreateOptionsMenu(menu, inflater)
    }
}
