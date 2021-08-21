package elieomatuku.cineast_android.domain.model

import kotlinx.android.parcel.Parcelize

@Parcelize
data class Cast(
    val cast_id: Int?,
    val character: String?,
    val credit_id: String?,
    val gender: Int?,
    override val id: Int?,
    override val name: String?,
    val order: Int?,
    override val profilePath: String?
) : Person()