package id.erwinka.madesubmission4.util

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter

class ViewPagerAdapter(fm: FragmentManager?) : FragmentPagerAdapter(fm) {

    private val fragmentList = mutableListOf<Fragment>()

    override fun getItem(position: Int): Fragment {
        return fragmentList[position]
    }

    override fun getCount(): Int {
        return fragmentList.size
    }

    fun addFragment(f:Fragment) {
        fragmentList.add(f)
    }

}