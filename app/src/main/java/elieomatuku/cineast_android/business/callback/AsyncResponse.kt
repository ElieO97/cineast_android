package elieomatuku.cineast_android.business.callback

import elieomatuku.cineast_android.domain.model.CineastError

interface AsyncResponse<T> {
    fun onSuccess(response: T?)
    fun onFail(error: CineastError)
}
