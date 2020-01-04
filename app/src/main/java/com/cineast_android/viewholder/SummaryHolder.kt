package com.cineast_android.viewholder

import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.cineast_android.R
import kotlinx.android.synthetic.main.holder_summary.view.*

class SummaryHolder(itemView: View): RecyclerView.ViewHolder (itemView) {
    companion object {
        fun createView(parent: ViewGroup, layoutRes: Int? = null): View {
            return LayoutInflater.from(parent.context).inflate(layoutRes?: R.layout.holder_summary, parent, false)
        }

        fun newInstance(parent: ViewGroup, layoutRes: Int? = null): SummaryHolder {
            return SummaryHolder(createView(parent, layoutRes))
        }
    }

    val summaryTitleView by lazy {
        itemView.summary_title_view
    }

    val summaryView by lazy {
        itemView.summary_view
    }

    fun update(summary: String? = null, summaryTitleRes: Int? = null) {

        summaryTitleRes?.let {
            val summaryTitle: String? = itemView.resources.getString(it)
            if (summaryTitle != null) {
                summaryTitleView.text = summaryTitle
            }
        }


        if (summary != null) {
            summaryView.text = summary
        }
    }
}