package elieomatuku.cineast_android.business.service



import elieomatuku.cineast_android.business.callback.AsyncResponse
import elieomatuku.cineast_android.business.model.data.Movie
import elieomatuku.cineast_android.business.model.data.MovieDetails
import elieomatuku.cineast_android.business.model.response.*
import elieomatuku.cineast_android.business.rest.RestApi
import elieomatuku.cineast_android.utils.ApiUtils
import elieomatuku.cineast_android.utils.RestUtils
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import timber.log.Timber

class ContentManager(private val restApi: RestApi) {
    companion object {
         val API_KEY = RestUtils.API_KEY
    }

    fun getPopularMovies(asyncResponse: AsyncResponse<MovieResponse>){
        restApi.movie.getPopularMovie(API_KEY).enqueue(object : Callback<MovieResponse> {
            override fun onResponse(call: Call<MovieResponse>?, response: Response<MovieResponse>?) {
                val success = response?.isSuccessful ?: false

                if (success) {
                    asyncResponse.onSuccess(response?.body())
                } else {
                    asyncResponse.onFail(ApiUtils.throwableToCineastError(response?.errorBody()))
                }
            }

            override fun onFailure(call: Call<MovieResponse>?, t: Throwable?) {
                asyncResponse.onFail(ApiUtils.throwableToCineastError(t))
            }
        })
    }


    fun getUpcomingMovies(asyncResponse: AsyncResponse<MovieResponse>){
        restApi.movie.getUpcomingMovies(API_KEY).enqueue(object : Callback<MovieResponse> {
            override fun onResponse(call: Call<MovieResponse>?, response: Response<MovieResponse>?) {
                val success = response?.isSuccessful ?: false

                if (success) {
                    asyncResponse.onSuccess(response?.body())
                } else {
                    asyncResponse.onFail(ApiUtils.throwableToCineastError(response?.errorBody()))
                }


            }

            override fun onFailure(call: Call<MovieResponse>?, t: Throwable?) {
                asyncResponse.onFail(ApiUtils.throwableToCineastError(t))
            }
        })
    }


    fun getNowPlayingMovies(asyncResponse: AsyncResponse<MovieResponse>){
        restApi.movie.getNowPlayingMovie(API_KEY).enqueue(object : Callback<MovieResponse> {
            override fun onResponse(call: Call<MovieResponse>?, response: Response<MovieResponse>?) {
                val success = response?.isSuccessful ?: false

                if (success) {
                    asyncResponse.onSuccess(response?.body())
                } else {
                    asyncResponse.onFail(ApiUtils.throwableToCineastError(response?.errorBody()))
                }
            }

            override fun onFailure(call: Call<MovieResponse>?, t: Throwable?) {
                asyncResponse.onFail(ApiUtils.throwableToCineastError(t))
            }
        })
    }

    fun getTopRatedMovies(asyncResponse: AsyncResponse<MovieResponse>){
        restApi.movie.getTopRatedMovies(API_KEY).enqueue(object : Callback<MovieResponse> {
            override fun onResponse(call: Call<MovieResponse>?, response: Response<MovieResponse>?) {
                val success = response?.isSuccessful ?: false

                if (success) {
                    asyncResponse.onSuccess(response?.body())
                } else {
                    asyncResponse.onFail(ApiUtils.throwableToCineastError(response?.errorBody()))
                }
            }

            override fun onFailure(call: Call<MovieResponse>?, t: Throwable?) {
                asyncResponse.onFail(ApiUtils.throwableToCineastError(t))
            }
        })
    }


    fun getPopularPeople(asyncResponse: AsyncResponse<PeopleResponse>){
        restApi.people.getPopularPeople(API_KEY).enqueue(object: Callback<PeopleResponse> {
            override fun onResponse(call: Call<PeopleResponse>?, response: Response<PeopleResponse>?) {
                val success = response?.isSuccessful ?: false

                if (success) {
                    asyncResponse.onSuccess(response?.body())
                } else {
                    asyncResponse.onFail(ApiUtils.throwableToCineastError(response?.errorBody()))
                }
            }

            override fun onFailure(call: Call<PeopleResponse>?, t: Throwable?) {
                asyncResponse.onFail(ApiUtils.throwableToCineastError(t))
            }
        })
    }

