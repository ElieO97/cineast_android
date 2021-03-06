package elieomatuku.cineast_android.business.client

import android.content.res.Resources
import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import elieomatuku.cineast_android.R
import elieomatuku.cineast_android.core.ValueStore
import elieomatuku.cineast_android.utils.RestUtils
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit


/**
 * Created by elieomatuku on 2019-12-07
 */

interface BaseClient {


    val resources: Resources


    val retrofit: Retrofit
     get() = getRetrofitAdapter()

    val okHttpClient:  OkHttpClient
     get() = buildHttpClient()


    val persistClient: ValueStore

    val interceptor: Interceptor?


    fun getRetrofitAdapter(): Retrofit {
        return Retrofit.Builder()
                .baseUrl(resources.getString(R.string.rest_base_url))
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(CoroutineCallAdapterFactory())
                .client(okHttpClient)
                .build()
    }

    fun buildHttpClient(): OkHttpClient {
        val logging = HttpLoggingInterceptor()
        val logLevel = HttpLoggingInterceptor.Level.BODY
        logging.level = logLevel



        val builder =  OkHttpClient.Builder()
                .addNetworkInterceptor {chain ->
                    val original = chain.request()
                    val url = original.url.newBuilder()
                            .addQueryParameter("api_key", RestUtils.API_KEY)
                            .build()

                    val request = original.newBuilder()
                            .method(original.method, original.body)
                            .url(url)
                            .build()

                     chain.proceed(request)

                }
                .addInterceptor(logging)
                .connectTimeout(30000, TimeUnit.MILLISECONDS)
                .readTimeout(30000, TimeUnit.MILLISECONDS)
                .writeTimeout(30000, TimeUnit.MILLISECONDS)


        return interceptor?.let {
            builder.addInterceptor(it).build()
        } ?: builder.build()

    }

}