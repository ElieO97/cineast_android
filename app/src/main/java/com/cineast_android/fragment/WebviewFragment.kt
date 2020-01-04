package com.cineast_android.fragment



import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebView
import com.cineast_android.R
import com.cineast_android.utils.UiUtils
import kotlinx.android.synthetic.main.fragment_webview.view.*
import android.webkit.WebViewClient


open class WebviewFragment() : Fragment() {
    companion object {
        const val URL = "url"

        fun newInstance(url: String?): WebviewFragment {
            val fragment = WebviewFragment()
            val args = Bundle()
            url?.let {
                args.putString(URL, it)
            }
            fragment.arguments = args
            return fragment
        }
    }


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_webview, container, false)
        val url = arguments?.getString(URL)

        val progressBar = view.web_progress

        val webview by lazy {
            val webv = view.web_html_widget
            UiUtils.configureWebView(webv, progressBar)
        }

        webview.setWebViewClient(object: WebViewClient() {
            override fun shouldOverrideUrlLoading(view: WebView?, url: String?): Boolean {
                view?.loadUrl(url)
                return true
            }
        })

        if (!url.isNullOrEmpty()) {
             webview.loadUrl(url)
        }

        view.html_close_icon.setOnClickListener { view ->
            closeIconListener()
        }

        return view
    }

     open fun closeIconListener() {
        val fm = activity?.supportFragmentManager
        if (fm != null) {
            fm.popBackStack()
        }
    }
}