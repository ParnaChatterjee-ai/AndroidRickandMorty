package com.example.data.repository

import com.apollographql.apollo.ApolloClient
import com.apollographql.apollo.exception.ApolloException
import com.apollographql.apollo.exception.ApolloGraphQLException
import com.apollographql.apollo.exception.ApolloHttpException
import com.apollographql.apollo.exception.ApolloNetworkException
import com.apollographql.apollo.exception.ApolloWebSocketClosedException
import com.apollographql.apollo.exception.DefaultApolloException
import com.example.common.CustomExceptions
import com.example.data.GetAllCharactersQuery
import com.example.data.GetCharacterByIdQuery
import com.example.data.datamapper.getCharacterDetailsToCharacterDetails
import com.example.data.datamapper.getCharactersQueryToCharacterModel
import com.example.domain.models.CharacterDetails
import com.example.domain.models.Characters
import com.example.domain.repository.CharacterRepository
import javax.inject.Inject

class CharacterRepositoryImpl @Inject constructor(private val apolloClient: ApolloClient):
    CharacterRepository {
    override suspend fun getCharacters(): List<Characters?> {
        return try {
            val response = apolloClient.query(GetAllCharactersQuery()).execute()
            val results = response.data?.characters?.results?.map {
                getCharactersQueryToCharacterModel(it)
            } ?: emptyList()
            results
        } catch (ex: ApolloException) {
            when(ex){
                is ApolloNetworkException ->
                    throw CustomExceptions.ApolloNetworkError(ex)
                is ApolloGraphQLException ->
                    throw CustomExceptions.ApolloGraphQLError(ex)
                is ApolloHttpException ->
                    throw CustomExceptions.ApolloHttpError(ex)
                is ApolloWebSocketClosedException ->
                    throw CustomExceptions.ApolloWebSocketClose(ex)

                else -> {
                    // Default case for unknown ApolloExceptions
                    throw DefaultApolloException("Unknown Apollo error occurred",ex)
                }

            }
        }
    }

    override suspend fun getCharacterById(id: String): CharacterDetails? {
        return try{
            val response = apolloClient.query(GetCharacterByIdQuery(id)).execute()
            response.data?.character?.let { getCharacterDetailsToCharacterDetails(it) }
        }
        catch (ex: ApolloException) {
            when(ex){
                is ApolloNetworkException ->
                    throw CustomExceptions.ApolloNetworkError(ex)
                is ApolloGraphQLException ->
                    throw CustomExceptions.ApolloGraphQLError(ex)
                is ApolloHttpException ->
                    throw CustomExceptions.ApolloHttpError(ex)
                is ApolloWebSocketClosedException ->
                    throw CustomExceptions.ApolloWebSocketClose(ex)

                else -> {
                    // Default case for unknown ApolloExceptions
                    throw DefaultApolloException("Unknown Apollo error occurred",ex)
                }

            }
        }
    }
}
