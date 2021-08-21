package elieomatuku.cineast_android.ui.home

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import elieomatuku.cineast_android.R
import elieomatuku.cineast_android.ui.discover.DiscoverFragment
import elieomatuku.cineast_android.ui.search.SearchFragment
import elieomatuku.cineast_android.ui.settings.MyTMBDFragment

class HomeFragmentPagerAdapter(fm: FragmentManager) : FragmentPagerAdapter(fm) {

    companion object {
        val titleResList: List<Int> by lazy {
            listOf(
                R.string.nav_title_discover,
                R.string.nav_title_search,
                R.string.nav_title_my_tmdb
            )
        }
    }

    override fun getCount(): Int {
        return titleResList.size
    }

    override fun getItem(position: Int): Fragment {
        return when (titleResList[position]) {
            R.string.nav_title_discover -> DiscoverFragment.newInstance()
            R.string.nav_title_search -> SearchFragment.newInstance()
            R.string.nav_title_my_tmdb -> MyTMBDFragment.newInstance()
            else -> MyTMBDFragment.newInstance()
        }
    }
}