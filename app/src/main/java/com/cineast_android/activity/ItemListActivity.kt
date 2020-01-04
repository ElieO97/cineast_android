package com.cineast_android.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Parcelable
import com.cineast_android.core.model.Content
import com.cineast_android.presenter.ItemListPresenter
import com.cineast_android.utils.UiUtils
import com.cineast_android.vu.ItemListVu
import io.chthonic.mythos.mvp.MVPDispatcher
import io.chthonic.mythos.mvp.PresenterCacheLoaderCallback
import java.util.ArrayList


class ItemListActivity : ToolbarMVPActivity<ItemListPresenter, ItemListVu>() {
    companion object {
        private val MVP_UID by lazy {
            hashCode()
        }


        fun startItemListActivity (context: Context, contents: List<Content>, screenNameRes: Int? = null) {
            val intent = Intent (context, ItemListActivity::class.java)
            val params = Bundle()
            params.putParcelableArrayList(UiUtils.WIDGET_KEY, contents as ArrayList<out Parcelable>)

            if (screenNameRes != null) {
                params.putInt(UiUtils.SCREEN_NAME_KEY, screenNameRes)
            }

            intent.putExtras(params)
            context.startActivity(intent)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        mvpDispatcher.vu?.toolbar?.let {
            UiUtils.initToolbar(this, it, true)
        }
    }


    override fun createMVPDispatcher(): MVPDispatcher<ItemListPresenter, ItemListVu> {
        return MVPDispatcher(MVP_UID,
                PresenterCacheLoaderCallback(this, { ItemListPresenter() }),
                ::ItemListVu)
    }

    override fun onSupportNavigateUp(): Boolean {
        if (!super.onSupportNavigateUp()) {
            onBackPressed()
        }
        return true
    }
}