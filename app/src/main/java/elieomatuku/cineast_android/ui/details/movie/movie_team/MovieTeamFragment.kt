package elieomatuku.cineast_android.ui.details.movie.movie_team

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import elieomatuku.cineast_android.R
import elieomatuku.cineast_android.domain.model.Cast
import elieomatuku.cineast_android.domain.model.Content
import elieomatuku.cineast_android.domain.model.Crew
import elieomatuku.cineast_android.domain.model.MovieSummary
import elieomatuku.cineast_android.databinding.FragmentMovieteamBinding
import elieomatuku.cineast_android.ui.base.BaseFragment
import elieomatuku.cineast_android.ui.details.people.PeopleActivity
import elieomatuku.cineast_android.ui.details.people.PeoplePresenter
import elieomatuku.cineast_android.utils.Constants.SCREEN_NAME_KEY
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.subjects.PublishSubject
import timber.log.Timber

class MovieTeamFragment : BaseFragment() {
    companion object {
        const val MOVIE_SUMMARY = "movie_summary"
        const val PEOPLE_KEY = "peopleApi"

        fun newInstance(movieSummary: MovieSummary): MovieTeamFragment {
            val args = Bundle()

            args.putParcelable(MOVIE_SUMMARY, movieSummary)

            val fragment = MovieTeamFragment()
            fragment.arguments = args
            return fragment
        }
    }

    private lateinit var viewDataBinding: FragmentMovieteamBinding

    var movieTitle: String? = null

    private val onPeopleSelectPublisher: PublishSubject<Content> by lazy {
        PublishSubject.create<Content>()
    }

    private val onPeopleSelectObservable: Observable<Content>
        get() = onPeopleSelectPublisher.hide()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val rootView = inflater.inflate(R.layout.fragment_movieteam, container, false)
        viewDataBinding = FragmentMovieteamBinding.bind(rootView)

        val movieSummary: MovieSummary? = arguments?.getParcelable<MovieSummary>(MOVIE_SUMMARY)

        val cast: List<Cast>? = movieSummary?.cast
        val crew: List<Crew>? = movieSummary?.crew
        movieTitle = movieSummary?.movie?.title

        if (cast != null && crew != null) {
            updateView(cast, crew)
        }

        return viewDataBinding.root
    }

    override fun onResume() {
        super.onResume()

        rxSubs.add(
            onPeopleSelectObservable
                .subscribeOn(AndroidSchedulers.mainThread())
                .subscribe(this::onPeopleSelectedSuccess, this::onSelectionFail)
        )
    }

    private fun updateView(cast: List<Cast>, crew: List<Crew>) {
        viewDataBinding.overviewList.adapter = MovieTeamAdapter(cast, crew, onPeopleSelectPublisher)
        viewDataBinding.overviewList.layoutManager = LinearLayoutManager(activity)
    }

    private fun onPeopleSelectedSuccess(person: Content) {
        val params = Bundle()
        params.putString(SCREEN_NAME_KEY, movieTitle)
        params.putParcelable(PEOPLE_KEY, person)
        params.putBoolean(PeoplePresenter.MOVIE_TEAM_KEY, true)
        gotoPeople(params)
    }

    private fun onSelectionFail(t: Throwable) {
        Timber.d("movieSelectObservable failed:$t")
    }

    private fun gotoPeople(params: Bundle) {
        val intent = Intent(activity, PeopleActivity::class.java)
        intent.putExtras(params)
        activity?.startActivity(intent)
    }
}
