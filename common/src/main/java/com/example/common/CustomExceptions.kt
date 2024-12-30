package com.example.common

sealed class CustomExceptions(message: String) : Exception(message) {

 // Represents network-related errors
 class ApolloNetworkError(cause: Throwable? = null) : CustomExceptions(cause?.message ?: "Apollo Network error")
 // Represents Socket errors
 class ApolloWebSocketClose(cause: Throwable? = null) : CustomExceptions(cause?.message ?: "Apollo WebSocket error")
 // Represents unexpected errors
 class ApolloGraphQLError(cause: Throwable? = null) : CustomExceptions(cause?.message ?: "ApolloGraphQLError")
 // Represents invalid input errors
 class ApolloHttpError(cause: Throwable? = null) : CustomExceptions(cause?.message ?: "ApolloHttpError")
}

