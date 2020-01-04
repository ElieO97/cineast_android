package com.cineast_android.business.api.response

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import com.cineast_android.core.model.Backdrop
import com.cineast_android.core.model.Poster

class ImageResponse {
    @SerializedName("backdrops")
    @Expose
    var backdrops: List<Backdrop> = listOf()

    @SerializedName("posters")
    @Expose
    var posters: List<Poster> = listOf()

    @SerializedName("profiles")
    @Expose
    var peoplePosters: List<Poster> = listOf()

}