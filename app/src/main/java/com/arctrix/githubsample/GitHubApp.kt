package com.arctrix.githubsample

import android.app.Application
import coil.ImageLoader
import coil.ImageLoaderFactory
import dagger.hilt.android.HiltAndroidApp
import javax.inject.Inject

@HiltAndroidApp
open class GitHubApp : Application(), ImageLoaderFactory {
    @Inject
    lateinit var coilImageLoader: ImageLoader
    override fun newImageLoader(): ImageLoader {
        return coilImageLoader
    }
}