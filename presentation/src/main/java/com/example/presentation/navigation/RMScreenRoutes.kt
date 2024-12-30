package com.example.presentation.navigation
import kotlinx.serialization.Serializable

sealed class RMScreenRoutes {
 @Serializable
 data object HomeScreen : RMScreenRoutes()

 @Serializable
 data class CharacterDetail(val characterId : String):RMScreenRoutes()

 @Serializable
 data class EpisodeDetail(val episodeId : String):RMScreenRoutes()

}

