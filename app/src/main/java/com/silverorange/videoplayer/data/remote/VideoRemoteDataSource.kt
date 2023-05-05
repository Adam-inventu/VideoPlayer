package com.silverorange.videoplayer.data.remote

import com.silverorange.videoplayer.data.model.VideoDto
import com.silverorange.videoplayer.network.RetrofitService
import javax.inject.Inject

data class Resource<out T>(
    val status: Status,
    val data: T?,
    val message: String?
) {
    enum class Status {
        SUCCESS,
        ERROR,
    }

    companion object {
        fun <T> success(data: T): Resource<T> {
            return Resource(Status.SUCCESS, data, null)
        }

        fun <T> error(
            message: String,
            data: T? = null,
        ): Resource<T> {
            return Resource(Status.ERROR, data, message)
        }

    }
}

class VideoRemoteDataSource
@Inject constructor(
    private val retrofitService: RetrofitService
) {
    suspend fun getVideos(): Resource<List<VideoDto>?> {
        return try {
            val response = retrofitService.getVideos()
            if (response.isSuccessful) {
                val body = response.body()
                Resource.success(body)
            } else {
                Resource.error(response.message(), null)
            }
        } catch (e: Exception) {
            Resource.error(e.message ?: e.toString())
        }

    }
}