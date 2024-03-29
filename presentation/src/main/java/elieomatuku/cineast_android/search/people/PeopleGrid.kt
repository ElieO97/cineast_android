package elieomatuku.cineast_android.search.people

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import elieomatuku.cineast_android.contents.ContentGrid
import elieomatuku.cineast_android.domain.model.Content
import elieomatuku.cineast_android.widgets.EmptyStateWidget

/**
 * Created by elieomatuku on 2021-06-05
 */

@Composable
fun PeopleGrid(
    viewModelFactory: ViewModelProvider.Factory,
    viewModel: PeopleGridViewModel = viewModel(factory = viewModelFactory),
    hasNetworkConnection: Boolean,
    onContentClick: (content: Content) -> Unit
) {
    val viewState by viewModel.viewState.observeAsState()

    viewState?.contents?.let { contents ->
        ContentGrid(contents = contents) {
            onContentClick(it)
        }
    }

    viewState?.viewError?.apply {
        EmptyStateWidget(
            errorMsg = peek().message,
            hasNetworkConnection = hasNetworkConnection
        )
    }
}
