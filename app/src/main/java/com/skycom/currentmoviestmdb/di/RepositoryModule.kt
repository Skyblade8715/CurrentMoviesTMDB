package com.skycom.currentmoviestmdb.di

import com.skycom.currentmoviestmdb.data.repository.LikedMoviesRepositoryImpl
import com.skycom.currentmoviestmdb.data.repository.MovieDetailsRepositoryImpl
import com.skycom.currentmoviestmdb.data.repository.NowPlayingRepositoryImpl
import com.skycom.currentmoviestmdb.domain.repository.LikedMoviesRepository
import com.skycom.currentmoviestmdb.domain.repository.MovieDetailsRepository
import com.skycom.currentmoviestmdb.domain.repository.NowPlayingRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    @Singleton
    abstract fun bindNowPlayingRepository (
        nowPlayingRepository: NowPlayingRepositoryImpl
    ) : NowPlayingRepository

    @Binds
    @Singleton
    abstract fun bindMovieDetailsRepository (
        movieDetailsRepository: MovieDetailsRepositoryImpl
    ) : MovieDetailsRepository

    @Binds
    @Singleton
    abstract fun bindLikedMoviesRepository (
        likedMoviesRepository: LikedMoviesRepositoryImpl
    ) : LikedMoviesRepository
}