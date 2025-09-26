package com.srg.zara.di

import android.app.Application
import android.content.Context
import android.content.res.Resources
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.srg.data.zara.network.RetrofitBase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.Cache
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

private const val READ_TIMEOUT = 15L
private const val WRITE_TIMEOUT = 45L

@InstallIn(SingletonComponent::class)
@Module
class AppModule {

    @Provides
    @Singleton
    fun retrofit(retrofitBase: RetrofitBase): Retrofit = retrofitBase.retrofit

    @Provides
    @Singleton
    fun resources(application: Application): Resources = application.resources

    @Provides
    @Singleton
    fun gson(): Gson = GsonBuilder().create()

    @Provides
    @Singleton
    fun provideCache(@ApplicationContext context: Context): Cache {
        val cacheSize = (10 * 1024 * 1024).toLong()
        return Cache(context.cacheDir, cacheSize)
    }

    @Provides
    @Singleton
    fun okHttpClient(cache: Cache, @ApplicationContext context: Context): OkHttpClient {
        val builder = OkHttpClient.Builder()
        val interceptor = HttpLoggingInterceptor()
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY)
        return builder
            .addInterceptor(interceptor)
            .cache(cache)
            .addInterceptor { chain ->
                var request = chain.request()
                request = if (hasNetwork(context)) {
                    request.newBuilder().header("Cache-Control", "public, max-age=60").build()
                } else {
                    request.newBuilder()
                        .header("Cache-Control", "public, only-if-cached, max-stale=604800").build()
                }
                chain.proceed(request)
            }
            .readTimeout(READ_TIMEOUT, TimeUnit.SECONDS)
            .writeTimeout(WRITE_TIMEOUT, TimeUnit.SECONDS)
            .build()
    }

    private fun hasNetwork(context: Context): Boolean {
        val connectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeNetwork = connectivityManager.activeNetwork ?: return false
        val networkCapabilities =
            connectivityManager.getNetworkCapabilities(activeNetwork) ?: return false
        return when {
            networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
            networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
            networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> true
            networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_BLUETOOTH) -> true
            else -> false
        }
    }

}