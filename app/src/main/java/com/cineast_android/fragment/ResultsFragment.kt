package com.cineast_android.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.cineast_android.R
import com.cineast_android.core.model.Content

class ResultsFragment(): Fragment() {
    companion object {
        val LOG_TAG = ResultsFragment::class.java.simpleName

        fun newInstance(contents: List<Content>): ResultsFragment{
            val fragment =  ResultsFragment()
            val args = Bundle()
            fragment.arguments = args
            return fragment
        }
    }


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.vu_results, container, false)
        return view
    }

}