package elieomatuku.cineast_android.business.api.response

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import elieomatuku.cineast_android.core.model.KnownFor


class PeopleCreditsResponse {
    @SerializedName("cast")
    @Expose
    var cast: List<KnownFor> = listOf()
}