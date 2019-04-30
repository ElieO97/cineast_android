package elieomatuku.restapipractice.presenter

import android.os.Bundle
import android.os.Parcelable
import elieomatuku.restapipractice.App
import elieomatuku.restapipractice.business.business.callback.AsyncResponse
import elieomatuku.restapipractice.business.business.model.data.*
import elieomatuku.restapipractice.business.business.model.response.*
import elieomatuku.restapipractice.business.business.service.DiscoverService
import elieomatuku.restapipractice.business.business.service.UserService
import elieomatuku.restapipractice.vu.DiscoverVu
import io.reactivex.android.schedulers.AndroidSchedulers
import org.kodein.di.generic.instance
import timber.log.Timber
import java.util.ArrayList


class DiscoverPresenter:  BasePresenter <DiscoverVu>(){
    companion object {
        const val SCREEN_NAME = "Discover"

        const val POPULAR_MOVIE_KEY = "popular_movie"
        const val POPULAR_PEOPLE_KEY = "popular_people"
        const val NOW_PLAYING_KEY = "nowplaying_movie"
        const val UPCOMING_MOVIE_KEY = "upcoming_movie"
        const val TOP_RATED_MOVIE_KEY = "toprated_movie"

        const val SCREEN_NAME_KEY = "screen_name"

        const val MOVIE_GENRES_KEY = "genres"
        const val MOVIE_KEY = "movie"

        const val PEOPLE_KEY = "people"
    }

    private val discoverClient: DiscoverService by App.kodein.instance()
    private val userService : UserService by App.kodein.instance()
    private var popularMovie: List<Movie>? = listOf()
    private var upcomingMovie: List <Movie>? = listOf()
    private var popularPeople: List <People>? = listOf()
    private var nowPlayingMovie:  List <Movie>? = listOf()
    private var topRatedMovie:  List <Movie>? = listOf()
    private var genres: List<Genre>? = listOf()


    override fun onLink(vu: DiscoverVu, inState: Bundle?, args: Bundle) {
        super.onLink(vu, inState, args)

        popularMovie = inState?.getParcelableArrayList(POPULAR_MOVIE_KEY)
        upcomingMovie = inState?.getParcelableArrayList(UPCOMING_MOVIE_KEY)
        popularPeople = inState?.getParcelableArrayList(POPULAR_PEOPLE_KEY)
        nowPlayingMovie = inState?.getParcelableArrayList(NOW_PLAYING_KEY)
        topRatedMovie  = inState?.getParcelableArrayList(TOP_RATED_MOVIE_KEY)
        genres = inState?.getParcelableArrayList(MOVIE_GENRES_KEY)

        if (popularMovie == null || upcomingMovie == null || popularPeople == null || nowPlayingMovie == null || topRatedMovie == null || genres == null) {
            //Todo: Refactor this with a method
            vu.showLoading()
            discoverClient.getPopularMovies(asyncResponse)
            discoverClient.getGenres(genreAsyncResponse)
        } else {
            //Todo: Refactor this with a method
            val movieContainer = getMovieContainer(popularMovie, nowPlayingMovie, upcomingMovie, topRatedMovie)

            if (movieContainer != null) {
                if (popularPeople != null) {
                    vu.setWigdet(popularPeople as List<People>, movieContainer)
                }
            }
        }

        rxSubs.add(vu.movieSelectObservable
                .subscribeOn(AndroidSchedulers.mainThread())
                .subscribe({ movie: Movie ->
                    val params = Bundle()
                    params.putString(SCREEN_NAME_KEY, DiscoverPresenter.SCREEN_NAME)
                    params.putParcelable(MOVIE_KEY, movie)
                    params.putParcelableArrayList(MOVIE_GENRES_KEY, genres as ArrayList<out Parcelable>)
                    vu.gotoMovie(params)

                }, {t: Throwable ->
                   Timber.d( "movieSelectObservable failed:$t")
                }))

        rxSubs.add(vu.personSelectObservable
                .subscribeOn(AndroidSchedulers.mainThread())
                .subscribe({actor: Person ->
                    val params = Bundle()
                    params.putString(SCREEN_NAME_KEY, DiscoverPresenter.SCREEN_NAME)
                    params.putParcelable(PEOPLE_KEY, actor)
                    vu.gotoPeople(params)
                })
        )

        rxSubs.add(vu.loginClickObservable
                .subscribeOn(AndroidSchedulers.mainThread())
                .subscribe({clicked  ->
                    if (clicked) {
                        userService.getAccessToken(object : AsyncResponse<AccessToken> {
                            override fun onSuccess(result: AccessToken?) {
                                vu.gotoWebview(result)

                            }

                            override fun onFail(error: String) {
                                Timber.d("error : $error")
                            }
                        })
                    }
                }))
    }

