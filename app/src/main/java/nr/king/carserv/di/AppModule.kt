package nr.king.carserv.di

import android.content.Context
import android.util.Log
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import nr.king.carserv.BuildConfig
import nr.king.carserv.common.Common
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Response
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.*
import java.util.concurrent.TimeUnit

@Module
@InstallIn(SingletonComponent::class)
class AppModule {

    @Provides
    fun logger() = HttpLoggingInterceptor().apply { level = HttpLoggingInterceptor.Level.BODY }

    @Provides
    fun client(logger: HttpLoggingInterceptor, @ApplicationContext context: Context): OkHttpClient {
        val preferenceManager = PreferenceManager(context)
        val httpClient = OkHttpClient.Builder().apply {
            connectTimeout(20, TimeUnit.SECONDS)
            readTimeout(20, TimeUnit.SECONDS)
        }
        if (BuildConfig.DEBUG) httpClient.addInterceptor(logger) //enabling only in debug build
        httpClient.addInterceptor(HeaderInterceptor(context))
        return httpClient.build()

    }

    @Provides
    fun serialization() = GsonConverterFactory.create()

    @URL_TRACK
    @Provides
    fun retrofitBuilderApi(client: OkHttpClient, factory: GsonConverterFactory): GetHomeInterface =
        Retrofit.Builder()
            .client(client)
            .baseUrl("http://192.168.43.198:8082/")
            .addConverterFactory(factory)
            .build()
            .create(GetHomeInterface::class.java)



    @Provides
    fun getCalendar() = Calendar.getInstance()

    @Provides
    fun preference(@ApplicationContext context: Context) = PreferenceManager(context)

}


 class HeaderInterceptor(private val context: Context) : Interceptor {
    private val HEADER_TOKEN = "X-Auth-Token"
     var TAG="HeaderInterceptor"

    override fun intercept(chain: Interceptor.Chain): Response {
        val preferenceManager = PreferenceManager(context)
        val modRequest = chain.request().newBuilder()
        modRequest.addHeader(HEADER_TOKEN, PreferenceManager(context).getAuthToken())
        val response = chain.proceed(modRequest.build())
        Log.d(TAG, "intercept: ${response.code}||${response.headers}")
        for (item in response.headers)
        {
            if (item.first.equals("X-Auth-Token"))
            {
                Common.isTokenGetted = true
                preferenceManager.setAuthToken(response.headers[HEADER_TOKEN] ?:"")
                break
            }
        }

        return  response
    }
}