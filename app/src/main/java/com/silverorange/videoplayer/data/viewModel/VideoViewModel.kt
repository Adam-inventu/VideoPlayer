package com.silverorange.videoplayer.data.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.silverorange.videoplayer.data.model.Video
import com.silverorange.videoplayer.data.repository.VideoRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class VideoViewModel @Inject constructor(
    private val videoRepository: VideoRepository
) : ViewModel() {
    private val _videos = MutableStateFlow(emptyList<Video>())
    val videos: StateFlow<List<Video>> = _videos


    init {
        viewModelScope.launch {
            videoRepository.videos.collect { videos ->
                _videos.value = videos.sortedByDescending { it.publishedAt }
            }
        }
    }

    fun refreshVideos() {
        viewModelScope.launch {
            videoRepository.refreshVideos()
        }
    }

}