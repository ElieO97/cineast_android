package elieomatuku.cineast_android.core.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Trailer(
        val id: String?,
        val iso_639_1: String?,
        val iso_3166_1: String?,
        val key: String?,
        val name: String?,
        val site: String?,
        val size: Int?,
        val type: String?) : Parcelable