package com.example.domain.models

data class CharacterDetails(
    val id: String = "",
    val name: String = "",
    val image: String = "",
    val status: String? = null,
    val species: String? = null,
    val gender: String? = null,
    val origin: Origins? = null,
    val episodes: List<Episode> = emptyList(),
    val locations: Locations? = null

)

data class Origins(
    val name: String = "",
    val dimension: String? = null
)

data class Episode(
    val id: String = "",
    val name: String = "",
    val airdate: String = "",
    val episode: String = "",
    val characters: List<Characters> = emptyList()

)

data class Locations(
    val id: String = "",
    val name: String = "",
    val dimension: String? = null
)
