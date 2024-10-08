package com.arctrix.githubsample.di

import android.content.Context
import com.arctrix.githubsample.util.interceptors.NetworkConnectionInterceptor
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.Cache
import okhttp3.Dispatcher
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import java.io.File
import java.util.concurrent.TimeUnit
import javax.inject.Named

/**
 * Module for providing different network clients
 */
@Module(includes = [AppModule::class])
@InstallIn(SingletonComponent::class)
class OkHttpModule {

    private val MB: Long = 1024 * 1024

    @Provides
    fun provideHttpApiCache(@ApplicationContext context: Context): Cache {
        val cacheDir = File(context.cacheDir, "apiCache")
        val cacheSize: Long = 5 * MB
        return Cache(cacheDir, cacheSize)
    }

    @Provides
    @Named("coil")
    fun provideCoilCache(@ApplicationContext context: Context): Cache {
        val cacheDir = File(context.cacheDir, "apiCache")
        val cacheSize: Long = 50 * MB
        return Cache(cacheDir, cacheSize)
    }

    @Provides
    fun provideOkHttpClient(
        @ApplicationContext context: Context,
        cache: Cache
    ): OkHttpClient = OkHttpClient
        .Builder()
        .dispatcher(Dispatcher().apply { maxRequests = 5 })
        .connectTimeout(20, TimeUnit.SECONDS)
        .readTimeout(20, TimeUnit.SECONDS)
        .addInterceptor {
            it.proceed(it.request().newBuilder().build())
        }
        .addInterceptor(HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        })
        .addInterceptor(NetworkConnectionInterceptor(context))
        .cache(cache)
        .build()

    @Provides
    @Named("coil")
    fun provideCoilOkHttpClient(
        @ApplicationContext context: Context,
        @Named("coil") cache: Cache
    ): OkHttpClient = OkHttpClient
        .Builder()
        .dispatcher(Dispatcher().apply { maxRequests = 5 })
        .connectTimeout(20, TimeUnit.SECONDS)
        .readTimeout(20, TimeUnit.SECONDS)
        .addInterceptor {
            it.proceed(it.request().newBuilder().build())
        }
        .addInterceptor(HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        })
        .addInterceptor(NetworkConnectionInterceptor(context))
        .cache(cache)
        .build()
}