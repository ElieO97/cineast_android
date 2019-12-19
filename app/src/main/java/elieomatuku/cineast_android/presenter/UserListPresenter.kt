package elieomatuku.cineast_android.presenter

import android.os.Bundle
import elieomatuku.cineast_android.business.callback.AsyncResponse
import elieomatuku.cineast_android.model.data.CineastError
import elieomatuku.cineast_android.model.data.Movie
import elieomatuku.cineast_android.vu.UserListVu
import io.reactivex.android.schedulers.AndroidSchedulers
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
            tmdbContentClient.getFavoriteList(object : AsyncResponse<List<Movie>> {
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
        } else if (isWatchList) {
            tmdbContentClient.getWatchList(object : AsyncResponse<List<Movie>> {
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