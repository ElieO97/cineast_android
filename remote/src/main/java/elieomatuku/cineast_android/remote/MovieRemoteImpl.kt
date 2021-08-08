package elieomatuku.cineast_android.remote

import elieomatuku.cineast_android.data.model.*
import elieomatuku.cineast_android.data.repository.movie.MovieRemote
import elieomatuku.cineast_android.remote.api.MovieApi
import elieomatuku.cineast_android.remote.model.RemoteException
import elieomatuku.cineast_android.remote.request.FavouritesMediaRequest
import elieomatuku.cineast_android.remote.request.RateRequest
import elieomatuku.cineast_android.remote.request.WatchListMediaRequest

/**
 * Created by elieomatuku on 2021-07-04
 */

class MovieRemoteImpl(private val movieApi: MovieApi) : MovieRemote {

    override suspend fun getPopularMovies(): List<MovieEntity> {
        val response = movieApi.getPopularMovies()
        if (response.isSuccessful) {
            val body = response.body()
            return body!!
        } else {
            throw RemoteException(
                response.code(),
                response.errorBody()?.toString(),
                response.message()
            )
        }
    }

    override suspend fun getUpcomingMovies(): List<MovieEntity> {
        val response = movieApi.getUpcomingMovies()
        if (response.isSuccessful) {
            val body = response.body()
            return body!!
        } else {
            throw RemoteException(
                response.code(),
                response.errorBody()?.toString(),
                response.message()
            )
        }
    }

    override suspend fun getNowPlayingMovies(): List<MovieEntity> {
        val response = movieApi.getNowPlayingMovies()
        if (response.isSuccessful) {
            val body = response.body()
            return body!!
        } else {
            throw RemoteException(
                response.code(),
                response.errorBody()?.toString(),
                response.message()
            )
        }
    }

    override suspend fun getTopRatedMovies(): List<MovieEntity> {
        val response = movieApi.getTopRatedMovies()
        if (response.isSuccessful) {
            val body = response.body()
            return body!!
        } else {
            throw RemoteException(
                response.code(),
                response.errorBody()?.toString(),
                response.message()
            )
        }
    }

    override suspend fun getGenres(): List<GenreEntity> {
        val response = movieApi.getGenre()
        if (response.isSuccessful) {
            val body = response.body()
            return body!!
        } else {
            throw RemoteException(
                response.code(),
                response.errorBody()?.toString(),
                response.message()
            )
        }
    }

    override suspend fun getTrailers(movieId: Int): List<TrailerEntity> {
        val response = movieApi.getMovieVideos(movieId)
        if (response.isSuccessful) {
            val body = response.body()
            return body!!
        } else {
            throw RemoteException(
                response.code(),
                response.errorBody()?.toString(),
                response.message()
            )
        }
    }

    override suspend fun getMovieFacts(movieId: Int): MovieFactsEntity {
        val response = movieApi.getMovieDetails(movieId)
        if (response.isSuccessful) {
            val body = response.body()
            return body!!
        } else {
            throw RemoteException(
                response.code(),
                response.errorBody()?.toString(),
                response.message()
            )
        }
    }

    override suspend fun getMovieCredits(movieId: Int): MovieCreditsEntity {
        val response = movieApi.getCredits(movieId)
        if (response.isSuccessful) {
            val body = response.body()
            return body!!
        } else {
            throw RemoteException(
                response.code(),
                response.errorBody()?.toString(),
                response.message()
            )
        }
    }

    override suspend fun getSimilarMovie(movieId: Int): List<MovieEntity> {
        val response = movieApi.getSimilarMovies(movieId)
        if (response.isSuccessful) {
            val body = response.body()
            return body!!
        } else {
            throw RemoteException(
                response.code(),
                response.errorBody()?.toString(),
                response.message()
            )
        }
    }

    override suspend fun getMovieImages(movieId: Int): ImageEntities {
        val response = movieApi.getMovieImages(movieId)
        if (response.isSuccessful) {
            val body = response.body()
            return body!!
        } else {
            throw RemoteException(
                response.code(),
                response.errorBody()?.toString(),
                response.message()
            )
        }
    }

    override suspend fun getMovie(movieId: Int): MovieEntity {
        val response = movieApi.getMovie(movieId)
        if (response.isSuccessful) {
            val body = response.body()
            return body!!
        } else {
            throw RemoteException(
                response.code(),
                response.errorBody()?.toString(),
                response.message()
            )
        }
    }

    override suspend fun searchMovies(query: String): List<MovieEntity> {
        val response = movieApi.getMoviesWithSearch(query)
        if (response.isSuccessful) {
            val body = response.body()
            return body!!
        } else {
            throw RemoteException(
                response.code(),
                response.errorBody()?.toString(),
                response.message()
            )
        }
    }

    override suspend fun updateWatchList(
        sessionId: String,
        movie: MovieEntity,
        watchList: Boolean
    ): PostResultEntity {
        val mediaRequest = WatchListMediaRequest(media_id = movie.id, watchlist = watchList)
        val response = movieApi.updateWatchList(sessionId, mediaRequest)
        if (response.isSuccessful) {
            val body = response.body()
            return body!!
        } else {
            throw RemoteException(
                response.code(),
                response.errorBody()?.toString(),
                response.message()
            )
        }
    }

    override suspend fun getFavoriteList(sessionId: String): List<MovieEntity> {
        val response = movieApi.getFavoritesList(sessionId)
        if (response.isSuccessful) {
            val body = response.body()
            return body!!
        } else {
            throw RemoteException(
                response.code(),
                response.errorBody()?.toString(),
                response.message()
            )
        }
    }

    override suspend fun updateFavoriteList(
        sessionId: String,
        movie: MovieEntity,
        favorite: Boolean
    ): PostResultEntity {
        val mediaRequest = FavouritesMediaRequest(media_id = movie.id, favorite = favorite)
        val response = movieApi.updateFavorites(sessionId, mediaRequest)
        if (response.isSuccessful) {
            val body = response.body()
            return body!!
        } else {
            throw RemoteException(
                response.code(),
                response.errorBody()?.toString(),
                response.message()
            )
        }
    }

    override suspend fun postMovieRate(
        movieId: Int,
        sessionId: String,
        rate: Double
    ): PostResultEntity {
        val requestBody = RemoteUtils.getRequestBody(RateRequest(rate))
        val response = movieApi.postMovieRate(movieId, sessionId, requestBody)
        if (response.isSuccessful) {
            val body = response.body()
            return body!!
        } else {
            throw RemoteException(
                response.code(),
                response.errorBody()?.toString(),
                response.message()
            )
        }
    }

    override suspend fun getUserRatedMovies(sessionId: String): List<MovieEntity> {
        val response = movieApi.getUserRatedMovies(sessionId)
        if (response.isSuccessful) {
            val body = response.body()
            return body!!
        } else {
            throw RemoteException(
                response.code(),
                response.errorBody()?.toString(),
                response.message()
            )
        }
    }
}
