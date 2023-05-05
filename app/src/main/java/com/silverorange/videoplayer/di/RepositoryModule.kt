package com.silverorange.videoplayer.di

import com.silverorange.videoplayer.data.remote.VideoRemoteDataSource
import com.silverorange.videoplayer.data.repository.VideoRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {

    @Provides
    @Singleton
    fun provideVideoRepository(
        dataSource: VideoRemoteDataSource
    ): VideoRepository {
        return VideoRepository(dataSource)
    }
}