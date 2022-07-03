package elieomatuku.cineast_android.viewholder

import elieomatuku.cineast_android.domain.model.Content
import elieomatuku.cineast_android.extensions.Contents

/**
 * Created by elieomatuku on 2019-12-29
 */

interface ContentHolder {
    fun update(content: Contents)
    fun update(content: List<Content>, titleRes: Int)
}