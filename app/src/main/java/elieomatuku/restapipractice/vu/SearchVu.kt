package elieomatuku.restapipractice.vu

import android.app.Activity
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import android.view.LayoutInflater
import android.view.ViewGroup
import elieomatuku.restapipractice.adapter.SearchFragmentPagerAdapter
import elieomatuku.restapipractice.utils.UiUtils
import kotlinx.android.synthetic.main.vu_main.view.*
import kotlinx.android.synthetic.main.vu_search.view.*
import android.support.design.widget.TabLayout
import elieomatuku.restapipractice.R
import elieomatuku.restapipractice.business.business.model.data.Widget
import io.chthonic.mythos.mvp.FragmentWrapper
import io.reactivex.Observable
import io.reactivex.subjects.PublishSubject
import timber.log.Timber


class SearchVu(inflater: LayoutInflater,
               activity: Activity,
               fragmentWrapper: FragmentWrapper?,
               parentView: ViewGroup?) : ToolbarVu(inflater,
        activity = activity,
        fragmentWrapper = fragmentWrapper,
        parentView = parentView) {

    override val toolbar: Toolbar?
        get() = rootView.toolbar

    override fun getRootViewLayoutId(): Int {
        return R.layout.vu_search
    }

    val searchQueryPublisher: PublishSubject<String> by lazy {
        PublishSubject.create<String>()
    }

    val searchQueryObservable: Observable<String>
        get() = searchQueryPublisher.hide()

    private var searchAdapter: SearchFragmentPagerAdapter ? = null

    val tabLayout by lazy {
        rootView.sliding_tabs
    }

   private val searchPager by lazy {
        val pager  = rootView.search_viewpager

       pager
   }

    val tabLayoutOnPageChangeListener by lazy {
        TabLayout.TabLayoutOnPageChangeListener(tabLayout)
    }

    var isMovieSearchScreen: Boolean = true

    override fun onCreate() {
        Timber.d("SearchVu created")

        searchAdapter = SearchFragmentPagerAdapter(fragmentWrapper?.support?.childFragmentManager)

        if (searchAdapter != null) {
            searchPager.adapter = searchAdapter
            searchPager.addOnPageChangeListener(tabLayoutOnPageChangeListener)
        }

        if (toolbar != null) {
            UiUtils.initToolbar(activity as AppCompatActivity, toolbar)
        }

        tabLayout.addOnTabSelectedListener( object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {
                if (tab != null) {
                    if (tab.position == 0) {
                        isMovieSearchScreen = true
                    } else {
                        isMovieSearchScreen = false
                    }
                    searchPager.setCurrentItem(tab.position, true)
                }
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {
                Timber.d("Tab : ${tab?.position} unselected")
            }

            override fun onTabReselected(tab: TabLayout.Tab?) {
                Timber.d("Tab : ${tab?.position} reselected")
            }
        })

    }

    fun openItemListActivity(results: List<Widget>?) {
        if (results != null) {
            UiUtils.startItemListActivity(activity, results, R.string.search_hint)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        searchPager.removeOnPageChangeListener(tabLayoutOnPageChangeListener)

    }
}


