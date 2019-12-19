package elieomatuku.cineast_android.business.api.response

import androidx.annotation.Keep
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import elieomatuku.cineast_android.model.data.Movie

@Keep
class MovieResponse {
//    @SerializedName("results")
//    @Expose
    var results: List<Movie> = listOf()
}