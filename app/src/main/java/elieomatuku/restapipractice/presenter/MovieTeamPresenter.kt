package elieomatuku.restapipractice.presenter

import android.os.Bundle
import elieomatuku.restapipractice.business.business.model.data.Cast
import elieomatuku.restapipractice.business.business.model.data.Crew
import elieomatuku.restapipractice.business.business.model.data.Person
import elieomatuku.restapipractice.fragment.MovieTeamFragment
import elieomatuku.restapipractice.vu.MovieTeamVu
import io.reactivex.android.schedulers.AndroidSchedulers
import timber.log.Timber


class MovieTeamPresenter : BasePresenter <MovieTeamVu>() {
    companion object {
        const val MOVIE_CAST = "movie_cast"
        const val MOVIE_CREW = "movie_crew"
        const val SCREEN_NAME_KEY = "screen_name"
        const val PEOPLE_KEY = "people"
    }


    var movieTitle : String? = null

    override fun onLink(vu: MovieTeamVu, inState: Bundle?, args: Bundle) {
        super.onLink(vu, inState, args)

        val cast: List<Cast>? = args.get(MOVIE_CAST)  as List<Cast>
        val crew: List<Crew>? = args.get(MOVIE_CREW) as List<Crew>
        movieTitle = args.getString(MovieTeamFragment.MOVIE_TITLE)

        if (cast != null && crew != null) {
            vu.updateVu(cast, crew)
        }

        rxSubs.add(vu.onCastSelectObservable
                .subscribeOn(AndroidSchedulers.mainThread())
                .subscribe(this::onPersonSelectedSuccess,this::onSelectionFail))

        rxSubs.add(vu.onCrewSelectObservable
                .subscribeOn(AndroidSchedulers.mainThread())
                .subscribe(this::onPersonSelectedSuccess, this::onSelectionFail))
    }

    private fun onPersonSelectedSuccess(person: Person) {
        val params = Bundle()
        params.putString(SCREEN_NAME_KEY, movieTitle)
        params.putParcelable(PEOPLE_KEY, person)
        params.putBoolean(PeoplePresenter.MOVIE_TEAM_KEY, true)
        vu?.gotoPeople(params)
    }

    private fun onSelectionFail(t: Throwable) {
        Timber.d( "movieSelectObservable failed:$t")
    }
}