package elieomatuku.cineast_android.database.dao

import androidx.annotation.WorkerThread
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import elieomatuku.cineast_android.database.entity.MovieEntity
import io.reactivex.Flowable
import io.reactivex.Single


/**
 * Created by elieomatuku on 2019-12-06
 */

@Dao
interface MovieDao  {
    @Query("SELECT * from ${MovieEntity.MOVIE_TABLE}")
    fun getAllMovies():  Flowable<List<MovieEntity>>


    @WorkerThread
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertMovie(movie: MovieEntity)


    @WorkerThread
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertMovies(movie: List<MovieEntity>)


    @WorkerThread
    @Query("DELETE FROM ${MovieEntity.MOVIE_TABLE} WHERE id = :id")
    fun delete(id: Int)

    @WorkerThread
    @Query("DELETE FROM ${MovieEntity.MOVIE_TABLE}")
    fun deleteAll()
}

