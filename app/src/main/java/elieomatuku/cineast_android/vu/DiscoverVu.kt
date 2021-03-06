package elieomatuku.cineast_android.vu

import android.app.Activity
import android.graphics.drawable.Drawable
import android.net.Uri
import androidx.core.content.res.ResourcesCompat
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import elieomatuku.cineast_android.core.DiscoverContent
import elieomatuku.cineast_android.R
import elieomatuku.cineast_android.adapter.DiscoverAdapter
import elieomatuku.cineast_android.core.model.*
import elieomatuku.cineast_android.fragment.LoginWebviewFragment
import elieomatuku.cineast_android.fragment.WebviewFragment
import elieomatuku.cineast_android.utils.WebLink
import io.chthonic.mythos.mvp.FragmentWrapper
import io.reactivex.Observable
import io.reactivex.subjects.PublishSubject
import kotlinx.android.synthetic.main.vu_discover.view.*
import timber.log.Timber

class DiscoverVu(inflater: LayoutInflater,
                 activity: Activity,
                 fragmentWrapper: FragmentWrapper?,
                 parentView: ViewGroup?) : BaseVu(inflater,
        activity = activity,
        fragmentWrapper = fragmentWrapper,
        parentView = parentView), WebLink<AccessToken?> {


    private val movieSelectPublisher: PublishSubject<Movie> by lazy {
        PublishSubject.create<Movie>()
    }
    val movieSelectObservable: Observable<Movie>
        get() = movieSelectPublisher.hide()


    private val personSelectPublisher: PublishSubject<Person> by lazy {
        PublishSubject.create<Person>()
    }

    val personSelectObservable: Observable<Person>
        get() = personSelectPublisher.hide()


    private val loginClickPublisher: PublishSubject<Boolean> by lazy {
        PublishSubject.create<Boolean>()
    }

    val loginClickObservable: Observable<Boolean>
        get() = loginClickPublisher.hide()

    private val listView: RecyclerView by lazy {
        rootView.recyclerview
    }

    private val refreshLayout: SwipeRefreshLayout by lazy {
        rootView.refresh_layout
    }

    private val refreshPublisher: PublishSubject<Boolean> by lazy {
        PublishSubject.create<Boolean>()
    }

    val refreshObservable: Observable<Boolean>
        get() = refreshPublisher.hide()

    private val sessionPublisher: PublishSubject<Pair<String, Account>> by lazy {
        (activity as elieomatuku.cineast_android.activity.MainActivity).sessionPublisher
    }

    val sessionObservable: Observable<Pair<String, Account>> by lazy {
        sessionPublisher.hide()
    }

    val adapter: DiscoverAdapter by lazy {
        DiscoverAdapter(movieSelectPublisher, personSelectPublisher, loginClickPublisher)
    }

    override fun getRootViewLayoutId(): Int {
        return R.layout.vu_discover
    }

    override fun onCreate() {
        super.onCreate()

        listView.adapter = adapter
        val itemDecoration = DividerItemDecoration(listView.context, DividerItemDecoration.VERTICAL)
        val drawable: Drawable? = ResourcesCompat.getDrawable(activity.resources, R.drawable.item_decoration, activity.theme)
        if (drawable != null) {
            itemDecoration.setDrawable(drawable)
        }
        listView.addItemDecoration(itemDecoration)
        listView.layoutManager = LinearLayoutManager(activity)


        refreshLayout.setOnRefreshListener {
            refreshPublisher.onNext(true)
        }
    }

    fun updateView(discoverContent: elieomatuku.cineast_android.core.DiscoverContent, isLoggedIn: Boolean) {
        Timber.d("update View is called")
        adapter.filteredContent = discoverContent.getFilteredWidgets()
        adapter.isLoggedIn = isLoggedIn
        adapter.notifyDataSetChanged()
        listView.visibility = View.VISIBLE
        dismissRefreshLayout()
    }


    fun updateErrorView(errorMsg: String?) {
        adapter.errorMessage = errorMsg
        adapter.notifyDataSetChanged()
        listView.visibility = View.VISIBLE
        dismissRefreshLayout()
    }

    override fun gotoWebview(value: AccessToken?) {
        value?.let {
            val authenticateUrl = Uri.parse(activity.getString(R.string.authenticate_url))
                    .buildUpon()
                    .appendPath(it.request_token)
                    .build()
                    .toString()

            val webviewFragment: WebviewFragment? = LoginWebviewFragment.newInstance(authenticateUrl)
            val fm = (activity as AppCompatActivity).supportFragmentManager

            if (webviewFragment != null && fm != null) {
                fm.beginTransaction().add(android.R.id.content, webviewFragment, null).addToBackStack(null).commit()
            }
        }
    }

    fun updateLoginState(isLoggedIn: Boolean) {
        adapter.isLoggedIn = isLoggedIn
        adapter.notifyDataSetChanged()
    }

    fun dismissRefreshLayout() {
        refreshLayout.setRefreshing(false)
    }
}