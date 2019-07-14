package elieomatuku.cineast_android.business.service



import elieomatuku.cineast_android.business.callback.AsyncResponse
import elieomatuku.cineast_android.business.model.data.Widget
import elieomatuku.cineast_android.business.model.response.*
import elieomatuku.cineast_android.utils.RestUtils
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DiscoverService(private val restService: RestService) {
    companion object {
         val API_KEY = RestUtils.API_KEY
    }

    fun getPopularMovies(asyncResponse: AsyncResponse<MovieResponse>){
        var movie: List<Widget> = listOf()

        restService.movieApi.getPopularMovie(API_KEY).enqueue(object : Callback<MovieResponse> {
            override fun onResponse(call: Call<MovieResponse>?, response: Response<MovieResponse>?) {
                asyncResponse.onSuccess(response?.body())
            }

            override fun onFailure(call: Call<MovieResponse>?, t: Throwable?) {
                asyncResponse.onFail(t.toString())
            }
        })
    }


    fun getUpcomingMovies(asyncResponse: AsyncResponse<MovieResponse>){
        restService.movieApi.getUpcomingMovies(API_KEY).enqueue(object : Callback<MovieResponse> {
            override fun onResponse(call: Call<MovieResponse>?, response: Response<MovieResponse>?) {
                asyncResponse.onSuccess(response?.body())
            }

            override fun onFailure(call: Call<MovieResponse>?, t: Throwable?) {
                asyncResponse.onFail(t.toString())
            }
        })
    }


    fun getNowPlayingMovies(asyncResponse: AsyncResponse<MovieResponse>){
        restService.movieApi.getNowPlayingMovie(API_KEY).enqueue(object : Callback<MovieResponse> {
            override fun onResponse(call: Call<MovieResponse>?, response: Response<MovieResponse>?) {
                asyncResponse.onSuccess(response?.body())
            }

            override fun onFailure(call: Call<MovieResponse>?, t: Throwable?) {
                asyncResponse.onFail(t.toString())
            }
        })
    }

    fun getTopRatedMovies(asyncResponse: AsyncResponse<MovieResponse>){
        restService.movieApi.getTopRatedMovies(API_KEY).enqueue(object : Callback<MovieResponse> {
            override fun onResponse(call: Call<MovieResponse>?, response: Response<MovieResponse>?) {
                asyncResponse.onSuccess(response?.body())
            }

            override fun onFailure(call: Call<MovieResponse>?, t: Throwable?) {
                asyncResponse.onFail(t.toString())
            }
        })
    }


    fun getPopularPeople(asyncResponse: AsyncResponse<PeopleResponse>){
        restService.peopleApi.getPopularPeople(API_KEY).enqueue(object: Callback<PeopleResponse> {
            override fun onResponse(call: Call<PeopleResponse>?, response: Response<PeopleResponse>?) {
                asyncResponse.onSuccess(response?.body())
            }

            override fun onFailure(call: Call<PeopleResponse>?, t: Throwable?) {
                asyncResponse.onFail(t.toString())
            }
        })
    }

    fun getGenres (asyncResponse: AsyncResponse<GenreResponse>) {
        restService.movieApi.getGenre(API_KEY).enqueue(object : Callback<GenreResponse> {
            override fun onResponse(call: Call<GenreResponse>?, response: Response<GenreResponse>?) {
                asyncResponse.onSuccess(response?.body())
            }

            override fun onFailure(call: Call<GenreResponse>?, t: Throwable?) {
                asyncResponse.onFail(t.toString())
            }
        })
    }
}