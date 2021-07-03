package elieomatuku.cineast_android.ui.search.movie

import android.content.Intent
import android.os.Bundle
import android.os.Parcelable
import android.view.View
import androidx.lifecycle.Observer
import elieomatuku.cineast_android.domain.model.Content
import elieomatuku.cineast_android.domain.model.Genre
import elieomatuku.cineast_android.ui.contents.ContentGridFragment
import elieomatuku.cineast_android.ui.details.movie.MovieActivity
import elieomatuku.cineast_android.utils.Constants

/**
 * Created by elieomatuku on 2021-06-05
 */

class MoviesGridFragment :
    ContentGridFragment<MoviesGridViewModel>(MoviesGridViewModel::class.java) {
    companion object {
        const val CONTENT_KEY = "movieApi"
        const val MOVIE_GENRES_KEY = "genres"
        const val SCREEN_NAME = "Search"

        fun newInstance(): ContentGridFragment<MoviesGridViewModel> {
            return MoviesGridFragment()
        }
    }

    private var genres: List<Genre>? = listOf()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        viewModel.genresLiveData.observe(
            this.viewLifecycleOwner,
            Observer {
                genres = it
            }
        )

        super.onViewCreated(view, savedInstanceState)
    }

    override fun gotoContent(content: Content) {
        val params = Bundle()
        params.putString(Constants.SCREEN_NAME_KEY, SCREEN_NAME)
        params.putParcelable(CONTENT_KEY, content)
        params.putParcelableArrayList(MOVIE_GENRES_KEY, genres as ArrayList<out Parcelable>)
        gotoMovie(params)
    }

    private fun gotoMovie(params: Bundle) {
        val intent = Intent(activity, MovieActivity::class.java)
        intent.putExtras(params)
        activity?.startActivity(intent)
    }
}
