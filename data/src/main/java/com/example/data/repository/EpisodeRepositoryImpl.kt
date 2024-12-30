package com.example.data.repository

import com.apollographql.apollo.ApolloClient
import com.apollographql.apollo.exception.ApolloException
import com.apollographql.apollo.exception.ApolloGraphQLException
import com.apollographql.apollo.exception.ApolloHttpException
import com.apollographql.apollo.exception.ApolloNetworkException
import com.apollographql.apollo.exception.ApolloWebSocketClosedException
import com.apollographql.apollo.exception.DefaultApolloException
import com.example.common.CustomExceptions
import com.example.data.GetEpisodeDetailsByIdQuery
import com.example.data.datamapper.getEpisodeQueryToEpisode
import com.example.domain.models.Episode
import com.example.domain.repository.EpisodeRepository
import javax.inject.Inject

class EpisodeRepositoryImpl @Inject constructor(private val apolloClient: ApolloClient) :
    EpisodeRepository {
    override suspend fun getEpisodeById(id: String): Episode? {
        return try {
            val response = apolloClient.query(GetEpisodeDetailsByIdQuery(id)).execute()
            response.data?.episode?.let { getEpisodeQueryToEpisode(it) }

        } catch (ex: ApolloException) {
            when (ex) {
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
                    throw DefaultApolloException("Unknown Apollo error occurred", ex)
                }

            }
        }
    }
}

