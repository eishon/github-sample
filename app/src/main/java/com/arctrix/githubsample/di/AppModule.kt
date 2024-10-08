package com.arctrix.githubsample.di

import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Named
import javax.inject.Singleton

/**
 * Module for providing Application related dependencies
 */
@Module
@InstallIn(SingletonComponent::class)
class AppModule {

    @Provides
    @Named("app_name")
    fun providesPackageTitle(): String {
        return "GitHub Sample"
    }

    @Provides
    @Singleton
    fun provideMoshi(): Moshi {
        return Moshi.Builder()
            .add(KotlinJsonAdapterFactory())
            .build()
    }
}