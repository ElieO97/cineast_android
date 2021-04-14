package elieomatuku.cineast_android.ui.details.movie.movie_team

import android.app.Activity
import androidx.recyclerview.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.ViewGroup
import elieomatuku.cineast_android.R
import elieomatuku.cineast_android.core.model.Cast
import elieomatuku.cineast_android.core.model.Crew
import elieomatuku.cineast_android.ui.common_vu.BaseVu
import io.chthonic.mythos.mvp.FragmentWrapper
import io.reactivex.Observable
import io.reactivex.subjects.PublishSubject
import kotlinx.android.synthetic.main.fragment_overview.view.*


class MovieTeamVu(inflater: LayoutInflater,
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
        overviewList.adapter = MovieTeamAdapter(cast, crew, onCrewSelectPublisher, onCastSelectPublisher)
        overviewList.layoutManager = LinearLayoutManager(activity)
    }
}