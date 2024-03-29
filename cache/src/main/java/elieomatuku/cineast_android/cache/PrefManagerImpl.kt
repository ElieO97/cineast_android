package elieomatuku.cineast_android.cache

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import elieomatuku.cineast_android.data.PrefManager

/**
 * Created by elieomatuku on 2021-09-01
 */

class PrefManagerImpl(storeKey: String, appContext: Application) : PrefManager {

    private val prefs: SharedPreferences by lazy {
        appContext.getSharedPreferences(storeKey, Context.MODE_PRIVATE)
    }

    override fun set(key: String, value: String) {
        prefs.edit().putString(key, value).apply()
    }

    override fun get(key: String, fallback: String?): String? {
        return prefs.getString(key, fallback)
    }

    override fun remove(key: String) {
        prefs.edit().remove(key).apply()
    }
}
