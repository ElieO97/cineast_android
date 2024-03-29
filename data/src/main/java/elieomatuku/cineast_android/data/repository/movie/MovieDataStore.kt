package elieomatuku.cineast_android.data.repository.movie

import elieomatuku.cineast_android.data.model.*

/**
 * Created by elieomatuku on 2021-08-22
 */

interface MovieDataStore {

    suspend fun genres(): List<GenreEntity>

    suspend fun getPopularMovies(): List<MovieEntity>

    suspend fun getUpcomingMovies(): List<MovieEntity>

    suspend fun getNowPlayingMovies(): List<MovieEntity>

    suspend fun getTopRatedMovies(): List<MovieEntity>

    suspend fun getMovieTrailers(movie: MovieEntity): List<TrailerEntity>

    suspend fun getMovieDetails(movie: MovieEntity): MovieFactsEntity

    suspend fun getMovieCredits(movie: MovieEntity): MovieCreditsEntity

    suspend fun getSimilarMovies(movie: MovieEntity): List<MovieEntity>

    suspend fun getMovieImages(movieId: Int): ImageEntities

    suspend fun getMovie(movieId: Int): MovieEntity

    suspend fun searchMovies(argQuery: String): List<MovieEntity>

    suspend fun getWatchList(sessionId: String): List<MovieEntity>

    suspend fun getFavorites(sessionId: String): List<MovieEntity>

    suspend fun deleteAllMovies()

    suspend fun deleteMovie(movie: MovieEntity)

    suspend fun insertGenres(genres: List<GenreEntity>)

    suspend fun insertTopRatedMovie(movies: List<MovieEntity>)

    suspend fun insertNowPlayingMovie(movies: List<MovieEntity>)

    suspend fun insertUpcomingMovie(movies: List<MovieEntity>)

    suspend fun insertPopularMovie(movies: List<MovieEntity>)

    suspend fun insertMovie(movie: MovieEntity, type: MovieType)

    suspend fun updateMovie(movie: MovieEntity)

    suspend fun updateWatchList(
        sessionId: String,
        movie: MovieEntity,
        account: AccountEntity,
        watchList: Boolean
    ): PostResultEntity

    suspend fun getFavoriteList(sessionId: String): List<MovieEntity>

    suspend fun updateFavoriteList(
        sessionId: String,
        movie: MovieEntity,
        account: AccountEntity,
        favorite: Boolean
    ): PostResultEntity

    suspend fun postMovieRate(
        movieId: Int,
        sessionId: String,
        rate: Double
    ): PostResultEntity

    suspend fun getUserRatedMovies(sessionId: String): List<MovieEntity>
}
