package com.cineast_android.viewholder

import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.cineast_android.R
import com.cineast_android.activity.ItemListActivity
import com.cineast_android.adapter.CrewAdapter
import com.cineast_android.core.model.Crew
import io.reactivex.subjects.PublishSubject
import kotlinx.android.synthetic.main.holder_people.view.*

class CrewHolder(itemView: View) : RecyclerView.ViewHolder (itemView){
    companion object {
        fun createView(parent: ViewGroup): View {
            return LayoutInflater.from(parent.context).inflate(R.layout.holder_people, parent, false)
        }

        fun newInstance(parent: ViewGroup): CrewHolder {
            return CrewHolder(createView(parent))
        }
    }

    val seeAllView: TextView? by lazy {
        itemView.see_all_title
    }

    fun update(crew: List<Crew>, onCrewClickPublisher: PublishSubject<Crew>) {
        itemView.section_title.text = itemView.context.getString(R.string.crew)
        itemView.recyclerview_people.adapter = CrewAdapter (crew, onCrewClickPublisher)
        itemView.recyclerview_people.layoutManager = LinearLayoutManager(itemView.context, LinearLayoutManager.HORIZONTAL, false)

        seeAllView?.setOnClickListener {
            ItemListActivity.startItemListActivity(itemView.context, crew, R.string.people)
        }
    }
}