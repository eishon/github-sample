package com.arctrix.githubsample.di

import android.content.Context
import coil.ImageLoader
import coil.request.CachePolicy
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import javax.inject.Named

@Module(includes = [OkHttpModule::class])
@InstallIn(SingletonComponent::class)
class ImageLoaderModule {

    @Provides
    fun provideCoilImageLoader(
        @ApplicationContext context: Context,
        @Named("coil") client: OkHttpClient
    ): ImageLoader {
        return ImageLoader.Builder(context)
            .okHttpClient(client)
            .crossfade(true)
            .networkCachePolicy(CachePolicy.ENABLED)
            .diskCachePolicy(CachePolicy.ENABLED)
            .addLastModifiedToFileCacheKey(true)
            .build()
    }
}