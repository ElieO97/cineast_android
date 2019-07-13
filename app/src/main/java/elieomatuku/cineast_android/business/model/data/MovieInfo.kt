package elieomatuku.cineast_android.business.model.data

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class MovieInfo (val movie: Movie?,
                      val trailers: List<Trailer>?,
                      val movieDetails: MovieDetails?,
                      val genres: List<Genre>?,
                      val screenName: String?,
                      val cast: List<Cast>?,
                      val crew: List<Crew>?,
                      val similarMovies: List<Movie>?) : Parcelable {



    val movieId = movie?.id

}