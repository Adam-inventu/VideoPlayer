package com.silverorange.videoplayer.network

import com.silverorange.videoplayer.data.model.VideoDto
import retrofit2.Response
import retrofit2.http.*

interface RetrofitService {
    @GET("/videos")
    @Headers("Content-Type: application/json")
    suspend fun getVideos(
    ): Response<List<VideoDto>>
}

