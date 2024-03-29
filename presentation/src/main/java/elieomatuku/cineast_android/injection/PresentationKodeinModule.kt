package elieomatuku.cineast_android.injection

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModelProvider
import elieomatuku.cineast_android.connection.ConnectionService
import elieomatuku.cineast_android.widgets.movieswidget.MoviesViewModel
import elieomatuku.cineast_android.details.movie.MovieViewModel
import elieomatuku.cineast_android.details.person.PersonViewModel
import elieomatuku.cineast_android.discover.DiscoverViewModel
import elieomatuku.cineast_android.domain.interactor.movie.*
import elieomatuku.cineast_android.domain.interactor.people.*
import elieomatuku.cineast_android.domain.interactor.user.*
import elieomatuku.cineast_android.fragment.RateViewModel
import elieomatuku.cineast_android.search.SearchViewModel
import elieomatuku.cineast_android.search.movie.MoviesGridViewModel
import elieomatuku.cineast_android.search.people.PeopleGridViewModel
import elieomatuku.cineast_android.settings.SettingsViewModel
import elieomatuku.cineast_android.settings.usercontents.UserContentsViewModel
import org.kodein.di.*


/**
 * Created by elieomatuku on 2021-10-21
 */

object PresentationKodeinModule {
    private const val moduleName = "presentation"

    fun getModule(): DI.Module {

        return DI.Module(name = moduleName) {

            bind<ConnectionService>() with singleton { ConnectionService(instance()) }

            bind<ViewModelProvider.Factory>() with singleton { DIViewModelFactory(this.di) }

            bind<GetDiscoverContent>() with singleton {
                GetDiscoverContent(instance(), instance())
            }

            bind<GetGenres>() with singleton {
                GetGenres(instance())
            }

            bind<GetPersonalities>() with singleton {
                GetPersonalities(instance())
            }

            bind<GetPopularMovies>() with singleton {
                GetPopularMovies(instance())
            }

            bind<GetMovieSummary>() with singleton {
                GetMovieSummary(instance())
            }

            bind<IsLoggedIn>() with singleton {
                IsLoggedIn(instance())
            }

            bind<GetWatchList>() with singleton {
                GetWatchList(instance(), instance())
            }

            bind<GetFavorites>() with singleton {
                GetFavorites(instance(), instance())
            }

            bind<GetUserRatedMovies>() with singleton {
                GetUserRatedMovies(instance(), instance())
            }

            bind<AddMovieToFavorites>() with singleton {
                AddMovieToFavorites(instance(), instance())
            }

            bind<AddMovieToWatchList>() with singleton {
                AddMovieToWatchList(instance(), instance())
            }

            bind<RemoveMovieFromFavorites>() with singleton {
                RemoveMovieFromFavorites(instance(), instance())
            }

            bind<RemoveMovieFromWatchList>() with singleton {
                RemoveMovieFromWatchList(instance(), instance())
            }

            bind<Logout>() with singleton {
                Logout(instance())
            }

            bind<GetAccessToken>() with singleton {
                GetAccessToken(instance())
            }

            bind<GetPersonDetails>() with singleton {
                GetPersonDetails(instance())
            }

            bind<GetKnownForMovies>() with singleton {
                GetKnownForMovies(instance())
            }

            bind<GetImages>() with singleton {
                GetImages(instance())
            }

            bind<SearchPeople>() with singleton {
                SearchPeople(instance())
            }

            bind<SearchMovies>() with singleton {
                SearchMovies(instance())
            }

            bind<GetSession>() with singleton {
                GetSession(instance())
            }

            bind<GetAccount>() with singleton {
                GetAccount(instance())
            }

            bind<GetMovie>() with singleton {
                GetMovie(instance())
            }

            bindViewModel<DiscoverViewModel>() with provider {
                DiscoverViewModel(instance(), instance(), instance(), instance(), instance())
            }

            bindViewModel<MoviesGridViewModel>() with provider {
                MoviesGridViewModel(instance(), instance())
            }

            bindViewModel<PeopleGridViewModel>() with provider {
                PeopleGridViewModel(instance(), instance())
            }

            bindViewModel<MoviesViewModel>() with provider {
                MoviesViewModel(instance())
            }

            bindFactory { savedStateHandle: SavedStateHandle ->
                MovieViewModel(
                    savedStateHandle,
                    instance(),
                    instance(),
                    instance(),
                    instance(),
                    instance(),
                    instance(),
                    instance(),
                    instance(),
                    instance()
                )
            }

            bindFactory { savedStateHandle: SavedStateHandle ->
                PersonViewModel(
                    savedStateHandle,
                    instance(),
                    instance(),
                    instance()
                )
            }

            bindViewModel<SearchViewModel>() with provider {
                SearchViewModel(
                    instance(),
                    instance()
                )
            }

            bindViewModel<RateViewModel>() with provider {
                RateViewModel(
                    instance()
                )
            }

            bindViewModel<UserContentsViewModel>() with provider {
                UserContentsViewModel(
                    instance(),
                    instance(),
                    instance(),
                    instance(),
                    instance(),
                    instance()
                )
            }

            bindViewModel<SettingsViewModel>() with provider {
                SettingsViewModel(
                    instance(),
                    instance(),
                    instance(),
                    instance(),
                    instance()
                )
            }
        }
    }
}