    fun getGenres (asyncResponse: AsyncResponse<GenreResponse>) {
        restApi.movie.getGenre(API_KEY).enqueue(object : Callback<GenreResponse> {
            override fun onResponse(call: Call<GenreResponse>?, response: Response<GenreResponse>?) {
                val success = response?.isSuccessful ?: false

                if (success) {
                    asyncResponse.onSuccess(response?.body())
                } else {
                    asyncResponse.onFail(ApiUtils.throwableToCineastError(response?.errorBody()))
                }

            }

            override fun onFailure(call: Call<GenreResponse>?, t: Throwable?) {
                asyncResponse.onFail(ApiUtils.throwableToCineastError(t))
            }
        })
    }

    fun getMovieVideos( movie: Movie, asyncResponse: AsyncResponse<TrailerResponse>) {
        restApi.movie.getMovieVideos(movie.id,  API_KEY).enqueue( object : Callback<TrailerResponse> {
            override fun onResponse(call: Call<TrailerResponse>?, response: Response<TrailerResponse>?) {
                val success = response?.isSuccessful ?: false
                if (success) {
                    asyncResponse.onSuccess(response?.body())
                } else {
                    asyncResponse.onFail(ApiUtils.throwableToCineastError(response?.errorBody()))
                }

            }
            override fun onFailure(call: Call<TrailerResponse>?, t: Throwable?) {
                Timber.e("error: $t")
                asyncResponse.onFail(ApiUtils.throwableToCineastError(t))
            }
        })
    }


    fun getMovieDetails(movie: Movie, asyncResponse: AsyncResponse<MovieDetails> ) {
        restApi.movie.getMovieDetails(movie.id, API_KEY).enqueue(object : Callback<MovieDetails> {
            override fun onResponse(call: Call<MovieDetails>?, response: Response<MovieDetails>?) {
                val success = response?.isSuccessful ?: false
                if (success) {
                    asyncResponse.onSuccess(response?.body())
                } else {
                    asyncResponse.onFail(ApiUtils.throwableToCineastError(response?.errorBody()))
                }
            }

            override fun onFailure(call: Call<MovieDetails>?, t: Throwable?) {
                asyncResponse.onFail(ApiUtils.throwableToCineastError(t))
            }
        })
    }


    fun getMovieCredits (movie: Movie, asyncResponse: AsyncResponse<MovieCreditsResponse>) {
        restApi.movie.getCredits( movie.id, API_KEY).enqueue(object : Callback<MovieCreditsResponse> {
            override fun onResponse(call: Call<MovieCreditsResponse>?, response: Response<MovieCreditsResponse>?) {
                val success = response?.isSuccessful ?: false
                if (success) {
                    asyncResponse.onSuccess(response?.body())
                } else {
                    asyncResponse.onFail(ApiUtils.throwableToCineastError(response?.errorBody()))
                }
            }
            override fun onFailure(call: Call<MovieCreditsResponse>?, t: Throwable?) {
                Timber.e("error: $t")
                asyncResponse.onFail(ApiUtils.throwableToCineastError(t))
            }
        })
    }

    fun getSimilarMovie(movie: Movie, asyncResponse: AsyncResponse<MovieResponse>) {
        restApi.movie.getSimilarMovie(movie.id, API_KEY).enqueue(object : Callback<MovieResponse> {
            override fun onResponse(call: Call<MovieResponse>?, response: Response<MovieResponse>?) {

                val success = response?.isSuccessful ?: false
                if (success) {
                    asyncResponse.onSuccess(response?.body())
                } else {
                    asyncResponse.onFail(ApiUtils.throwableToCineastError(response?.errorBody()))
                }
            }

            override fun onFailure(call: Call<MovieResponse>?, t: Throwable?) {
                Timber.e("error: $t")
                asyncResponse.onFail(ApiUtils.throwableToCineastError(t))
            }
        })
    }

    fun getMovieImages(movieId: Int, asyncResponse: AsyncResponse<ImageResponse>) {
        restApi.movie.getMovieImages(movieId, API_KEY).enqueue(object : Callback<ImageResponse> {
            override fun onResponse(call: Call<ImageResponse>?, response: Response<ImageResponse>?) {

                val success = response?.isSuccessful ?: false
                if (success) {
                    asyncResponse.onSuccess(response?.body())
                } else {
                    asyncResponse.onFail(ApiUtils.throwableToCineastError(response?.errorBody()))
                }
            }

            override fun onFailure(call: Call<ImageResponse>?, t: Throwable?) {
                Timber.e("error: $t")
                asyncResponse.onFail(ApiUtils.throwableToCineastError(t))
            }
        })
    }
}