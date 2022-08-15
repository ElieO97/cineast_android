package elieomatuku.cineast_android.discover

import android.graphics.drawable.Drawable
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.res.ResourcesCompat
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.NavigationUI.setupWithNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import elieomatuku.cineast_android.R
import elieomatuku.cineast_android.domain.model.*
import elieomatuku.cineast_android.extensions.getFilteredWidgets
import elieomatuku.cineast_android.base.BaseFragment
import elieomatuku.cineast_android.contents.ContentsActivity
import elieomatuku.cineast_android.databinding.FragmentDiscoverBinding
import elieomatuku.cineast_android.fragment.WebViewFragment
import elieomatuku.cineast_android.settings.LoginWebViewFragment
import io.reactivex.Observable
import io.reactivex.subjects.PublishSubject
import kotlinx.android.synthetic.main.fragment_discover.*
import elieomatuku.cineast_android.utils.*

class DiscoverFragment : BaseFragment() {
    companion object {
        fun newInstance(): DiscoverFragment {
            return DiscoverFragment()
        }
    }

    private val viewModel: DiscoverViewModel by viewModel()

    private val movieSelectPublisher: PublishSubject<Movie> by lazy {
        PublishSubject.create()
    }
    private val movieSelectObservable: Observable<Movie>
        get() = movieSelectPublisher.hide()

    private val personSelectPublisher: PublishSubject<Content> by lazy {
        PublishSubject.create()
    }

    private val personSelectObservable: Observable<Content>
        get() = personSelectPublisher.hide()

    private val loginClickPublisher: PublishSubject<Boolean> by lazy {
        PublishSubject.create()
    }

    private val loginClickObservable: Observable<Boolean>
        get() = loginClickPublisher.hide()

    private val onSeeAllClickPublisher: PublishSubject<Pair<List<Content>, Int>> by lazy {
        PublishSubject.create()
    }

    private val onSeeAllClickObservable: Observable<Pair<List<Content>, Int>>
        get() = onSeeAllClickPublisher.hide()

    val adapter: DiscoverAdapter by lazy {
        DiscoverAdapter(
            movieSelectPublisher,
            personSelectPublisher,
            loginClickPublisher,
            onSeeAllClickPublisher
        )
    }

    private lateinit var binding: FragmentDiscoverBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentDiscoverBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupWithNavController(
            binding.toolbar,
            findNavController(),
            AppBarConfiguration(findNavController().graph)
        )
        binding.toolbar.title = this.getString(R.string.nav_title_discover)

        binding.recyclerview.adapter = adapter
        val itemDecoration =
            DividerItemDecoration(context, DividerItemDecoration.VERTICAL)
        val drawable: Drawable? = ResourcesCompat.getDrawable(
            resources,
            R.drawable.item_decoration,
            activity?.theme
        )
        if (drawable != null) {
            itemDecoration.setDrawable(drawable)
        }
        binding.recyclerview.addItemDecoration(itemDecoration)
        binding.recyclerview.layoutManager = LinearLayoutManager(activity)

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
            state.accessToken.consume {
                gotoWebView(it)
            }
        }

        binding.refreshLayout.setOnRefreshListener {
            viewModel.getDiscoverContent()
        }
    }

    override fun onResume() {
        super.onResume()
        viewModel.getIsLoggedIn()

        rxSubs.add(
            loginClickObservable
                .subscribe {
                    if (!viewModel.isLoggedIn()) {
                        viewModel.logIn()
                    } else {
                        viewModel.logout()
                    }
                }
        )

        rxSubs.add(
            personSelectObservable
                .subscribe({ actor: Content ->
                    if (actor is Person) {
                        gotoPerson(actor)
                    }
                }, {

                })
        )

        rxSubs.add(
            movieSelectObservable
                .subscribe({ movie: Movie ->
                    gotoMovie(movie)
                },
                    {}
                )
        )

        rxSubs.add(
            onSeeAllClickObservable
                .subscribe({ pair: Pair<List<Content>, Int> ->
                    ContentsActivity.startActivity(requireContext(), pair.first, pair.second)
                }, {

                })
        )
    }

    private fun updateView(
        discoverContents: DiscoverContents?,
        isLoggedIn: Boolean
    ) {
        discoverContents?.let {
            adapter.filteredContents =
                discoverContents.getFilteredWidgets()
            adapter.isLoggedIn = isLoggedIn
            adapter.notifyDataSetChanged()
            binding.recyclerview.visibility = View.VISIBLE
            dismissRefreshLayout()
        }
    }

    private fun updateErrorView(errorMsg: String?) {
        adapter.errorMessage = errorMsg
        adapter.notifyDataSetChanged()
        binding.recyclerview.visibility = View.VISIBLE
        dismissRefreshLayout()
    }

    private fun gotoWebView(accessToken: AccessToken?) {
        accessToken?.let {
            val authenticateUrl = Uri.parse(resources.getString(R.string.authenticate_url))
                .buildUpon()
                .appendPath(it.requestToken)
                .build()
                .toString()

            val webViewFragment: WebViewFragment =
                LoginWebViewFragment.newInstance(authenticateUrl)
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

    private fun gotoMovie(movie: Movie) {
        val directions = DiscoverFragmentDirections.navigateToMovieDetail(
            getString(R.string.nav_title_discover),
            movie
        )
        findNavController().navigate(directions)
    }

    private fun gotoPerson(person: Person) {
        val directions = DiscoverFragmentDirections.navigateToPersonDetail(
            getString(R.string.nav_title_discover),
            person
        )
        findNavController().navigate(directions)
    }
}
