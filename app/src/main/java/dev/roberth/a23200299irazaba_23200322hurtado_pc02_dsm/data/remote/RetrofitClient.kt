package dev.roberth.a23200299irazaba_23200322hurtado_pc02_dsm.data.remote

import dev.roberth.a23200299irazaba_23200322hurtado_pc02_dsm.BuildConfig
import okhttp3.HttpUrl
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

object RetrofitClient {

    private const val BASE_URL_APOD = "https://science.nasa.gov/"
    private const val BASE_URL_NEOWS = "https://api.nasa.gov/"

    private val apiKeyInterceptor = Interceptor { chain ->
        val originalRequest = chain.request()
        val originalHttpUrl: HttpUrl = originalRequest.url

        val newUrl = originalHttpUrl.newBuilder()
            .addQueryParameter("api_key", BuildConfig.API_KEY)
            .build()

        val newRequest = originalRequest.newBuilder()
            .url(newUrl)
            .build()

        chain.proceed(newRequest)
    }

    private val okHttpClient: OkHttpClient by lazy {
        val loggingInterceptor = HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }
        OkHttpClient.Builder()
            .addInterceptor(apiKeyInterceptor)
            .addInterceptor(loggingInterceptor)
            .connectTimeout(30, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .build()
    }

    val apodRetrofit: Retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL_APOD)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    val neoWsRetrofit: Retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL_NEOWS)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    val apodApiService: ApodApiService by lazy {
        apodRetrofit.create(ApodApiService::class.java)
    }

    val neoWsApiService: NeoWsApiService by lazy {
        neoWsRetrofit.create(NeoWsApiService::class.java)
    }
}
