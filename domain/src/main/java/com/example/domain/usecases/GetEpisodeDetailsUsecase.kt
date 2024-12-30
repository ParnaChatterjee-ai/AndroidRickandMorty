package com.example.domain.usecases

import com.example.domain.models.Episode
import com.example.domain.repository.EpisodeRepository
import javax.inject.Inject

class GetEpisodeDetailsUsecase @Inject constructor(private val repository: EpisodeRepository) {
    suspend fun invoke(id: String): Episode? {
        return repository.getEpisodeById(id)
    }
}
