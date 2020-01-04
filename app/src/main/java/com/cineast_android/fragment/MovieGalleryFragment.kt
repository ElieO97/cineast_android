package com.cineast_android.fragment


import com.cineast_android.presenter.MovieGalleryPresenter
import com.cineast_android.presenter.PresenterCacheLazy
import com.cineast_android.vu.MovieGalleryVu
import io.chthonic.mythos.mvp.MVPDispatcher
import io.chthonic.mythos.mvp.MVPFragment


class MovieGalleryFragment: MVPFragment<MovieGalleryPresenter, MovieGalleryVu>() {
    companion object {
        private val MVP_UID by lazy {
           hashCode()
        }

        fun newInstance(): MovieGalleryFragment{
            return MovieGalleryFragment()
        }
    }


    override fun createMVPDispatcher(): MVPDispatcher<MovieGalleryPresenter, MovieGalleryVu> {
        return MVPDispatcher(MVP_UID,
//                 Using PresenterCacheLazy since PresenterCacheLoaderCallback gives issues where presenter is null in onSaveState
                PresenterCacheLazy({ MovieGalleryPresenter() }),
                ::MovieGalleryVu)
    }
}