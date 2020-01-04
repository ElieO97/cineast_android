package com.cineast_android.presenter

import android.os.Bundle
import com.cineast_android.core.model.Poster
import com.cineast_android.vu.MovieGalleryVu


class MovieGalleryPresenter: BasePresenter<MovieGalleryVu> () {
    companion object {
        const val POSTERS = "posters"
        const val MOVIE_POSTER_PATH = "movie_poster_path"

    }

    override fun onLink(vu: MovieGalleryVu, inState: Bundle?, args: Bundle) {
        val posters: List<Poster> = args.getParcelableArrayList<Poster>(POSTERS) as List<Poster>
        vu.updateImages(posters)
        super.onLink(vu, inState, args)
    }
}