package elieomatuku.cineast_android.ui.viewholder

import android.text.Spannable
import android.text.style.URLSpan
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import elieomatuku.cineast_android.ui.fragment.WebviewFragment
import elieomatuku.cineast_android.utils.UiUtils
import elieomatuku.cineast_android.utils.WebLink

abstract class ProfileHolder(itemView: View) : RecyclerView.ViewHolder(itemView), WebLink<String> {

    fun linkify(spannable: Spannable): Spannable {
        val spans = spannable.getSpans(0, spannable.length, URLSpan::class.java)
        for (urlSpan in spans) {
            UiUtils.configSpannableLinkify(
                    urlSpan, spannable,
                    object : URLSpan(urlSpan.url) {
                        override fun onClick(view: View) {
                            gotoWebview(url)
                        }
                    }
            )
        }

        return spannable
    }


    override fun gotoWebview(value: String) {
        val webViewFragment: WebviewFragment = WebviewFragment.newInstance(value)
        val fm = (itemView.context as AppCompatActivity).supportFragmentManager
        fm.beginTransaction().add(android.R.id.content, webViewFragment, null).addToBackStack(null).commit()
    }
}
