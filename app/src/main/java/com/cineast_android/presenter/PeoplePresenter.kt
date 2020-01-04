package com.cineast_android.presenter

import android.os.Bundle
import android.os.Parcelable
import com.cineast_android.business.callback.AsyncResponse
import com.cineast_android.business.api.response.ImageResponse
import com.cineast_android.business.api.response.PeopleCreditsResponse
import com.cineast_android.core.model.CineastError
import com.cineast_android.core.model.KnownFor
import com.cineast_android.core.model.Person
import com.cineast_android.core.model.PersonalityDetails
import com.cineast_android.vu.PeopleVu
import io.reactivex.android.schedulers.AndroidSchedulers
import timber.log.Timber

class PeoplePresenter : BasePresenter<PeopleVu>() {
    companion object {
        const val PEOPLE_KEY = "peopleApi"
        const val SCREEN_NAME_KEY = "screen_name"
        const val PEOPLE_DETAILS_KEY = "people_details"
        const val PEOPLE_MOVIES_KEY = "people_movies"
        const val MOVIE_TEAM_KEY = "movie_team"
    }


    var personalityDetails: PersonalityDetails? = null
    var peopleMovies: List<KnownFor>? = listOf()

    override fun onLink(vu: PeopleVu, inState: Bundle?, args: Bundle) {
        super.onLink(vu, inState, args)

        val screenName = args.getString(SCREEN_NAME_KEY)
        val people: Person = args.getParcelable(PEOPLE_KEY) as Person

        vu.personPresentedPublisher?.onNext(people)

        personalityDetails = inState?.getParcelable(PEOPLE_DETAILS_KEY)
        peopleMovies = inState?.getParcelableArrayList(PEOPLE_MOVIES_KEY)

        if (personalityDetails != null && peopleMovies != null) {
            vu.updateVu(personalityDetails, screenName, peopleMovies)
        } else {
            vu.showLoading()
            contentManager.getPeopleDetails(people, object : AsyncResponse<PersonalityDetails> {
                override fun onSuccess(response: PersonalityDetails?) {
                    personalityDetails = response
                    getPeopleMovies(people, personalityDetails, screenName)
                }

                override fun onFail(error: CineastError) {
                    Timber.e("error: $error")
                    handler.post {
                        vu.hideLoading()
                        vu.updateErrorView(error.status_message)
                    }
                }
            })
        }

        rxSubs.add(vu.onProfileClickedPictureObservable
                .subscribeOn(AndroidSchedulers.mainThread())
                .subscribe({ peopleId ->

                    contentManager.getPeopleImages(peopleId, object : AsyncResponse<ImageResponse> {
                        override fun onSuccess(response: ImageResponse?) {
                            val poster = response?.peoplePosters
                            handler.post {
                                vu.goToGallery(poster)
                            }
                        }

                        override fun onFail(error: CineastError) {
                            Timber.d("error: $error")
                        }
                    })
                }))


    }

    private fun getPeopleMovies(actor: Person, personalityDetails: PersonalityDetails?, screenName: String) {
        contentManager.getPeopleMovies(actor, object : AsyncResponse<PeopleCreditsResponse> {
            override fun onSuccess(response: PeopleCreditsResponse?) {
                peopleMovies = response?.cast as List<KnownFor>
                handler.post {
                    vu?.hideLoading()
                    vu?.updateVu(personalityDetails, screenName, peopleMovies)
                }
            }

            override fun onFail(error: CineastError) {
                handler.post {
                    vu?.hideLoading()
                    vu?.updateErrorView(error.status_message)
                }
            }
        })
    }

    override fun onSaveState(outState: Bundle) {
        super.onSaveState(outState)

        personalityDetails?.let {
            outState.putParcelable(PEOPLE_DETAILS_KEY, it)
        }

        peopleMovies?.let {
            outState.putParcelableArrayList(PEOPLE_MOVIES_KEY, it as ArrayList<out Parcelable>)
        }
    }
}