package elieomatuku.restapipractice.business.business.rest

import elieomatuku.restapipractice.business.business.model.data.PeopleDetails
import elieomatuku.restapipractice.business.business.model.response.ImageResponse
import elieomatuku.restapipractice.business.business.model.response.PeopleCreditsResponse
import elieomatuku.restapipractice.business.business.model.response.PeopleResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface PeopleApi {
    @GET ("person/popular")
    fun getPopularPeople (@Query("api_key") apiKey: String): Call<PeopleResponse>

    @GET ("person/{person_id}")
    fun getPeopleDetails (@Path("person_id") personId: Int, @Query("api_key") apiKey: String): Call <PeopleDetails>

    @GET ("person/{person_id}/movie_credits")
    fun getPeopleCredits (@Path("person_id") personId: Int, @Query ("api_key") apiKey: String): Call <PeopleCreditsResponse>


    @GET("person/{person_id}/images")
    fun getPeopleImages(@Path("person_id") movie_id: Int, @Query("api_key") apiKey: String): Call <ImageResponse>


    @GET ("search/person")
    fun getPeopleWithSearch(@Query("api_key") apiKey: String, @Query("query") query: String): Call <PeopleResponse>
}