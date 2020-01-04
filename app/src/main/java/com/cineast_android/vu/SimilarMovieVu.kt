package com.cineast_android.vu

import android.app.Activity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import com.cineast_android.R
import com.cineast_android.activity.ItemListActivity
import com.cineast_android.adapter.SimilarAdapter
import com.cineast_android.core.model.Movie
import io.chthonic.mythos.mvp.FragmentWrapper
import io.reactivex.Observable
import io.reactivex.subjects.PublishSubject
import kotlinx.android.synthetic.main.holder_movie.view.*


class SimilarMovieVu(inflater: LayoutInflater,
                     activity: Activity,
                     fragmentWrapper: FragmentWrapper?,
                     parentView: ViewGroup?) : BaseVu(inflater,
        activity = activity,
        fragmentWrapper = fragmentWrapper,
        parentView = parentView) {

    override fun getRootViewLayoutId(): Int {
        return R.layout.holder_movie
    }

    private val itemSelectPublisher: PublishSubject<Movie> by lazy {
        PublishSubject.create<Movie>()
    }

    val itemSelectObservable: Observable<Movie>
        get() = itemSelectPublisher.hide()


    private val listView: RecyclerView by lazy {
        rootView.recyclerview_popular_movie
    }

    private val sectionTitleView: TextView by lazy {
        rootView.section_title
    }

    private val seeAllView: TextView by lazy {
        rootView.see_all_title
    }

    override fun onCreate() {
        super.onCreate()
        sectionTitleView.text = activity.getText(R.string.movies)
    }

    override fun onDestroy() {
        super.onDestroy()
        listView.adapter = null
        listView.layoutManager = null
    }


    fun updateVu(similarMovies: List<Movie>) {
        val adapter = SimilarAdapter(similarMovies, itemSelectPublisher)
        listView.adapter = adapter
        listView.layoutManager = LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false)
        adapter.notifyDataSetChanged()

        seeAllView.setOnClickListener {
            ItemListActivity.startItemListActivity(activity, similarMovies, R.string.movies)
        }
    }
}