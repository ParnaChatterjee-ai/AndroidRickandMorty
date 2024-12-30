package com.example.data.di

import com.apollographql.apollo.ApolloClient
import com.apollographql.apollo.network.okHttpClient
import com.example.data.RickMortConstants
import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import javax.inject.Singleton

@Module
object ApolloClient{
    @Provides
    @Singleton
    fun provideApolloClient(httpClient : OkHttpClient): ApolloClient {
        return ApolloClient.Builder().serverUrl(RickMortConstants.RICK_MORTY_URL)
            .okHttpClient(httpClient)
            .build()
    }

    @Provides
    @Singleton
    fun provideOkHttpClient(): OkHttpClient{
        return OkHttpClient.Builder().addInterceptor(
            HttpLoggingInterceptor().apply {
                level = HttpLoggingInterceptor.Level.BODY
            }
        ).build()
    }
}
