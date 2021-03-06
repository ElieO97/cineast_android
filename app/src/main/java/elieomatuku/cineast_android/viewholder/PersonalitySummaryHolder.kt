package elieomatuku.cineast_android.viewholder


import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import elieomatuku.cineast_android.R
import elieomatuku.cineast_android.activity.ItemListActivity
import elieomatuku.cineast_android.adapter.PeopleLisAdapter
import elieomatuku.cineast_android.core.model.Content
import elieomatuku.cineast_android.core.model.Personality
import elieomatuku.cineast_android.core.model.Person
import io.reactivex.subjects.PublishSubject
import kotlinx.android.synthetic.main.holder_people.view.*

class PersonalitySummaryHolder(itemView: View, private val onPersonalityClickPublisher: PublishSubject<Person>) : ContentHolder(itemView) {

    companion object {
        fun createView(parent: ViewGroup): View {
            return LayoutInflater.from(parent.context).inflate(R.layout.holder_people, parent, false)
        }

        fun newInstance(parent: ViewGroup, onPersonalityClickPublisher: PublishSubject<Person>): PersonalitySummaryHolder {
            return PersonalitySummaryHolder(createView(parent), onPersonalityClickPublisher)
        }
    }

    private val seeAllView by lazy {
        itemView.see_all
    }

    private val adapter: PeopleLisAdapter by lazy {
        PeopleLisAdapter(onPersonalityClickPublisher)
    }

    private val listView: RecyclerView by lazy {
        itemView.recyclerview_people
    }

    override fun update(content: Pair<Int, List<Content>>) {
        val personalities: List<Personality> = content.second as List<Personality>

        adapter.popularPersonalities = personalities.toMutableList()
        listView.adapter = adapter
        adapter.notifyDataSetChanged()
        listView.layoutManager = LinearLayoutManager(itemView.context, LinearLayoutManager.HORIZONTAL, false)

        seeAllView.setOnClickListener {
            ItemListActivity.startItemListActivity(itemView.context, personalities, R.string.popular_people)
        }
    }
}