package com.arctrix.githubsample.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Named
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
}