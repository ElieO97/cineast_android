package elieomatuku.cineast_android

import android.app.Application
import androidx.lifecycle.ViewModelProvider
import elieomatuku.cineast_android.ui.connection.ConnectionService
import elieomatuku.cineast_android.cache.*
import elieomatuku.cineast_android.cache.dao.*
import elieomatuku.cineast_android.data.AuthenticationRepositoryImpl
import elieomatuku.cineast_android.data.MovieRepositoryImpl
import elieomatuku.cineast_android.data.PersonRepositoryImpl
import elieomatuku.cineast_android.data.PrefManager
import elieomatuku.cineast_android.data.repository.authentication.AuthenticationCache
import elieomatuku.cineast_android.data.repository.authentication.AuthenticationRemote
import elieomatuku.cineast_android.data.repository.movie.MovieCache
import elieomatuku.cineast_android.data.repository.movie.MovieRemote
import elieomatuku.cineast_android.data.repository.person.PersonCache
import elieomatuku.cineast_android.data.repository.person.PersonRemote
import elieomatuku.cineast_android.data.source.authentication.AuthenticationCacheDataStore
import elieomatuku.cineast_android.data.source.authentication.AuthenticationDataStoreFactory
import elieomatuku.cineast_android.data.source.authentication.AuthenticationRemoteDataStore
import elieomatuku.cineast_android.data.source.movie.MovieCacheDataStore
import elieomatuku.cineast_android.data.source.movie.MovieDataStoreFactory
import elieomatuku.cineast_android.data.source.movie.MovieRemoteDataStore
import elieomatuku.cineast_android.data.source.person.PersonCacheDataStore
import elieomatuku.cineast_android.data.source.person.PersonDataStoreFactory
import elieomatuku.cineast_android.data.source.person.PersonRemoteDataStore
import elieomatuku.cineast_android.domain.interactor.movie.*
import elieomatuku.cineast_android.domain.interactor.people.*
import elieomatuku.cineast_android.domain.interactor.user.*
import elieomatuku.cineast_android.domain.repository.AuthenticationRepository
import elieomatuku.cineast_android.domain.repository.MovieRepository
import elieomatuku.cineast_android.domain.repository.PersonRepository
import elieomatuku.cineast_android.remote.AuthenticationRemoteImpl
import elieomatuku.cineast_android.remote.MovieRemoteImpl
import elieomatuku.cineast_android.remote.PersonRemoteImpl
import elieomatuku.cineast_android.remote.api.MovieApi
import elieomatuku.cineast_android.remote.api.PersonApi
import elieomatuku.cineast_android.remote.api.AuthenticationApi
import elieomatuku.cineast_android.ui.details.MoviesViewModel
import elieomatuku.cineast_android.ui.details.movie.MovieViewModel
import elieomatuku.cineast_android.ui.details.person.PersonViewModel
import elieomatuku.cineast_android.ui.discover.DiscoverViewModel
import elieomatuku.cineast_android.ui.fragment.RateViewModel
import elieomatuku.cineast_android.ui.search.SearchViewModel
import elieomatuku.cineast_android.ui.search.movie.MoviesGridViewModel
import elieomatuku.cineast_android.ui.search.people.PeopleGridViewModel
import elieomatuku.cineast_android.ui.settings.SettingsViewModel
import elieomatuku.cineast_android.ui.settings.user_movies.UserMoviesViewModel
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.kodein.di.Kodein
import org.kodein.di.android.x.androidXModule
import org.kodein.di.generic.bind
import org.kodein.di.generic.instance
import org.kodein.di.generic.provider
import org.kodein.di.generic.singleton
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

