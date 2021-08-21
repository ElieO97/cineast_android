package elieomatuku.cineast_android.domain.model

import kotlinx.android.parcel.Parcelize

@Parcelize
data class Personality(
    override val profilePath: String?,
    val adult: Boolean?,
    override val id: Int,
    override val name: String?
) : Person()