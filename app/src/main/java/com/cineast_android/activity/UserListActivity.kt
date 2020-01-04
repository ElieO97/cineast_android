package com.cineast_android.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import com.cineast_android.R
import com.cineast_android.presenter.UserListPresenter
import com.cineast_android.utils.UiUtils
import com.cineast_android.vu.UserListVu
import io.chthonic.mythos.mvp.MVPDispatcher
import io.chthonic.mythos.mvp.PresenterCacheLoaderCallback
import timber.log.Timber



class UserListActivity : ToolbarMVPActivity<UserListPresenter, UserListVu>() {
    companion object {
        private val MVP_UID by lazy {
            hashCode()
        }

        const val DISPLAY_FAVORITE_LIST = "favorite_list_key"
        const val DISPLAY_WATCH_LIST = "watch_list_key"

        fun gotoUserListActivity  (context: Context, resources: Int? = null): Intent {
            val intent = Intent (context, UserListActivity::class.java)
            val params = Bundle()

            if (resources != null) {
                params.putInt(UiUtils.SCREEN_NAME_KEY, resources)
            }

            intent.putExtras(params)

            return intent
        }


        fun gotoFavoriteList(context: Context) {
            val intent = gotoUserListActivity(context,  R.string.settings_favorites)
            val params = Bundle()
            params.putBoolean(DISPLAY_FAVORITE_LIST, true)
            intent.putExtras(params)

            context.startActivity(intent)
        }

        fun gotoWatchList(context: Context) {
            val intent = gotoUserListActivity(context, R.string.settings_watchlist)
            val params = Bundle()
            params.putBoolean(DISPLAY_WATCH_LIST, true)
            intent.putExtras(params)


            context.startActivity(intent)
        }

        fun gotoRatedMovies(context: Context) {
            val intent = gotoUserListActivity(context, R.string.settings_rated)
            context.startActivity(intent)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        mvpDispatcher.vu?.toolbar?.let {
            UiUtils.initToolbar(this, it, true)
        }
    }

    override fun createMVPDispatcher(): MVPDispatcher<UserListPresenter, UserListVu> {
        return MVPDispatcher(MVP_UID,
                PresenterCacheLoaderCallback(this, { UserListPresenter() }),
                ::UserListVu)
    }


    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.edit_menu, menu)

        return true
    }

    override fun onPrepareOptionsMenu(menu: Menu?): Boolean {
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        Timber.d("onOptionsItemSelected")

        when (item?.itemId) {
            android.R.id.home -> onSupportNavigateUp()
//            R.id.action_edit -> {
//                onEditMenuClicked()
//            }

            else -> super.onOptionsItemSelected(item)
        }

        return true
    }

    override fun onSupportNavigateUp(): Boolean {
        if (!super.onSupportNavigateUp()) {
            onBackPressed()
        }
        return true
    }

}