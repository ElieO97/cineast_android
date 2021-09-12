package elieomatuku.cineast_android.ui.discover

import android.graphics.drawable.Drawable
import android.net.Uri
import android.os.Bundle
import android.os.Parcelable
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.res.ResourcesCompat
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import elieomatuku.cineast_android.R
import elieomatuku.cineast_android.domain.model.*
import elieomatuku.cineast_android.extensions.getFilteredWidgets
import elieomatuku.cineast_android.ui.base.BaseFragment
import elieomatuku.cineast_android.ui.fragment.WebviewFragment
import elieomatuku.cineast_android.ui.settings.LoginWebviewFragment
import elieomatuku.cineast_android.utils.Constants
import elieomatuku.cineast_android.utils.consume
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.subjects.PublishSubject
import kotlinx.android.synthetic.main.fragment_discover.*
import timber.log.Timber
import java.util.ArrayList


class DiscoverFragment : BaseFragment(R.layout.fragment_discover) {
    companion object {
        fun newInstance(): DiscoverFragment {
            return DiscoverFragment()
        }
    }

    private val viewModel: DiscoverViewModel by viewModel<DiscoverViewModel>()

    private val movieSelectPublisher: PublishSubject<Movie> by lazy {
        PublishSubject.create<Movie>()
    }
    val movieSelectObservable: Observable<Movie>
        get() = movieSelectPublisher.hide()

    private val personSelectPublisher: PublishSubject<Content> by lazy {
        PublishSubject.create<Content>()
    }

    val personSelectObservable: Observable<Content>
        get() = personSelectPublisher.hide()

    private val loginClickPublisher: PublishSubject<Boolean> by lazy {
        PublishSubject.create<Boolean>()
    }

    val loginClickObservable: Observable<Boolean>
        get() = loginClickPublisher.hide()

    val adapter: DiscoverAdapter by lazy {
        DiscoverAdapter(movieSelectPublisher, personSelectPublisher, loginClickPublisher)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        recyclerview.adapter = adapter
        val itemDecoration =
            DividerItemDecoration(recyclerview.context, DividerItemDecoration.VERTICAL)
        val drawable: Drawable? = ResourcesCompat.getDrawable(
            resources,
            R.drawable.item_decoration,
            activity?.theme
        )
        if (drawable != null) {
            itemDecoration.setDrawable(drawable)
        }
        recyclerview.addItemDecoration(itemDecoration)
        recyclerview.layoutManager = LinearLayoutManager(activity)


        viewModel.viewState.observe(viewLifecycleOwner) { state ->
            if (state.isLoading) {
                showLoading(requireView())
            } else {
                dismissRefreshLayout()
                hideLoading(requireView())
            }

            state.viewError.consume {
                updateErrorView(it.message)
            }
            updateLoginState(state.isLoggedIn)
            updateView(state.discoverContents, state.isLoggedIn)
        }

        refreshLayout.setOnRefreshListener {
            viewModel.getDiscoverContent()
        }
    }

    private fun updateView(
        discoverContents: DiscoverContents?,
        isLoggedIn: Boolean
    ) {
        discoverContents?.let {
            adapter.filteredContents = discoverContents.getFilteredWidgets()
            adapter.isLoggedIn = isLoggedIn
            adapter.notifyDataSetChanged()
            recyclerview.visibility = View.VISIBLE
            dismissRefreshLayout()
        }
    }

    private fun updateErrorView(errorMsg: String?) {
        adapter.errorMessage = errorMsg
        adapter.notifyDataSetChanged()
        recyclerview.visibility = View.VISIBLE
        dismissRefreshLayout()
    }

    fun gotoWebView(accessToken: AccessToken?) {
        accessToken?.let {
            val authenticateUrl = Uri.parse(resources.getString(R.string.authenticate_url))
                .buildUpon()
                .appendPath(it.requestToken)
                .build()
                .toString()

            val webViewFragment: WebviewFragment =
                LoginWebviewFragment.newInstance(authenticateUrl)
            val fm = (activity as AppCompatActivity).supportFragmentManager

            fm.beginTransaction().add(android.R.id.content, webViewFragment, null)
                .addToBackStack(null).commit()
        }
    }

    private fun updateLoginState(isLoggedIn: Boolean) {
        adapter.isLoggedIn = isLoggedIn
        adapter.notifyDataSetChanged()
    }

    private fun dismissRefreshLayout() {
        refreshLayout.isRefreshing = false
    }
}
