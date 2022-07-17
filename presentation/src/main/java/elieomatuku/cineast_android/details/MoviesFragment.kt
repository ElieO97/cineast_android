package elieomatuku.cineast_android.details

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.annotation.ExperimentalCoilApi
import coil.compose.rememberImagePainter
import com.google.accompanist.appcompattheme.AppCompatTheme
import elieomatuku.cineast_android.R
import elieomatuku.cineast_android.databinding.FragmentMoviesBinding
import elieomatuku.cineast_android.domain.model.Content
import elieomatuku.cineast_android.domain.model.Genre
import elieomatuku.cineast_android.domain.model.Movie
import elieomatuku.cineast_android.base.BaseFragment
import elieomatuku.cineast_android.contents.ContentsActivity
import elieomatuku.cineast_android.details.movie.MovieFragment
import elieomatuku.cineast_android.utils.Constants
import elieomatuku.cineast_android.utils.UiUtils
import java.io.Serializable

/**
 * Created by elieomatuku on 2021-05-05
 */

class MoviesFragment : BaseFragment() {
    companion object {
        const val MOVIES = "movies"
        const val TITLE = "title"
        private const val SELECTED_MOVIE_TITLE = "gotomovie_title"
        const val MOVIE_KEY = "movieApi"
        const val MOVIE_GENRES_KEY = "genres"

        fun newInstance(
            movies: List<Movie>,
            title: String? = null,
            selectedMovieTitle: String? = null
        ): MoviesFragment {
            val args = Bundle()
            args.putSerializable(MOVIES, movies as Serializable)

            title?.let {
                args.putString(TITLE, it)
            }

            selectedMovieTitle?.let {
                args.putString(SELECTED_MOVIE_TITLE, it)
            }

            val fragment = MoviesFragment()
            fragment.arguments = args
            return fragment
        }
    }

    private lateinit var viewDataBinding: FragmentMoviesBinding

    private val viewModel: MoviesViewModel by viewModel<MoviesViewModel>()

    private lateinit var movies: List<Movie>
    private var genres: List<Genre>? = listOf()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        viewDataBinding =
            FragmentMoviesBinding.bind(inflater.inflate(R.layout.fragment_movies, container, false))

        arguments?.getSerializable(MOVIES)?.let {
            movies = it as List<Movie>
        }

        viewModel.viewState.observe(
            this.viewLifecycleOwner
        ) { state ->
            state.genres.let {
                genres = it
            }
        }

        updateView()

        return viewDataBinding.root
    }

    private fun updateView() {
        viewDataBinding.moviesWidget.apply {
            setViewCompositionStrategy(ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed)
            setContent {
                AppCompatTheme {
                    MoviesWidget(
                        movies,
                        arguments?.getString(SELECTED_MOVIE_TITLE) ?: arguments?.getString(TITLE)
                        ?: getString(R.string.movies),
                        onItemClick = {
                            val params = Bundle()
                            if (it is Movie) {
                                params.putString(Constants.SCREEN_NAME_KEY, it.title)
                            }
                            params.putSerializable(MOVIE_KEY, it)
                            params.putSerializable(MOVIE_GENRES_KEY, genres as Serializable)
                            gotoMovie(params)
                        },
                        onSeeAllClick = {
                            context?.let {
                                ContentsActivity.startActivity(it, movies, R.string.movies)
                            }
                        }
                    )
                }
            }
        }
    }

    private fun gotoMovie(params: Bundle) {
        val intent = Intent(activity, MovieFragment::class.java)
        intent.putExtras(params)
        activity?.startActivity(intent)
    }
}

@Composable
fun MoviesWidget(
    movies: List<Movie>,
    sectionTitle: String = String(),
    onItemClick: (content: Content) -> Unit = {},
    onSeeAllClick: () -> Unit = {}
) {
    Column(modifier = Modifier.padding(bottom = dimensionResource(id = R.dimen.holder_item_movie_textview_margin))) {
        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    start = dimensionResource(id = R.dimen.holder_movie_layout_padding_start),
                    end = dimensionResource(id = R.dimen.holder_movie_layout_padding),
                    top = dimensionResource(id = R.dimen.holder_movie_layout_padding),
                    bottom = dimensionResource(id = R.dimen.holder_item_movie_image_view_margin)
                )
                .clickable(onClick = onSeeAllClick)
        ) {
            Text(
                text = sectionTitle,
                color = colorResource(R.color.color_white)
            )
            Row(horizontalArrangement = Arrangement.End) {
                Text(
                    stringResource(id = R.string.see_all),
                    color = colorResource(R.color.color_orange_app)
                )
                Icon(
                    painter = painterResource(id = R.drawable.ic_keyboard_arrow_right_black_24dp),
                    contentDescription = null,
                    tint = colorResource(R.color.color_orange_app)
                )
            }
        }
        LazyRow(
            modifier = Modifier.padding(
                top = dimensionResource(id = R.dimen.holder_movie_layout_padding),
                start = dimensionResource(id = R.dimen.holder_movie_listview_padding_start)
            )
        ) {
            items(movies) { movie ->
                MovieItem(movie = movie, onMovieClick = onItemClick)
            }
        }
    }
}

@OptIn(ExperimentalCoilApi::class)
@Composable
fun MovieItem(movie: Movie, onMovieClick: (content: Content) -> Unit) {
    val imageUrl = UiUtils.getImageUrl(movie.posterPath, stringResource(id = R.string.image_small))
    Column(Modifier.clickable(onClick = { onMovieClick.invoke(movie) })) {
        Image(
            painter = rememberImagePainter(
                data = imageUrl,
            ),
            contentDescription = null,
            modifier = Modifier
                .height(dimensionResource(id = R.dimen.movie_summary_image_size))
                .width(dimensionResource(id = R.dimen.movie_summary_image_width))
        )
        (movie.title ?: movie.originalTitle)?.let {
            Text(
                text = it,
                color = colorResource(R.color.color_white),
                maxLines = 1,
                fontSize = dimensionResource(id = R.dimen.holder_item_movie_textview_size).value.sp,
                fontWeight = FontWeight.Bold,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier
                    .padding(
                        top = dimensionResource(id = R.dimen.holder_item_movie_textview_margin),
                        start = dimensionResource(id = R.dimen.holder_item_movie_textview_margin),
                        end = dimensionResource(id = R.dimen.holder_item_movie_textview_margin)
                    )
                    .widthIn(max = 70.dp)
            )
        }
        movie.releaseDate?.let {
            Text(
                text = it,
                color = colorResource(R.color.color_white),
                fontSize = dimensionResource(id = R.dimen.holder_item_movie_textview_size).value.sp,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .padding(
                        start = dimensionResource(id = R.dimen.holder_item_movie_textview_margin),
                        end = dimensionResource(id = R.dimen.holder_item_movie_textview_margin)
                    )
                    .widthIn(max = 70.dp)
            )
        }
    }
}