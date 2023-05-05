package com.silverorange.videoplayer.data.repository

import android.util.Log
import com.silverorange.videoplayer.data.model.Video
import com.silverorange.videoplayer.data.remote.Resource
import com.silverorange.videoplayer.data.remote.VideoRemoteDataSource
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow


class VideoRepository(
    private val videoRemoteDataSource: VideoRemoteDataSource
) {
    private val mTAG = "VideoRepository"

    private val _videos = MutableStateFlow(emptyList<Video>())
    val videos: StateFlow<List<Video>> = _videos

    suspend fun refreshVideos() {
        val response = videoRemoteDataSource.getVideos()
        if (response.status == Resource.Status.SUCCESS) {
            _videos.value = response.data?.map { it.toVideo() } ?: emptyList()
        } else {
            val message = response.message ?: "an unknown error occurred"
            Log.d(mTAG, message)
        }
    }
}