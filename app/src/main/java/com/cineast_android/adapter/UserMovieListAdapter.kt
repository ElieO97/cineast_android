package com.cineast_android.adapter


import com.cineast_android.core.model.Movie
import io.reactivex.subjects.PublishSubject


class UserMovieListAdapter(onItemClickPublisher: PublishSubject<Movie>,
                           itemListLayoutRes: Int? = null, private val onMovieRemovedPublisher: PublishSubject<Movie>? = null)
    : MovieListAdapter(onItemClickPublisher, itemListLayoutRes) {


    fun deleteItem(position: Int) {
        val movie = movies[position]
        onMovieRemovedPublisher?.onNext(movie)
        (movies).removeAt(position)
        notifyItemRemoved(position)  
    }
}