package elieomatuku.cineast_android.details.person

import android.content.Intent
import android.os.Bundle
import android.view.*
import androidx.core.content.res.ResourcesCompat
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import androidx.recyclerview.widget.LinearLayoutManager
import elieomatuku.cineast_android.R
import elieomatuku.cineast_android.domain.model.Image
import elieomatuku.cineast_android.domain.model.Movie
import elieomatuku.cineast_android.domain.model.PersonDetails
import elieomatuku.cineast_android.base.BaseFragment
import elieomatuku.cineast_android.databinding.FragmentContentDetailsBinding
import elieomatuku.cineast_android.details.BareOverviewFragment
import elieomatuku.cineast_android.details.MoviesFragment
import elieomatuku.cineast_android.details.gallery.GalleryFragment
import elieomatuku.cineast_android.utils.ContentUtils
import elieomatuku.cineast_android.utils.DividerItemDecorator
import elieomatuku.cineast_android.utils.UiUtils
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.subjects.PublishSubject
import java.io.Serializable
import io.reactivex.Observable

class PersonFragment : BaseFragment() {
    companion object {
        const val OVERVIEW = "overview"
        const val KNOWN_FOR = "known_for"
    }

    private val viewModel: PersonViewModel by viewModel()
    private lateinit var binding: FragmentContentDetailsBinding
    private val args: PersonFragmentArgs by navArgs()

    private val onProfileClickedPicturePublisher: PublishSubject<Int> by lazy {
        PublishSubject.create()
    }

    private val onProfileClickedPictureObservable: Observable<Int>
        get() = onProfileClickedPicturePublisher.hide()


    private val onSegmentedButtonsPublisher: PublishSubject<Pair<String, PersonDetails>> by lazy {
        PublishSubject.create()
    }

    private val onSegmentedButtonsObservable: Observable<Pair<String, PersonDetails>>
        get() = onSegmentedButtonsPublisher.hide()

    val adapter: PersonAdapter by lazy {
        PersonAdapter(onProfileClickedPicturePublisher, onSegmentedButtonsPublisher)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentContentDetailsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val navController = findNavController()
        val appBarConfiguration = AppBarConfiguration(navController.graph)
        binding.toolbar.setupWithNavController(navController, appBarConfiguration)
        binding.toolbar.inflateMenu(R.menu.item_menu)
        binding.toolbar.setOnMenuItemClickListener {
            when (it.itemId) {
                R.id.action_share -> {
                    onShareMenuClicked()
                    true
                }
                else -> false
            }
        }

        binding.listViewContainer.adapter = adapter
        binding.listViewContainer.layoutManager = LinearLayoutManager(requireContext())

        val screenName = args.screenName
        binding.toolbar.title = screenName
        val person = args.person
        viewModel.getPersonDetails(person)
        viewModel.getKnownForMovies(person)
        viewModel.getImages(person)


        viewModel.viewState.observe(viewLifecycleOwner) {
            if (it.isLoading) {
                showLoading(view)
            } else {
                hideLoading(view)
            }

            updateActionShare()
            updateView(it.person, it.knownFor)

            it.viewError?.consume()?.apply {
                updateErrorView(this.message)
            }
        }
    }

    override fun onResume() {
        super.onResume()
        rxSubs.add(
            onSegmentedButtonsObservable
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe {
                    gotoTab(it)
                }
        )

        rxSubs.add(
            onProfileClickedPictureObservable
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe {
                    goToGallery(viewModel.posters)
                }
        )
    }

    private fun updateActionShare() {
        binding.toolbar.menu?.findItem(R.id.action_share)?.apply {
            isVisible = ContentUtils.supportsShare(viewModel.person?.id)
            UiUtils.tintMenuItem(this, requireContext(), R.color.color_orange_app)
        }
    }

    private fun onShareMenuClicked() {
        val path = "person"
        val shareIntent: Intent? =
            UiUtils.getShareIntent(viewModel.person?.name, viewModel.person?.id, path)

        // Make sure there is an activity that supports the intent
        if (shareIntent?.resolveActivity(requireContext().packageManager) != null) {
            startActivity(
                Intent.createChooser(
                    shareIntent,
                    getString(R.string.share_title)
                )
            )
        }
    }

    private fun updateView(
        person: PersonDetails?,
        knownFor: List<Movie>?
    ) {
        if (person != null && knownFor != null) {
            adapter.personDetails = person

            val dividerItemDecoration = DividerItemDecorator(
                ResourcesCompat.getDrawable(
                    resources,
                    R.drawable.item_decoration,
                    requireContext().theme
                )
            )
            binding.listViewContainer.addItemDecoration(dividerItemDecoration)

            adapter.notifyDataSetChanged()
            initDetailsFragment(person)
        }
    }

    private fun initDetailsFragment(personDetails: PersonDetails) {
        childFragmentManager.beginTransaction().replace(
            R.id.fragment_container,
            BareOverviewFragment.newInstance(
                getString(R.string.biography),
                personDetails.biography
            )
        ).commit()
    }

    private fun goToGallery(posters: List<Image>) {
        val galleryFragment = GalleryFragment.newInstance()

        val args = Bundle()
        args.putSerializable(GalleryFragment.POSTERS, posters as Serializable)
        galleryFragment.arguments = args

        childFragmentManager.beginTransaction()
            .add(android.R.id.content, galleryFragment, null).addToBackStack(null).commit()
    }

    private fun updateErrorView(errorMsg: String?) {
        adapter.errorMessage = errorMsg
        adapter.notifyDataSetChanged()
    }

    private fun gotoTab(
        displayAndPersonDetails: Pair<String, PersonDetails>
    ) {
        val fragment = when (displayAndPersonDetails.first) {
            OVERVIEW -> {
                BareOverviewFragment.newInstance(
                    getString(R.string.biography),
                    displayAndPersonDetails.second.biography
                )
            }
            KNOWN_FOR -> {
                MoviesFragment.newInstance(
                    viewModel.knownForMovies,
                    getString(R.string.cast),
                    displayAndPersonDetails.second.name
                )
            }
            else -> null
        }

        if (fragment != null) {
            childFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, fragment).commit()
        }
    }
}