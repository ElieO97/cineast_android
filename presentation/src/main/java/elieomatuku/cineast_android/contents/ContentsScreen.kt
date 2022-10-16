package elieomatuku.cineast_android.contents

import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import elieomatuku.cineast_android.domain.model.Content
import elieomatuku.cineast_android.domain.model.Movie
import elieomatuku.cineast_android.domain.model.Person
import elieomatuku.cineast_android.viewholder.MovieItem
import elieomatuku.cineast_android.viewholder.SwipeableContentItem

@Composable
fun ContentsScreen(
    contents: List<Content>,
    onContentClick: (content: Content) -> Unit,
    onSwipeItem: ((content: Content) -> Unit)? = null
) {
    LazyColumn {
        items(contents) { content ->
            when (content) {
                is Person -> {
                    elieomatuku.cineast_android.viewholder.ContentItem(
                        content = content
                    )
                }
                is Movie -> {
                    if (onSwipeItem != null) {
                        SwipeableContentItem(
                            content = content,
                            onContentClick = onContentClick,
                            onSwipeItem = onSwipeItem
                        )
                    } else {
                        MovieItem(movie = content, onContentClick = onContentClick)
                    }
                }
            }
        }
    }
}