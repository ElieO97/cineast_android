package elieomatuku.cineast_android.core.model


import kotlinx.android.parcel.Parcelize


@Parcelize
data class Personality(
        override val profile_path: String?,
        val adult: Boolean?,
        override val id: Int,
        override val name: String?) : Person()
