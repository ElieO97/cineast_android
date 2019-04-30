package elieomatuku.restapipractice.vu

import android.app.Activity
import android.support.design.widget.BottomNavigationView
import android.support.v4.view.ViewPager
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import android.view.LayoutInflater
import android.view.ViewGroup
import elieomatuku.restapipractice.R
import elieomatuku.restapipractice.adapter.HomeFragmentPagerAdapter
import elieomatuku.restapipractice.utils.UiUtils
import io.chthonic.mythos.mvp.FragmentWrapper
import kotlinx.android.synthetic.main.vu_main.view.*

class MainVu (inflater: LayoutInflater,
              activity: Activity,
              fragmentWrapper: FragmentWrapper?,
              parentView: ViewGroup?): ToolbarVu (inflater,
        activity,
        fragmentWrapper,
        parentView){

        private val adapter by lazy {
          HomeFragmentPagerAdapter(baseActivity.supportFragmentManager)
        }

        private val pager by lazy {
            rootView.home_pager
        }

        private val navIds: List<Int> by lazy {
            listOf(R.id.action_discover, R.id.action_search, R.id.action_my_TMDb)
        }
        val bottomNav: BottomNavigationView by lazy {
            rootView.bottom_navig
        }

       override val toolbar: Toolbar?
          get() = rootView.toolbar

        override fun getRootViewLayoutId(): Int {
            return R.layout.vu_main
        }


        override fun onCreate() {
            super.onCreate()
            pager.adapter = adapter


            toolbar?.let {
                UiUtils.initToolbar(activity as AppCompatActivity, it, false)
            }

            bottomNav.setOnNavigationItemSelectedListener {
                toolbar?.title = it.title
                pager.setCurrentItem(navIds.indexOf(it.itemId), true)
                true
            }

            pager.addOnPageChangeListener(object: ViewPager.OnPageChangeListener{
                override fun onPageScrollStateChanged(state: Int) {

                }

                override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {

                }

                override fun onPageSelected(position: Int) {
                    toolbar?.title = bottomNav.menu.getItem(position).title
                    bottomNav.menu.getItem(position).isChecked = true
                }
            })
        }

    override fun onDestroy() {
        super.onDestroy()
        pager.adapter = null
    }
}
