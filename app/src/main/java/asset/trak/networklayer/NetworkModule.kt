package asset.trak.networklayer

import asset.trak.utils.LogUtils
import asset.trak.networklayer.retrofitutils.JSONConverterFactory
import asset.trak.utils.AESUtils
import com.squareup.moshi.Moshi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.*
import okhttp3.logging.HttpLoggingInterceptor
import okhttp3.sse.EventSource
import okhttp3.sse.EventSources
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Named
import javax.inject.Singleton

private const val TAG = "NetworkModule"

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    private val moshi = Moshi.Builder().build()
    private val moshiConverterFactory = MoshiConverterFactory.create(moshi)
    private val jsonConverterFactory = JSONConverterFactory.create()

    @Provides
    @Singleton
    fun provideOkHttpClient(): OkHttpClient {
        val httpClient = OkHttpClient.Builder()
        httpClient.connectTimeout(120, TimeUnit.MINUTES)
        httpClient.readTimeout(120, TimeUnit.MINUTES)
        httpClient.writeTimeout(120, TimeUnit.MINUTES)
        httpClient.addInterceptor(RequestInterceptor())
        httpClient.addInterceptor(BasicAuthInterceptor())
        httpClient.addInterceptor(ErrorInterceptor())
        httpClient.addInterceptor(requestLogger)
        return httpClient.build()
    }

    private val requestLogger =
        HttpLoggingInterceptor { message ->
            LogUtils.logD(NetworkModule::class.java.simpleName, message)
        }.setLevel(HttpLoggingInterceptor.Level.BODY)

    class RequestInterceptor : Interceptor {
        override fun intercept(chain: Interceptor.Chain): Response {
            var request = chain.request()
            request = request.newBuilder()
                .addHeader("Content-Type", "ApplicationRfid/json")
                .addHeader("Accept", "ApplicationRfid/json")
                .build()
            return chain.proceed(request)
        }
    }

    class ErrorInterceptor : Interceptor {
        override fun intercept(chain: Interceptor.Chain): Response {
            val request: Request = chain.request()
            val response = chain.proceed(request)
            when (response.code) {
                400 -> {
                    LogUtils.logD(TAG, "Bad Request Error ")
                }
                401 -> {
                    LogUtils.logD(TAG, "UnauthorizedError")
                }

                403 -> {
                    LogUtils.logD(TAG, "Forbidden Message")
                }

                404 -> {
                    LogUtils.logD(TAG, "NotFound Message")
                }
            }
            return response
        }
    }

    @Provides
    @Singleton
    fun provideEventSourceFactory(okHttpClient: OkHttpClient): EventSource.Factory {
        val noReadTimeoutClient = okHttpClient.newBuilder()
            .readTimeout(60, TimeUnit.SECONDS)
            .build()
        return EventSources.createFactory(noReadTimeoutClient)
    }

    @Provides
    @Named("AssetTrackApi")
    @Singleton
    fun provideHerCircleAPI(
        okHttpClient: OkHttpClient,
    ): Retrofit = Retrofit.Builder()
        .baseUrl("https://resqqa.ril.com/RFIDService/AssetTracker/")
        .addConverterFactory(jsonConverterFactory)
        .addConverterFactory(moshiConverterFactory)
        .client(okHttpClient)
        .build()

    @Provides
    @Singleton
    fun providerOfHerCircleAPI(@Named("AssetTrackApi") retrofit: Retrofit): AssetTrakAPIInterface {
        return retrofit.create(AssetTrakAPIInterface::class.java)
    }


    class BasicAuthInterceptor : Interceptor {
        override fun intercept(chain: Interceptor.Chain): Response {
            val request = chain.request()
            val authenticatedRequest = request.newBuilder()
                .header("Authorization",
                    Credentials.basic("uname","pwd")).build()
            return chain.proceed(authenticatedRequest)
        }
    }
}