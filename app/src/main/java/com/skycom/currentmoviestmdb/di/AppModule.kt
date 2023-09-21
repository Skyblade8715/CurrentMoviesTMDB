package com.skycom.currentmoviestmdb.di

import android.content.Context
import android.content.SharedPreferences
import com.skycom.currentmoviestmdb.data.repository.LikedMoviesRepositoryImpl
import com.skycom.currentmoviestmdb.data.repository.MovieDetailsRepositoryImpl
import com.skycom.currentmoviestmdb.data.repository.NowPlayingRepositoryImpl
import com.skycom.currentmoviestmdb.data.source.TMDBApi
import com.skycom.currentmoviestmdb.domain.use_cases.AddLikedMovie
import com.skycom.currentmoviestmdb.domain.use_cases.DeleteLikedMovie
import com.skycom.currentmoviestmdb.domain.use_cases.GetAllLikedMovies
import com.skycom.currentmoviestmdb.domain.use_cases.GetMovieDetails
import com.skycom.currentmoviestmdb.domain.use_cases.GetNowPlayingMovies
import com.skycom.currentmoviestmdb.domain.use_cases.SearchMovies
import com.skycom.currentmoviestmdb.domain.use_cases.UseCaseMovies
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideTMDBApi() : TMDBApi {
        val headerInterceptor = Interceptor { chain ->
            val request = chain.request().newBuilder()
                .addHeader("Accept", "application/json")
                .addHeader("Authorization", "Bearer eyJhbGciOiJIUzI1NiJ9.eyJhdWQiOiJjYWI1OTc4NmZhYzRhMWFhNWFjYTBmODgwOGYxNDQ3YyIsInN1YiI6IjY1MDFhZWIxZmZjOWRlMGVlM2M2NmIyMyIsInNjb3BlcyI6WyJhcGlfcmVhZCJdLCJ2ZXJzaW9uIjoxfQ.bPgUVihx_TzMZXCUZ-L0r6txb-65KCeioIysxpPMbl4")
                .build()
            return@Interceptor chain.proceed(request)
        }
        val okHttpClient = OkHttpClient.Builder()
            .addInterceptor(headerInterceptor)
            .build()
        return Retrofit.Builder()
            .baseUrl("https://api.themoviedb.org/3/")
            .addConverterFactory(GsonConverterFactory.create())
            .client(okHttpClient)
            .build()
            .create(TMDBApi::class.java)
    }

    @Provides
    @Singleton
    fun provideSharedPreferences(@ApplicationContext context: Context): SharedPreferences {
        return context.getSharedPreferences("my_prefs", Context.MODE_PRIVATE)
    }

    @Provides
    @Singleton
    fun provideMovieUseCases(
        nowPlayingRepository: NowPlayingRepositoryImpl,
        movieDetailsRepository: MovieDetailsRepositoryImpl,
        likedMoviesRepository: LikedMoviesRepositoryImpl
    ) : UseCaseMovies {
        return UseCaseMovies(
            addLikedMovie = AddLikedMovie(likedMoviesRepository),
            deleteLikedMovie = DeleteLikedMovie(likedMoviesRepository),
            getAllLikedMovies = GetAllLikedMovies(likedMoviesRepository),
            getMovieDetails = GetMovieDetails(movieDetailsRepository),
            getNowPlayingMovies = GetNowPlayingMovies(nowPlayingRepository),
            searchMovies = SearchMovies(nowPlayingRepository)
        )
    }
}