fun depInjecT(app: Application): Kodein {

    return Kodein.lazy {
        import(androidXModule(app))
        bind<ConnectionService>() with singleton { ConnectionService(instance()) }

        bind<OkHttpClient>() with singleton {
            val logging = HttpLoggingInterceptor()
            val logLevel = HttpLoggingInterceptor.Level.BODY
            logging.level = logLevel

            val builder = OkHttpClient.Builder()
                .addNetworkInterceptor { chain ->
                    val original = chain.request()
                    val url = original.url.newBuilder()
                        .addQueryParameter("api_key", BuildConfig.api_key)
                        .build()

                    val request = original.newBuilder()
                        .method(original.method, original.body)
                        .url(url)
                        .build()

                    chain.proceed(request)
                }
                .addInterceptor(logging)
                .connectTimeout(30000, TimeUnit.MILLISECONDS)
                .readTimeout(30000, TimeUnit.MILLISECONDS)
                .writeTimeout(30000, TimeUnit.MILLISECONDS)

            builder.build()
        }

        bind<Retrofit>() with singleton {
            Retrofit.Builder()
                .client(instance())
                .baseUrl(BuildConfig.TMDB_BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
        }

        bind<PersonApi>() with singleton {
            val retrofit: Retrofit = instance()
            retrofit.create<PersonApi>(
                PersonApi::class.java
            )
        }

        bind<MovieApi>() with singleton {
            val retrofit: Retrofit = instance()
            retrofit.create<MovieApi>(
                MovieApi::class.java
            )
        }

        bind<AuthenticationApi>() with singleton {
            val retrofit: Retrofit = instance()
            retrofit.create<AuthenticationApi>(
                AuthenticationApi::class.java
            )
        }

        bind<ContentDatabase>() with singleton {
            ContentDatabase.getInstance(instance())
        }

        bind<GenreDao>() with singleton {
            val contentDatabase: ContentDatabase = instance()
            contentDatabase.genreDao()
        }

        bind<MovieDao>() with singleton {
            val contentDatabase: ContentDatabase = instance()
            contentDatabase.movieDao()
        }

        bind<MovieTypeDao>() with singleton {
            val contentDatabase: ContentDatabase = instance()
            contentDatabase.movieTypeDao()
        }

        bind<MovieTypeJoinDao>() with singleton {
            val contentDatabase: ContentDatabase = instance()
            contentDatabase.movieTypeJoinDao()
        }

        bind<PersonDao>() with singleton {
            val contentDatabase: ContentDatabase = instance()
            contentDatabase.personDao()
        }

        bind<PrefManager>() with singleton {
            val storeKey = "cineast_prefs"
            PrefManagerImpl(storeKey, instance())
        }

        bind<MovieRepository>() with singleton {
            MovieRepositoryImpl(instance())
        }

        bind<AuthenticationRepository>() with singleton {
            AuthenticationRepositoryImpl(instance())
        }

        bind<PersonRepository>() with singleton {
            PersonRepositoryImpl(instance())
        }

        bind<MovieCache>() with singleton {
            MovieCacheImpl(instance(), instance(), instance(), instance())
        }

        bind<AuthenticationCache>() with singleton {
            AuthenticationCacheImpl(instance())
        }

        bind<PersonCache>() with singleton {
            PersonCacheImpl(instance(), instance())
        }

        bind<MovieRemote>() with singleton {
            MovieRemoteImpl(instance())
        }

        bind<AuthenticationRemote>() with singleton {
            AuthenticationRemoteImpl(instance())
        }

        bind<PersonRemote>() with singleton {
            PersonRemoteImpl(instance())
        }

        bind<AuthenticationCacheDataStore>() with singleton {
            AuthenticationCacheDataStore(instance())
        }

        bind<AuthenticationRemoteDataStore>() with singleton {
            AuthenticationRemoteDataStore(instance())
        }

        bind<AuthenticationDataStoreFactory>() with singleton {
            AuthenticationDataStoreFactory(instance(), instance(), instance())
        }

        bind<PersonCacheDataStore>() with singleton {
            PersonCacheDataStore(instance())
        }

        bind<PersonRemoteDataStore>() with singleton {
            PersonRemoteDataStore(instance())
        }

        bind<PersonDataStoreFactory>() with singleton {
            PersonDataStoreFactory(instance(), instance(), instance())
        }

        bind<MovieCacheDataStore>() with singleton {
            MovieCacheDataStore(instance())
        }

        bind<MovieRemoteDataStore>() with singleton {
            MovieRemoteDataStore(instance())
        }

        bind<MovieDataStoreFactory>() with singleton {
            MovieDataStoreFactory(instance(), instance(), instance())
        }

        bind<ViewModelProvider.Factory>() with singleton { KodeinViewModelFactory(this.kodein) }

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

        bind<GetRequestToken>() with singleton {
            GetRequestToken(instance())
        }

        bind<GetSession>() with singleton {
            GetSession(instance())
        }

        bind<SetAccount>() with singleton {
            SetAccount(instance())
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

        bindViewModel<MovieViewModel>() with provider {
            MovieViewModel(
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

        bindViewModel<PersonViewModel>() with provider {
            PersonViewModel(
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

        bindViewModel<UserMoviesViewModel>() with provider {
            UserMoviesViewModel(
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
