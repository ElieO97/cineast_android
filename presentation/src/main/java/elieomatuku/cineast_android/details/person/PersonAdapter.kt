package elieomatuku.cineast_android.details.person

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import elieomatuku.cineast_android.domain.model.PersonDetails
import elieomatuku.cineast_android.viewholder.EmptyStateHolder
import io.reactivex.subjects.PublishSubject
import kotlin.properties.Delegates

class PersonAdapter(
    private val onProfileClickedPicturePublisher: PublishSubject<Int>,
    private val segmentedButtonPublisher: PublishSubject<Pair<String, PersonDetails>>,
    private val onProfileLinkClickedPublisher: PublishSubject<String>
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    companion object {
        const val TYPE_PEOPLE_PROFILE = 0
        const val TYPE_MENU_PEOPLE = 1
        const val TYPE_EMPTY_STATE = -2
    }

    var personDetails: PersonDetails by Delegates.observable(PersonDetails()) { prop, oldPeopleDetails, nuPeopleDetails ->
        hasValidData = true
        errorMessage = null
    }

    private var initialCheckedTab: String = PersonFragment.OVERVIEW

    var hasValidData = false
        private set

    private var _errorMessage: String? = null
    var errorMessage: String?
        get() = _errorMessage
        set(nuErrorMessage) {
            _errorMessage = nuErrorMessage
            hasValidData = true
        }

    private val hasEmptyState: Boolean
        // only display empty state after valid data is set
        get() = hasValidData && (personDetails.isEmpty())

    override fun getItemCount(): Int {
        return if (hasEmptyState) {
            1
        } else {
            2
        }
    }

    override fun getItemViewType(position: Int): Int {
        return if (hasEmptyState) {
            TYPE_EMPTY_STATE
        } else {
            when (position) {
                TYPE_PEOPLE_PROFILE -> TYPE_PEOPLE_PROFILE
                TYPE_MENU_PEOPLE -> TYPE_MENU_PEOPLE
                else -> -1
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            TYPE_PEOPLE_PROFILE -> PersonProfileHolder.newInstance(
                parent,
                onProfileClickedPicturePublisher,
                onProfileLinkClickedPublisher
            )
            TYPE_MENU_PEOPLE -> PersonSegmentedButtonHolder.newInstance(parent)
            TYPE_EMPTY_STATE -> {
                EmptyStateHolder.newInstance(parent)
            }
            else -> throw RuntimeException("View Type does not exist.")
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is EmptyStateHolder -> holder.update(errorMessage)
            is PersonProfileHolder -> holder.update(personDetails)
            is PersonSegmentedButtonHolder -> {
                holder.update(personDetails, initialCheckedTab)

                holder.overviewSegmentBtn.setOnClickListener {
                    segmentedButtonPublisher.onNext(Pair(PersonFragment.OVERVIEW, personDetails))
                }

                holder.knownForSegmentBtn.setOnClickListener {
                    segmentedButtonPublisher.onNext(Pair(PersonFragment.KNOWN_FOR, personDetails))
                }
            }
        }
    }

    override fun onViewRecycled(holder: RecyclerView.ViewHolder) {
        when (holder) {
            is EmptyStateHolder -> holder.composeView.disposeComposition()
            else -> {}
        }
    }
}
