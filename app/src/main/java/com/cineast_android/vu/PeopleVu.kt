package com.cineast_android.vu

import android.app.Activity
import android.os.Bundle
import android.os.Parcelable
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.core.content.res.ResourcesCompat
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.appcompat.widget.Toolbar
import android.view.LayoutInflater
import android.view.ViewGroup
import com.cineast_android.R
import com.cineast_android.activity.PeopleActivity
import com.cineast_android.adapter.PeopleItemAdapter
import com.cineast_android.core.model.KnownFor
import com.cineast_android.core.model.Person
import com.cineast_android.core.model.PersonalityDetails
import com.cineast_android.core.model.Poster
import com.cineast_android.fragment.MovieGalleryFragment
import com.cineast_android.fragment.OverviewPeopleFragment
import com.cineast_android.presenter.MovieGalleryPresenter
import com.cineast_android.utils.DividerItemDecorator
import com.cineast_android.utils.UiUtils
import io.chthonic.mythos.mvp.FragmentWrapper
import io.reactivex.Observable
import io.reactivex.subjects.PublishSubject
import kotlinx.android.synthetic.main.vu_movie.view.*
import java.util.ArrayList

class PeopleVu(inflater: LayoutInflater,
               activity: Activity,
               fragmentWrapper: FragmentWrapper?,
               parentView: ViewGroup?) : ToolbarVu(inflater,
        activity = activity,
        fragmentWrapper = fragmentWrapper,
        parentView = parentView) {

    override val toolbar: Toolbar?
        get() = rootView.toolbar

    override fun getRootViewLayoutId(): Int {
        return R.layout.vu_movie
    }

    private val listView: RecyclerView by lazy {
        rootView.list_view_container
    }

    private val onProfileClickedPicturePublisher: PublishSubject<Int> by lazy {
        PublishSubject.create<Int>()
    }

    val onProfileClickedPictureObservable: Observable<Int>
        get() = onProfileClickedPicturePublisher.hide()

    val personPresentedPublisher: PublishSubject<Person>? by lazy {
        if (activity is PeopleActivity) {
            activity.personPresentedPublisher
        } else {
            null
        }
    }

    val adapter: PeopleItemAdapter by lazy {
        PeopleItemAdapter(onProfileClickedPicturePublisher)
    }

    override fun onCreate() {
        super.onCreate()

        if (toolbar != null) {
            UiUtils.initToolbar(activity as AppCompatActivity, toolbar)
        }

        listView.adapter = adapter
        listView.layoutManager = LinearLayoutManager(activity)
    }

    override fun onDestroy() {
        super.onDestroy()
        listView.adapter = null
        listView.layoutManager = null
    }

    fun updateVu(personalityDetails: PersonalityDetails?, screenName: String?, peopleMovies: List<KnownFor>?) {
        if (personalityDetails != null && screenName != null && peopleMovies != null) {
            toolbar?.title = screenName


            adapter.personalityDetails = personalityDetails
            adapter.peopleMovies = peopleMovies.toMutableList()


            val dividerItemDecoration = DividerItemDecorator(ResourcesCompat.getDrawable(activity.resources, R.drawable.item_decoration, activity.theme))
            listView.addItemDecoration(dividerItemDecoration)


            adapter.notifyDataSetChanged()
            initializeFragmentOnPeopleClicked(personalityDetails.biography)
        }
    }


    private fun initializeFragmentOnPeopleClicked(peopleBio: String?) {
        if (peopleBio != null) {
            val initialFragmentOnMovieClicked = OverviewPeopleFragment.newInstance(peopleBio)
            (activity as FragmentActivity).supportFragmentManager.beginTransaction().replace(R.id.fragment_container, initialFragmentOnMovieClicked).commit()
        }
    }

    fun goToGallery(posters: List<Poster>?) {
        val galleryFragment = MovieGalleryFragment.newInstance()
        galleryFragment.arguments = getArgs(posters)
        addFragment(galleryFragment, activity)
    }

    private fun getArgs(posters: List<Poster>?): Bundle {
        val args = Bundle()
        args.putParcelableArrayList(MovieGalleryPresenter.POSTERS, posters as ArrayList<out Parcelable>)
        return args
    }

    private fun addFragment(fragment: Fragment, activity: Activity) {
        val fm = (activity as AppCompatActivity).supportFragmentManager
        if (fragment != null && fm != null) {
            fm.beginTransaction().add(android.R.id.content, fragment, null).addToBackStack(null).commit()
        }
    }

    fun updateErrorView(errorMsg: String?) {
        adapter.errorMessage = errorMsg
        adapter.notifyDataSetChanged()
    }
}