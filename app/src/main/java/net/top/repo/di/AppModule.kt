package net.top.repo.di

import com.google.gson.Gson
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import net.top.repo.api.TopRepoApiService
import net.top.repo.repository.*
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Singleton
    @Provides
    fun provideGson() : Gson {
        return Gson()
    }

    @Singleton
    @Provides
    fun provideTopRepoApiService() : TopRepoApiService {
        return TopRepoApiService.create()
    }

    @Singleton
    @Provides
    fun provideDefaultHomeRepository(
        topRepoApiService: TopRepoApiService
    ) = DefaultHomeRepository(topRepoApiService) as HomeRepository

}