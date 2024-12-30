package com.example.data.module

import com.apollographql.apollo.ApolloClient
import com.example.data.repository.CharacterRepositoryImpl
import com.example.data.repository.EpisodeRepositoryImpl
import com.example.domain.repository.CharacterRepository
import com.example.domain.repository.EpisodeRepository
import com.example.domain.usecases.GetCharacterDetailsUsecase
import com.example.domain.usecases.GetCharactersUsecase
import com.example.domain.usecases.GetEpisodeDetailsUsecase
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
object DataModule {
    @Provides
    @Singleton
    fun provideCharacterRepository(apolloClient: ApolloClient): CharacterRepository {
        return CharacterRepositoryImpl(apolloClient)
    }

    @Provides
    @Singleton
    fun provideEpisodeRepository(apolloClient: ApolloClient): EpisodeRepository {
        return EpisodeRepositoryImpl(apolloClient)
    }

    @Provides
    @Singleton
    fun provideGetCharactersUseCase(repository: CharacterRepository): GetCharactersUsecase {
        return GetCharactersUsecase(repository)
    }

    @Provides
    @Singleton
    fun provideGetCharacterDetailsUsecase(repository: CharacterRepository): GetCharacterDetailsUsecase{
        return GetCharacterDetailsUsecase(repository)
    }

    @Provides
    @Singleton
    fun provideGetEpisodeDetailsUsecase(repository: EpisodeRepository): GetEpisodeDetailsUsecase {
        return GetEpisodeDetailsUsecase(repository)
    }
}
