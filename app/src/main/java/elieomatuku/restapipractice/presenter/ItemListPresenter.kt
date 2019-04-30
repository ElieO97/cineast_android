package elieomatuku.restapipractice.presenter

import android.os.Bundle
import android.os.Parcelable
import elieomatuku.restapipractice.App
import elieomatuku.restapipractice.business.business.callback.AsyncResponse
import elieomatuku.restapipractice.business.business.service.DiscoverService
import elieomatuku.restapipractice.business.business.model.data.*
import elieomatuku.restapipractice.business.business.model.response.GenreResponse
import elieomatuku.restapipractice.vu.ItemListVu
import io.reactivex.android.schedulers.AndroidSchedulers
import org.kodein.di.generic.instance
import timber.log.Timber

class ItemListPresenter: BasePresenter <ItemListVu>() {
    companion object {
        const val WIDGET_KEY = "widget"
        const val SCREEN_NAME_KEY = "screen_name"
        const val SCREEN_NAME = "Search"
        const val MOVIE_KEY = "movie"
        const val MOVIE_GENRES_KEY = "genres"
        const val PEOPLE_KEY = "people"
    }

    private val discoverClient: DiscoverService by App.kodein.instance()
    private var genres: List<Genre>? = listOf()

    override fun onLink(vu: ItemListVu, inState: Bundle?, args: Bundle) {
        super.onLink(vu, inState, args)
        val listOfWidgets: List<Widget>  = args.getParcelableArrayList(WIDGET_KEY)
        val screenNameRes = args.getInt(SCREEN_NAME_KEY)
        vu.updateVu(listOfWidgets, screenNameRes)

        discoverClient.getGenres(genreAsyncResponse)
        rxSubs.add(vu.movieSelectObservable
                .subscribeOn(AndroidSchedulers.mainThread())
                .subscribe({ movie: Movie ->
                    val params = Bundle()
                    params.putString(SCREEN_NAME_KEY, SCREEN_NAME)
                    params.putParcelable(MOVIE_KEY, movie)
                    params.putParcelableArrayList(MOVIE_GENRES_KEY, genres as ArrayList<out Parcelable>)
                    vu.gotoMovie(params)

                }, {t: Throwable ->
                    Timber.d( "movieSelectObservable failed:$t")
                }))

        rxSubs.add(vu.personSelectObservable
                .subscribeOn(AndroidSchedulers.mainThread())
                .subscribe({person: Person ->
                    val params = Bundle()
                    params.putString(SCREEN_NAME_KEY, DiscoverPresenter.SCREEN_NAME)
                    params.putParcelable(PEOPLE_KEY, person)
                    vu.gotoPeople(params)
                })
        )
    }

    private val genreAsyncResponse: AsyncResponse<GenreResponse> by lazy {
        object: AsyncResponse<GenreResponse> {
            override fun onSuccess(result: GenreResponse?) {
                genres = result?.genres
            }
            override fun onFail(error: String) {
                Timber.d("Network Error:$error")
            }
        }
    }
}