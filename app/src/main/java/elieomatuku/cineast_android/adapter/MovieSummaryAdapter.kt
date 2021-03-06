package elieomatuku.cineast_android.adapter


import androidx.recyclerview.widget.RecyclerView
import android.view.ViewGroup
import elieomatuku.cineast_android.core.model.*
import elieomatuku.cineast_android.viewholder.EmptyStateHolder
import elieomatuku.cineast_android.viewholder.itemHolder.*
import elieomatuku.cineast_android.vu.MovieVu
import io.reactivex.subjects.PublishSubject
import timber.log.Timber
import kotlin.properties.Delegates

class MovieSummaryAdapter(private val onProfileClickedPicturePublisher: PublishSubject<Int>,
                          private val segmentedButtonsPublisher: PublishSubject<Pair<String, MovieSummary>>) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    companion object {
        const val TYPE_MOVIE_PROFILE = 0
        const val TYPE_MENU_MOVIE = 1
        const val TYPE_EMPTY_STATE = -2
    }


    var movieSummary: MovieSummary by Delegates.observable(MovieSummary()) { prop, oldMovieSummary, nuMovieSummary ->
        Timber.d("summary = $nuMovieSummary")
        hasValidData = true
        errorMessage = null
    }

    private var initialCheckedTab: String = MovieVu.MOVIE_OVERVIEW

    var hasValidData = false
        private set


    private var _errorMessage: String? = null
    var errorMessage: String?
        get() = _errorMessage
        set(nuErrorMessage) {
            Timber.d("from MovieListAdapter: $nuErrorMessage")
            _errorMessage = nuErrorMessage
            hasValidData = true
        }

    val hasEmptyState: Boolean
        // only display empty state after valid data is set
        get() = hasValidData && (movieSummary.isEmpty())

    override fun getItemCount(): Int {
        Timber.d("hasEmptyState: $hasEmptyState")
        return if (hasEmptyState) {
            1
        } else {
            2
        }
    }

    override fun getItemViewType(position: Int): Int {
        return if (hasEmptyState) {
            TYPE_EMPTY_STATE

        } else {
            when (position) {
                TYPE_MOVIE_PROFILE -> TYPE_MOVIE_PROFILE
                TYPE_MENU_MOVIE -> TYPE_MENU_MOVIE
                else -> -1
            }
        }
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            TYPE_MOVIE_PROFILE -> ProfileMovieHolder.newInstance(parent, onProfileClickedPicturePublisher)
            TYPE_MENU_MOVIE -> MovieSegmentedButtonHolder.newInstance(parent)
            TYPE_EMPTY_STATE -> EmptyStateHolder.newInstance(parent)
            else -> throw RuntimeException("View Type does not exist.")
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is ProfileMovieHolder) {
            holder.update(movieSummary)
        }

        if (holder is MovieSegmentedButtonHolder) {
            holder.update(movieSummary, initialCheckedTab)

            holder.overviewSegmentBtn.setOnClickListener {
                segmentedButtonsPublisher.onNext(Pair(MovieVu.MOVIE_OVERVIEW, movieSummary))
            }

            holder.peopleSegmentBtn.setOnClickListener {
                segmentedButtonsPublisher.onNext(Pair(MovieVu.MOVIE_CREW, movieSummary))
            }

            holder.similarSegmentBtn.setOnClickListener {
                segmentedButtonsPublisher.onNext(Pair(MovieVu.SIMILAR_MOVIES, movieSummary))
            }
        }

        if (holder is EmptyStateHolder) {
            holder.update(errorMessage)
        }
    }
}
