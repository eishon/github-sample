package com.arctrix.githubsample.di

import com.arctrix.githubsample.data.api.GitHubApi
import com.arctrix.githubsample.data.repository.GitHubRepository
import com.arctrix.githubsample.data.repository.impl.GitHubRepositoryImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module(includes = [NetworkModule::class])
@InstallIn(SingletonComponent::class)
class RepositoryModule {

    @Provides
    fun provideGitHubRepository(gitHubUserApi: GitHubApi): GitHubRepository {
        return GitHubRepositoryImpl(gitHubUserApi)
    }
}