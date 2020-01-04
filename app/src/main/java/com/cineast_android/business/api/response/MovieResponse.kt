package com.cineast_android.business.api.response

import androidx.annotation.Keep
import com.cineast_android.core.model.Movie

@Keep
class MovieResponse {
    var results: List<Movie> = listOf()
}