    private val asyncResponse: AsyncResponse<MovieResponse> by lazy{
        object: AsyncResponse<MovieResponse> {
            override fun onSuccess(result: MovieResponse?) {
                popularMovie = result?.results as List<Movie>
                handler.post{
                    vu?.showLoading()
                }
                discoverClient.getNowPlayingMovies( nowPlayingMovieAsyncResponse)
            }

            override fun onFail(error: String) {
                Timber.d( "Network Error:$error")
            }
        }
    }

    val nowPlayingMovieAsyncResponse: AsyncResponse<MovieResponse> by lazy {
        object : AsyncResponse<MovieResponse> {
            override fun onSuccess(result: MovieResponse?) {
                nowPlayingMovie = result?.results
                discoverClient.getUpcomingMovies(upComingMovieAsyncResponse)
            }
            override fun onFail(error: String) {
                Timber.d( "Network Error:$error")

            }
        }
    }

    val upComingMovieAsyncResponse: AsyncResponse<MovieResponse> by lazy {
        object : AsyncResponse<MovieResponse> {
            override fun onSuccess(result: MovieResponse?) {
                upcomingMovie = result?.results
                discoverClient.getTopRatedMovies(topRatedMovieAsyncResponse)
            }
            override fun onFail(error: String) {
                Timber.d("Network Error:$error")
            }
        }
    }

    val topRatedMovieAsyncResponse: AsyncResponse <MovieResponse> by lazy {
        object : AsyncResponse <MovieResponse> {
            override fun onSuccess(result: MovieResponse?) {
                topRatedMovie = result?.results
                discoverClient.getPopularPeople(popularPeopleAsyncResponse)
            }

            override fun onFail(error: String) {
                Timber.d( "Network Error:$error")
            }
        }
    }

    val popularPeopleAsyncResponse: AsyncResponse <PeopleResponse> by lazy {
        object : AsyncResponse<PeopleResponse> {
            override fun onSuccess(result: PeopleResponse?) {
                popularPeople = result?.results
                handler.post {
                    vu?.hideLoading()
                    val movieContainer = getMovieContainer(popularMovie, nowPlayingMovie, upcomingMovie, topRatedMovie)

                    if ( movieContainer != null) {
                        if (popularPeople != null) {
                            vu?.setWigdet(popularPeople as List<People>, movieContainer)
                        }
                    }
                }
            }

            override fun onFail(error: String) {
                Timber.d( "Network Error:$error")
            }
        }
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

    fun getMovieContainer(popularMovie : List <Movie>?, nowPlayingMovie: List <Movie>?, upcomingMovie: List <Movie>?,
                          topRatedMovie: List <Movie>?): MovieContainer? {
        return if (popularMovie != null && nowPlayingMovie != null && upcomingMovie != null && topRatedMovie != null) {
            MovieContainer(popularMovie, nowPlayingMovie, upcomingMovie , topRatedMovie)
        } else {
            null
        }
    }

    override fun onSaveState(outState: Bundle) {
        super.onSaveState(outState)

        popularMovie?.let {

            //Todo: check if not empty
            outState.putParcelableArrayList(POPULAR_MOVIE_KEY, it as ArrayList<out Parcelable>)
        }

        upcomingMovie?.let {
            outState.putParcelableArrayList(UPCOMING_MOVIE_KEY, it as ArrayList<out Parcelable>)
        }

        popularPeople?.let{
            outState.putParcelableArrayList(POPULAR_PEOPLE_KEY, it as ArrayList<out Parcelable>)
        }

        nowPlayingMovie?.let {
            outState.putParcelableArrayList(NOW_PLAYING_KEY, it as ArrayList<out Parcelable>)
        }

        topRatedMovie?.let {
            outState.putParcelableArrayList(TOP_RATED_MOVIE_KEY, it as ArrayList<out Parcelable>)
        }

        genres?.let {
            outState.putParcelableArrayList(MOVIE_GENRES_KEY, it as ArrayList<out Parcelable>)
        }
    }
}