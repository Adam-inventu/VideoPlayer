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

    private val _currentVideo: MutableStateFlow<Video?> = MutableStateFlow(null)
    val currentVideo: StateFlow<Video?> = _currentVideo

    init {
        viewModelScope.launch {
            videoRepository.videos.collect { videos ->
                _videos.value = videos.sortedByDescending { it.publishedAt }
                _currentVideo.value = _videos.value.firstOrNull()
            }
        }
    }

    fun refreshVideos() {
        viewModelScope.launch {
            videoRepository.refreshVideos()
        }
    }

    fun getNextVideo() {
        val count = videos.value.size
        if (count > 0) {
            val index = (videos.value.indexOf(currentVideo.value) + 1) % count
            _currentVideo.value = videos.value[index]
        }
    }

    fun getPreviousVideo() {
        val count = videos.value.size
        if (count > 0) {
            var index = videos.value.indexOf(currentVideo.value) - 1
            if (index < 0) index = count - 1
            _currentVideo.value = videos.value[index]
        }
    }

}