package com.cineast_android.vu

import android.app.Activity
import androidx.recyclerview.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.ViewGroup
import com.cineast_android.R
import com.cineast_android.adapter.PeopleAdapter
import com.cineast_android.core.model.Cast
import com.cineast_android.core.model.Crew
import io.chthonic.mythos.mvp.FragmentWrapper
import io.reactivex.Observable
import io.reactivex.subjects.PublishSubject
import kotlinx.android.synthetic.main.fragment_overview.view.*


class MovieTeamVu (inflater: LayoutInflater,
                   activity: Activity,
                   fragmentWrapper: FragmentWrapper?,
                   parentView: ViewGroup?) : BaseVu(inflater,
        activity = activity,
        fragmentWrapper = fragmentWrapper,
        parentView = parentView) {

    override fun getRootViewLayoutId(): Int {
        return R.layout.fragment_overview
    }

    val overviewList by lazy {
        rootView.overview_list
    }

    private val onCrewSelectPublisher: PublishSubject<Crew> by lazy {
        PublishSubject.create<Crew>()
    }

    val onCrewSelectObservable: Observable<Crew>
        get() = onCrewSelectPublisher.hide()

    private val onCastSelectPublisher: PublishSubject<Cast> by lazy {
        PublishSubject.create<Cast>()
    }

    val onCastSelectObservable: Observable<Cast>
        get() = onCastSelectPublisher.hide()

    fun updateVu(cast: List<Cast>, crew: List<Crew>) {
        overviewList.adapter = PeopleAdapter (cast, crew, onCrewSelectPublisher, onCastSelectPublisher)
        overviewList.layoutManager = LinearLayoutManager(activity)
    }
}