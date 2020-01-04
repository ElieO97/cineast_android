package com.cineast_android.business.api.response

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import com.cineast_android.core.model.KnownFor


class PeopleCreditsResponse {
    @SerializedName("cast")
    @Expose
    var cast: List<KnownFor> = listOf()
}