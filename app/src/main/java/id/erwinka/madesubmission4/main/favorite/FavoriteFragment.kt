package id.erwinka.madesubmission4.main.favorite


import android.os.Bundle
import android.support.design.widget.TabLayout
import android.support.v4.app.Fragment
import android.support.v4.view.ViewPager
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import android.view.*

import id.erwinka.madesubmission4.R
import id.erwinka.madesubmission4.main.MainActivity.Companion.INSTANCE
import id.erwinka.madesubmission4.main.favorite.favoritemovie.FavoriteMovieFragment
import id.erwinka.madesubmission4.main.favorite.favoritetvshow.FavoriteTVShowFragment
import id.erwinka.madesubmission4.util.ViewPagerAdapter

class FavoriteFragment : Fragment() {

    private lateinit var adapter: ViewPagerAdapter
    private lateinit var viewPager: ViewPager
    private lateinit var tabLayout: TabLayout
    private lateinit var toolbar: Toolbar

    companion object {
        const val INTENT_REQUEST_CODE = 101
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_favorite, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        toolbar = view.findViewById(R.id.toolbar)
        viewPager = view.findViewById(R.id.vp_favorites)
        tabLayout = view.findViewById(R.id.tablayout)
        super.onViewCreated(view, savedInstanceState)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {

        (activity as AppCompatActivity).setSupportActionBar(toolbar)
        toolbar.setTitleTextColor(resources.getColor(R.color.white))
        (activity as AppCompatActivity).supportActionBar?.setTitle(R.string.bottom_menu_favorite)

        adapter = ViewPagerAdapter(childFragmentManager)
        adapter.addFragment(FavoriteMovieFragment())
        adapter.addFragment(FavoriteTVShowFragment())
        viewPager.adapter = adapter

        viewPager.addOnPageChangeListener(TabLayout.TabLayoutOnPageChangeListener(tabLayout))
        tabLayout.addOnTabSelectedListener(tabLayoutListener)

        super.onActivityCreated(savedInstanceState)
    }

    override fun onSaveInstanceState(outState: Bundle) {
        outState.putString(INSTANCE, FavoriteFragment::class.java.simpleName)
        super.onSaveInstanceState(outState)
    }

    private val tabLayoutListener = object : TabLayout.OnTabSelectedListener {
        override fun onTabSelected(tab: TabLayout.Tab) {
            viewPager.currentItem = tab.position
        }

        override fun onTabUnselected(tab: TabLayout.Tab) {

        }

        override fun onTabReselected(tab: TabLayout.Tab) {

        }

    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        // kok ga ngilang
        val menuItem = menu.findItem(R.id.m_search)
        menuItem.isVisible = false
        menuItem.isEnabled = false
        super.onCreateOptionsMenu(menu, inflater)
    }
}
