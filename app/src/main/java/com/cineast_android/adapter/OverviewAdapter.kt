package com.cineast_android.adapter

import androidx.recyclerview.widget.RecyclerView
import android.view.ViewGroup
import com.cineast_android.R
import com.cineast_android.core.model.Movie
import com.cineast_android.core.model.MovieFacts
import com.cineast_android.core.model.Trailer
import com.cineast_android.viewholder.SummaryHolder
import com.cineast_android.viewholder.itemHolder.BottomHolder
import com.cineast_android.viewholder.itemHolder.MovieFactsHolder
import com.cineast_android.viewholder.itemHolder.TrailersHolder

class OverviewAdapter(private val movie: Movie, private val trailers: List<Trailer>, private val movieFacts: MovieFacts)
    : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    companion object {
        const val TYPE_PLOT_SUMMARY = 0
        const val TYPE_TRAILERS = 1
        const val TYPE_MOVIE_FACTS = 2
        const val TYPE_BOTTOM = 3
    }


    override fun getItemCount(): Int {
        return 4
    }

    override fun getItemViewType(position: Int): Int {
        return when (position) {
            TYPE_PLOT_SUMMARY -> TYPE_PLOT_SUMMARY
            TYPE_TRAILERS -> TYPE_TRAILERS
            TYPE_MOVIE_FACTS -> TYPE_MOVIE_FACTS
            TYPE_BOTTOM -> TYPE_BOTTOM
            else -> -1
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            TYPE_PLOT_SUMMARY -> SummaryHolder.newInstance(parent)
            TYPE_TRAILERS -> TrailersHolder.newInstance(parent)
            TYPE_MOVIE_FACTS -> MovieFactsHolder.newInstance(parent)
            TYPE_BOTTOM -> BottomHolder.newInstance(parent)
            else -> throw RuntimeException("View Type does not exist.")
        }
    }


    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val viewType = getItemViewType(position)
        when (viewType) {
            TYPE_PLOT_SUMMARY -> {
                val plotSummaryHolder = holder as SummaryHolder
                plotSummaryHolder.update(movie.overview, R.string.plot_summary)
            }

            TYPE_TRAILERS -> {
                val trailersHolder = holder as TrailersHolder
                trailersHolder.update(trailers)
            }

            TYPE_MOVIE_FACTS -> {
                val movieFactsHolder = holder as MovieFactsHolder
                movieFactsHolder.update(movieFacts)
            }
        }
    }
}
