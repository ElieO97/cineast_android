package elieomatuku.cineast_android

import android.app.Application
import android.content.Context
import android.content.res.Resources
import elieomatuku.cineast_android.business.client.TmdbContentClient
import elieomatuku.cineast_android.business.service.ConnectionService
import elieomatuku.cineast_android.business.ContentManager
import elieomatuku.cineast_android.business.client.TmdbUserClient
import org.kodein.di.Kodein
import org.kodein.di.generic.bind
import org.kodein.di.generic.instance
import org.kodein.di.generic.singleton


fun depInjecT(app: Application): Kodein {

    return Kodein.lazy {
        bind<Application>() with instance(app)
        bind<Context>() with instance(app.applicationContext)
        bind<Resources>() with instance(app.applicationContext.resources)
        bind<TmdbContentClient>() with singleton { TmdbContentClient(instance(), instance())}
        bind<ContentManager>() with singleton { ContentManager(instance(), instance()) }
        bind<TmdbUserClient>() with singleton {
            TmdbUserClient(instance(), instance())
        }
        bind<ConnectionService>() with singleton { ConnectionService(instance()) }


        importOnce(DatabaseKodeinModule.getModule(app))
    }

}