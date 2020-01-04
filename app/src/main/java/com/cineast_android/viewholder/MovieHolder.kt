package com.cineast_android.viewholder

import androidx.recyclerview.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.cineast_android.R
import com.cineast_android.activity.ItemListActivity
import com.cineast_android.adapter.MovieListAdapter
import com.cineast_android.core.model.Content
import com.cineast_android.core.model.Movie
import io.reactivex.subjects.PublishSubject
import kotlinx.android.synthetic.main.holder_movie.view.*
import timber.log.Timber

class MovieHolder(itemView: View, private val onItemClickPublisher: PublishSubject<Movie>) :  ContentHolder(itemView) {
    companion object {
        fun createView(parent: ViewGroup): View {
            return LayoutInflater.from(parent.context).inflate(R.layout.holder_movie, parent, false)
        }

        fun newInstance(parent: ViewGroup, onItemClickPublisher: PublishSubject<Movie>): MovieHolder {
            return MovieHolder(createView(parent), onItemClickPublisher)
        }
    }

    private val sectionTitle by lazy {
        itemView.section_title
    }

    private val seeAllView by lazy {
        itemView.see_all
    }

    private val listView by lazy {
        itemView.recyclerview_popular_movie
    }

    private val adapter: MovieListAdapter by lazy {
        MovieListAdapter(onItemClickPublisher)
    }

    override fun update(content: Pair<Int, List<Content>>) {
        val movies = content.second as List<Movie>
        val contentTitle = content.first

        Timber.d("movies from discover: $movies")

        sectionTitle.text = itemView.context.getString(contentTitle)
        adapter.movies = movies.toMutableList()
        listView.adapter = adapter
        listView.layoutManager = LinearLayoutManager(itemView.context, LinearLayoutManager.HORIZONTAL, false)

        seeAllView.setOnClickListener {
            ItemListActivity.startItemListActivity(itemView.context, movies, contentTitle)
        }
    }
}