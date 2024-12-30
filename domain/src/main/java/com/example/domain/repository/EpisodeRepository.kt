package com.example.domain.repository


import com.example.domain.models.Episode

interface EpisodeRepository {
    suspend fun getEpisodeById(id: String): Episode?
}
