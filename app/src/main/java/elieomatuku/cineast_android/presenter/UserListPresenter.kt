package elieomatuku.cineast_android.presenter

import android.os.Bundle
import elieomatuku.cineast_android.business.callback.AsyncResponse
import elieomatuku.cineast_android.core.model.CineastError
import elieomatuku.cineast_android.core.model.Movie
import elieomatuku.cineast_android.vu.UserListVu
import io.reactivex.android.schedulers.AndroidSchedulers
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import timber.log.Timber


class UserListPresenter : ListPresenter<UserListVu>() {

    companion object {

        const val DISPLAY_FAVORITE_LIST = "favorite_list_key"
        const val DISPLAY_WATCH_LIST = "watch_list_key"

    }

    private var isWatchList: Boolean = false
    private var isFavoriteList: Boolean = false


    override fun onLink(vu: UserListVu, inState: Bundle?, args: Bundle) {
        super.onLink(vu, inState, args)

        isFavoriteList = args.getBoolean(DISPLAY_FAVORITE_LIST)
        isWatchList = args.getBoolean(DISPLAY_WATCH_LIST)

        val screenNameRes = args.getInt(SCREEN_NAME_KEY)


        if (isFavoriteList) {
            launch {
                val movieResponse = tmdbContentClient.getFavoriteList()
                if (movieResponse.isSuccess) {
                    val movies = movieResponse.getOrNull()?.results
                    movies?.let {
                        launch(Dispatchers.Main) {
                            vu.updateVu(it, screenNameRes)
                        }
                    }
                } else {
                    vu.updateErrorView(movieResponse.exceptionOrNull()?.message)
                }
            }
        } else if (isWatchList) {
            launch {
                val movieResponse = tmdbContentClient.getWatchList()
                if (movieResponse.isSuccess) {
                    val movies = movieResponse.getOrNull()?.results
                    movies?.let {
                        launch(Dispatchers.Main) {
                            vu.updateVu(it, screenNameRes)
                        }
                    }
                } else {
                    launch(Dispatchers.Main){
                        Timber.e("error : ${movieResponse.exceptionOrNull()}")
                        vu.updateErrorView(movieResponse.exceptionOrNull()?.message)
                    }
                }
            }

        } else {
            tmdbContentClient.getUserRatedMovies(object : AsyncResponse<List<Movie>> {
                override fun onSuccess(result: List<Movie>?) {
                    handler.post {
                        result?.let {
                            vu.updateVu(it, screenNameRes)
                        }
                    }
                }

                override fun onFail(error: CineastError) {
                    Timber.e("error : $error")

                    vu.updateErrorView(error?.status_message)
                }
            })

        }


        rxSubs.add(vu.onMovieRemovedObservable
                .subscribeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    if (isWatchList) {

                        tmdbContentClient.removeMovieFromWatchList(it)
                    } else if (isFavoriteList) {
                        tmdbContentClient.removeMovieFromFavoriteList(it)
                    }
                }, { t: Throwable ->
                    Timber.e("onMovieRemovedObservable failed $t")

                }))
    }


}