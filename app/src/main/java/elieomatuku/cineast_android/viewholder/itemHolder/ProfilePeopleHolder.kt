package elieomatuku.cineast_android.viewholder.itemHolder


import androidx.appcompat.widget.AppCompatImageView
import android.text.Html
import android.text.SpannableString
import android.text.method.LinkMovementMethod
import android.text.util.Linkify
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.squareup.picasso.Picasso
import elieomatuku.cineast_android.R
import elieomatuku.cineast_android.core.model.PersonalityDetails
import elieomatuku.cineast_android.utils.UiUtils
import io.reactivex.subjects.PublishSubject
import kotlinx.android.synthetic.main.holder_profile_people.view.*

class ProfilePeopleHolder(itemView: View, private val onProfileClickedPicturePublisher: PublishSubject<Int>): ProfileHolder(itemView){
    companion object {
        fun createView(parent: ViewGroup): View{
            return LayoutInflater.from(parent.context).inflate(R.layout.holder_profile_people, parent, false)
        }

        fun newInstance(parent: ViewGroup, onProfileClickedPicturePublisher: PublishSubject<Int>): ProfilePeopleHolder {
            return ProfilePeopleHolder(createView(parent), onProfileClickedPicturePublisher)
        }
    }

    private val peopleNameView: TextView by lazy {
        itemView.people_name_view
    }

    private val peopleDateOfBirthView: TextView by lazy {
        itemView.people_dateofbirth_view
    }

    private val peoplePlaceofbirthView: TextView by lazy {
        itemView.people_placeofbirth_view
    }

    private val profileImageView: AppCompatImageView by lazy {
        itemView.profile_image
    }

    private val homepageView: TextView by lazy {
        itemView.homepage_view
    }

    fun update(peopleDetails: PersonalityDetails?) {
        val imageUrl: String? =  if (peopleDetails?.profile_path != null) {
            UiUtils.getImageUrl(peopleDetails.profile_path,  itemView.context.getString(R.string.image_small))
        } else null

        if (!imageUrl.isNullOrEmpty()) {
            Picasso.get()
                    .load(imageUrl)
                    .into(profileImageView)
        }

        profileImageView.setOnClickListener {
            peopleDetails?.id?.let {
                onProfileClickedPicturePublisher.onNext(it)
            }
        }

        if (!peopleDetails?.name.isNullOrEmpty()) {
            peopleNameView.visibility = View.VISIBLE
            peopleNameView.text = peopleDetails?.name
        } else {
            peopleNameView.visibility = View.GONE
        }

        val birthDay = peopleDetails?.birthday
        if (birthDay != null) {
            peopleDateOfBirthView.visibility = View.VISIBLE
            peopleDateOfBirthView.text = birthDay
        } else {
            peopleDateOfBirthView.visibility = View.GONE
        }

        if (!peopleDetails?.place_of_birth.isNullOrEmpty()) {
            peoplePlaceofbirthView.visibility = View.VISIBLE
            peoplePlaceofbirthView.text = peopleDetails?.place_of_birth
        } else {
            peoplePlaceofbirthView.visibility = View.GONE
        }


        if (!peopleDetails?.homepage.isNullOrEmpty() ) {
            homepageView.visibility = View.VISIBLE
            val spannable = SpannableString(Html.fromHtml(peopleDetails?.homepage))
            Linkify.addLinks(spannable, Linkify.WEB_URLS)
            homepageView.setMovementMethod(LinkMovementMethod.getInstance())
            homepageView.setText(linkify(spannable), TextView.BufferType.SPANNABLE)

        } else {
            homepageView.visibility = View.GONE
        }

    }
}