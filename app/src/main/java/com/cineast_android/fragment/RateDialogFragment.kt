package com.cineast_android.fragment

import android.app.Dialog
import android.os.Bundle
import androidx.fragment.app.DialogFragment
import androidx.core.content.ContextCompat
import androidx.appcompat.widget.AppCompatRatingBar
import android.widget.TextView
import com.cineast_android.App
import com.cineast_android.R
import com.cineast_android.business.client.TmdbContentClient
import com.cineast_android.core.model.Movie
import com.cineast_android.business.client.TmdbUserClient
import org.kodein.di.generic.instance


class RateDialogFragment : DialogFragment() {
    companion object {
        const val TAG = "fragment_rate_dialog"
        const val MOVIE_KEY = "movie_key"

        fun newInstance(movie: Movie?): RateDialogFragment {
            val args = Bundle()
            args.putParcelable(MOVIE_KEY, movie)

            val fragment = RateDialogFragment()
            fragment.arguments = args

            return fragment
        }
    }

    private val tmdbUserClient : TmdbUserClient by App.kodein.instance()
    private val tmdbContentClient : TmdbContentClient by App.kodein.instance()
    private var movie: Movie? = null
    private var submitBtn: TextView? = null
    private var ratingBar: AppCompatRatingBar? = null
    private var dialogTitle: TextView? = null

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val inflater = activity?.layoutInflater
        val dialogView = inflater?.inflate(R.layout.fragment_rate_dialog, null)

        dialogTitle = dialogView?.findViewById<TextView>(R.id.dialog_title)

        ratingBar = dialogView?.findViewById<AppCompatRatingBar>(R.id.rating_bar)
        ratingBar?.rating?.let {
            displayRating(it)
        }

        submitBtn = dialogView?.findViewById<TextView>(R.id.dialog_submit)
        movie = arguments?.getParcelable(MOVIE_KEY)


        val dialog = Dialog(activity)

        dialogView?.let {
            dialog.setContentView(it)
        }

        activity?.let {
            dialog.window?.setBackgroundDrawable(ContextCompat.getDrawable(it, R.drawable.bg_rate_dialog))
        }


        onRatingInput()
        onSubmitClick()
        dialog.show()

        return dialog
    }


    private fun onSubmitClick() {
        submitBtn?.setOnClickListener {
            val rating = ratingBar?.rating?.toDouble()

            rating?.let {_rating ->
                movie?.let {
                    tmdbContentClient.postMovieRate(it, _rating)
                }
            }

            dialog?.cancel()
        }
    }

    private fun onRatingInput() {
        ratingBar?.setOnRatingBarChangeListener { ratingBar, rating, fromUser ->
            displayRating(rating)
        }
    }

    private fun displayRating(rating: Float) {
        dialogTitle?.text = String.format("%s %.1f", activity?.getString(R.string.rate_this_movie), rating)
    }
}