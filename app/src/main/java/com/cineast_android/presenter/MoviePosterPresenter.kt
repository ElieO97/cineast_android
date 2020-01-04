package com.cineast_android.presenter

import android.os.Bundle
import com.cineast_android.vu.MoviePosterVu


class  MoviePosterPresenter: BasePresenter<MoviePosterVu>(){
    override fun onLink(vu: MoviePosterVu, inState: Bundle?, args: Bundle) {
        vu.updateImage(args.getString(MovieGalleryPresenter.MOVIE_POSTER_PATH))
        super.onLink(vu, inState, args)
    }
}