package elieomatuku.cineast_android.vu

import android.app.Activity
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import elieomatuku.cineast_android.R
import elieomatuku.cineast_android.adapter.MoviesAdapter
import elieomatuku.cineast_android.adapter.PeopleLisAdapter
import elieomatuku.cineast_android.core.model.Movie
import elieomatuku.cineast_android.core.model.Person
import elieomatuku.cineast_android.core.model.Content
import io.chthonic.mythos.mvp.FragmentWrapper
import io.reactivex.Observable
import io.reactivex.subjects.PublishSubject


class ItemListVu(inflater: LayoutInflater, activity: Activity, fragmentWrapper: FragmentWrapper?, parentView: ViewGroup?) :
        ListVu(inflater, activity = activity, fragmentWrapper = fragmentWrapper, parentView = parentView) {


    override val adapter: RecyclerView.Adapter<*>
        get() = moviesAdapter

    private val personSelectPublisher: PublishSubject<Person> by lazy {
        PublishSubject.create<Person>()
    }

    val personSelectObservable: Observable<Person>
        get() = personSelectPublisher.hide()

    private val moviesAdapter: MoviesAdapter by lazy {
        MoviesAdapter(movieSelectPublisher, R.layout.holder_movie_list)
    }

    private val popularPeopleItemAdapter: PeopleLisAdapter by lazy {
        PeopleLisAdapter(personSelectPublisher, R.layout.holder_people_list)
    }


    override fun setUpListView(contents: List<Content>) {
        if (areWidgetsMovies(contents)) {
            moviesAdapter.movies = contents as MutableList<Movie>
            listView.adapter = moviesAdapter
            moviesAdapter.notifyDataSetChanged()

        } else {
            popularPeopleItemAdapter.popularPersonalities = contents as MutableList<Person>
            listView.adapter = popularPeopleItemAdapter
            popularPeopleItemAdapter.notifyDataSetChanged()
        }
    }


    private fun areWidgetsMovies(contents: List<Content?>): Boolean {
        return (contents[FIRST_WIDGET_TYPE_OCCURENCE] != null) && (contents[FIRST_WIDGET_TYPE_OCCURENCE] is Movie)
    }
}