package com.arctrix.githubsample.di

import com.arctrix.githubsample.data.api.GitHubApi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit

/**
 * Module for providing network related dependencies
 */
@Module(includes = [RetrofitModule::class])
@InstallIn(SingletonComponent::class)
class NetworkModule {

    @Provides
    fun provideGitHubUserApi(retrofit: Retrofit): GitHubApi {
        return retrofit.create(GitHubApi::class.java)
    }
}