package com.silverorange.videoplayer.data.model

data class VideoDto (
    val author: Author,
    val description: String,
    val fullURL: String,
    val hlsURL: String,
    val id: String,
    val publishedAt: String,
    val title: String
) {
    fun toVideo() : Video {
        return Video(
            author = author,
            description = description,
            fullURL = fullURL,
            hlsURL = hlsURL,
            id = id,
            publishedAt = publishedAt,
            title = title
        )
    }
}