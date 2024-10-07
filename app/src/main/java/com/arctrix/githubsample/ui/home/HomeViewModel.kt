package com.arctrix.githubsample.ui.home

import androidx.lifecycle.ViewModel
import com.arctrix.githubsample.data.repository.GitHubRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    gitHubRepository: GitHubRepository
) : ViewModel() {
}