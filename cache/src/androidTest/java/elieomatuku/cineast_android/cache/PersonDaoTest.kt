package elieomatuku.cineast_android.cache

import android.content.Context
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import elieomatuku.cineast_android.cache.dao.PersonDao
import elieomatuku.cineast_android.cache.entity.CachePerson
import elieomatuku.cineast_android.domain.model.Person
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

/**
 * Created by elieomatuku on 2019-12-20
 */

@RunWith(AndroidJUnit4::class)
class PersonDaoTest {
    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    private lateinit var contentDatabase: ContentDatabase
    private lateinit var personDao: PersonDao

    @Before
    fun initDb() = runBlocking {
        val context = ApplicationProvider.getApplicationContext<Context>()
        contentDatabase = Room.inMemoryDatabaseBuilder(
            context, ContentDatabase::class.java
        ).build()

        personDao = contentDatabase.personDao()
    }

    @After
    fun closeDb() {
        contentDatabase.close()
    }

    @Test
    fun insertAndGetPersonalities() {
        val personality = Person(id = 1234, adult = true, name = "Eddie Murphy", profilePath = "personality_profile_path")

        personDao.insertPerson(CachePerson.fromPersonEntity(personality))

        personDao.getAllPersonById(personality.id)
            .test()
            .assertValue { it.id == personality.id && it.name == personality.name }
    }
}
