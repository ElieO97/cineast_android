package com.cineast_android.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.cineast_android.fragment.DiscoverFragment
import com.cineast_android.fragment.MyTMBDFragment
import com.cineast_android.fragment.SearchFragment
import com.cineast_android.R

class HomeFragmentPagerAdapter(fm: FragmentManager): FragmentPagerAdapter(fm) {

    companion object {
        val titleResList: List<Int> by lazy {
            listOf(R.string.nav_title_discover,
                    R.string.nav_title_search,
                    R.string.nav_title_my_tmdb)
        }
    }

    override fun getCount(): Int {
       return titleResList.size
    }

    override fun getItem(position: Int): Fragment {
        return when (titleResList[position]){
            R.string.nav_title_discover ->  DiscoverFragment.newInstance()
            R.string.nav_title_search -> SearchFragment.newInstance()
            R.string.nav_title_my_tmdb -> MyTMBDFragment.newInstance()
            else -> MyTMBDFragment.newInstance()
        }
    }
}