package elieomatuku.cineast_android.viewholder

import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import elieomatuku.cineast_android.R
import elieomatuku.cineast_android.activity.ItemListActivity
import elieomatuku.cineast_android.adapter.CastAdapter
import elieomatuku.cineast_android.core.model.Cast
import io.reactivex.subjects.PublishSubject
import kotlinx.android.synthetic.main.holder_people.view.*

class CastHolder(itemView: View) : RecyclerView.ViewHolder (itemView){
    companion object {
        fun createView(parent: ViewGroup): View{
            return LayoutInflater.from(parent.context).inflate(R.layout.holder_people, parent, false)
        }

        fun newInstance(parent: ViewGroup): CastHolder {
            return CastHolder(createView(parent))
        }
    }

    val seeAllView: TextView? by lazy {
        itemView.see_all_title
    }

    fun update(cast: List<Cast>, onCastClickPublisher: PublishSubject<Cast>) {
        itemView.section_title.text = itemView.context.getString(R.string.cast)
        itemView.recyclerview_people.adapter = CastAdapter (cast, onCastClickPublisher)
        itemView.recyclerview_people.layoutManager = LinearLayoutManager(itemView.context, LinearLayoutManager.HORIZONTAL, false)


        seeAllView?.setOnClickListener {
            ItemListActivity.startItemListActivity(itemView.context, cast, R.string.people)
        }

    }
}